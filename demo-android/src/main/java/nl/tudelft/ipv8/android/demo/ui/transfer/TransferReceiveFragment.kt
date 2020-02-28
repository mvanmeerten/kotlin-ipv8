package nl.tudelft.ipv8.android.demo.ui.transfer

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_transfer.*
import kotlinx.android.synthetic.main.fragment_transfer_receive.view.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment

class TransferReceiveFragment : BaseFragment() {
    val qrCodeUtils = QRCodeUtils(activity, requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transfer_receive, container, false)
        view.textSenderPublicKey.text = "Public key: "
        view.textTransferAmount.text = "Amount: "
        view.buttonConfirmReceipt.setOnClickListener {
            qrCodeUtils.startQRScanner()
        }
        view.buttonCancelReceipt.setOnClickListener {
            //TODO: Go back to TransferFragment screen
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if(result.contents != null) {
                //txtValue.text = result.contents
                //TODO: Handle agreement block creation
                val view: View = requireView()
                view.transferReceiveLinear.visibility = View.GONE
                view.transferReceiveLinearConfirmed.visibility = View.VISIBLE
                val bitmap: Bitmap? = qrCodeUtils.createQR(view)
                view.image3rdQR.setImageBitmap(bitmap)
            } else {
                println("Failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
