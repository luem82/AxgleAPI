package com.example.axgleapi.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.room.Room
import com.example.axgleapi.R
import com.example.pexelsretrofitapi.room.AppDatabase
import me.ibrahimsn.lib.SmoothBottomBar

class HomeActivity : AppCompatActivity() {

    lateinit var smoothBottomBar: SmoothBottomBar
    lateinit var navController: NavController

    companion object {
        var appDatabase: AppDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)
        smoothBottomBar = findViewById(R.id.bottom_bar)
        navController = findNavController(R.id.nav_host_fragment)

        appDatabase = Room.databaseBuilder(
            this, AppDatabase::class.java, "video_database"
        ).allowMainThreadQueries().build()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_smooth, menu)
        smoothBottomBar.setupWithNavController(menu!!, navController)
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        navController.navigateUp()
//        return true
//    }
}
