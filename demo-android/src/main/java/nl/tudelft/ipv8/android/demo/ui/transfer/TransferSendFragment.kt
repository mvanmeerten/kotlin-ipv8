package nl.tudelft.ipv8.android.demo.ui.transfer

import android.graphics.Bitmap
import android.os.Bundle
import nl.tudelft.ipv8.android.demo.ui.BaseFragment

class TransferSendFragment(content: String) : BaseFragment() {
    internal var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
