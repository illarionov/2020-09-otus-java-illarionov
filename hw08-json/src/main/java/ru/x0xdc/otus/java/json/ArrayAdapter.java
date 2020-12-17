package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;
import java.lang.reflect.Array;

class ArrayAdapter implements JsonAdapter<Object> {

    private final JsonAdapters adapters;

    public ArrayAdapter(JsonAdapters adapters) {
        this.adapters = adapters;
    }

    @Override
    public void write(JsonGenerator generator, Object object) {
        if (object == null) {
            generator.writeNull();
        } else {
            write(generator, null, false, object);
        }
    }

    @Override
    public void write(JsonGenerator generator, String fieldName, Object object) {
        if (object == null) {
            generator.writeNull(fieldName);
        } else {
            write(generator, fieldName, true, object);
        }
    }

    private void write(JsonGenerator generator, String fieldName, boolean writeFieldName, Object array) {
        if (writeFieldName) {
            generator.writeStartArray(fieldName);
        } else {
            generator.writeStartArray();
        }

        Class<?> componentType = array.getClass().getComponentType();
        JsonAdapter<Object> componentAdapter = adapters.getAdapter(componentType);

        for (int i = 0; i < Array.getLength(array); ++i) {
            componentAdapter.write(generator, Array.get(array, i));
        }
        generator.writeEnd();
    }
}
