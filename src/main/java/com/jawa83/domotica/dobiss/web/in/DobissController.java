package com.jawa83.domotica.dobiss.web.in;

import com.jawa83.domotica.dobiss.core.domotica.Connection;
import com.jawa83.domotica.dobiss.core.domotica.model.DobissOutput;
import com.jawa83.domotica.dobiss.core.domotica.model.Group;
import com.jawa83.domotica.dobiss.core.domotica.service.DobissService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/v1/dobiss")
@AllArgsConstructor
public class DobissController {

    private Connection connection;
    private DobissService dobissService;

    @GetMapping(path = "/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Group> getGroups() {
        return connection.getGroups();
    }

    /**
     * Get the status of all the outputs of a certain module
     *
     * @param moduleId Id of the Dobiss module
     */
    @GetMapping(path = "/module/{moduleId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissOutput>> getStatusOfModule(@PathVariable int moduleId) throws Exception {
        return ok().body(dobissService.requestModuleStatusAsObject(moduleId));
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void refreshStatuses() throws Exception {
        connection.getStatusOfAllModules();
    }

    /**
     * Change state of Dobiss output at a specific module + address
     *
     * @param module Id of the Dobiss module
     * @param address Address of the output on the module
     */
    @PostMapping(path = "/module/{module}/address/{address}")
    public void updateAddress(@PathVariable int module, @PathVariable int address) throws Exception {
        dobissService.toggleOutput(module, address);
    }
}
