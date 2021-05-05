package com.target.shot.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.target.shot.R
import com.target.shot.databinding.SettingsScreenBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsScreenBinding
    private lateinit var preferences: SharedPreferences

    private var sound: Boolean = true
    private var vibro: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getPreferences(MODE_PRIVATE)
        sound = preferences.getBoolean(SOUND_KEY, true) == true
        vibro = preferences.getBoolean(VIBRO_KEY, true) == true
        changeSoundImage()
        changeVibroImage()

        binding.homeButton?.setOnClickListener {
            finish()
        }

        binding.soundButton?.setOnClickListener {
            sound = !sound
            val editor = preferences.edit()
            editor?.putBoolean(SOUND_KEY, sound)
            editor?.apply()
            changeSoundImage()
        }

        binding.vibroButton?.setOnClickListener {
            vibro = !vibro
            val editor = preferences.edit()
            editor?.putBoolean(VIBRO_KEY, vibro)
            editor?.apply()
            changeVibroImage()
        }
    }

    private fun changeVibroImage() {
        if (vibro) {
            binding.vibroSwitcher?.setImageResource(R.drawable.on)
        } else {
            binding.vibroSwitcher?.setImageResource(R.drawable.off)
        }
    }

    private fun changeSoundImage() {
        if (sound) {
            binding.soundSwitcher?.setImageResource(R.drawable.on)
        } else {
            binding.soundSwitcher?.setImageResource(R.drawable.off)
        }
    }

    companion object {
        private const val SOUND_KEY = "sound"
        private const val VIBRO_KEY = "vibro"
    }
}