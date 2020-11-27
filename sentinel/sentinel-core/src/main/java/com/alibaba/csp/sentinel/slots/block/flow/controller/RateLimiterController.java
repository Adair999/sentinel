/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.slots.block.flow.controller;

import com.alibaba.csp.sentinel.node.Node;
import com.alibaba.csp.sentinel.slots.block.flow.TrafficShapingController;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jialiang.linjl
 */
public class RateLimiterController implements TrafficShapingController {
    // 排队最大时长，默认 500ms
    private final int maxQueueingTimeMs;
    // QPS 设置的值
    private final double count;
    // 上一次请求通过的时间
    private final AtomicLong latestPassedTime = new AtomicLong(-1);
    public RateLimiterController(int timeOut, double count) {
        this.maxQueueingTimeMs = timeOut;
        this.count = count;
    }
    // 通常 acquireCount为1，这里不用关心参数 prioritized
    @Override
    public boolean canPass(Node node, int acquireCount) {
        return canPass(node, acquireCount, false);
    }
    @Override
    public boolean canPass(Node node, int acquireCount, boolean prioritized) {
        // Pass when acquire count is less or equal than 0.
        if (acquireCount <= 0) {
            return true;
        }
        // Reject when count is less or equal than 0.
        // Otherwise,the costTime will be max of long and waitTime will overflow in some cases.
        if (count <= 0) {
            return false;
        }
        long currentTime = TimeUtil.currentTimeMillis();
        // Calculate the interval between every two requests.
        // 计算每 2 个请求之间的间隔，比如 QPS 限制为 10，那么间隔就是 100ms
        long costTime = Math.round(1.0 * (acquireCount) / count * 1000);
        // Expected pass time of this request.
        long expectedTime = costTime + latestPassedTime.get();

        // 可以通过，设置 latestPassedTime 然后就返回 true 了
        if (expectedTime <= currentTime) {
            // Contention may exist here, but it's okay.
            latestPassedTime.set(currentTime);
            return true;
        } else {
            // Calculate the time to wait.
            // 不可以通过，需要等待
            long waitTime = costTime + latestPassedTime.get() - TimeUtil.currentTimeMillis();
            // 等待时长大于最大值，返回 false
            if (waitTime > maxQueueingTimeMs) {
                return false;
            } else {
                // 将 latestPassedTime 往前推
                long oldTime = latestPassedTime.addAndGet(costTime);
                try {
                    // 需要 sleep 的时间
                    waitTime = oldTime - TimeUtil.currentTimeMillis();
                    if (waitTime > maxQueueingTimeMs) {
                        latestPassedTime.addAndGet(-costTime);
                        return false;
                    }
                    // in race condition waitTime may <= 0
                    if (waitTime > 0) {
                        Thread.sleep(waitTime);
                    }
                    return true;
                } catch (InterruptedException e) {
                }
            }
        }
        return false;
    }
}