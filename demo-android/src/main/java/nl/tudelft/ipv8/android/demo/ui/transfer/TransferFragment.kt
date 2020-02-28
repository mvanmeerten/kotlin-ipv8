package nl.tudelft.ipv8.android.demo.ui.transfer

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.fragment_peers.*
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment


class TransferFragment : BaseFragment() {
    internal var bitmap: Bitmap? = null

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
    private fun TextToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
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
        var sendOrReceive = true;
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

            }
            else{
                view3.visibility = View.GONE
                view2.visibility = View.VISIBLE
                sendOrReceive = true;}
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

    }
}
