package world.trav.lazyfood.androidApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefaultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_acitivy)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onActivityResult(requestCode: Int , resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}