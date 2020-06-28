package tacos.controller;

import tacos.domain.Taco;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

@Controller
public class TestController {
    @GetMapping("/test")
    public String test(Model model){
        Taco taco = new Taco();
        taco.setName("xixi");
        model.addAttribute("taco",taco);

        return "test";
    }

    @GetMapping("/test2")
    public String test2(@Valid Taco taco, Errors errors) {
        if(errors.hasErrors()) {
            return "test";
        }
        System.out.println(taco);
        return "test";
    }



}
