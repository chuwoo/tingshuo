package com.example.asus.myapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MyService extends Service {
    String LOG="MyService";
    private DatagramSocket ds=null;
    public  AudPlay ap =null;
    private boolean audio=true;
    private AudRec2 m_recorder = null;
    private LinkedList<byte []> m_pkg_q;
    public static Integer index = 0;
    private AudioManager ar=null;
    public MyService() {
    }
    public void onCreate(){
        m_pkg_q = new LinkedList<byte[]>();
        ar=(AudioManager)MyService.this.getSystemService(Context.AUDIO_SERVICE);


        ar.setMicrophoneMute(true);

        try {
            ds=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //AudioManager注册一个MediaButton对象
        ComponentName chinaCN = new ComponentName(getPackageName(),China_MBReceiver.class.getName());
        //只有China_MBReceiver能够接收到了，它是出于栈顶的。
        //不过，在模拟上检测不到这个效果，因为这个广播是我们发送的，流程不是我们在上面介绍的。
        mAudioManager.registerMediaButtonEventReceiver(chinaCN);
        //sendBroadcast(mbIntent,null);


        new AudPlay(ds).start();
        super.onCreate();
        Log.d(LOG,"oncreate");

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(LOG,"Command exe");

        String str=intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        if(str.length()>3) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", str);
                obj.put("content", "");
                obj.put("action", "online");
            } catch (JSONException e) {
                e.printStackTrace();
            }//创建json对象

            Log.d(LOG, "json");


            new UdpSend(obj.toString(), ds).start();
        }else {
            startAE();
        }




        return  super.onStartCommand(intent,flags,startId);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void startAE(){
        if(audio){

            index=0;
            ar.setMicrophoneMute(false);        //麦克风静音为假
            m_pkg_q.clear();
            m_recorder = new AudRec2(ds) ;
            m_recorder.init(m_pkg_q) ;

            m_recorder.start() ;                //开始录音线程
            audio=false;
        }else{
            audio=true;
            index=1;
            ar.setMicrophoneMute(true);

        }
    }
//    public class China_MBReceiver extends BroadcastReceiver {
//        String TAG="China_Mbreceiver";
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String intentAction = intent.getAction() ;
//            //获得KeyEvent对象
//            KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//
//            Log.i(TAG, "Action ---->"+intentAction + "  KeyEvent----->"+keyEvent.toString());
//            //MyService.class.star
//            if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
//                //获得按键字节码
//                int keyCode = keyEvent.getKeyCode() ;
//                //按下 / 松开 按钮
//                int keyAction = keyEvent.getAction() ;
//                //获得事件的时间
//                long downtime = keyEvent.getEventTime();
//
//                //获取按键码 keyCode
//                StringBuilder sb = new StringBuilder();
//                //这些都是可能的按键码 ， 打印出来用户按下的键
//                if(KeyEvent.KEYCODE_MEDIA_NEXT == keyCode){
//                    sb.append("KEYCODE_MEDIA_NEXT");
//                }
//                //说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是 KEYCODE_MEDIA_PLAY_PAUSE
//                if(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ==keyCode){
//                    sb.append("KEYCODE_MEDIA_PLAY_PAUSE");
//                }
//                if(KeyEvent.KEYCODE_HEADSETHOOK == keyCode){
//                    sb.append("KEYCODE_HEADSETHOOK");
//                }
//                if(KeyEvent.KEYCODE_MEDIA_PREVIOUS ==keyCode){
//                    sb.append("KEYCODE_MEDIA_PREVIOUS");
//                }
//                if(KeyEvent.KEYCODE_MEDIA_STOP ==keyCode){
//                    sb.append("KEYCODE_MEDIA_STOP");
//                }
//                //输出点击的按键码
//                Log.i(TAG, sb.toString());
//
//            }
//        }
//    }

}
