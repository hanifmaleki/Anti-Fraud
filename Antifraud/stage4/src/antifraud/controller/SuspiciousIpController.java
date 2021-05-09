package antifraud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
public class SuspiciousIpController {

    @GetMapping
    public ResponseEntity<List<String>> getAll(){
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestParam String ip){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@PathVariable String ip){
        return null;
    }
}
