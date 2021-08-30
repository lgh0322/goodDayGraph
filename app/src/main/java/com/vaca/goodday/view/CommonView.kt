package com.vaca.chip8.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.vaca.goodday.R
import java.lang.Thread.sleep
import java.util.*


class CommonView : SurfaceView, Runnable {
    private val wavePaint = Paint()
    private val bgPaint = Paint()



    var canUpdate = false



    fun mainX(b:Int):String {

        val st = String.format("%02X", b)

        return st
    }

    private var surfaceHolder: SurfaceHolder = this.holder

    private val screenBuffer = BooleanArray(128 * 128) {
        false
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }


    private fun init() {
        wavePaint.apply {
            color = getColor(R.color.wave_color)
            style = Paint.Style.FILL
        }

        bgPaint.apply {
            color = getColor(R.color.gray)
            style = Paint.Style.FILL

        }


    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }


    private fun getColor(resource_id: Int): Int {
        return ContextCompat.getColor(context, resource_id)
    }


    fun ptOn(x:Int,y:Int){
        screenBuffer[y*128+x]=true
    }

    fun ptOff(x:Int,y:Int){
        screenBuffer[y*128+x]=false
    }


    override fun run() {
        while (true) {
            sleep(10)
            if (surfaceHolder.surface.isValid && canUpdate) {
                canUpdate = false
                val canvas = surfaceHolder.lockCanvas()
                val h = height.toFloat() / 128
                val w = width.toFloat() / 128
                for (k in 0 until 128) {
                    for (j in 0 until 128) {
                        if (screenBuffer[j * 128 + k]) {
                            canvas.drawRect(k * w, j * h, k * w + w, j * h + h, bgPaint)
                        } else {
                            canvas.drawRect(k * w, j * h, k * w + w, j * h + h, wavePaint)
                        }
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }




    inner class chipRun : TimerTask() {
        override fun run() {
            canUpdate=true
        }
    }


    fun startGame(){
        Timer().schedule(chipRun(), Date(),30)
    }


    fun resume() {
        val thread = Thread(this)
        thread.start()
    }
}