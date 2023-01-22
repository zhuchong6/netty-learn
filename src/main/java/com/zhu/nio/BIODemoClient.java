package com.zhu.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 21:27
 */
public class BIODemoClient {
    public static void main(String[] args) throws IOException {
        int port  = 8080;
        final Socket socket = new Socket("localhost", port);
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("查询时间");
        out.println("QUERY TIME ORDER");
        final String resp = in.readLine();
        System.out.println("服务器返回时间是："+ resp);

    }
}