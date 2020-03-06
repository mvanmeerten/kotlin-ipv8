package nl.tudelft.ipv8.android.demo.ui.peers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattskala.itemadapter.ItemAdapter
import kotlinx.android.synthetic.main.fragment_peers.*
import kotlinx.coroutines.*
import nl.tudelft.ipv8.android.demo.DemoCommunity
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment
import nl.tudelft.ipv8.attestation.trustchain.BlockListener
import nl.tudelft.ipv8.attestation.trustchain.TrustChainBlock
import nl.tudelft.ipv8.attestation.trustchain.store.TrustChainStore
import nl.tudelft.ipv8.attestation.trustchain.validation.TransactionValidator
import nl.tudelft.ipv8.attestation.trustchain.validation.ValidationResult
import nl.tudelft.ipv8.util.toHex

class PeersFragment : BaseFragment() {
    private val adapter = ItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.registerRenderer(PeerItemRenderer {
            findNavController().navigate(
                PeersFragmentDirections.actionPeersFragmentToBlocksFragment(
                    it.peer.publicKey.keyToBin().toHex()
                )
            )
        })

        adapter.registerRenderer(AddressItemRenderer {
            // NOOP
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_peers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        loadNetworkInfo()
    }

    private fun loadNetworkInfo() {
        lifecycleScope.launchWhenStarted {
            val trustchain = getTrustChainCommunity()

            trustchain.registerTransactionValidator("demo_block", object : TransactionValidator {
                override fun validate(
                    block: TrustChainBlock,
                    database: TrustChainStore
                ): Boolean {
                    return block.transaction["message"] != null
                }
            })

            trustchain.addListener(object : BlockListener {
                override fun shouldSign(block: TrustChainBlock): Boolean {
                    return true
                }

                override fun onBlockReceived(block: TrustChainBlock) {
                    Log.d("TrustChainDemo", "onBlockReceived: ${block.blockId} ${block.transaction}")
                }
            }, "demo_block")

            trustchain.registerTransactionValidator("demo_tx_block", object : TransactionValidator {
                override fun validate(
                    block: TrustChainBlock,
                    database: TrustChainStore
                ): Boolean {
                    return block.transaction["amount"] != null
                }
            })

            trustchain.addListener(object : BlockListener {
                override fun shouldSign(block: TrustChainBlock): Boolean {
                    return true
                }

                override fun onBlockReceived(block: TrustChainBlock) {
                    Log.d("TrustChainDemo", "onBlockReceived: ${block.blockId} ${block.transaction}")
                }
            }, "demo_tx_block")

            while (isActive) {
                val overlays = getIpv8().overlays

                for ((_, overlay) in overlays) {
                    logger.debug(overlay.javaClass.simpleName + ": " + overlay.getPeers().size + " peers")
                }

                val demoCommunity = getDemoCommunity()
                val peers = demoCommunity.getPeers()

                // Filter only addresses that are not verified yet
                val discoveredAddresses = demoCommunity.discoveredAddresses.filter { address ->
                    peers.find { peer -> peer.address == address } == null
                }

                val peerItems = peers.map { PeerItem(it) }

                val addressItems = discoveredAddresses.map { address ->
                    val introduced = demoCommunity.discoveredAddressesIntroduced[address]!!
                    val contacted = demoCommunity.discoveredAddressesContacted[address]
                    AddressItem(address, introduced, contacted)
                }

                val items = peerItems + addressItems

                adapter.updateItems(items)
                txtCommunityName.text = demoCommunity.javaClass.simpleName
                txtPeerCount.text = resources.getQuantityString(
                    R.plurals.x_peers, peers.size,
                    peers.size
                )
                imgEmpty.isVisible = items.isEmpty()

                delay(1000)
            }
        }
    }
}
