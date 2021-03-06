package nl.tudelft.ipv8.android.demo.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_debug.*
import kotlinx.coroutines.*
import nl.tudelft.ipv8.Community
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment
import nl.tudelft.ipv8.util.toHex

class DebugFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_debug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            while (isActive) {
                updateView()
                delay(1000)
            }
        }
    }

    private fun updateView() {
        val ipv8 = getIpv8()
        val demo = getDemoCommunity()
        txtBootstrap.text = Community.DEFAULT_ADDRESSES.joinToString("\n")
        txtLanAddress.text = demo.myEstimatedLan.toString()
        txtWanAddress.text = demo.myEstimatedWan.toString()
        txtPeerId.text = ipv8.myPeer.mid
        txtPublicKey.text = ipv8.myPeer.publicKey.keyToBin().toHex()
        txtOverlays.text = ipv8.overlays.values.toList().joinToString("\n") {
            it.javaClass.simpleName + " (" + it.getPeers().size + " peers)"
        }

        lifecycleScope.launch {
            val blockCount = withContext(Dispatchers.IO) {
                getTrustChainCommunity().database.getAllBlocks().size
            }
            txtBlockCount.text = blockCount.toString()
        }

        lifecycleScope.launch {
            val chainLength = withContext(Dispatchers.IO) {
                getTrustChainCommunity().getChainLength()
            }
            txtChainLength.text = chainLength.toString()
        }
    }
}
