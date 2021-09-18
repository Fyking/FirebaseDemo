package io.dcloud.uniplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;


public class FcmModule extends UniModule {

    String TAG = "FcmModule";
    public static int REQUEST_CODE = 1000;

    @UniJSMethod(uiThread = true)
    public void getToken(UniJSCallback callback){
        //获取推送Token
        try {
            Toast.makeText(mUniSDKInstance.getContext(),"获取DId：" + FirePush.getDeviceId(mUniSDKInstance.getContext()), Toast.LENGTH_SHORT).show();
            String ckToken = FirePush.checkToken(mUniSDKInstance.getContext().getApplicationContext());
            if(!ckToken.isEmpty()){
                JSONObject data = new JSONObject();
                data.put("success",true);
                data.put("message", "Fetching FCM registration token Successful");
                data.put("token", ckToken);
                callback.invoke(data);
            }else{
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    String msg = "Fetching FCM registration token Successful";
                    if (!task.isSuccessful()) {
                        msg = "Fetching FCM registration token failed";
                        Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    if(token != null&&!token.isEmpty()) FirePush.saveToken(mUniSDKInstance.getContext().getApplicationContext(),token);
                    if(callback != null) {
                        JSONObject data = new JSONObject();
                        data.put("success",task.isSuccessful());
                        data.put("message", msg);
                        data.put("token", token);
                        callback.invoke(data);
                    }
                });
            }
        }catch (Exception ex){
            if(callback != null) {
                JSONObject data = new JSONObject();
                data.put("success",false);
                data.put("message","register token error：" + ex.getMessage());
                data.put("token", "");
                callback.invoke(data);
            }
            Log.e(TAG, "Fetching FCM registration token failed", ex);
        }
    }

    @UniJSMethod(uiThread = true)
    public void testPush(){
        //获取推送Token
        FirePush.sendNotification(mUniSDKInstance.getContext(),"你好","测试信息");
    }

    //run ui thread
    @UniJSMethod(uiThread = true)
    public void testAsyncFunc(JSONObject options, UniJSCallback callback) {
        Log.e(TAG, "testAsyncFunc--"+options);
        if(callback != null) {
            JSONObject data = new JSONObject();
            data.put("code", "success");
            callback.invoke(data);
            //callback.invokeAndKeepAlive(data);
        }
    }

    //run JS thread
    @UniJSMethod (uiThread = false)
    public JSONObject testSyncFunc(){
        JSONObject data = new JSONObject();
        data.put("code", "success");
        return data;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && data.hasExtra("respond")) {
            Log.e("TestModule", "原生页面返回----"+data.getStringExtra("respond"));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
