package com.jesse.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 双进程 + 前台服务 + job
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
        MyJobService.startJob(this);
    }
}
