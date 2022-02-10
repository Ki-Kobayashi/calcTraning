package com.example.calctraining

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.calctraining.consts.Consts
import kotlinx.android.synthetic.main.activity_test.*
import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random

class TestActivity : AppCompatActivity(), View.OnClickListener {
        private var mGameFinished = false
        private var mRemaining = 0

        // TODO:正解数。正答率追加
        private var mIntFormulaLeft = 0
        private var mIntFormulaRight = 0
        private var mIntOperator = 0
        private var mIntAns = 0
        private var mStringAns = ""

        private lateinit var mTimer: Timer
        private lateinit var mHandler: Handler

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_test)

                initView()

                // 残り問題数が0になるまで出題
                for (i in mRemaining downTo 0) {
                        textViewRemaining.text = i.toString()
                        if (i == 0) {
                                gameFinished()
                                return
                        }

                        giveQuestion()
                }
        }

        // TODO:正答率・正回数を表示
//        /**
//         * 問題終了かチェック
//         */
//        private fun isGameFinish(): Boolean{
//                if (textViewRemaining.toString().toInt() == 0) {
//                        return true
//                }
//
//        }

        /**
         * Vieｗの初期設定
         */
        private fun initView(){
                val bundle = intent.extras!!
                mRemaining = bundle.getInt(Consts.IntentKey.NUMBER_OF_QUESTION)

                disableView(btnBack)
                disableView(btnAnsChk)

                btnAnsChk.setOnClickListener { chkAnswer(textViewAnswer.toString()) }

                btn0.setOnClickListener(this)
                btn1.setOnClickListener(this)
                btn2.setOnClickListener(this)
                btn3.setOnClickListener(this)
                btn4.setOnClickListener(this)
                btn5.setOnClickListener(this)
                btn6.setOnClickListener(this)
                btn7.setOnClickListener(this)
                btn8.setOnClickListener(this)
                btn9.setOnClickListener(this)
                btnClear.setOnClickListener(this)
                btnMinus.setOnClickListener(this)
        }

        /**
         * 問題出題
         */
        private fun giveQuestion() {
                initQuestion()

                mIntFormulaLeft = Random.nextInt(0, 100) + 1
                mIntFormulaRight = Random.nextInt(0, 100) + 1
                mIntOperator = Random.nextInt(0, 2) + 1

                textViewLeft.text = mIntFormulaLeft.toString()
                textViewRight.text = mIntFormulaRight.toString()

                when (mIntOperator) {
                        1 -> {
                                textViewOperator.text = "+"
                                mIntAns = mIntFormulaLeft + mIntFormulaRight
                        }
                        2 -> {
                                textViewOperator.text = "-"
                                mIntAns = mIntFormulaLeft - mIntFormulaRight
                        }
                }
        }

        /**
         * Viewの非活性化
         */
        private fun disableView(v: View) {
                v.isEnabled = false
                v.setBackgroundColor(resources.getColor(R.color.white_gray))
        }

        /**
         * Viewの活性化
         */
        private fun enabieView(v: View){
                v.isEnabled = true
                v.setBackgroundColor(resources.getColor(R.color.purple_200))
        }

        /**
         * 問題の初期化
         */
        private fun initQuestion() {
                textViewAnswer.text = ""
                imageViewIsCorrect.visibility = View.INVISIBLE
                textViewCorrect.visibility = View.INVISIBLE
        }


        /**
         * 回答チェック処理
         */
        private fun chkAnswer(userAns: String) {
                if(userAns.isEmpty() || userAns.isBlank()) return

                val intUserAns = userAns.toInt()
                enabieView(btnAnsChk)

                if (intUserAns == mIntAns) {
                        imageViewIsCorrect.setImageResource(R.drawable.pic_correct)
                        textViewCorrect.text = resources.getString(R.string.is_correct)
                } else {
                        imageViewIsCorrect.setImageResource(R.drawable.pic_incorrect)
                        textViewCorrect.text = resources.getString(R.string.is_not_correct)
                }
                imageViewIsCorrect.visibility = View.VISIBLE
                textViewCorrect.visibility = View.VISIBLE

                // TODO:タイマーセット
        }

        /**
         * 計算ゲーム終了
         */
        private fun gameFinished() {
                textViewCorrect.text = resources.getString(R.string.game_finished)
                textViewCorrect.visibility = View.VISIBLE

                enabieView(btnBack)

                // TODO:戻る以外ボタンを押せなくする
                // TODO:戻るボタン押下でTOP画面へ

        }

        /**
         *   ユーザーの入力値をセット
         */
        private fun setAnswerText(btnAns: String){
                textViewAnswer.text = mStringAns + btnAns
        }

        override fun onClick(v: View?) {

                when(v){
                        btn0 -> {
                                if (mStringAns.isNotBlank()) textViewAnswer.text = "-"
                        }
                        btn1 -> setAnswerText("0")
                        btn2 -> setAnswerText("0")
                        btn3 -> setAnswerText("0")
                        btn4 -> setAnswerText("0")
                        btn5 -> setAnswerText("0")
                        btn6 -> setAnswerText("0")
                        btn7 -> setAnswerText("0")
                        btn8 -> setAnswerText("0")
                        btn9 -> setAnswerText("0")
                        btnClear -> {
                                textViewAnswer.text = ""
                                mStringAns = ""
                        }
                        btnMinus -> {
                                if (mStringAns.isBlank()) textViewAnswer.text = "-"
                        }
                }
        }


}