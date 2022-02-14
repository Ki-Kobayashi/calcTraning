package com.example.calctraining

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.calctraining.consts.Consts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
        private val TAG = this::class.java.simpleName

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                // 1つ目の方法
//                val arrayAdapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item)
//                arrayAdapter.add(10)
//                arrayAdapter.add(20)
//                arrayAdapter.add(30)

                //  TODO:2つ目の方法🌟これでアイテムリストをすでに持ったAdapterがインスタンス化される
                val arrayAdapter = ArrayAdapter.createFromResource(
                        this,
                        R.array.num_of_question,
                        android.R.layout.simple_spinner_item //スピナーを作るときのおまじない
                )
                // スピナーとアダプター（選択肢）をつなぐ
                spinnerSelectQuestion.adapter = arrayAdapter
                btnGameStart.setOnClickListener { setAdapterGameSpinner(spinnerSelectQuestion) }
        }

        /**
         * ゲームのスタートボタン押下時の処理
         */
        private fun setAdapterGameSpinner(spinner: Spinner) {
                // ユーザーが選択したアイテムをリソースからGET
                val numberOfQuestion = spinner.selectedItem.toString().toInt()
                // 画面遷移
                val intent = Intent(this@MainActivity, TestActivity::class.java)
                intent.putExtra(Consts.IntentKey.NUMBER_OF_QUESTION, numberOfQuestion)
                startActivity(intent)
        }

}