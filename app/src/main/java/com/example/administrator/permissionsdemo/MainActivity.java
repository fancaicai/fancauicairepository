package com.example.administrator.permissionsdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int CALL_POHNE_REQUESTCODE = 1;
    private Button mbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbtn = (Button) findViewById(R.id.btn_callphone);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCall();
            }
        });
    }

    //检测权限是否授权成功
    private void testCall() {
//        1.检测,返回值有两个
//       PackageManager.PERMISSION_GRANTED授权成功
//        PackageManager.PERMISSION_DENIED授权失败
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            // 清单里面授权被拒绝，向用户申请
            // 2. 申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE},CALL_POHNE_REQUESTCODE);

        }else {
            // 拨打电话
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri data=Uri.parse("tel:"+"10010");
        intent.setData(data);
        startActivity(intent);
    }


    //处理回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_POHNE_REQUESTCODE) {
//            判断权限是不是授权了
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                用户真正同意了授权
                callPhone();
            } else {
                // 用户给拒绝了
                Toast.makeText(this, "用户拒绝了打电话的权限", Toast.LENGTH_SHORT).show();
                // 能不能在用户彻底拒绝权限之后，可以友好的提示他一下
                // 4. 彻底拒绝的时候
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    showDialog();
                }
            }

        }
    }
    // 提示用户去设置
    public void showDialog(){
new AlertDialog.Builder(this)
        .setMessage("权限被彻底拒绝，请到设置里面打开，才可使用此功能！")
        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 跳转到本应用的设置页面，可以打开权限
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri data=Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                intent.setData(data);
            }
        })
      .setNegativeButton("取消",null)
        .create()
        .show();
    }
}
