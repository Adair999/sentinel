package com.txw.order.feign.fallback;

import com.txw.order.feign.FeignStorageService;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SentinelFallback implements FeignStorageService {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public String storage() {
        String msg = "storage接口-Sentinel熔断---" + sdf.format(new Date());
        System.out.println(msg);
        return msg;
    }
    @Override
    public String rt() {
        String msg = "rt接口-Sentinel熔断---" + sdf.format(new Date());
        System.out.println(msg);
        return msg;
    }
    @Override
    public String flow() {
        String msg = "flow接口-Sentinel熔断---" + sdf.format(new Date());
        System.out.println(msg);
        return msg;
    }
    @Override
    public String error() {
        String msg = "error接口-Sentinel熔断---" + sdf.format(new Date());
        System.out.println(msg);
        return msg;
    }
    @Override
    public String thread() {
        String msg = "thread接口-Sentinel熔断---" + sdf.format(new Date());
        System.out.println(msg);
        return msg;
    }
}