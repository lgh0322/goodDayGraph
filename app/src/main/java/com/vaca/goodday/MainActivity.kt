package com.vaca.goodday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import com.vaca.goodday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var vibrator: Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.up.setOnClickListener {
            vibrator.vibrate(100)
        }


        binding.down.setOnClickListener {
            vibrator.vibrate(100)
        }


        binding.left.setOnClickListener {
            vibrator.vibrate(100)

        }


        binding.right.setOnClickListener {
            vibrator.vibrate(100)

        }
    }
}