package com.example.shinsoyoung.todays_phone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class CheckPermission extends AppCompatActivity {

    private final Context mContext;

    public CheckPermission(Context context){
        mContext = context;
        //context 즉 MainActivity의 레이아웃값을 가져옴
        Chk();
        //Chk메소드실행
        return;
    }

    public void Chk(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                        Manifest.permission.WRITE_CALL_LOG}, 0);
            }
            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                        Manifest.permission.READ_CALL_LOG},0);
            }
            if (!Settings.canDrawOverlays(mContext)) {
                if (!Settings.canDrawOverlays(mContext)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Toast.makeText(this, "앱 위에 그리기 권한을 추가해주세요", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        }
        /*
        if(체크퍼미션:Manifest 에 지정한 CheckPermission값이 GRANTED 가 아닌경우)}
        mContext액티비티 = MainActivity 란 엑티비티에 퍼미션을 체크하는 AlertDialog창을 띄워줌
        */
    }
}
