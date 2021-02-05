package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
    
}
