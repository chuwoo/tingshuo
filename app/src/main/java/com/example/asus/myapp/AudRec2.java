package com.example.asus.myapp;

//import Thread;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ASUS on 2017/1/22.
 */

public class AudRec2 extends Thread {
    private AudioRecord m_in_rec ;
    private LinkedList m_in_q ;
    private int         m_in_buf_size ;
    private short []     m_in_bytes ;
    private byte []     m_in_bytes1 ;
    private boolean     m_keep_running ;
    private  final Timer time=new Timer();
    private  TimerTask task;
    //private  String ipAdd;
    //private int dk;

    private DatagramSocket ds;

    AudRec2(DatagramSocket ds) {
        //System.out.println("测试1");
        this.ds=ds;




        m_keep_running = true ;

    }

    public void init() {
        //m_in_q = data_q ;
        m_in_buf_size = android.media.AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        m_in_rec = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                m_in_buf_size) ;

        //m_in_bytes = new short [m_in_buf_size] ;
        m_in_bytes1 = new byte[m_in_buf_size];
        //System.out.println(data_q);
        //m_keep_running = true ;
        task= new TimerTask() {
            @Override
            public void run() {
                MyService.index=1;
                time.cancel();
            }
        };
        time.schedule(task,5000);
        System.out.println("s2");
    }




    public void run() {
        int bytes_read ;
        //int i ;
        //String str=null;
        m_in_rec.startRecording() ;

        while(MyService.index==0) {
            bytes_read = m_in_rec.read(m_in_bytes1, 0, m_in_buf_size) ;
            Log.d("rec",""+m_in_buf_size);
            Log.d("rec2",""+m_in_bytes1.length);
            try {


                InetAddress ip = InetAddress.getByName("192.168.99.242");
                DatagramPacket dp=new DatagramPacket(m_in_bytes1,m_in_bytes1.length,ip,8124);
                ds.send(dp);
            }catch (Exception e){
                Log.d("socket","error");
            }


        }

        m_in_rec.stop() ;
        m_in_rec.release();
        m_in_rec = null ;
        m_in_bytes = null ;
    }

}
