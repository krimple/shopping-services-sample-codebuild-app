package com.rimple.shoppinglist.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class RootController {
    @RequestMapping(method = RequestMethod.GET, produces = "text/plain")
    public String healthCheck() {
        System.out.println("*** RENDERING");
        throw new RuntimeException("POOPE");
    }
}
