package com.zhu.nio;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 23:01
 */
public class NIODemoClient {
    public static void main(String[] args) {
        int port = 8090;
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}