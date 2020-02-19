package com.jawa83.domotica.dobiss.web.in;

import com.jawa83.domotica.dobiss.core.domotica.Connection;
import com.jawa83.domotica.dobiss.core.domotica.model.Group;
import com.jawa83.domotica.dobiss.core.domotica.model.Toggle;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/dobiss")
@AllArgsConstructor
public class DobissController {

    private Connection connection;

    @GetMapping(path = "/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Group> getGroups() {
        return connection.getGroups();
    }

    /**
     * Get the status of all the outputs of a certain group (across modules)
     *
     * @param groupId
     */
    @GetMapping(path = "/groups/{groupId}/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void getStatusOfGroup(@PathVariable int groupId) {
        connection.getStatusOfGroup(connection.getGroups().get(groupId));
    }

    /**
     * Get the status of all the outputs of a certain module
     *
     * @param moduleId
     */
    @GetMapping(path = "/modules/{moduleId}/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void getStatusOfModule(@PathVariable int moduleId) throws Exception {
        connection.getStatusOfModule(moduleId);
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void refreshStatuses() throws Exception {
        connection.getStatusOfAllModules();
    }

    /**
     * Change state of Dobiss output at a specific module + address
     *
     * @param module
     * @param address
     */
    @PostMapping(path = "/module/{module}/address/{address}")
    public void updateAddress(@PathVariable int module, @PathVariable int address) {
        connection.toggleOutput(new Toggle(null, address, module));
    }
}
