package com.wzjer.fucklib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wzjer.fucklib.service.Beacon;

public class MainActivity extends AppCompatActivity {

    Context context;
    Beacon b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // 获取核心按钮
        Button btn = findViewById(R.id.start_broadcast);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("开始发布广播".contentEquals(btn.getText())) {
                    btn.setText("停止发布广播");
                    b = new Beacon(context);
                } else {
                    btn.setText("开始发布广播");
                    b.stopAdvertising();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (b != null) {
            b.stopAdvertising();
        }
    }

}