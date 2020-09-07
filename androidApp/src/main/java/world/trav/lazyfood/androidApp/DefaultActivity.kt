package world.trav.lazyfood.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DefaultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_acitivy)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}