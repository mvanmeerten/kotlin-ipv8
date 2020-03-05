package nl.tudelft.ipv8.android.demo.ui.transfer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment


class TransferFragment : BaseFragment() {
    var sendOrReceive = true

    /**
     * Handle the toggleSwitch correctly switching views.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transfer, container, false)
        val view2: View = view.findViewById(R.id.transferSendLayout) as LinearLayout
        view2.visibility = View.VISIBLE
        val view3: View = view.findViewById(R.id.transferReceiveLayout) as LinearLayout
        view3.visibility = View.GONE
        view.QRPK.setImageBitmap(
            QRCodeUtils(requireActivity(), requireContext()).createQR(view, "MY PUBLIC KEY")
        )
        view.switch1.setOnClickListener {
            if (sendOrReceive){
                view2.visibility = View.GONE
                view3.visibility = View.VISIBLE
                sendOrReceive = false;
            } else {
                view3.visibility = View.GONE
                view2.visibility = View.VISIBLE
                sendOrReceive = true;
            }
        }
        view.QRPK_Next.setOnClickListener {
            QRCodeUtils(requireActivity(), requireContext()).startQRScanner(this)
            //Temporary QR scan skip
            //view.findNavController().navigate(R.id.action_transferFragment_to_transferReceiveFragment)
        }
        view.btnSendScan.setOnClickListener {
            QRCodeUtils(requireActivity(), requireContext()).startQRScanner(this)
            //Temporary QR scan skip
            //view.findNavController().navigate(R.id.action_transferFragment_to_transferSendFragment)
        }
        return view
    }

    /**
     * from: https://ariefbayu.xyz/create-barcode-scanner-for-android-using-kotlin-b1a9b1c4d848
     * This function is called when the QR scan has returned a value.
     * If the scan was successful, the appropriate new fragment is loaded, depending on the toggleSwitch
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        println("Acivitty returned")
        if(result != null) { // This is a result returned by the QR scanner
            println("Scanned result is okay")
            val content = result.contents
            if(content != null) {
                println("Content is okay")
                if(sendOrReceive) {
                    // TODO: Handle parsing of qr code and passing along to new fragment
                    println("viewproblem")
                    requireView().findNavController().navigate(R.id.action_transferFragment_to_transferSendFragment)
                } else {
                    // TODO: Handle parsing of qr code and passing along to new fragment
                    requireView().findNavController().navigate(R.id.action_transferFragment_to_transferReceiveFragment)
                }
            } else {
                Log.d("QR Scan", "Scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
