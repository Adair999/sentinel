package com.txw.order.feign;

import com.txw.order.feign.fallback.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient(value = "service-storage",fallbackFactory = FallbackFactory.class)
public interface FeignStorageService {
    @RequestMapping("/storage")
    String storage();
    @GetMapping("/rt")
    String rt();
    @GetMapping("/flow")
    String flow();
    @GetMapping("/error")
    String error();
    @GetMapping("/thread")
    String thread();
}