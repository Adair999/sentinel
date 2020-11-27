package com.txw.order.feign.fallback;

import com.txw.order.feign.FeignStorageService;
public class DefaultFallback implements FeignStorageService {
    @Override
    public String storage() {
        System.out.println("当前访问人数过多，请稍后再试！");
        return "当前访问人数过多，请稍后再试！";
    }
    @Override
    public String rt() {
        System.out.println("rt接口-默认熔断");
        return "rt接口-默认熔断";
    }
    @Override
    public String flow() {
        System.out.println("flow接口-默认熔断");
        return "flow接口-默认熔断";
    }
    @Override
    public String error() {
        System.out.println("error接口-默认熔断");
        return "error接口-默认熔断";
    }
    @Override
    public String thread() {
        System.out.println("thread接口-默认熔断");
        return "thread接口-默认熔断";
    }
}