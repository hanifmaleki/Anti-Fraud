package antifraud.controller;

import antifraud.service.StolenCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/stolencard")
@RequiredArgsConstructor
public class StolenCardController {

    private final StolenCardService cardService;

    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        return ResponseEntity.ok(cardService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestParam String serialNumber) {
        cardService.add(serialNumber);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@PathVariable String serialNumber) {
        cardService.delete(serialNumber);
        return ResponseEntity.ok().build();
    }
}
