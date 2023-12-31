package com.dong.tech.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The Class ViewController.
 *
 * @author dongsulee
 * @date 2023/09/25
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(@AuthenticationPrincipal User user , Model model){
        //model.addAttribute("loginId",user.getUsername());
        //model.addAttribute("loginRoles",user.getAuthorities());
        return "dashboard";
    }
}
