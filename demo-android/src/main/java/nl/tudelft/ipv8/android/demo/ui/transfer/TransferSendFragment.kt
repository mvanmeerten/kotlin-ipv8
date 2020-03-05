package nl.tudelft.ipv8.android.demo.ui.transfer

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_transfer_send.view.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment

class TransferSendFragment() : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transfer_send, container, false)
        val bitmap: Bitmap? = QRCodeUtils(activity, requireContext())
            .createQR(view, "MY Proposal BLOCK")
            view.proposalBlockQR.setImageBitmap(bitmap)
        view.btnProposalScannedNext.setOnClickListener {
            QRCodeUtils(requireActivity(), requireContext()).startQRScanner(this)
            //view.findNavController().navigate(R.id.action_transferSendFragment_to_transferConfirmationFragment)
        }
        return view
    }

    /**
     * from: https://ariefbayu.xyz/create-barcode-scanner-for-android-using-kotlin-b1a9b1c4d848
     * This function is called when the QR scan has returned a value.
     * If the scan was successful, the appropriate new fragment is loaded
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) { // This is a result returned by the QR scanner
            val content = result.contents
            if(content != null) {
                // TODO: Handle parsing of scanned contents
                requireView().findNavController().navigate(R.id.action_transferSendFragment_to_transferConfirmationFragment)
            } else {
                Log.d("QR Scan", "Scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
