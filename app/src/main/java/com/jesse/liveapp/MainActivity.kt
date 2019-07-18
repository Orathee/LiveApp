package com.jesse.liveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jesse.liveapp.account.AccountHelper
import com.jesse.liveapp.activity.KeepManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1像素保活
//        KeepManager.getInstance().registerKeep(this)

        //前台服务保活
//        startService(Intent(this, ForegroundService::class.java))

        //sticky
//        startService(Intent(this, StickyService::class.java))

        //账户拉活
        AccountHelper.addAccount(this)
        AccountHelper.autoSync()

        //JobService
//        MyJobService.startJob(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        KeepManager.getInstance().unregisterKeep(this)
    }
}
