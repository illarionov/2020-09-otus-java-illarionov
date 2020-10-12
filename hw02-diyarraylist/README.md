# DIY ArrayList (контейнеры и алгоритмы)

Цель: 
* Изучить как устроена стандартная коллекция *ArrayList*.  
* Попрактиковаться в создании своей коллекции.  
* Написать свою реализацию *ArrayList* на основе массива. `class DIYarrayList<T> implements List<T>{...}`

Проверить, что на ней работают методы из `java.util.Collections`:
```java
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
```

* Проверять на коллекциях с 20 и больше элементами.
* *DIYarrayList* должен имплементировать ТОЛЬКО ОДИН интерфейс - *List*.
* Если метод не имплементирован, то он должен выбрасывать исключение *UnsupportedOperationException*.