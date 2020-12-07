package ru.x0xdc.otus.java.json;

import javax.json.stream.JsonGenerator;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ObjectAdapter<T> implements JsonAdapter<T> {

    private final boolean skipNullFields;

    private final List<FieldInfo> fields;

    public static <T> ObjectAdapter<T> create(JsonAdapters adapters, Class<?> objectClass) throws UnsupportedTypeException {
        Stream<Field> fields;

        if (objectClass == Object.class) {
            fields = Stream.empty();
        } else {
            String packageName = objectClass.getPackageName();
            if (packageName.startsWith("java.")
                    || packageName.startsWith("javax.")) {
                throw new UnsupportedTypeException("Type " + objectClass.getName() + " not supported");
            }

            fields = Arrays.stream(objectClass.getDeclaredFields())
                    .filter(f -> {
                        int modifiers = f.getModifiers();
                        return !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers);
                    });
        }

        List<FieldInfo> fieldList = fields
                .map(field -> {
                    String jsonName = field.getName();
                    //Type type = field.getGenericType();
                    Class<?> type = field.getType();

                    JsonAdapter<Object> adapter;
                    try {
                        adapter = adapters.getAdapter(type);
                    } catch (UnsupportedTypeException e) {
                        throw new UnsupportedTypeException("Unsupported field `" + field.getName() + "` of type " + type);
                    }
                    return new FieldInfo(field, jsonName, adapter);
                })
                .collect(Collectors.toList());


        for (FieldInfo f : fieldList) {
            f.field.setAccessible(true);
        }

        return new ObjectAdapter<>(fieldList, true);
    }

    private ObjectAdapter(List<FieldInfo> fields, boolean skipNullFields) {
        this.fields = fields;
        this.skipNullFields = skipNullFields;
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
            if (!skipNullFields) {
                generator.writeNull(fieldName);
            }
        } else {
            write(generator, fieldName, true, object);
        }
    }

    private void write(JsonGenerator generator, String fieldName, boolean writeFieldName, Object object) {
        if (writeFieldName) {
            generator.writeStartObject(fieldName);
        } else {
            generator.writeStartObject();
        }

        for (FieldInfo f : fields) {
            Object value;
            try {
                value = f.field.get(object);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            if (!(value == null && skipNullFields)) {
                f.adapter.write(generator, f.fieldName, value);
            }
        }
        generator.writeEnd();
    }

    private static class FieldInfo {

        final Field field;

        final String fieldName;

        final JsonAdapter<Object> adapter;

        FieldInfo(Field field, String fieldName, JsonAdapter<Object> adapter) {
            this.field = field;
            this.fieldName = fieldName;
            this.adapter = adapter;
        }
    }
}
