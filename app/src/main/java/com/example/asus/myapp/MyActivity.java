package com.example.asus.myapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.asus.myapp";
    //private  boolean flag=true;
    String message;
    TextView textView;
    Button button;
    //String msg;
    //private Object intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

    }
    public void sendMessage(View view) {         //按钮按下的执行函数

        //if(message.length()>3) {

                EditText editTest = (EditText) findViewById(R.id.edit_message);
                message = editTest.getText().toString();

                Intent intent = new Intent(this, MyService.class);    //����һ��intent


                intent.putExtra(EXTRA_MESSAGE, message);             //通过intert向service传递message

                startService(intent);                               //启动service
                textView=(TextView)findViewById(R.id.textView);
        textView.setText("测试正在进行,请提宝贵意见!项目联系：zhuqunwu@gmail.com");
        button=(Button)findViewById(R.id.button);
        button.setEnabled(false);




        //msgServer.sendmsg(message);

    }
}
