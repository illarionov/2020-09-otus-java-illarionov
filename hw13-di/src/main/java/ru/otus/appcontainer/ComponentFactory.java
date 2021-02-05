package ru.otus.appcontainer;

interface ComponentFactory<T> {

    Class<T> getComponentClass();

    String getComponentName();

    int getComponentOrder();

    void link(Linker linker);

    T get();
}
