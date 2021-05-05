package com.target.shot.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.target.shot.databinding.RulesScreenBinding

class RulesActivity : AppCompatActivity() {
    private lateinit var binding: RulesScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RulesScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeButton?.setOnClickListener {
            finish()
        }
    }
}