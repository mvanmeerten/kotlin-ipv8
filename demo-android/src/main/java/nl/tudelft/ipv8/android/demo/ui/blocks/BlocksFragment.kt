package nl.tudelft.ipv8.android.demo.ui.blocks

import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattskala.itemadapter.Item
import com.mattskala.itemadapter.ItemAdapter
import kotlinx.android.synthetic.main.fragment_blocks.*
import kotlinx.android.synthetic.main.fragment_peers.recyclerView
import kotlinx.coroutines.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment
import nl.tudelft.ipv8.attestation.trustchain.ANY_COUNTERPARTY_PK
import nl.tudelft.ipv8.attestation.trustchain.TrustChainBlock
import nl.tudelft.ipv8.attestation.trustchain.UNKNOWN_SEQ
import nl.tudelft.ipv8.util.hexToBytes


@UseExperimental(ExperimentalUnsignedTypes::class)
open class BlocksFragment : BaseFragment() {
    private val adapter = ItemAdapter()

    private lateinit var publicKey: ByteArray

    private var blocks: List<TrustChainBlock> = listOf()
    private val expandedBlocks: MutableSet<String> = mutableSetOf()

    protected open val isNewBlockAllowed = true

    init {
        lifecycleScope.launchWhenStarted {
            while (isActive) {
                refreshBlocks()
                updateView()
                delay(1000)
            }
        }

        lifecycleScope.launchWhenStarted {
            crawlChain()
        }

        adapter.registerRenderer(BlockItemRenderer(
            onExpandClick = {
                val blockId = it.block.blockId
                if (expandedBlocks.contains(blockId)) {
                    expandedBlocks.remove(blockId)
                } else {
                    expandedBlocks.add(blockId)
                }
                lifecycleScope.launch {
                    updateView()
                }
            },
            onSignClick = {
                createAgreementBlock(it.block)
            }
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        publicKey = getPublicKey()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blocks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(resources,
            R.drawable.list_divider, null)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isNewBlockAllowed) {
            inflater.inflate(R.menu.blocks_options, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_new_block -> {
                showNewBlockDialog()
               true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected open fun getPublicKey(): ByteArray {
        val args = BlocksFragmentArgs.fromBundle(arguments!!)
        return args.publicKey.hexToBytes()
    }

    protected open fun getBlocks(): List<TrustChainBlock> {
        return trustchain.getChainByUser(publicKey)
    }

    private suspend fun refreshBlocks() = withContext(Dispatchers.IO) {
        blocks = getBlocks()
    }

    protected open suspend fun updateView() {
        val items = createItems(blocks)
        adapter.updateItems(items)
        imgNoBlocks.isVisible = items.isEmpty()
        progress.isVisible = false
    }

    private suspend fun createItems(blocks: List<TrustChainBlock>): List<Item> =
        withContext(Dispatchers.Default) {
            val myPk = getTrustChainCommunity().myPeer.publicKey.keyToBin()
            blocks.map { block ->
                val isAnyCounterpartyPk = block.linkPublicKey.contentEquals(ANY_COUNTERPARTY_PK)
                val isMyPk = block.linkPublicKey.contentEquals(myPk)
                val isProposalBlock = block.linkSequenceNumber == UNKNOWN_SEQ
                val hasLinkedBlock = blocks.find { it.linkedBlockId == block.blockId } != null
                val canSign = (isAnyCounterpartyPk || isMyPk) &&
                    isProposalBlock &&
                    !block.isSelfSigned &&
                    !hasLinkedBlock
                val status = when {
                    block.isSelfSigned -> BlockItem.BlockStatus.SELF_SIGNED
                    hasLinkedBlock -> BlockItem.BlockStatus.SIGNED
                    isProposalBlock -> BlockItem.BlockStatus.WAITING_FOR_SIGNATURE
                    else -> null
                }
                BlockItem(
                    block,
                    isExpanded = expandedBlocks.contains(block.blockId),
                    canSign = canSign,
                    status = status
                )
            }
        }

    private fun showNewBlockDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("New Block")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Create") { _, _ ->
            val message = input.text.toString()
            trustchain.createProposalBlock(message, publicKey)
            lifecycleScope.launch {
                refreshBlocks()
                updateView()
                recyclerView.smoothScrollToPosition(0)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private suspend fun crawlChain() {
        val peer = trustchain.getPeerByPublicKeyBin(publicKey)
        if (peer != null) {
            trustchain.crawlChain(peer)
            refreshBlocks()
            updateView()
        }
    }

    private fun createAgreementBlock(proposalBlock: TrustChainBlock) {
        trustchain.createAgreementBlock(proposalBlock, proposalBlock.transaction)
        lifecycleScope.launch {
            refreshBlocks()
            updateView()
        }
    }
}
