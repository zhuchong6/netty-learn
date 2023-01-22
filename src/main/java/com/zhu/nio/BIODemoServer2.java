package com.zhu.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 21:27
 */
public class BIODemoServer2 {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动,端口："+8080);
            //增加一个线程池
            final ExecutorService executorService = Executors.newFixedThreadPool(2);
            Socket socket = null;
            while(true){
                socket = serverSocket.accept();
                executorService.execute(new TimeServerHandler(socket));
//                new Thread(new TimeServerHandler(socket)).start();
            }

        } finally {
            if(serverSocket != null){
                System.out.println("服务器关闭");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}