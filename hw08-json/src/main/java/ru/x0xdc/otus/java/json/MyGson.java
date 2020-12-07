package ru.x0xdc.otus.java.json;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.io.Writer;

public class MyGson {

    private final JsonAdapters adapters;

    public MyGson() {
        adapters = new JsonAdapters();
    }

    public String toJson(Object object) {
        JsonGenerator generator;
        Writer writer;

        writer = new StringWriter();
        generator = Json.createGenerator(writer);

        if (object == null) {
            generator.writeNull();
        } else {
            JsonAdapter<Object> adapter = adapters.getAdapter(object.getClass());
            adapter.write(generator, object);
        }
        generator.close();

        return writer.toString();
    }
}
