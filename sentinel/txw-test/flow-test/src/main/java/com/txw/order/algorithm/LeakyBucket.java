package com.txw.order.algorithm;

/**
 * 漏桶限流算法
 */
public class LeakyBucket {
    public long timeStamp = System.currentTimeMillis();  // 当前时间
    public long capacity; // 桶的容量
    public long rate; // 水漏出的速度(每秒系统能处理的请求数)
    public long water; // 当前水量(当前累积请求数)
    public boolean limit() {
        long now = System.currentTimeMillis();
        water = Math.max(0, water - ((now - timeStamp) / 1000) * rate); // 先执行漏水，计算剩余水量
        timeStamp = now;
        if ((water + 1) < capacity) {
            // 尝试加水,并且水还未满
            water += 1;
            return true;
        } else {
            // 水满，拒绝加水
            return false;
        }
    }
    public static void main(String[] args) {
        LeakyBucket leakyBucket = new LeakyBucket();
        leakyBucket.capacity = 4;
        leakyBucket.rate = 4;
        leakyBucket.water = 0;
        while (true) {
            boolean result = leakyBucket.limit();
            if (result) {
                System.out.println("正常请求");
            } else {
                System.out.println("请求被限速");
                return;
            }
        }
    }
}