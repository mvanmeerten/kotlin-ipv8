package nl.tudelft.ipv8.android.demo.ui.peers

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging
import nl.tudelft.ipv8.android.demo.R

val logger = KotlinLogging.logger {}

class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        findNavController(R.id.navHostFragment)
    }

    private val appBarConfiguration by lazy {
        val topLevelDestinationIds = setOf(R.id.peersFragment, R.id.transferFragment, R.id.usersFragment,
            R.id.latestBlocksFragment, R.id.myChainFragment, R.id.debugFragment)
        AppBarConfiguration(topLevelDestinationIds)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Setup ActionBar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup bottom navigation
        bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launch {
            while (isActive) {

                delay(1000)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
