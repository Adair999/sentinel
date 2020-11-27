package com.txw.order.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping
public class SentinelController {
    private static final String LIMIT_KEY_1 = "testQps1";
    private static final String LIMIT_KEY_2 = "testQps2";
    @PostConstruct
    public void initFlowRule() {
        FlowRule flowRule = new FlowRule();
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(1); //QPS值
        flowRule.setResource(LIMIT_KEY_1);
        flowRule.setLimitApp("default");
        FlowRule flowRule2 = new FlowRule();
        flowRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule2.setCount(2); //QPS值
        flowRule2.setResource(LIMIT_KEY_2);
        flowRule2.setLimitApp("default");
        List<FlowRule> flowRuleList = new ArrayList<>();
        flowRuleList.add(flowRule);
        flowRuleList.add(flowRule2);
        FlowRuleManager.loadRules(flowRuleList);
        System.out.println("配置限流规则成功");
    }
    @GetMapping("/testQPS1")
    public String testQPS1() {
        // Entry获取许可
        long threadId = Thread.currentThread().getId();
        Entry entry = null;
        try {
            entry = SphU.entry(LIMIT_KEY_1);
            System.out.println("testQPS1 success---" + threadId);
            return "testQPS1 success---" + threadId;
        } catch (BlockException e) {
            // 如果抛异常了，则表示被限流了
            // e.printStackTrace();
            System.out.println("当前访问量过大，请稍后重试..." + threadId);
            return "当前访问量过大，请稍后重试..." + threadId;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
    @GetMapping("/testQPS2")
    @SentinelResource(value = LIMIT_KEY_2, blockHandler = "testBlockHandler")
    public String testQPS2() {
        long threadId = Thread.currentThread().getId();
        System.out.println("testQPS1 success---" + threadId);
        return "testQPS1 success---" + threadId;
    }
    public String testBlockHandler(BlockException e) {
        long threadId = Thread.currentThread().getId();
        System.out.println("当前访问量过大，请稍后重试..." + threadId);
        return "当前访问量过大，请稍后重试..." + threadId;
    }
}