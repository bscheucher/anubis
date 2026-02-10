package com.ibosng.dbservice.dtos.moxis;

import com.ibosng.dbservice.entities.moxis.UserClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AddressBookEntryDto extends BasicMoxisUserDto {
    private String email;
    private String phoneNumber;
    private String locale;

    public AddressBookEntryDto() {
        super();
        this.setUserClass(UserClass.ADDRESS_BOOK_ENTRY.getType());
        this.setLocale("de");
    }
}
