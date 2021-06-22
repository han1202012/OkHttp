package com.example.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.okhttp.databinding.ActivityMainBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * ViewBinding 类
     * activity_main 布局映射出来的类
     * 该类主要作用是封装组件的获取
     */
    ActivityMainBinding binding;

    /**
     * OkHttp 客户端
     * 注意 : 该类型对象较大, 尽量在应用中创建较少的该类型对象
     * 推荐使用单例
     */
    OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOkHttpClient = new OkHttpClient();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //httpSynchronousGet();
                //httpAsynchronousGet();
                //httpSynchronousPost();
                httpAsynchronousPost();
            }
        });
    }

    /**
     * OkHttp 同步 Get 请求
     */
    private void httpSynchronousGet() {
        // Request 中封装了请求相关信息
        Request request = new Request.Builder()
                .url("https://www.baidu.com")   // 设置请求地址
                .get()                          // 使用 Get 方法
                .build();

        // 同步 Get 请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "result : " + result);
            }
        }).start();
    }

    /**
     * OkHttp 异步 Get 请求
     */
    private void httpAsynchronousGet() {
        // Request 中封装了请求相关信息
        Request request = new Request.Builder()
                .url("https://www.baidu.com")   // 设置请求地址
                .get()                          // 使用 Get 方法
                .build();

        // 创建异步回调
        Callback callback = new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功 , 获取
                String result = response.body().string();
                Log.i(TAG, "result : " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 主线程中执行相关代码
                    }
                });
            }
        };

        // 异步 Get 请求
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * OkHttp 同步 Post 请求
     */
    private void httpSynchronousPost() {
        // 创建 Post 表单 , 主要用于设置 Post 请求键值对
        FormBody formBody = new FormBody.Builder()
                .add("Key", "Value")
                .build();

        // Request 中封装了请求相关信息
        Request request = new Request.Builder()
                .url("https://www.baidu.com")   // 设置请求地址
                .post(formBody)                 // 使用 Post 方法
                .build();

        // 同步 Get 请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "result : " + result);
            }
        }).start();
    }

    /**
     * OkHttp 异步 Post 请求
     */
    private void httpAsynchronousPost() {
        // 创建 Post 表单 , 主要用于设置 Post 请求键值对
        FormBody formBody = new FormBody.Builder()
                .add("Key", "Value")
                .build();

        // Request 中封装了请求相关信息
        Request request = new Request.Builder()
                .url("https://www.baidu.com")   // 设置请求地址
                .post(formBody)                 // 使用 Post 方法
                .build();

        // 创建异步回调
        Callback callback = new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功 , 获取
                String result = response.body().string();
                Log.i(TAG, "result : " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 主线程中执行相关代码
                    }
                });
            }
        };

        // 异步 Get 请求
        mOkHttpClient.newCall(request).enqueue(callback);
    }


}