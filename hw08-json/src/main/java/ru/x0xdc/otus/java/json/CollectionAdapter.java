package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;
import java.util.Collection;

public class CollectionAdapter implements JsonAdapter<Collection<?>> {

    private final JsonAdapters adapters;

    public CollectionAdapter(JsonAdapters adapters) {
        this.adapters = adapters;
    }

    @Override
    public void write(JsonGenerator generator, Collection<?> collection) {
        if (collection == null) {
            generator.writeNull();
        } else {
            write(generator, null, false, collection);
        }
    }

    @Override
    public void write(JsonGenerator generator, String fieldName, Collection<?> collection) {
        if (collection == null) {
            generator.writeNull(fieldName);
        } else {
            write(generator, fieldName, true, collection);
        }
    }

    private void write(JsonGenerator generator, String fieldName, boolean writeFieldName, Collection<?> list) {
        if (writeFieldName) {
            generator.writeStartArray(fieldName);
        } else {
            generator.writeStartArray();
        }

        for (Object item: list) {
            JsonAdapter<Object> adapter = adapters.getAdapter(item.getClass());
            adapter.write(generator, item);
        }

        generator.writeEnd();
    }
}
