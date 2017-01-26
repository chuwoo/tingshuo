package com.example.asus.myapp;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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
    String TAG="MyService";
    private DatagramSocket ds=null;
    //public static MyHandler handler;
    private  MY_MBReceiver rceiver;
    //public  AudPlay ap =null;
    private boolean audio=true;
    private AudRec2 m_recorder = null;
    //private LinkedList<byte []> m_pkg_q;
    public static Integer index = 0;
    private AudioManager ar=null;



    public MyService() {
    }
    public void onCreate(){
        //handler = new MyHandler();
        super.onCreate();
        //m_pkg_q = new LinkedList<byte[]>();
        ar=(AudioManager)MyService.this.getSystemService(Context.AUDIO_SERVICE);


        ar.setMicrophoneMute(true);
        ar.setSpeakerphoneOn(true);             //打开扬声器

        try {
            ds=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        MY_MBReceiver.registerMediaButtonListener(mMediaButtonlisttonLstener);
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //AudioManager注册一个MediaButton对象
        ComponentName chinaCN = new ComponentName(getPackageName(),MY_MBReceiver.class.getName());
        //只有China_MBReceiver能够接收到了，它是出于栈顶的。
        //不过，在模拟上检测不到这个效果，因为这个广播是我们发送的，流程不是我们在上面介绍的。
        mAudioManager.registerMediaButtonEventReceiver(chinaCN);
        //sendBroadcast(mbIntent,null);
        IntentFilter filter=new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        //filter.addAction(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS);
        filter.setPriority(100);
        rceiver=new MY_MBReceiver();
        registerReceiver(rceiver,filter);

//
//



        new AudPlay(ds).start();

        //Log.d(LOG,"oncreate");

    }

    public int onStartCommand(Intent intent, int flags, int startId){

//

        //Log.d(LOG,"Command exe");
        //Intent i=new Intent(this,MyService.class);
//        Notification notification=new Notification(R.mipmap.ic_launcher,"后台通知",System.currentTimeMillis());
//
//        startForeground(startId,notification);                                //提高服务优先级别
//
        String str=intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

            JSONObject obj = new JSONObject();
            try {
                obj.put("name", str);
                obj.put("content", "");
                obj.put("action", "online");
            } catch (JSONException e) {
                e.printStackTrace();
            }//创建json对象

            //Log.d(LOG, "json");


            new UdpSend(obj.toString(), ds).start();





        return  super.onStartCommand(intent,flags,startId);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
    public void onDestroy(){

        super.onDestroy();

        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ComponentName chinaCN = new ComponentName(getPackageName(),MY_MBReceiver.class.getName());
        //取消注册
        mAudioManager.unregisterMediaButtonEventReceiver(chinaCN);
    }

    public void startAE(){
        if(audio){

            index=0;
            ar.setMicrophoneMute(false);        //麦克风静音为假
            //m_pkg_q.clear();
            m_recorder = new AudRec2(ds) ;
            m_recorder.init() ;

            m_recorder.start() ;                //开始录音线程
            audio=false;
        }else{
            audio=true;
            index=1;
            ar.setMicrophoneMute(true);

        }
    }

    public interface MediaButtonListener {
        void onMediaButtonCall(Intent intent);
    }
    private  MediaButtonListener mMediaButtonlisttonLstener=new MediaButtonListener() {
        @Override
        public void onMediaButtonCall(Intent intent) {
            String intentAction = intent.getAction();
            //Log.d(TAG, "onMediaButtonCall.action:" + intentAction);
            KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            //Log.i(TAG, "Action ---->"+intentAction + "  KeyEvent----->"+keyEvent.toString());
            //MyService.class.star
            if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                //获得按键字节码
                //int keyCode = keyEvent.getKeyCode();
                //按下 / 松开 按钮
                int keyAction = keyEvent.getAction();
                if (keyAction == 1) {
                    //if(index==0) {
                        Log.d(TAG, "servir");                                    //调用处理按键事件
                        //intent.putExtra("Message",new Message());
                        index = 0;
                        ar.setMicrophoneMute(false);        //麦克风静音为假
                        //m_pkg_q.clear();
                        m_recorder = new AudRec2(ds);
                        m_recorder.init();

                        m_recorder.start();                //开始录音线程

                    //}
                    //startAE();

                }


            }



        }
    };
    public  static  class MY_MBReceiver extends BroadcastReceiver {
          static  MediaButtonListener mListener=null;
          String TAG="MY_Mbreceiver";
        @Override
        public void onReceive(Context context, Intent intent) {

            if (null != mListener) {
                mListener.onMediaButtonCall(intent);
                this.abortBroadcast();

            }
        }
        public  static void registerMediaButtonListener(MediaButtonListener callback){
            mListener=callback;
        }
            //获得KeyEvent对象
//



    }

}
//<receiver android:name=".China_MBReceiver">
//<intent-filter>
//<action android:name="android.intent.action.MEDIA_BUTTON" />
//</intent-filter>
//</receiver>
