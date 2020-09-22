package world.trav.lazyfood.androidApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//
// Created by  on 22/9/20.
//
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        route()
        finish()
    }

    private fun route(){
        startActivity(Intent(this, DefaultActivity::class.java))
    }

}