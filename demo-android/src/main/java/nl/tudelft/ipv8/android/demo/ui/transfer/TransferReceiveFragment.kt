package nl.tudelft.ipv8.android.demo.ui.transfer

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_transfer_receive.view.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment

class TransferReceiveFragment() : BaseFragment() {

    /**
     * Populate confirmation screen with correct public key and amount.
     * Handle next and cancel buttons correctly
     */
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transfer_receive, container, false)
        val publicKey = "hmm"
        val amount = "zoveel"
        view.textSenderPublicKey.text = "Public key: $publicKey"
        view.textTransferAmount.text = "Amount: $amount"
        view.buttonConfirmReceipt.setOnClickListener {
            QRCodeUtils(activity, requireContext()).startQRScanner(this)
            // TODO: The code below is for skipping actual qr scanning, remove when implemented
//            val view: View = requireView()
//            view.transferReceiveLinear.visibility = View.GONE
//            view.transferReceiveLinearConfirmed.visibility = View.VISIBLE
//            val bitmap: Bitmap? = QRCodeUtils(activity, requireContext())
//                .createQR(view, "MY AGREEMENT BLOCK")
//            view.image3rdQR.setImageBitmap(bitmap)
        }
        // Go back to transfer without the ability to go back to this fragment
        view.buttonCancelReceipt.setOnClickListener {
            view.findNavController().navigate(R.id.action_transferReceiveFragment_to_transferFragment)
        }
        // Go to success animation
        view.buttonConfirmReceiptTransferEnd.setOnClickListener {
            view.findNavController().navigate(R.id.action_transferReceiveFragment_to_transferConfirmationFragment)
        }
        return view
    }

    /**
     * This function is called when the QR scan has returned a value.
     * The proposal block is parsed and an agreement block is created.
     * A QR code of the created agreement block is generated.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if(result.contents != null) {
                //TODO: Handle agreement block creation
                val proposalBlock = result.contents
                val view: View = requireView()
                view.transferReceiveLinear.visibility = View.GONE
                view.transferReceiveLinearConfirmed.visibility = View.VISIBLE
                val bitmap: Bitmap? = QRCodeUtils(activity, requireContext())
                    .createQR(view, "MY AGREEMENT BLOCK")
                view.image3rdQR.setImageBitmap(bitmap)
            } else {
                Log.d("Scan Proposal QR","Scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
