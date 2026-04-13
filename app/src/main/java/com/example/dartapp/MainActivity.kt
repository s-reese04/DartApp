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
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private var playerCount = 2;
    private var currentPlayer = 0;
    private val playerScores = mutableListOf<Int>()
    private var currentTry = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        playerCount = intent.getIntExtra("playerCount", 2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val playerGroup  = findViewById<RadioGroup>(R.id.playerGroup)
        val titleText    = findViewById<TextView>(R.id.titleText)
        val sumText      = findViewById<TextView>(R.id.sumText)
        val switchDouble = findViewById<Switch>(R.id.switchDouble)
        val switchTriple = findViewById<Switch>(R.id.switchTriple)
        val btnReset     = findViewById<Button>(R.id.btnReset)
        val dartboard    = findViewById<DartboardView>(R.id.dartboardView)

        //Set Up Players
       for(i in 1..playerCount){
           playerScores.add(i-1, 301)
       }

        for(i in 0 until playerCount){
            val btn = RadioButton(this).apply {
                text = "Player${i+1}"
                id = View.generateViewId()
            }
            playerGroup.addView(btn)

            if(i == 0){
                playerGroup.check(btn.id)
            }
        }

        titleText.text = "Player ${currentPlayer+1}"

        playerGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = group.indexOfChild(group.findViewById(checkedId))
            currentPlayer = index

            sumText.text = playerScores[currentPlayer].toString()
            titleText.text = "Player ${currentPlayer+1}"
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

            currentTry+=1

            if(currentTry == 3){
                if(currentPlayer < playerCount-1){
                    currentPlayer += 1
                    currentTry = 0
                }else{
                    currentPlayer = 0
                    currentTry = 0
                }

            }

            titleText.text = "Player ${currentPlayer+1}"

            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }

        btnReset.setOnClickListener {
            playerScores.set(currentPlayer, 301)
            sumText.text = "301"
            switchDouble.isChecked = false
            switchTriple.isChecked = false
        }
    }
}
