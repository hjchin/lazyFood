package world.trav.lazyfood.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import world.trav.lazyfood.androidApp.ui.main.DefaultFragment

class DefaultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_acitivy)

    }
}