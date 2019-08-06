package com.ishvatov.controller;

import com.ishvatov.service.inner.truck.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @Autowired
    private TruckService service;

    @RequestMapping("/admin/test1")
    public String test1() {
        return "error/error";
    }

}
