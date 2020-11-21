package ru.otus;

import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public ObjectForMessage() {
    }

    public ObjectForMessage(List<String> data) {
        if (data != null) {
            this.data = List.copyOf(data);
        }
    }

    public ObjectForMessage(ObjectForMessage other) {
        if (other != null && other.data != null) {
            this.data = List.copyOf(other.data);
        }
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
