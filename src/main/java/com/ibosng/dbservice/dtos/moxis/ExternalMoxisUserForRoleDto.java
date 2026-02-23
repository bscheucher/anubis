package com.ibosng.dbservice.dtos.moxis;

import com.ibosng.dbservice.entities.moxis.UserClass;
import com.ibosng.dbservice.entities.moxis.UserClassifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ExternalMoxisUserForRoleDto extends MoxisUserDto {
    private String externalUserName;
    private String locale;
    private String phoneNumber;

    public ExternalMoxisUserForRoleDto() {
        super();
        this.locale = "de";
        this.setClassifier(UserClassifier.EMAIL.getType());
        this.setUserClass(UserClass.EXTERNAL_USER_FOR_ROLE.getType());
    }
}
