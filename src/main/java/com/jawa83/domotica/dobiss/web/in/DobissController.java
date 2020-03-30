package com.jawa83.domotica.dobiss.web.in;

import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissModule;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissOutput;
import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissOutputValue;
import com.jawa83.domotica.dobiss.core.domotica.service.DobissService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for the main Dobiss output interaction REST endpoints
 *
 * Contains endpoints to:
 *  - trigger changes on outputs
 *  - fetch the status of outputs
 *
 * @author jawa83
 * @since 5/3/2020
 */
@RestController
@RequestMapping(value = "/v1/dobiss")
@AllArgsConstructor
public class DobissController {

    private DobissService dobissService;

    /**
     * Get the status of all the outputs of a certain module
     *
     * @param moduleId Id of the Dobiss module
     */
    @GetMapping(path = "/module/{moduleId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissOutput>> getStatusOfModule(@PathVariable int moduleId) throws Exception {
        return ok().body(dobissService.requestModuleStatusAsObject(moduleId));
    }

    /**
     * Get the status of all the outputs of a certain module in hex string format
     *
     * @param moduleId Id of the Dobiss module
     */
    @GetMapping(path = "/module/{moduleId}/hex", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStatusOfModuleAsHex(@PathVariable int moduleId) throws Exception {
        return ok().body(dobissService.requestModuleStatusAsHex(moduleId));
    }

    /**
     * Get the status of a specific output
     *
     * @param module Id of the Dobiss module
     */
    @GetMapping(path = "/module/{module}/address/{address}")
    public ResponseEntity<DobissOutput> getStatusOfOutput(@PathVariable int module, @PathVariable int address) throws Exception {
        return ok().body(dobissService.requestOutputStatusAsObject(module, address));
    }

    /**
     * Get the status of a specific output in hex string format
     *
     * @param module Id of the Dobiss module
     */
    @GetMapping(path = "/module/{module}/address/{address}/hex")
    public ResponseEntity<String> getStatusOfOutputAsHex(@PathVariable int module, @PathVariable int address) throws Exception {
        return ok().body(dobissService.requestOutputStatusAsHex(module, address));
    }

    /**
     * Get the statuses of all modules
     *
     * @return A List of modules with lists of all outputs including their actual status
     */
    @GetMapping(path = "/module", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissModule>> getAllStatuses() throws Exception {
        return ok().body(dobissService.requestAllStatus());
    }

    /**
     * Change state of Dobiss output at a specific module + address
     * @param module Id of the Dobiss module
     * @param address Address of the output on the module
     */
    @PostMapping(path = "/module/{module}/address/{address}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateAddress(@PathVariable int module, @PathVariable int address, @RequestBody(required = false) DobissOutputValue dobissOutputValue) throws Exception {
        if (dobissOutputValue != null) {
            dobissService.dimOutput(module, address, dobissOutputValue.getValue());
        } else {
            dobissService.toggleOutput(module, address);
        }
    }
}
