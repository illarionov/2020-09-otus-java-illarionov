# Автоматическое логирование (АОП)

Цель:
* Понять как реализуется AOP, какие для этого есть технические средства


Разработать функционал: метод класса можно пометить самодельной аннотацией `@Log`, например, так:

```java
class TestLogging {
    @Log
    public void calculation(int param) {
    }
}
```

При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
Например так.
```java
class Demo {
    public void action() {
        new TestLogging().calculation(6);
    }
}
```

В консоле дожно быть:
```
executed method: calculation, param: 6
```

Явного вызова логирования быть не должно.

Аннотацию можно поставить, например, на такие методы:

```java
public void calculation(int param1)

public void calculation(int param1, int param2)

public void calculation(int param1, int param2, String param3)
```
