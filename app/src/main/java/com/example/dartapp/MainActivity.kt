package com.example.dartapp

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {

    private var totalSum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val sumText      = findViewById<TextView>(R.id.sumText)
        val switchDouble = findViewById<Switch>(R.id.switchDouble)
        val switchTriple = findViewById<Switch>(R.id.switchTriple)
        val btnReset     = findViewById<Button>(R.id.btnReset)
        val dartboard    = findViewById<DartboardView>(R.id.dartboardView)

        switchDouble.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) switchTriple.isChecked = false
        }
        switchTriple.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) switchDouble.isChecked = false
        }

        dartboard.onSegmentClick = { baseValue ->
            val score = when {
                switchDouble.isChecked -> baseValue * 2
                switchTriple.isChecked -> baseValue * 3
                else                   -> baseValue
            }
            totalSum += score
            sumText.text = totalSum.toString()

            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }

        btnReset.setOnClickListener {
            totalSum = 0
            sumText.text = "0"
            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }
    }
}
