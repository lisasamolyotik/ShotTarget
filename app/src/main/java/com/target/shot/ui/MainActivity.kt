package com.target.shot.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.target.shot.databinding.ActivityMainBinding
import com.target.shot.databinding.MenuScreenBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("main", "opened")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.playButton?.setOnClickListener {
            openLevel()
        }

        binding.rulesButton?.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }

        binding.settingsButton?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openLevel() {
        val intent = Intent(this, LevelActivity::class.java)
        startActivity(intent)
    }
}