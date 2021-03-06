package io.github.anotherjack.avoidonresultdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.github.anotherjack.avoidonresult.AvoidOnResult
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //callback方式
        callback.setOnClickListener {
            AvoidOnResult(this).startForResult(FetchDataActivity::class.java, object : AvoidOnResult.Callback {
                override fun onActivityResult(resultCode: Int, data: Intent?) =
                        if (resultCode == Activity.RESULT_OK) {
                            val text = data?.getStringExtra("text")
                            Toast.makeText(this@MainActivity, "callback -> " + text, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "callback canceled", Toast.LENGTH_SHORT).show()
                        }

            })
        }

        //callback方式（封装了一下）
        callback.setOnLongClickListener {
            startActivityForResult(Intent(this, FetchDataActivity::class.java), this,
                    AvoidOnResult.Callback { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            val text = data?.getStringExtra("text")
                            Toast.makeText(this@MainActivity, "callback base -> " + text, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "callback base canceled", Toast.LENGTH_SHORT).show()
                        }
                    })
            false
        }

        //rxjava方式
        rxjava.setOnClickListener {
            AvoidOnResult(this)
                    .startForResult(FetchDataActivity::class.java)
                    //下面可自由变换
                    .filter { it.resultCode == Activity.RESULT_OK }
                    .flatMap {
                        val text = it.data.getStringExtra("text")
                        Observable.fromIterable(text.asIterable())
                    }
                    .subscribe({
                        Log.d("-------> ", it.toString())
                    }, {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                    })
        }

        //rxjava方式（封装了一下）
        rxjava.setOnLongClickListener {
            startActivityForResult(Intent(this, FetchDataActivity::class.java), this)
                    //下面可自由变换
                    .filter { it.resultCode == Activity.RESULT_OK }
                    .flatMap {
                        val text = it.data.getStringExtra("text")
                        Observable.fromIterable(text.asIterable())
                    }
                    .subscribe({
                        Log.d("-------> ", it.toString())
                    }, {
                        Toast.makeText(this, "error base", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(this, "complete base", Toast.LENGTH_SHORT).show()
                    })

            false
        }

        //普通方式
        normal.setOnClickListener {
            val intent = Intent(this, FetchDataActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val text = data?.getStringExtra("text")
            Toast.makeText(this, "normal -> " + text, Toast.LENGTH_SHORT).show()
        }
    }
}
