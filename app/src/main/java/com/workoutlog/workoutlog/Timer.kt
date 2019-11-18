package com.workoutlog.workoutlog

import android.os.Handler
import android.os.Looper
import android.os.Message

class Timer(private var millis: Long, private val interval: Long) {

    companion object {
        private const val ON_TICK = 0
        private const val ON_FINISH = 1
        private const val RESET_TIMER = 2
    }

    init {
        require(millis >= 0)
        require(interval >= 0)
    }

    private var isRunning = false

    private var listener: ITimerListener? = null

    private var currentMillis = millis

    private val threadReset = Thread(Runnable {
        threadTimer.interrupt()
        while (!threadTimer.isInterrupted) {
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        handler.sendEmptyMessage(RESET_TIMER)
    })

    private val threadTimer = Thread(Runnable {
        while (!Thread.currentThread().isInterrupted) {
            isRunning = true
            try {
                Thread.sleep(interval)
                currentMillis -= interval
                handler.sendEmptyMessage(ON_TICK)
                if(currentMillis <= 0) {
                    currentMillis = millis
                    Thread.currentThread().interrupt()
                    handler.sendEmptyMessage(ON_FINISH)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            isRunning = false
        }
    })

    private val handler: Handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                ON_TICK -> if(listener != null) listener!!.onTimerTicked(currentMillis)
                ON_FINISH -> if(listener != null) listener!!.onTimerFinished()
                RESET_TIMER -> currentMillis = millis
                else -> super.handleMessage(msg)
            }
        }
    }

    fun setMillis(millis: Long) {
        if(!isRunning) {
            this.millis = millis
            currentMillis = millis
        }
    }

    fun reset(millis: Long) {
        this.millis = millis
        threadReset.start()
    }

    fun start() {
        threadTimer.start()
    }

    fun pause() {
        threadTimer.interrupt()
    }

    fun setTimerListener(listener: ITimerListener) {
        this.listener = listener
    }

    fun isRunning() = isRunning

    interface ITimerListener {
        fun onTimerFinished()
        fun onTimerTicked(currentMillis: Long)
    }
}