package com.vaca.goodday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import com.vaca.chip8.view.CommonView
import com.vaca.goodday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var vibrator: Vibrator
    lateinit var fuck:CommonView


    var x=20;
    var y=30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding= ActivityMainBinding.inflate(layoutInflater)
        fuck=binding.fuck
        setContentView(binding.root)


        binding.up.setOnClickListener {
            vibrator.vibrate(100)
            fuck.ptOn(x,y)
            y--
        }


        binding.down.setOnClickListener {
            vibrator.vibrate(100)
            fuck.ptOn(x,y)
            y++
        }


        binding.left.setOnClickListener {
            vibrator.vibrate(100)
            fuck.ptOn(x,y)
            x--
        }


        binding.right.setOnClickListener {
            vibrator.vibrate(100)
            fuck.ptOn(x,y)
            x++
        }


        binding.fuck.resume()
        binding.fuck.startGame()
    }
}