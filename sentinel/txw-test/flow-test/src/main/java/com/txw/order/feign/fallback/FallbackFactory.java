package com.txw.order.feign.fallback;

import com.alibaba.csp.sentinel.slots.block.BlockException;
public class FallbackFactory implements feign.hystrix.FallbackFactory {
    private SentinelFallback sentinelFallback = new SentinelFallback();
    private DefaultFallback defaultFallback = new DefaultFallback();
    @Override
    public Object create(Throwable cause) {
        if (cause instanceof BlockException) {
            return sentinelFallback;
        } else {
            return defaultFallback;
        }
    }
}