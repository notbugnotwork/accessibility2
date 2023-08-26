package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class LogSystem extends Activity {

    private Uri selectedDirectoryUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public enum LogLevel {
        INFO, DEBUG, WARNING, ERROR
    }

    private static LogLevel logLevel = LogLevel.INFO;
    public static void setLogLevel(LogLevel level) {
        logLevel = level;
    }


    private static LinearLayout logLayout;
    public static void initialize(Context context, ViewGroup container) {
        logLayout = (LinearLayout) container;
    }


    private static final int BUFFER_SIZE = 2048;
    private static byte[] buffer = new byte[BUFFER_SIZE];
    private static final String TAG = "My App";

    public static void log(Context context,LogLevel level, String message) {
        // 创建日志消息
        String logMessage = level.toString() + "::" + message;
        Log.i(TAG,logMessage);
        // 将日志消息写入缓冲区
        writeData(logMessage.getBytes(),context);
    }

    private static void writeData(byte[] data,Context context){
        if (isBufferFull(data.length)) {
            Log.i(TAG,"缓冲区已满，执行保存操作");
            // 如果缓冲区已满，执行保存操作
            saveBuffer(context);
        }

        // 将数据写入缓冲区
        System.arraycopy(data, 0, buffer, getCurrentBufferSize(), data.length);
    }
    private static boolean isBufferFull(int dataSize){
        return getCurrentBufferSize() + dataSize > (int)(BUFFER_SIZE*0.9);
    }
    public static int getCurrentBufferSize() {
        Log.i(TAG,"获取当前Buffersize");
        int currentSize = 0;
        while (currentSize < BUFFER_SIZE && buffer[currentSize] != 0) {
            currentSize++;
        }
        Log.i(TAG,"目前Buffer大小:"+currentSize);

        return currentSize;
    }
    private static void saveBuffer(Context context) {
        // 执行保存操作，将缓冲区数据写入日志文件或其他位置
        // 在这里你需要实现具体的保存逻辑
        // 示例：保存到日志文件
        Log.i(TAG,"将数据写入缓冲区");
        String logData = new String(buffer, 0, getCurrentBufferSize());

        saveToFile(context,logData);

    }
    public static void saveToFile(Context context,String logData) {
        // 将日志数据写入文件的逻辑
        try {
            Log.i(TAG,"进入到日志数据写入文件的逻辑");
            File logFile = new File(context.getExternalFilesDir(null), "app_log.txt");
            FileWriter writer = new FileWriter(logFile, true); // 追加到文件末尾
            writer.write(logData);
            writer.close();
            Log.i(TAG,"关闭输入流");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 重置缓冲区
        resetBuffer();
    }
    public static String readFromFile(Context context) {
        StringBuilder data = new StringBuilder();

        try {
            Log.i(TAG, "开始读取日志文件");
            File logFile = new File(context.getExternalFilesDir(null), "app_log.txt");

            if (logFile.exists()) {
                FileReader reader = new FileReader(logFile);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line).append("\n");
                }

                reader.close();
                Log.i(TAG, "日志文件读取完成");
            } else {
                Log.i(TAG, "日志文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data.toString();
    }


    private static void resetBuffer() {
        Log.i(TAG,"重置缓冲区");
        buffer = new byte[BUFFER_SIZE];
    }

    public static void info(Context context,String message) {
        log(context,LogLevel.INFO, message);
    }

    public static void debug(Context context,String message) {
        log(context,LogLevel.DEBUG, message);
    }

    public static void warning(Context context,String message) {
        log(context,LogLevel.WARNING, message);
    }

    public static void error(Context context,String message) {
        log(context,LogLevel.ERROR, message);
    }

//    private static String formatLogMessage(LogLevel level, String message) {
//        String timestamp = dateFormat.format(new Date());
//        return String.format("[%s] [%s] - %s", timestamp, level.toString(), message);
//    }


}
