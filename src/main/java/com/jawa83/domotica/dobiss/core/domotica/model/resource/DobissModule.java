package com.jawa83.domotica.dobiss.core.domotica.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DobissModule {

    int module;
    List<DobissOutput> outputs;

}
