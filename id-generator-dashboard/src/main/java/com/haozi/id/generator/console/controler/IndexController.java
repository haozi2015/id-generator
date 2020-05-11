package com.haozi.id.generator.console.controler;

import com.haozi.id.generator.metric.util.HostUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author haozi
 * @date 2020/4/2611:04 上午
 */
@Controller
public class IndexController {
    @Value("${server.port:-1}")
    private String port;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("host", HostUtil.getIp() + ":" + port);
        return "index";
    }
}
