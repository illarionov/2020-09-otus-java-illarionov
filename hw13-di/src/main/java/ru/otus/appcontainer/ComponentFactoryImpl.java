package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ComponentFactoryImpl<T> implements ComponentFactory<T> {

    private final Class<T> componentClass;
    private final String name;
    private final int order;

    private final Method method;
    private final Object component;

    private List<ComponentFactory<?>> dependencies;

    private T instance;

    static <T> ComponentFactoryImpl<T> create(Object containerConfig, Method componentMethod) {
        AppComponent annotation = componentMethod.getAnnotation(AppComponent.class);
        //noinspection unchecked
        return new ComponentFactoryImpl<>((Class<T>) componentMethod.getReturnType(),
                annotation.name(), annotation.order(), componentMethod, containerConfig);
    }

    ComponentFactoryImpl(Class<T> tClass, String name, int order, Method method, Object component) {
        this.componentClass = tClass;
        this.name = name;
        this.order = order;
        this.method = method;
        this.component = component;
    }

    @Override
    public void link(Linker linker) {
        this.dependencies = Arrays.stream(method.getParameterTypes())
                .map(linker::getFactory)
                .collect(Collectors.toUnmodifiableList());
    }
    
    @Override
    public Class<T> getComponentClass() {
        return componentClass;
    }

    @Override
    public String getComponentName() {
        return name;
    }

    @Override
    public int getComponentOrder() {
        return order;
    }

    @Override
    public T get() {
        if (instance == null) {
            instance = instantiate();
        }
        return instance;
    }

    private T instantiate() {
        if (dependencies == null) {
            throw new IllegalStateException("Factory not linked");
        }

        Object[] dependencies = this.dependencies.stream()
                .map(ComponentFactory::get)
                .toArray();

        method.setAccessible(true);
        try {
            //noinspection unchecked
            return (T) method.invoke(component, dependencies);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AppContainerException("Can not instantiate component `" + name + "`" + "of type " + componentClass , e);
        }
    }
}
