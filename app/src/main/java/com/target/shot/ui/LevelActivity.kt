package com.target.shot.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.target.shot.R
import com.target.shot.databinding.LevelScreenBinding
import com.target.shot.model.*
import com.target.shot.util.Constants
import com.target.shot.util.DimensionConverter
import kotlin.random.Random

class LevelActivity : AppCompatActivity() {
    private lateinit var binding: LevelScreenBinding
    private var isGameActive = false
    private var yDelta = 0f
    private val sunLocation = IntArray(2)
    private var layoutChangedListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var sunHeight = 0
    private var sunWidth = 0
    private val sunPadding = 20
    private var duration = 3000L

    private val slowDuration = 6000L
    private val basicDuration = 4000L

    var timer: CountDownTimer? = null
    private var time = Constants.LEVEL_TIME

    private val modeDuration = 10000L
    private val lastSeconds = 3000L

    private var superMode = false
    private var superModeEnd = 0L

    private var slowMode = false
    private var slowModeEnd = 0L

    private var anim: AnimationDrawable? = null

    private val items = mutableListOf<Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("level", "onCreate")
        binding = LevelScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isGameActive = true
        moveSun()
        binding.timeText?.text = getString(R.string.time, time / 1000)

        layoutChangedListener = ViewTreeObserver.OnGlobalLayoutListener {
            binding.sun?.getLocationInWindow(sunLocation)
            sunHeight = binding.sun?.height!!
            sunWidth = binding.sun?.width!!
            Log.d("tag", "w: $sunWidth, h: $sunHeight, loc: ${sunLocation[0]}, ${sunLocation[1]}")
            generateBall()
            binding.layout.viewTreeObserver?.removeOnGlobalLayoutListener(layoutChangedListener)
            layoutChangedListener = null
        }
        binding.layout.viewTreeObserver?.addOnGlobalLayoutListener(layoutChangedListener)
    }

    override fun onResume() {
        super.onResume()
        Log.d("level", "onResume")
        timer = object : CountDownTimer(time, 500) {
            override fun onTick(millisUntilFinished: Long) {
                time = millisUntilFinished
                binding.timeText?.text = getString(R.string.time, time / 1000)
                if (time > lastSeconds) {
                    if (time / 500 % 4 == 0L) {
                        generateBall()
                    }
                    if (time / 500 % 14 == 0L && time > lastSeconds) {
                        generateBooster()
                    }
                } else {
                    if (time / 500 % 2 == 0L) {
                        generateBall()
                    }
                }
                if (superMode && time <= superModeEnd) {
                    superMode = false
                    changeSunMode()
                }
                if (slowMode && time <= slowModeEnd) {
                    slowMode = false
                    changeSpeedMode()
                }
            }

            override fun onFinish() {
                openWinAlert()
            }
        }
        (timer as CountDownTimer).start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun moveSun() {
        binding.sun?.setOnTouchListener { v, event ->
            if (isGameActive) {
                val y = event.rawY
                val lParams = v.layoutParams as ConstraintLayout.LayoutParams
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        yDelta = y - lParams.topMargin
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val topMargin = (y - yDelta).toInt()
                        if (topMargin < binding.layout.height - binding.sun?.height!!) {
                            lParams.topMargin = topMargin
                            v.layoutParams = lParams
                            v.getLocationInWindow(sunLocation)
                        }
                    }
                }
            }
            isGameActive
        }
    }

    private fun generateBooster() {
        when (Random.nextInt(0, 2)) {
            0 -> {
                val ball = SuperBooster(this)
                val params = ball.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = generateFireBallMargin()
                binding.container?.addView(ball)
                Log.d("tag", "view is added")
                items.add(ball)
                animateBall(ball)

            }
            1 -> {
                val ball = SlowBooster(this)
                val params = ball.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = generateWaterBombMargin()
                binding.container?.addView(ball)
                items.add(ball)
                animateBall(ball)
            }
        }
    }

    private fun generateBall() {
        when (Random.nextInt(0, 2)) {
            0 -> {
                val ball = FireBall(this)
                val params = ball.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = generateFireBallMargin()
                binding.container?.addView(ball)
                items.add(ball)
                anim = ball.drawable as AnimationDrawable
                ball.post(run)
                animateBall(ball)

            }
            1 -> {
                val ball = WaterBomb(this)
                val params = ball.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = generateWaterBombMargin()
                binding.container?.addView(ball)
                items.add(ball)
                animateBall(ball)
            }
        }
    }

    private fun animateBall(ball: Item) {
        ball.animator =
            ObjectAnimator.ofFloat(ball, "translationX", -binding.container?.width?.toFloat()!!)
        ball.animator?.interpolator = LinearInterpolator()
        ball.animator?.duration = duration
        ball.animator?.start()
        ball.getLocationInWindow(ball.location)
        Log.d("tag", "${ball.location[0]}, ${ball.location[1]}")
        ball.animator?.addUpdateListener {
            ball.getLocationInWindow(ball.location)
            Log.d("tag", "${ball.location[0]}, ${ball.location[1]}")
            if (checkIfBallIsInsideBasket(ball.location) && isGameActive) {
                if ((ball is FireBall || ball is WaterBomb) && !superMode) {
                    items.remove(ball)
                    ball.animator?.cancel()
                    openLoseAlert()
                } else if (ball is SlowBooster) {
                    ball.animator?.cancel()
                    ball.animator = null
                    items.remove(ball)
                    slowMode = true
                    slowModeEnd = time - modeDuration
                    changeSpeedMode()
                    binding.container?.removeView(ball)
                } else if (ball is SuperBooster) {
                    ball.animator?.cancel()
                    ball.animator = null
                    superMode = true
                    superModeEnd = time - modeDuration
                    changeSunMode()
                    items.remove(ball)
                    binding.container?.removeView(ball)
                }
            } else if (ball.location[0] < sunLocation[0] + sunWidth / 2 && isGameActive && ball.location[0] != 0) {
                ball.animator?.cancel()
                ball.animator = null
                items.remove(ball)
                binding.container?.removeView(ball)
            }
        }
    }

    private fun checkIfBallIsInsideBasket(ballLocation: IntArray): Boolean {
        return ballLocation[0] != 0 && ballLocation[0] <= sunLocation[0] + sunWidth - DimensionConverter.dpToPixels(
            sunPadding.toFloat(),
            this
        ) &&
                ballLocation[1] >= sunLocation[1] &&
                ballLocation[1] <= sunLocation[1] + sunHeight - 2 * sunPadding
    }

    private fun generateFireBallMargin(): Int {
        return Random.nextInt(0, binding.container?.height!! - FireBall.height)
    }

    private fun generateWaterBombMargin(): Int {
        return Random.nextInt(0, binding.container?.height!! - WaterBomb.height)
    }

    private fun changeSunMode() {
        val params = binding.sun?.layoutParams as ConstraintLayout.LayoutParams
        if (superMode) {
            binding.sun?.setImageResource(R.drawable.strong_sun)
            params.width = DimensionConverter.dpToPixels(115f, this)
            params.height = DimensionConverter.dpToPixels(115f, this)
        } else {
            binding.sun?.setImageResource(R.drawable.small_sun)
            params.width = DimensionConverter.dpToPixels(91f, this)
            params.height = DimensionConverter.dpToPixels(91f, this)
            params.marginStart = DimensionConverter.dpToPixels(15f, this) //dp
        }
    }


    private fun changeSpeedMode() {
        if (slowMode) {
            duration = slowDuration
        } else {
            duration = basicDuration
        }
        for (item in items) {
            item.animator?.cancel()
            animateBall(item)
        }
    }

    private fun openWinAlert() {
        for (item in items) {
            item.animator?.cancel()
            item.animator = null
        }
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(RESULT_KEY, WIN)
        startActivityForResult(intent, 1)
    }

    private fun openLoseAlert() {
        timer?.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(RESULT_KEY, LOSE)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == ResultActivity.RESULT_REPEAT) {
            val intent = Intent(this, LevelActivity::class.java)
            startActivity(intent)
            finish()
        } else if (requestCode == 1 && resultCode == ResultActivity.RESULT_HOME) {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        for (item in items) {
            item.animator?.cancel()
            item.animator = null
        }
    }

    override fun onRestart() {
        super.onRestart()
        for (item in items) {
            animateBall(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    companion object {
        const val RESULT_KEY = "result"
        const val WIN = "win"
        const val LOSE = "lose"
    }

    var run = Runnable { anim?.start() }
}