package com.txw.order.algorithm;

import java.util.LinkedList;
import java.util.Random;
/**
 * 滑动时间窗口限流实现
 * 假设某个服务最多只能每秒钟处理100个请求，我们可以设置一个1秒钟的滑动时间窗口，
 * 窗口中有10个格子，每个格子100毫秒，每100毫秒移动一次，每次移动都需要记录当前服务请求的次数
 */
public class SlidingTimeWindow {
    // 服务访问次数，可以放在Redis中，实现分布式系统的访问计数
    Long counter = 0L;
    // 使用LinkedList来记录滑动窗口的10个格子。
    LinkedList<Long> slots = new LinkedList<Long>();
    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindow timeWindow = new SlidingTimeWindow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timeWindow.doCheck();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // 模拟随机时间，执行并发请求，记录QPS
        while (true) {
            // TODO 判断限流标记
            timeWindow.counter++;
            Thread.sleep(new Random().nextInt(15));
        }
    }
    private void doCheck() throws InterruptedException {
        while (true) {
            slots.addLast(counter);
            // 窗口数大于10，移除第一个窗口
            if (slots.size() > 10) {
                slots.removeFirst();
            }
            // 比较最后一个窗口的QPS和第一个窗口的QPS，两者相差100以上就限流
            if ((slots.peekLast() - slots.peekFirst()) > 100) {
                System.out.println("限流了。。");
                // TODO 修改限流标记为true
            } else {
                // TODO 修改限流标记为false
            }
            Thread.sleep(100);
        }
    }
}