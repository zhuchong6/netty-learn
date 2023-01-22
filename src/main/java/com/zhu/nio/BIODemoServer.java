package com.zhu.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 21:27
 */
public class BIODemoServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动,端口："+8080);
            Socket socket = null;
            while(true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
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