package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;

class NullSafeDecorator<T> implements JsonAdapter<T> {
    private final JsonAdapter<T> delegate;

    NullSafeDecorator(JsonAdapter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(JsonGenerator generator, T object) {
        if (object == null) {
            generator.writeNull();
        } else {
            delegate.write(generator, object);
        }
    }

    @Override
    public void write(JsonGenerator generator, String fieldName, T object) {
        if (object == null) {
            generator.writeNull();
        } else {
            delegate.write(generator, fieldName, object);
        }
    }
}
