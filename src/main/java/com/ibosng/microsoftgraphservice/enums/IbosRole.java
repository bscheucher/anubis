package com.ibosng.microsoftgraphservice.enums;

import lombok.Getter;

@Getter
public enum IbosRole {

    ADMIN_JU("iBOSNG_Rolle_AdminJU"),
    ADMIN_PR("iBOSNG_Rolle_AdminPR"),
    ALL_CUSTOM_ROLES("iBOSNG_Rolle_Alle-Custom-Roles"),
    CO("iBOSNG_Rolle_CO"),
    FK("iBOSNG_Rolle_FK"),
    FK_KAOS("iBOSNG_Rolle_FK-KAOS"),
    HR("iBOSNG_Rolle_HR"),
    IT("iBOSNG_Rolle_IT"),
    LV("iBOSNG_Rolle_LV"),
    MA("iBOSNG_Rolle_MA"),
    MA_KAOS("iBOSNG_Rolle_MA-KAOS"),
    PL("iBOSNG_Rolle_PL"),
    TR("iBOSNG_Rolle_TR");

    private final String value;

    IbosRole(String value) {
        this.value = value;
    }

    public static IbosRole fromValue(String value) {
        for (IbosRole request : IbosRole.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}
