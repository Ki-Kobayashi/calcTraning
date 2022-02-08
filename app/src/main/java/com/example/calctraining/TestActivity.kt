package com.example.calctraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.calctraining.consts.Consts
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bundle = intent.extras!!
        val numberOfQuestion = bundle.getInt(Consts.IntentKey.NUMBER_OF_QUESTION)
        textViewRemaining.text = numberOfQuestion.toString()
        giveQuestion()
    }

    /**
     * 問題を出す
     */
    private fun giveQuestion(){
        unEnableView(btnBack)

    }

    /**
     * Viewの非活性化
      */
    private fun unEnableView(view: View){
        view.isEnabled = false
        view.setBackgroundColor(resources.getColor(R.color.white_gray))
    }
}