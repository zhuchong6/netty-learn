package com.zhu.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 21:31
 */
public class TimeServerHandler implements Runnable{
    private  Socket socket;
    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String body = null;
            String currentTime = null;
            while (true){
               body = in.readLine();
               if(body == null){
                   break;
               }
               System.out.println("时间服务器接收命令："+body);

               currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString(): "BAD ORDER";
               out.println(currentTime);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭资源
            if(in !=null){
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                socket = null;

            }        }
    }
}