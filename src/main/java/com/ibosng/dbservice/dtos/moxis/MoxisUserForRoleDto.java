package com.ibosng.dbservice.dtos.moxis;

import com.ibosng.dbservice.entities.moxis.UserClass;
import com.ibosng.dbservice.entities.moxis.UserClassifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class MoxisUserForRoleDto extends MoxisUserDto {
    public MoxisUserForRoleDto() {
        super();
        this.setClassifier(UserClassifier.UPN.getType());
        this.setUserClass(UserClass.USER_FOR_ROLE.getType());
    }
}
