package com.haozi.id.generator.console.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author haozi
 * @date 2020/4/2611:04 上午
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(ModelMap map) {
        return "index";
    }
}
