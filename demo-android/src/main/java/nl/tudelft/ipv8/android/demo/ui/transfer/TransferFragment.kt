package nl.tudelft.ipv8.android.demo.ui.transfer

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import mu.KotlinLogging
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment


class TransferFragment : BaseFragment() {
    private val logger = KotlinLogging.logger {}
    internal var bitmap: Bitmap? = null
    var sendOrReceive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // from: https://demonuts.com/kotlin-generate-qr-code/
    fun createQR(view: View) {
        try {
                bitmap = TextToImageEncode("MY PUBLIC KEY")
                view.QRPK.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
        }
    }

    @Throws(WriterException::class)
    private fun TextToImageEncode(value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )
        } catch (IllegalArgumentException: IllegalArgumentException) {
            return null
        }

        val bitMatrixWidth = bitMatrix.getWidth()
        val bitMatrixHeight = bitMatrix.getHeight()
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y))
                    ContextCompat.getColor(requireContext(), R.color.black);
                else
                    ContextCompat.getColor(requireContext(), R.color.white);
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    companion object {
        val QRcodeWidth = 500
        private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
    }

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
        createQR(view);
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
            QRCodeUtils(requireActivity(), requireContext()).startQRScanner()
        }
        view.btnSendScan.setOnClickListener {
            QRCodeUtils(requireActivity(), requireContext()).startQRScanner()
        }
        return view
    }

    // from: https://ariefbayu.xyz/create-barcode-scanner-for-android-using-kotlin-b1a9b1c4d848
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            val content = result.contents
            if(content != null) {
                val fragmentManager = requireActivity().supportFragmentManager
                if(sendOrReceive) {
                    val transferSendFragment = TransferSendFragment(content)
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view_tag, transferSendFragment)
                        .commit()
                } else {
                    val transferReceiveFragment = TransferReceiveFragment(content)
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view_tag, transferReceiveFragment)
                        .commit()
                }
            } else {
                Log.d("QR Scan", "Scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
