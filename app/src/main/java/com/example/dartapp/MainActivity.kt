package com.example.dartapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {

    private val playerCount = 2;
    private var currentPlayer = 0;
    private val playerScores = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        //Set Up Players
       for(i in 1..playerCount){
           playerScores.add(i-1, 301)
       }
        val playerGroup = findViewById<RadioGroup>(R.id.playerGroup)

        val sumText      = findViewById<TextView>(R.id.sumText)
        val switchDouble = findViewById<Switch>(R.id.switchDouble)
        val switchTriple = findViewById<Switch>(R.id.switchTriple)
        val btnReset     = findViewById<Button>(R.id.btnReset)
        val dartboard    = findViewById<DartboardView>(R.id.dartboardView)

        for(i in 0 until playerCount){
            val btn = RadioButton(this).apply {
                val index = i +1
                text = "Player$index"
                id = View.generateViewId()
            }
            playerGroup.addView(btn)

            if(i == 0){
                playerGroup.check(btn.id)
            }
        }

        playerGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = group.indexOfChild(group.findViewById(checkedId))
            currentPlayer = index

            sumText.text = playerScores[currentPlayer].toString()
        }

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
            val currentScore = playerScores.get(currentPlayer) - score;
            playerScores.set(currentPlayer,currentScore)
            sumText.text = playerScores.get(currentPlayer).toString()

            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }

        btnReset.setOnClickListener {
            playerScores.set(currentPlayer, 0)
            sumText.text = "0"
            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }
    }
}
