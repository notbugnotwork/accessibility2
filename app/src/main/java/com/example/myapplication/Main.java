package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.logging.Logger;

public class Main extends Activity {
    public static void main(String[] args) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button write = findViewById(R.id.button);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里定义按钮点击时要执行的操作
                // 例如，显示一个Toast消息
                LogSystem.info(getApplicationContext(),"Button Clicked");
                int i = LogSystem.getCurrentBufferSize();
                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });
        Button read = findViewById(R.id.button2);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String readText =  LogSystem.readFromFile(getApplicationContext());
                Toast.makeText(getApplicationContext() ,readText.length(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
