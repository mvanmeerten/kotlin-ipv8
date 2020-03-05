package nl.tudelft.ipv8.android.demo.ui.transfer

import FragmentIntentIntegrator
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import nl.tudelft.ipv8.android.demo.R

class QRCodeUtils(private val activity: FragmentActivity?, private val context: Context) {

    fun startQRScanner(fragment: Fragment) {
        run {
            FragmentIntentIntegrator(fragment).initiateScan()
        }
    }

    // from: https://demonuts.com/kotlin-generate-qr-code/
    fun createQR(view: View, text: String): Bitmap? {
        if (text.isEmpty()) {
            Toast.makeText(activity, "Enter String!", Toast.LENGTH_SHORT).show()
        } else {
            try {
                return TextToImageEncode(text)
            } catch (e: WriterException) {
                e.printStackTrace()
            }

        }
        return null
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
                    ContextCompat.getColor(context, R.color.black);
                else
                    ContextCompat.getColor(context, R.color.white);
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
}
