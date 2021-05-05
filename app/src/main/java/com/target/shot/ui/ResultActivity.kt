package com.target.shot.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.target.shot.R
import com.target.shot.databinding.ResultFragmentBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ResultFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (intent.extras?.get(LevelActivity.RESULT_KEY)) {
            LevelActivity.WIN -> {
                binding.resultImageView?.setImageResource(R.drawable.you_win)
            }
            else -> {
                binding.resultImageView?.setImageResource(R.drawable.try_agaim)
            }
        }

        binding.homeButton?.setOnClickListener {
            setResult(RESULT_HOME)
            finish()
        }

        binding.repeatButton?.setOnClickListener {
            setResult(RESULT_REPEAT)
            finish()
        }
    }

    companion object {
        const val RESULT_HOME = 0
        const val RESULT_REPEAT = 1
    }
}