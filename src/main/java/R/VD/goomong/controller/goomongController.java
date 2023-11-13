package R.VD.goomong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class goomongController {

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }
}