package com.ibosng.gatewayservice.dtos.response;

import java.util.Map;

public class PayloadTypeMap<T, V> extends PayloadType {
    private Map<T, V> attributes;

    public Map<T, V> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<T, V> attributes) {
        this.attributes = attributes;
    }

    public PayloadTypeMap(String type) {
        super(type);
    }

    public PayloadTypeMap(String type, Map<T, V> attributes) {
        super(type);
        this.attributes = attributes;
    }

}
