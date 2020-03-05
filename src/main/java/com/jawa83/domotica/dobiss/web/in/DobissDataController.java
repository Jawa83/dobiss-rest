package com.jawa83.domotica.dobiss.web.in;

import com.jawa83.domotica.dobiss.core.domotica.service.DobissDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller to fetch data configured in the Dobiss modules
 *
 * @author jawa83
 * @since 5/3/2020
 */
@RestController
@RequestMapping(value = "/v1/dobiss")
@AllArgsConstructor
public class DobissDataController {

    private DobissDataService dobissDataService;

    @GetMapping(path = "/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGroups() throws Exception {
        return ok().body(dobissDataService.fetchGroupsData());
    }

}
