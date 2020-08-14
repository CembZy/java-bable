package com.master.demo.controller;

import com.master.demo.vote.NodeVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {

    @Autowired
    private NodeVote nodeVote;

    // 获取服务信息
    @GetMapping("/getServerInfo")
    public String getServerInfo(HttpServletRequest request) {
        nodeVote.vote(request);
        return NodeVote.isMatser ? "当前服务器为主节点" : "当前服务器为从节点";
    }
}
