package nl.tudelft.ipv8.android.demo.ui.transfer

import android.graphics.Bitmap
import android.os.Bundle
import nl.tudelft.ipv8.android.demo.ui.BaseFragment

class TransferSendFragment(content: String) : BaseFragment() {
    internal var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//
//    fun startQRScanner() {
//        run {
//            IntentIntegrator(this.activity).initiateScan()
//        }
//    }
//
//    // from: https://demonuts.com/kotlin-generate-qr-code/
//    fun createQR(view: View) {
//        if (view.editTxtQRInput.text.toString().trim { it <= ' ' }.length == 0) {
//            Toast.makeText(activity, "Enter String!", Toast.LENGTH_SHORT).show()
//        } else {
//            try {
//                bitmap = TextToImageEncode(view.editTxtQRInput.text.toString())
//                view.imageQR.setImageBitmap(bitmap)
//            } catch (e: WriterException) {
//                e.printStackTrace()
//            }
//
//        }
//    }
//
//    @Throws(WriterException::class)
//    private fun TextToImageEncode(Value: String): Bitmap? {
//        val bitMatrix: BitMatrix
//        try {
//            bitMatrix = MultiFormatWriter().encode(
//                Value,
//                BarcodeFormat.QR_CODE,
//                QRcodeWidth, QRcodeWidth, null
//            )
//        } catch (Illegalargumentexception: IllegalArgumentException) {
//            return null
//        }
//
//        val bitMatrixWidth = bitMatrix.getWidth()
//        val bitMatrixHeight = bitMatrix.getHeight()
//        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
//
//        for (y in 0 until bitMatrixHeight) {
//            val offset = y * bitMatrixWidth
//            for (x in 0 until bitMatrixWidth) {
//                pixels[offset + x] = if (bitMatrix.get(x, y))
//                    ContextCompat.getColor(requireContext(), R.color.black);
//                else
//                    ContextCompat.getColor(requireContext(), R.color.white);
//            }
//        }
//        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
//
//        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
//        return bitmap
//    }
//
//    companion object {
//        val QRcodeWidth = 500
//        private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
//    }
//
//    // from: https://ariefbayu.xyz/create-barcode-scanner-for-android-using-kotlin-b1a9b1c4d848
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if(result != null) {
//            if(result.contents != null) {
//                txtValue.text = result.contents
//            } else {
//                txtValue.text = "scan failed"
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view: View = inflater.inflate(R.layout.fragment_transfer, container, false)
//        view.btnScan.setOnClickListener {
//            startQRScanner()
//        }
//        view.btnCreateQR.setOnClickListener {
//            createQR(view);
//        }
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
//
//    }
}
