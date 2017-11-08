package com.myview.acti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myview.R;

public class TwoActivity extends Activity {

    private static Context mContext;
    private SharedPreferences mIsCreateShoutcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        mIsCreateShoutcut = getSharedPreferences("isCreateShoutcut", MODE_PRIVATE);

        createShutCut();
        Button viewById = (Button) findViewById(R.id.ttttt);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createShutCut() {
        boolean aBoolean = mIsCreateShoutcut.getBoolean("isCreateShoutcut", false);
        if (!aBoolean){
            //快捷图标生成的位置? 桌面 设计的到不同软件的事件传递
            Intent intent=new Intent();
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            //  传递的图片
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            // 软件的名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "我是快捷图标");
            // 点击这个快捷图片后的行为
            Intent  shortIntent=new Intent();
            shortIntent.setAction("com.a520it.mobilsafe.SHOTCUT_MAINACTI");
            shortIntent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortIntent);
            sendBroadcast(intent);
            SharedPreferences.Editor edit = mIsCreateShoutcut.edit();
            edit.putBoolean("isCreateShoutcut",true);
            edit.apply();
        }


    }

}
