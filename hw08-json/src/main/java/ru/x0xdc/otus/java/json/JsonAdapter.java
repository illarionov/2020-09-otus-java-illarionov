package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;

interface JsonAdapter<T> {

    void write(JsonGenerator generator, T object);

    void write(JsonGenerator generator, String fieldName, T object);

}
