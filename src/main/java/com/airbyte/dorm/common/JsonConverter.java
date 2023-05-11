package com.airbyte.dorm.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;

@Converter
public class JsonConverter implements AttributeConverter<JsonObject, String> {

    @Override
    public String convertToDatabaseColumn(JsonObject jsonObject) {
        String result;
        return result = new Gson().toJson(jsonObject);
    }

    @Override
    public JsonObject convertToEntityAttribute(String s) {
        JsonObject result = new JsonObject();
        return result = new Gson().fromJson(s , JsonObject.class);
    }
}
