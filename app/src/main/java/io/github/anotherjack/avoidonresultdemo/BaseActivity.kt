package io.github.anotherjack.avoidonresultdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import io.github.anotherjack.avoidonresult.ActivityResultInfo
import io.github.anotherjack.avoidonresult.AvoidOnResult
import io.reactivex.Observable

/**
 * created by Meiji on 2018/7/16.
 */
open class BaseActivity : AppCompatActivity() {

    fun startActivityForResult(intent: Intent, activity: BaseActivity, call: AvoidOnResult.Callback) {
        AvoidOnResult(activity).startForResult(intent, call)
    }

    fun startActivityForResult(intent: Intent, activity: BaseActivity): Observable<ActivityResultInfo> {
        return AvoidOnResult(activity).startForResult(intent)
    }
}