package com.txw.order.controller;

import com.txw.order.feign.FeignStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class OrderController {
    @Autowired
    private FeignStorageService storageService;
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String order() {
        String result = storageService.storage();
        System.out.println("扣减库存结果："+result);
        return "order success";
    }
    @GetMapping("/rt")
    public String rt() {
        String result = storageService.rt();
        System.out.println(result);
        return result;
    }
    @GetMapping("/flow")
    public String flow() {
        String result = storageService.flow();
        System.out.println(result);
        return result;
    }
    @GetMapping("/errorTest")
    public String error() {
        String result = storageService.error();
        System.out.println(result);
        return result;
    }
    @GetMapping("/thread")
    public String thread() {
        String result = storageService.thread();
        System.out.println(result);
        return result;
    }
}