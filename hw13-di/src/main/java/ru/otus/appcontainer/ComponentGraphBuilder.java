package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

class ComponentGraphBuilder {

    private final Set<Class<?>> configClasses = new HashSet<>();

    ComponentGraphBuilder() {
    }

    public ComponentGraphBuilder addConfigClass(Class<?> configClass) {
        configClasses.add(configClass);
        return this;
    }

    public ComponentGraphBuilder addConfigClasses(Class<?>... configClasses) {
        Collections.addAll(this.configClasses, configClasses);
        return this;
    }

    public ComponentGraphBuilder addConfigPackage(String pkgName) {
        Reflections reflections = new Reflections(pkgName);
        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .stream()
                .filter(cls -> !cls.isAnnotation())
                .forEachOrdered(configClasses::add);
        return this;
    }

    public ComponentGraph build() throws AppContainerException {
        ComponentGraphImpl factories = new ComponentGraphImpl();
        for (Class<?> configClass : configClasses) {
            processConfig(configClass)
                    .forEachOrdered(factories::addFactory);
        }

        Linker linker = new Linker(factories);
        return linker.linkFactories();
    }

    private Stream<ComponentFactory<?>> processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object containerConfig = instantiateConfig(configClass);
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .map(method -> ComponentFactoryImpl.create(containerConfig, method));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new AppContainerException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private static <T> T instantiateConfig(Class<T> configClass) {
        try {
            Constructor<T> noArgConstructor = configClass.getConstructor();
            noArgConstructor.setAccessible(true);
            return noArgConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new AppContainerException("Container config `" + configClass.getName() + "` should have public no-arg constructor");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new AppContainerException("Could not instantiate container config `" + configClass.getName() + "`");
        }
    }
}
