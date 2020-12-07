# Json object writer

**Цель:**
Научиться сериализовывать объект в JSON, попрактиковаться в разборе структуры объекта.

Написать свой JSON Object Writer (object to JSON string), аналогичный gson, на основе *javax.json*.

**Пример использования:**
```java
MyGson myGson = new MyGson();
AnyObject obj = new AnyObject(22, "test", 10);
String myJson = myGson.toJson(obj);
```

Должно получиться:
```java
AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
System.out.println(obj.equals(obj2));
```

**Поддержать:**

* примитивные типы и Wrapper-ы (*Integer*, *Float* и т.д.)
* строки
* массивы примитивных типов
* коллекции (*interface Collection*)

Не забывать, что *obj* может быть *null*

