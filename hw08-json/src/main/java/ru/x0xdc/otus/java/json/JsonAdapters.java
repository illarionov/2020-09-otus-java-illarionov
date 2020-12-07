package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class JsonAdapters {

    private final Map<Class<?>, JsonAdapter<?>> defaultAdapters;

    private final Map<Type, JsonAdapter<?>> adapterCache;

    private final ArrayAdapter arrayAdapter;

    private final CollectionAdapter collectionAdapter;

    public JsonAdapters() {
        defaultAdapters = new LinkedHashMap<>();
        defaultAdapters.put(boolean.class, BOOLEAN_ADAPTER);
        defaultAdapters.put(byte.class, BYTE_ADAPTER);
        defaultAdapters.put(char.class, CHAR_ADAPTER);
        defaultAdapters.put(short.class, SHORT_ADAPTER);
        defaultAdapters.put(int.class, INTEGER_ADAPTER);
        defaultAdapters.put(long.class, LONG_ADAPTER);
        defaultAdapters.put(float.class, FLOAT_ADAPTER);
        defaultAdapters.put(double.class, DOUBLE_ADAPTER);
        defaultAdapters.put(Boolean.class, BOOLEAN_ADAPTER);
        defaultAdapters.put(Byte.class, BYTE_ADAPTER);
        defaultAdapters.put(Character.class, CHAR_ADAPTER);
        defaultAdapters.put(Short.class, SHORT_ADAPTER);
        defaultAdapters.put(Integer.class, INTEGER_ADAPTER);
        defaultAdapters.put(Long.class, LONG_ADAPTER);
        defaultAdapters.put(Float.class, FLOAT_ADAPTER);
        defaultAdapters.put(Double.class, DOUBLE_ADAPTER);
        defaultAdapters.put(String.class, STRING_ADAPTER);

        arrayAdapter = new ArrayAdapter(this);
        collectionAdapter = new CollectionAdapter(this);

        adapterCache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> JsonAdapter<T> getAdapter(Class<?> type) throws UnsupportedTypeException {
        if (type.isAnonymousClass()) {
            throw new UnsupportedTypeException("Anonymous classes are not supported");
        }

        if (type.isEnum()) {
            throw new UnsupportedTypeException("Enums are not supported");
        }
        
        if (type.isArray()) {
            return (JsonAdapter<T>) arrayAdapter;
        }

        if (Collection.class.isAssignableFrom(type)) {
            return (JsonAdapter<T>) collectionAdapter;
        }

        JsonAdapter<T> adapter = (JsonAdapter<T>) defaultAdapters.get(type);
        if (adapter != null) {
            return adapter;
        }

        adapter = (JsonAdapter<T>) adapterCache.get(type);
        if (adapter != null) {
            return adapter;
        }

        adapterCache.put(type, new LazyAdapter<T>(this, type));
        try {
            adapter = ObjectAdapter.create(this, type);
            adapterCache.put(type, adapter);
        } catch (Exception e) {
            adapterCache.remove(type);
            throw  e;
        }
        return adapter;
    }

    private static final JsonAdapter<Boolean> BOOLEAN_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {
        @Override
        public void write(JsonGenerator generator, Boolean object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Boolean object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Character> CHAR_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Character object) {
            generator.write(String.valueOf(object));
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Character object) {
            generator.write(fieldName, String.valueOf(object));
        }
    });

    private static final JsonAdapter<Byte> BYTE_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Byte object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Byte object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Integer> INTEGER_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Integer object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Integer object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Short> SHORT_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Short object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Short object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Long> LONG_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Long object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Long object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Float> FLOAT_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Float object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Float object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<Double> DOUBLE_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, Double object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, Double object) {
            generator.write(fieldName, object);
        }
    });

    private static final JsonAdapter<String> STRING_ADAPTER = new NullSafeDecorator<>(new JsonAdapter<>() {

        @Override
        public void write(JsonGenerator generator, String object) {
            generator.write(object);
        }

        @Override
        public void write(JsonGenerator generator, String fieldName, String object) {
            generator.write(fieldName, object);
        }
    });
}
