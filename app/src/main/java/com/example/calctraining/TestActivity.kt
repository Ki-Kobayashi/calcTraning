package com.example.calctraining

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calctraining.consts.Consts
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class TestActivity : AppCompatActivity(), View.OnClickListener {
        private val TAG = this::class.java.simpleName
        private var mGameFinished = false
        private lateinit var soundPool: SoundPool
        private lateinit var mTimer: Timer

        // 音源のリソースID
        private var intSoundId_Correct = 0
        private var intSoundId_InCorrect = 0
        // 残りの問題数
        private var mRemaining = 0
        // 正解数
        private var mCountCorrect = 0
        // 正答率
        private var mCountPoint = 0

        private var mNowQuestionNo = 0
        private var mIntFormulaLeft = 0
        private var mIntFormulaRight = 0
        private var mIntOperator = 0
        private var mRealAns = 0

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_test)

                initView()
                giveQuestion()
        }

        override fun onResume() {
                super.onResume()
                mTimer = Timer()
                // 音の準備は「onResume」メソッドでやる
                soundPool =
                        SoundPool.Builder().setAudioAttributes(
                                AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                        ).setMaxStreams(1).build()

                // 音源をセット
                intSoundId_Correct = soundPool.load(this, R.raw.sound_correct, 1)
                intSoundId_InCorrect = soundPool.load(this, R.raw.sound_incorrect, 1)
        }

        override fun onPause() {
                super.onPause()
                // TODO:★resumeでインスタンス化したものは、ここで後処理をする
                soundPool.release()
                mTimer.cancel()
        }

        /**
         * Vieｗの初期設定
         */
        private fun initView() {
                val bundle = intent.extras!!
                mRemaining = bundle.getInt(Consts.IntentKey.NUMBER_OF_QUESTION)
                textViewRemaining.text = mRemaining.toString()

                disableView(btnBack)
                disableView(btnAnsChk)

                btnAnsChk.setOnClickListener { chkAnswer(textViewAnswer.text.toString()) }
                btnBack.setOnClickListener { finish() }

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
         * 問題の初期化
         */
        private fun initQuestion() {
                textViewAnswer.text = ""
                imageViewIsCorrect.visibility = View.INVISIBLE
                textViewIsCorrectMsg.visibility = View.INVISIBLE
                enableAllNumBtn()
                disableView(btnAnsChk)
        }

        /**
         * 問題出題
         */
        private fun giveQuestion() {
                // 前の回答など初期化
                initQuestion()

                mIntFormulaLeft = Random.nextInt(0, 100) + 1
                mIntFormulaRight = Random.nextInt(0, 100) + 1
                mIntOperator = Random.nextInt(0, 2) + 1

                textViewLeft.text = mIntFormulaLeft.toString()
                textViewRight.text = mIntFormulaRight.toString()

                when (mIntOperator) {
                        1 -> {
                                textViewOperator.text = "+"
                                mRealAns = mIntFormulaLeft + mIntFormulaRight
                        }
                        2 -> {
                                textViewOperator.text = "-"
                                mRealAns = mIntFormulaLeft - mIntFormulaRight
                        }
                }
        }

        /**
         * 回答チェック処理
         */
        private fun chkAnswer(userAns: String) {
                if (userAns.isEmpty() || userAns.isBlank()) return
                // 数字入力の無効化
                disableAllNumBtn()
                val intUserAns = userAns.toInt()

                if (intUserAns == mRealAns) {
                        // 正解の場合
                        mCountCorrect++
                        textViewCorrect.text = mCountCorrect.toString()
                        imageViewIsCorrect.setImageResource(R.drawable.pic_correct)
                        textViewIsCorrectMsg.text = resources.getString(R.string.is_correct)
                        soundPool.play(intSoundId_Correct, 1.0f, 1.0f, 0, 0, 1.0f)
                } else {
                        //不正解の場合
                        imageViewIsCorrect.setImageResource(R.drawable.pic_incorrect)
                        textViewIsCorrectMsg.text = resources.getString(R.string.is_not_correct)
                        soundPool.play(intSoundId_InCorrect, 1.0f, 1.0f, 0, 0, 1.0f)
                }

                imageViewIsCorrect.visibility = View.VISIBLE
                textViewIsCorrectMsg.visibility = View.VISIBLE

                // 残り問題数
                mNowQuestionNo += 1
                textViewRemaining.text = (mRemaining - mNowQuestionNo).toString()
                // 正解数
                textViewCorrect.text = mCountCorrect.toString()
                // 正答率
                if (mCountCorrect != 0) {
                        textViewPoint.text =
                                ((mCountCorrect.toDouble() / mNowQuestionNo.toDouble()) * 100).toInt()
                                        .toString()
                }


                // タイマーセット
                if (mRemaining == mNowQuestionNo) {
                        gameFinished()
                        return
                } else {
                        mTimer.schedule(1000) { runOnUiThread { giveQuestion() } }
                }
        }

        /**
         * 計算ゲーム終了
         */
        private fun gameFinished() {
                // TODO: ゲーム終了のポップアップ表示
//                textViewCorrect.text = resources.getString(R.string.game_finished)
//                textViewCorrect.visibility = View.VISIBLE

                enableView(btnBack)
                disableView(btnAnsChk)
                textViewIsCorrectMsg.text = "テスト終了"
        }

        override fun onClick(v: View?) {
                val button = v as Button

                when (v) {
                        btn0 -> {
                                if (textViewAnswer.text.toString() == "0" || textViewAnswer.text.toString() == "-") return
                                textViewAnswer.append(button.text)
                                enableView(btnAnsChk)
                        }
                        btnClear -> {
                                textViewAnswer.text = ""
                                disableView(btnAnsChk)
                        }
                        btnMinus -> {
                                if (textViewAnswer.text.isBlank()) {
                                        textViewAnswer.text = "-"
                                        disableView(btnAnsChk)
                                }
                        }
                        else -> {
                                if (textViewAnswer.text.toString() == "0") {
                                        // 0が入力されている場合は、押下された数字に置き換える
                                        textViewAnswer.text = button.text
                                } else {
                                        textViewAnswer.append(button.text)
                                }
                                enableView(btnAnsChk)
                        }
                }
        }

        /**
         * 数字入力の無効化
         */
        private fun disableAllNumBtn() {
                disableView(btn0)
                disableView(btn1)
                disableView(btn2)
                disableView(btn3)
                disableView(btn4)
                disableView(btn5)
                disableView(btn6)
                disableView(btn7)
                disableView(btn8)
                disableView(btn9)
                disableView(btnMinus)
                disableView(btnClear)
        }

        /**
         * 数字入力の無効化
         */
        private fun enableAllNumBtn() {
                enableView(btn0)
                enableView(btn1)
                enableView(btn2)
                enableView(btn3)
                enableView(btn4)
                enableView(btn5)
                enableView(btn6)
                enableView(btn7)
                enableView(btn8)
                enableView(btn9)
                enableView(btnMinus)
                enableView(btnClear)
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
        private fun enableView(v: View) {
                v.isEnabled = true
                v.setBackgroundColor(resources.getColor(R.color.purple_500))
        }
}