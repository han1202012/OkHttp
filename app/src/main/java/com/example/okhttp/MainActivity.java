package com.example.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.okhttp.databinding.ActivityMainBinding;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

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

    /**
     * Activity 跨页访问的面请求码
     */
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EasyPermissions.requestPermissions(
                this,
                "权限申请原理对话框 : 描述申请权限的原理",
                100,

                // 下面是要申请的权限 可变参数列表
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        mOkHttpClient = new OkHttpClient();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //httpSynchronousGet();
                //httpAsynchronousGet();
                //httpSynchronousPost();
                //httpAsynchronousPost();
                httpUploadPhoto();
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

    /**
     * 上传图片
     */
    private void httpUploadPhoto() {
        // 申请权限
        AndPermission.with(this)
                .runtime()
                .permission(
                        // 申请 SD 卡权限
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE
                ).onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 所有权限都通过
                        // 跳转到相册界面
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }).onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 存在至少 1 个权限被拒绝
                    }
                }).start();
    }

    /**
     * 在相册中选择图片返回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && data != null) {
            // 获取图像 Uri
            Uri imageUri = data.getData();
            // 要查询的列字段名称
            String[] filePathColumns = {MediaStore.Images.Media.DATA};

            // 到数据库中查询 , 查询 _data 列字段信息
            Cursor cursor = getContentResolver().query(
                    imageUri,
                    filePathColumns,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            // 获取 _data 列所在的列索引
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            // 获取图片的存储路径
            String filePath = cursor.getString(columnIndex);

            // 使用 OkHttp 上传图片
            upload(filePath);

            // 获取数据完毕后, 关闭游标
            cursor.close();
        }
    }

    /**
     * 使用 OkHttp 上传图片
     * @param filePath
     */
    private void upload(String filePath){
        File file = new File(filePath);

        // 请求体
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        MultipartBody.create(MediaType.parse("multipart/form-data"), file)
                ).build();

        // Post 请求
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(body)
                .build();

        // 执行异步请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                // 上传完毕
            }
        });
    }


}