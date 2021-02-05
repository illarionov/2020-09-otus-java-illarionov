package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final ComponentGraph componentsGraph;

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        this(new ComponentGraphBuilder()
                .addConfigClasses(initialConfigClasses)
                .build());
    }

    public AppComponentsContainerImpl(String pkgName) {
        this(new ComponentGraphBuilder()
                .addConfigPackage(pkgName)
                .build());
    }

    private AppComponentsContainerImpl(ComponentGraph componentsGraph) {
        this.componentsGraph = componentsGraph;
        instantiateAllComponents();
    }

    private void instantiateAllComponents() {
        for (var component: componentsGraph.getComponents()) {
            componentsGraph.getFactory(component).get();
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        ComponentFactory<C> factory = componentsGraph.getFactory(componentClass);
        if (factory == null ){
            throw new AppContainerException("No component found for class " + componentClass);
        }
        return factory.get();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        ComponentFactory<C> factory = componentsGraph.getFactory(componentName);
        if (factory == null) {
            throw new AppContainerException("No component found for component `" + componentName + "`");
        }
        return factory.get();
    }
}
