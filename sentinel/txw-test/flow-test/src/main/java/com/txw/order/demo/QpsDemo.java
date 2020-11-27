package com.txw.order.demo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import java.util.ArrayList;
import java.util.List;
public class QpsDemo {
    public static void main(String[] args) {
        // 下面几行代码设置了QPS阈值是 100
        FlowRule rule = new FlowRule("test");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(100);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        List<FlowRule> list = new ArrayList<>();
        list.add(rule);
        FlowRuleManager.loadRules(list);
        // 先通过一个请求，让 clusterNode 先建立起来
        try (Entry entry = SphU.entry("test")) {
        } catch (BlockException e) {
        }
        // 起一个线程一直打印 qps 数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(ClusterBuilderSlot.getClusterNode("test").passQps());
                }
            }
        }).start();
        while (true) {
            try (Entry entry = SphU.entry("test")) {
                Thread.sleep(5);
            } catch (BlockException e) {
                // ignore
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}