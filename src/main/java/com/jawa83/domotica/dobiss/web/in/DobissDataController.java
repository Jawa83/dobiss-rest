package com.jawa83.domotica.dobiss.web.in;

import com.jawa83.domotica.dobiss.core.domotica.model.resource.DobissGroupData;
import com.jawa83.domotica.dobiss.core.domotica.service.DobissDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller to fetch data configured in the Dobiss modules
 *
 * @author Jawa83
 * @since 5/3/2020
 */
@RestController
@RequestMapping(value = "/v1/dobiss/data")
@AllArgsConstructor
public class DobissDataController {

    private DobissDataService dobissDataService;

    /**
     * Fetches the list of groups for all dobiss modules
     *
     * @return List of groups
     */
    @GetMapping(path = "/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissGroupData>> getGroups() throws Exception {
        return ok().body(dobissDataService.fetchGroupsData());
    }

    /**
     * Fetches the list of moods
     *
     * @return List of moods
     */
    @GetMapping(path = "/moods", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissGroupData>> getMoods() throws Exception {
        return ok().body(dobissDataService.fetchMoodsData());
    }

    /**
     * Fetches the list of outputs for a specific module
     *
     * @param moduleId Id of the module for which to fetch the outputs
     * @return List of outputs for the specified module
     */
    @GetMapping(path = "/modules/{moduleId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DobissGroupData>> getOutput(@PathVariable int moduleId) throws Exception {
        return ok().body(dobissDataService.fetchOutputsData(moduleId));
    }

}
