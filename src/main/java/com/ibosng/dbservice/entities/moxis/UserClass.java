package com.ibosng.dbservice.entities.moxis;

import lombok.Getter;

@Getter
public enum UserClass {

    USER(0, "com.xitrust.moxispe.api.User"),
    USER_FOR_ROLE(1, "com.xitrust.moxispe.api.UserForRole"),
    EXTERNAL_USER_FOR_ROLE(2, "com.xitrust.moxispe.api.ExternalUserForRole"),
    EXTERNAL_APPROVAL_FOR_ROLE(3, "com.xitrust.moxispe.api.ExternalApproverForRole"),
    EXTERNAL_APPROVAL(4, "com.xitrust.moxispe.api.ExternalApprover"),
    ADDRESS_BOOK_ENTRY(5, "com.xitrust.moxispe.api.AddressBookEntry");

    private final int code;
    private final String type;

    UserClass(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static UserClass fromType(String type) {
        for (UserClass userClass : UserClass.values()) {
            if (userClass.getType().equals(type)) {
                return userClass;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }
}
