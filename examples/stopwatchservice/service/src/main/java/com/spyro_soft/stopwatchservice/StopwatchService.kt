// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.stopwatchservice

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteCallbackList
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class StopwatchService : Service() {

    companion object {
        private val TAG = StopwatchService::class.java.simpleName
    }

    init {
        Timber.plant(Timber.DebugTree())
    }

    private class Stopwatch(private val callback: (Long, Long, Long) -> Unit) : Runnable {

        companion object {
            private val TAG = Stopwatch::class.java.simpleName
        }

        private val handler = Handler(Looper.myLooper()!!)

        var isRunning = false
        var time = TimeStruct()
            private set

        private var totalTimePassed: Long = 0
        private var startTime: Long = 0

        fun start() {
            Timber.tag(TAG).d("start")
            handler.removeCallbacks(this) // make sure only 1 callback is active
            startTime = System.currentTimeMillis()
            isRunning = true
            handler.postDelayed(this, 0)
        }

        fun stop() {
            Timber.tag(TAG).d("stop")
            isRunning = false
            startTime = 0
            totalTimePassed = 0
            callback(0, 0, 0)
            handler.removeCallbacks(this)
        }

        fun pause() {
            Timber.tag(TAG).d("pause")
            isRunning = false
            startTime = 0
            handler.removeCallbacks(this)
        }

        override fun run() {
            if (!isRunning) {
                return
            }

            totalTimePassed += System.currentTimeMillis() - startTime
            startTime = System.currentTimeMillis()

            Timber.tag(TAG).d("totalTimePassed: $totalTimePassed")

            var seconds = (totalTimePassed / 1000)
            val minutes = seconds / 60
            seconds %= 60
            val millis = totalTimePassed % 1000
            time.minutes = minutes.toInt()
            time.seconds = seconds.toInt()
            time.milliseconds = millis.toInt()

            callback(minutes, seconds, millis)
            handler.postDelayed(this, if (isRunning) 10 else 250) // can try to optimize it
        }

    }

    val lock = ReentrantLock()
    private val callbacks = RemoteCallbackList<IStopwatchControllerCallback>() // [1]
    private val binder = object : IStopwatchControllerService.Stub() { // [2]
        override fun registerCallback(cb: IStopwatchControllerCallback?) { // [3]
            Timber.tag(TAG).d("RegisterCallback $cb")
            callbacks.register(cb)
        }

        override fun unregisterCallback(cb: IStopwatchControllerCallback?) { // [4]
            Timber.tag(TAG).d("UnregisterCallback $cb")
            callbacks.unregister(cb)
        }

        override fun setIsRunning(isRunning: Boolean) { // [5]
            if (isRunning != stopwatch.isRunning) {
                if (stopwatch.isRunning) {
                    Timber.tag(TAG).d("toggle -> pause")
                    stopwatch.pause()
                } else {
                    Timber.tag(TAG).d("toggle -> start")
                    stopwatch.start()
                }
                postIsRunning(stopwatch.isRunning);             }
        }

        override fun reset() { // [6]
            Timber.tag(TAG).d("reset")
            stopwatch.stop()
            postIsRunning(stopwatch.isRunning);

        }

        override fun time(): TimeStruct { // [7]
            Timber.tag(TAG).d("time: ${stopwatch.time}")
            return stopwatch.time
        }

        override fun isRunning(): Boolean { // [8]
            Timber.tag(TAG).d("isRunning: ${stopwatch.isRunning}")
            return stopwatch.isRunning
        }
    }

    override fun onBind(intent: Intent): IBinder { // [9]
        Timber.tag(TAG).d("onBind")
        return binder
    }

    private lateinit var stopwatch: Stopwatch

    override fun onCreate() {
        super.onCreate()
        Timber.tag(TAG).d("onCreate")
        Timber.tag(TAG)
            .d("Running on thread with id: ${Thread.currentThread().id}, name: ${Thread.currentThread().name}")
        stopwatch = Stopwatch { min: Long, sec: Long, millis: Long ->
            val time = TimeStruct()
            time.minutes = min.toInt()
            time.seconds = sec.toInt()
            time.milliseconds = millis.toInt()
            postTime(time)
        }
    }

    private fun postIsRunning(isRunning: Boolean) { // [10]
        lock.withLock {
            Timber.tag(TAG).d("posting isRunning: ${isRunning}")
            // this will implicitly handle exceptions -> will propagate them
            val size = callbacks.beginBroadcast()
            for (i in 0 until size) {
                callbacks.getBroadcastItem(i).isRunningChanged(isRunning)
            }
            callbacks.finishBroadcast()
        }
    }

    private fun postTime(time: TimeStruct) { // [11]
        lock.withLock {
            Timber.tag(TAG).d("posting time: ${time.stringify()}")
            // this will implicitly handle exceptions -> will propagate them
            val size = callbacks.beginBroadcast()
            for (i in 0 until size) {
                callbacks.getBroadcastItem(i).timeChanged(time)
            }
            callbacks.finishBroadcast()
        }
    }
}

fun TimeStruct.stringify(): String {
    return "$minutes : $seconds : $milliseconds"
}