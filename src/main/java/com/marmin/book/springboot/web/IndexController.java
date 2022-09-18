package com.marmin.book.springboot.web;

import com.sun.org.apache.xml.internal.security.Init;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/")
    public  String index(){
        return "index";
    }
}
