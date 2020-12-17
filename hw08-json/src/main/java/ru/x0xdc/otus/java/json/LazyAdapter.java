package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;

class LazyAdapter<T> implements JsonAdapter<T> {

    private final JsonAdapters adapters;

    private final Class<?> type;

    private JsonAdapter<T> adapter;

    public LazyAdapter(JsonAdapters adapters, Class<?> type) {
        this.adapters = adapters;
        this.type = type;
    }

    @Override
    public void write(JsonGenerator generator, T object) {
        getAdapter().write(generator, object);
    }

    @Override
    public void write(JsonGenerator generator, String fieldName, T object) {
        getAdapter().write(generator, fieldName, object);
    }

    private JsonAdapter<T> getAdapter() {
        if (adapter == null) {
            adapter = adapters.getAdapter(type);
        }
        return adapter;
    }
}
