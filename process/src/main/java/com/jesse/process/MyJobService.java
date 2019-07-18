package com.jesse.process;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

@SuppressLint("NewApi")
public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    public static void startJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        //setPersisted 在设备重启依然执行
        JobInfo.Builder builder = new JobInfo.Builder(8, new ComponentName(context.getPackageName(),
                MyJobService.class.getName())).setPersisted(true);

        // 小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 每隔 1s 执行一次 job
            // 版本23 开始 进行了改进，最小周期为 5s
            builder.setPeriodic(1000);
        } else {
            // 延迟执行任务
            builder.setMinimumLatency(1000);
        }

        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob");

        // 如果7.0以上 轮询
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startJob(this);
        }
        boolean isLocal = Utils.isRunningService(this, LocalService.class.getName());
        boolean isRemote = Utils.isRunningService(this, RemoteService.class.getName());
        if (!isLocal || !isRemote) {
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
