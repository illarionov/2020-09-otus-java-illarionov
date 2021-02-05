package ru.otus.appcontainer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ComponentGraphImpl implements ComponentGraph {

    private final Map<Class<?>, ComponentFactory<?>> factories = new HashMap<>();

    private final Map<String, ComponentFactory<?>> factoriesByName = new HashMap<>();

    public ComponentGraphImpl() {
    }

    @Override
    public <T> ComponentFactory<T> getFactory(Class<T> componentClass) {
        @SuppressWarnings("unchecked")
        ComponentFactory<T> factory = (ComponentFactory<T>) factories.get(componentClass);

        if (factory == null) {
            factory = getFactoryWithMinOrder(componentClass);
        }

        return factory;
    }

    private <T> ComponentFactory<T> getFactoryWithMinOrder(Class<T> componentClass) {
        //noinspection unchecked
        return (ComponentFactoryImpl<T>) factories.values().stream()
                .filter(factory -> factory.getComponentClass().isAssignableFrom(componentClass))
                .min(Comparator.comparingInt(ComponentFactory::getComponentOrder))
                .orElse(null);
    }

    @Override
    public <T> ComponentFactory<T> getFactory(String name) {
        //noinspection unchecked
        return (ComponentFactoryImpl<T>) factoriesByName.get(name);
    }

    @Override
    public List<Class<?>> getComponents() {
        return List.copyOf(factories.keySet());
    }

    <T> void addFactory(ComponentFactory<T> factory) {
        if (factoriesByName.containsKey(factory.getComponentName())) {
            throw new AppContainerException("Can not instantiate component `" + factory.getComponentName() + "`: component name must be unique");
        }

        if (factories.containsKey(factory.getComponentClass())) {
            throw new AppContainerException("Can not instantiate component `" + factory.getComponentClass()
                    + "`: component of this return type already declared");
        }

        factories.put(factory.getComponentClass(), factory);
        factoriesByName.put(factory.getComponentName(), factory);
    }
}
