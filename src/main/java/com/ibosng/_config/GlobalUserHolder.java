package com.ibosng._config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GlobalUserHolder {
    public static final String IBOSNG_BACKEND = "iBOS_nG Backend";
    private String username;
    private Integer userId;


    public String getUsername() {
        if (isNullOrBlank(username)) {
            return IBOSNG_BACKEND;
        }
        return username;
    }
}
