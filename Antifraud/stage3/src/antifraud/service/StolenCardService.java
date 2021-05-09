package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class StolenCardService {

    private Set<String> cardSet = new HashSet<>();

    public void add(String serialNumber) {
        //TODO add IPv4 validation
        log.debug("Adding card {}", serialNumber);
        if (isBlacklist(serialNumber)) {
            val message = MessageFormat.format("Suspicious IP {0} has been already exist.", serialNumber);
            log.error(message);
            throw new DuplicateDataException(message);
        }
        cardSet.add(serialNumber);
        log.debug("Card has been added successfully.");
    }

    public void delete(String serialNumber) {
        log.debug("Removing card {}", serialNumber);
        if (!isBlacklist(serialNumber)) {
            val message = String.format("Could not find Card %s", serialNumber);
            log.error(message);
            throw new DataNotFoundException(message);
        }
        cardSet.remove(serialNumber);
        log.debug("Card has been removed successfully.");
    }


    public List<String> findAll() {
        return new ArrayList<>(cardSet);
    }

    public boolean isBlacklist(String cardSerial) {
        return cardSet.contains(cardSerial);
    }
}
