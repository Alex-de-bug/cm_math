package controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    @PostMapping("/send")
    public ResponseEntity<String> sendNumber(@RequestBody int number) {
        System.out.println("Received number: " + number);
        return ResponseEntity.ok("Received: " + number);
    }
}
