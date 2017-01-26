package com.example.asus.myapp;

//import Thread;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by ASUS on 2017/1/20.
 */

public class UdpSend extends Thread {
    //private DatagramPacket dp;
    private DatagramSocket socket;
    //private JSONObject obj;
    //private InetAddress ip;
    private String data;
    //private  int port;
    public   boolean sock=false;
    //private String msg;
    public UdpSend(String data,DatagramSocket ds)  {


            this.data=data;
            this.socket=ds;


    }
    //public static String resule;
    public  void run(){
        try {


            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName("192.168.99.242");
            DatagramPacket dp = new DatagramPacket(data.getBytes(), data.getBytes().length, ip, 8124);
            socket.send(dp);
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("socket","ok");

    }
}
