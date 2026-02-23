package com.ibosng.gatewayservice.dtos.response;

import java.io.Serializable;
import java.util.List;

public class PayloadTypeList<T> extends PayloadType implements Serializable {
    private List<T> attributes;

    public List<T> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<T> attributes) {
        this.attributes = attributes;
    }

    public PayloadTypeList(String type) {
        super(type);
    }

    public PayloadTypeList(String type, List<T> attributes) {
        super(type);
        this.attributes = attributes;
    }

}
