package io.dcloud.uniplugin;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import io.dcloud.feature.uniapp.UniAppHookProxy;


public class Fcm_AppProxy implements UniAppHookProxy {
    @Override
    public void onCreate(Application application) {
        //可写初始化触发逻辑
        Toast.makeText(application.getBaseContext(),"获取Token：onCreate", Toast.LENGTH_SHORT).show();
//        FirePush.getToken(application.getBaseContext());

    }

    @Override
    public void onSubProcessCreate(Application application) {
        //子进程初始化回调
    }
}
