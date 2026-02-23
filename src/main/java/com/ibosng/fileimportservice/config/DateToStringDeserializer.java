package com.ibosng.fileimportservice.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonToken jsonToken = jsonParser.getCurrentToken();
        if (jsonToken.isNumeric()) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(jsonParser.getValueAsLong());
            return df.format(date);
        }
        return jsonParser.getValueAsString();
    }
}
