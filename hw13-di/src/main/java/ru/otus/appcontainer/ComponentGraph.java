package ru.otus.appcontainer;

import java.util.List;

interface ComponentGraph {
    <T> ComponentFactory<T> getFactory(Class<T> componentClass);

    <T> ComponentFactory<T> getFactory(String name);

    List<Class<?>> getComponents();
}
