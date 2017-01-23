package com.example.asus.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.asus.myapp";
    private  boolean flag=true;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }
    public void sendMessage(View view) {         //按钮按下的执行函数

        //if(message.length()>3) {
            if(flag) {
                EditText editTest = (EditText) findViewById(R.id.edit_message);
                message = editTest.getText().toString();
                flag=false;
            }else {
                message="c";
            }
                Intent intent = new Intent(this, MyService.class);    //����һ��intent

            //bindService(intent, conn, BIND_AUTO_CREATE);
            //Log.d("button","strat");

            //msgServer.senduse(message);

            intent.putExtra(EXTRA_MESSAGE, message);             //通过intert向service传递message

            startService(intent);                               //启动service




        //msgServer.sendmsg(message);

    }
}
