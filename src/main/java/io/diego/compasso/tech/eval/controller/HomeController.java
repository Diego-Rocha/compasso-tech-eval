package io.diego.compasso.tech.eval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController {

    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        //TODO swagger docs
        response.sendRedirect("/city");
    }

}
