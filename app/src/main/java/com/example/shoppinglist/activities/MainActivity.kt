package com.example.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding


private const val TAG: String = "@@@"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    Log.d(TAG, "settings")
                }
                R.id.notes -> {
                    Log.d(TAG, "notes")
                }
                R.id.shop_list -> {
                    Log.d(TAG, "shop_list")
                }
                R.id.new_item -> {
                    Log.d(TAG, "new_item")
                }
            }
            true
        }
    }
}