package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class SuspiciousIpService {

    private Set<String> ipSet = new HashSet<>();

    public void add(String ip) {
        //TODO add IPv4 validation
        log.debug("Adding suspicious IP {}", ip);
        if (isSuspicious(ip)) {
            val message = String.format("Suspicious IP %s has been already exist.", ip);
            throw new DuplicateDataException(message);
        }
        ipSet.add(ip);
        log.debug("Ip added successfully");
    }

    public boolean isSuspicious(String ip) {
        return ipSet.contains(ip);
    }

    public void delete(String ip) {
        log.debug("Removing IP {}", ip);
        if (!isSuspicious(ip)) {
            val message = String.format("Could not find IP %s", ip);
            log.error(message);
            throw new DataNotFoundException(message);
        }
        ipSet.remove(ip);
        log.debug("Ip has been removed");
    }

    public List<String> findAll() {
        return new ArrayList<>(ipSet);
    }
}
