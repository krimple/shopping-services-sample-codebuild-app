package com.rimple.shoppinglist.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hi")
    public String justSayHi() {
        return "hi";
    }

}
