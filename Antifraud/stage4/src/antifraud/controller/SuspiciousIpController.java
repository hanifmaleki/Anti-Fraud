package antifraud.controller;

import antifraud.model.Role;
import antifraud.service.SuspiciousIpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
@Secured({"ROLE_SUPPORT", "ROLE_ADMIN"})
@RequiredArgsConstructor
public class SuspiciousIpController {

    private final SuspiciousIpService ipService;

    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        return ResponseEntity.ok(ipService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestParam String ip) {
        ipService.add(ip);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{ip}")
    public ResponseEntity<Object> delete(@PathVariable String ip) {
        ipService.delete(ip);
        return ResponseEntity.ok().build();
    }
}
