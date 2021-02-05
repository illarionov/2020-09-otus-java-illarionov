package ru.otus.appcontainer;

/**
 * Класс для рекурсивной инициализации всех зависимостей фабрик
 */
class Linker {
    private final ComponentGraphImpl factories;

    private final ComponentGraphImpl linkedFactories;

    public Linker(ComponentGraphImpl factories) {
        this.factories = factories;
        this.linkedFactories = new ComponentGraphImpl();
    }

    public ComponentGraphImpl linkFactories() {
        for (Class<?> componentClass: factories.getComponents()) {
            getFactory(componentClass);
        }
        return linkedFactories;
    }

    public <T> ComponentFactory<T> getFactory(Class<T> componentClass) {
        ComponentFactory<T> factory = linkedFactories.getFactory(componentClass);
        if (factory == null) {
            factory = loadFactory(componentClass);
            factory.link(this);
            linkedFactories.addFactory(factory);
        }
        return factory;
    }

    private <T> ComponentFactory<T> loadFactory(Class<T> componentClass) {
        ComponentFactory<T> factory = factories.getFactory(componentClass);
        if (factory == null) {
            throw new AppContainerException("No component found for class " + componentClass);
        }
        return factory;
    }
}
