package com.jawa83.domotica.dobiss.core.domotica.model;

import com.jawa83.domotica.dobiss.core.domotica.data.DobissTypes;
import com.jawa83.domotica.dobiss.core.domotica.utils.ConversionUtils;

/**
 * Created by wardjanssens on 27/02/2017.
 * @deprecated being refactored
 */
@Deprecated
public class Toggle extends AbstractDomoticaAddress {

    private static final int TOGGLE_VALUE = 100;    // A Toggle object only knows 'on' or 'off'
                                                    // Hence, the value always changes by 100%

    public Toggle(String name, int address, int module) {
        super(name, address, module);
        this.setType(DobissTypes.TOGGLE.getId());
    }

    public byte[] getToggleRequest() {
        return getTriggerRequest(TOGGLE_VALUE, ActionType.TOGGLE);
    }

    byte[] getTriggerRequest(int parameter, ActionType actionType) {
        String hexParameter = ConversionUtils.bytesToHex(new byte[] { (byte) parameter });
        String hexModule = getHexModule();
        String hexAddress = getHexAddress();
        return ConversionUtils.hexToBytes(
                "af02ff" +
                hexModule +
                "0000080108ffffffffffffaf" +
                hexModule +
                hexAddress +
                actionType.hexPresentation +
                "ffff" +
                hexParameter +
                "ffff");
    }

}
