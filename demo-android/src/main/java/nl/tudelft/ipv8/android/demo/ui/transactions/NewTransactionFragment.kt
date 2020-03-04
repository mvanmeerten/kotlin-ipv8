package nl.tudelft.ipv8.android.demo.ui.transactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_debug.*
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import kotlinx.coroutines.*
import nl.tudelft.ipv8.Community
import nl.tudelft.ipv8.android.demo.R
import nl.tudelft.ipv8.android.demo.ui.BaseFragment
import nl.tudelft.ipv8.util.hexToBytes
import nl.tudelft.ipv8.util.toHex

class NewTransactionFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendAmountBtn.setOnClickListener {
            val snack = Snackbar.make(it, amountTxt.text, Snackbar.LENGTH_LONG)
            snack.show()
            val amount = amountTxt.text.toString().toFloat()
            trustchain.createTxProposalBlock(amount, "4c69624e61434c504b3a511fdaab386c761a8320e522ae988ce055e3119cc9621583fed029732e88b1095cfc91ba0af79d6277f08fefc8b9ed28d4861ab3701ec7d5327ebf74eff0e464".hexToBytes())
        }

        val blocks = trustchain.getChainByUser(getPublicKey())
    }

    private fun getPublicKey(): ByteArray {
        return getTrustChainCommunity().myPeer.publicKey.keyToBin()
    }
}
