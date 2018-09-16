package com.zzh.component.controller;

import com.zzh.component.service.UserService;
import com.zzh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/t")
public class T {
    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("/s")
    public String ok()
    {
        return "ts";
    }

    @ResponseBody
    @RequestMapping("/getU")
    public User getU(User u)
    {
        return userService.getUser(u);
    }
}
