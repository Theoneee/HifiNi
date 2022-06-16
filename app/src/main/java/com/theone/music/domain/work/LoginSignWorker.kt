package com.theone.music.domain.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.theone.music.app.ext.md5encode
import com.theone.music.data.repository.ApiRepository
import kotlinx.coroutines.*

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2022-06-14 08:06
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LoginSignWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{

        const val TAG = "com.theone.music.domain.work.LoginSignWorker"

        const val EMAIL = "worker_email"
        const val PASSWORD = "worker_password"

    }

    override fun doWork(): Result {
        runBlocking {
            try {
                coroutineScope {
                    val email = inputData.getString(EMAIL)
                    val password = inputData.getString(PASSWORD)
                    if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
                        ApiRepository.INSTANCE.login(email, password.md5encode()).await()
                        ApiRepository.INSTANCE.sign().await()
                    }else{
                        return@coroutineScope Result.failure()
                    }
                }
            } catch (e: Throwable) {
                return@runBlocking Result.failure()
            }
        }
        return Result.success()
    }

}