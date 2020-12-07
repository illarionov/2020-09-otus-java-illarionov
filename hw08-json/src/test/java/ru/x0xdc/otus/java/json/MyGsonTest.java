package ru.x0xdc.otus.java.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MyGsonTest {

    Gson gson;

    MyGson myGson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder().create();
        myGson = new MyGson();
    }

    @ParameterizedTest
    @DisplayName("Сериализует примитивные типы и wrapper'ы корректно")
    @MethodSource("primitiveTypesFactory")
    void toJsonShouldSerializePrimitiveTypes(Object object) {
        String expected = gson.toJson(object);
        String actual = myGson.toJson(object);
        assertThat(actual)
                .isEqualTo(expected);
    }

    static Stream<Object> primitiveTypesFactory() {
        return Stream.of(null, (boolean) true, (char) 'c', (byte) 1, (short) 2, (int) 3, (long) 5, (float) 6, (double) 7,
                (String) "str");
    }

    @ParameterizedTest
    @DisplayName("Сериализует массивы примитивных типов и врапперов корректно")
    @MethodSource("arrayOfPrimitivesTypesFactory")
    void toJsonShouldSerializeArraysOfPrimitiveType(Object object) {
        String expected = gson.toJson(object);
        String actual = myGson.toJson(object);
        assertThat(actual)
                .isEqualTo(expected);
    }

    static Stream<Arguments> arrayOfPrimitivesTypesFactory() {
        return Stream.of(
                arguments((Object) new Object[]{}),
                arguments((Object) new Object[]{null, null, null}),
                arguments((Object) new boolean[]{true, false, true}),
                arguments((Object) new char[]{'a', 'b', 'c'}),
                arguments((Object) new byte[]{1, 2, 3}),
                arguments((Object) new short[]{4, 5, 6}),
                arguments((Object) new int[]{7, 8, 9}),
                arguments((Object) new long[]{10, 11, 12}),
                arguments((Object) new float[]{13, 14, 15}),
                arguments((Object) new double[]{16, 17, 18}),
                arguments((Object) new Boolean[]{true, false, true}),
                arguments((Object) new Character[]{'a', 'b', 'c'}),
                arguments((Object) new Byte[]{1, 2, 3}),
                arguments((Object) new Short[]{4, 5, 6}),
                arguments((Object) new Integer[]{7, 8, 9}),
                arguments((Object) new Long[]{10L, 11L, 12L}),
                arguments((Object) new Float[]{13f, 14f, 15f}),
                arguments((Object) new Double[]{16d, 17d, 18d}),
                arguments((Object) new String[]{"abc", "def"})
        );
    }

    @ParameterizedTest
    @DisplayName("Сериализует коллекции корректно")
    @MethodSource("collectionsFactory")
    void toJsonShouldSerializeCollections(Collection<?> collection) {
        String expected = gson.toJson(collection);
        String actual = myGson.toJson(collection);
        assertThat(actual)
                .isEqualTo(expected);
    }

    static Stream<Arguments> collectionsFactory() {
        return Stream.of(
                arguments(Collections.emptyList()),
                arguments(Set.of("a", "b")),
                arguments(List.of(1, 2 ,3)),
                arguments(new ArrayList<>(List.of(4, 5))),
                arguments(List.of(List.of(6, 7), Set.of('c', 'd'))),
                arguments(List.of(new TestObject(), new TestObject(), new TestObject()))
        );
    }


    @ParameterizedTest()
    @DisplayName("Сериализует объекты корректно")
    @MethodSource("objectsFactory")
    void toJsonShouldSerializeObjects(Object object) {
        String expected = gson.toJson(object);
        String actual = myGson.toJson(object);
        assertThat(actual)
                .isEqualTo(expected);
    }

    static Stream<Object> objectsFactory() {
        TestObject objectWithNulls = new TestObject();
        objectWithNulls.long1 = null;
        objectWithNulls.char2 = null;
        objectWithNulls.stringSet = null;
        return Stream.of(
                new Object(),
                new BooleanObject(),
                new BooleanArrayObject(new boolean[]{}),
                new BooleanArrayObject(null),
                new ListWrapper(null),
                new ListWrapper(Collections.emptyList()),
                new TestObject(),
                objectWithNulls
        );
    }

    static class BooleanObject {
        boolean b = true;
    }

    static class BooleanArrayObject {
        final boolean[] b;

        BooleanArrayObject(boolean[] b) {
            this.b = b;
        }
    }

    static class ListWrapper {
        final List<Boolean> l;

        ListWrapper(List<Boolean> l) {
            this.l = l;
        }
    }

    static class TestObject {
        boolean   bool0    = true;
        char      char0    = 'd';
        byte      byte0    = 42;
        short     short0   = 5556;
        int       int0     = 42424243;
        long      long0    = 42424242424243L;
        float     float0   = 5.5f;
        double    double0  = 6.6;
        Boolean   boolean1 = true;
        Character char1    = 'd';
        Byte      byte1    = -42;
        Short     short1   = -45;
        Integer   integer1 = -42424243;
        Long      long1    = -42424242424243L;
        Float     float1   = -5.5f;
        Double    double1  = -6.6;
        String    string1  = "str";
        char[]    char2    = new char[]{'a', 'b', 'c'};
        byte[]    byte2    = new byte[]{1, 2, 3};
        short[]   short2   = new short[]{4, 5, 6};
        int[]     int2     = new int[]{7, 8, 9};
        long[]    long2    = new long[]{10, 11, 12};
        float[]   float2   = new float[]{13, 14, 15};
        double[]  double2  = new double[]{16, 17, 18};
        List<Character> characterList = List.of('a', 'b', 'c');
        Set<String> stringSet = Set.of("aa", "bb", "cc");
        Collection<Integer> integerCollection = List.of(0,1);
        LinkedList<Double> shortList = new LinkedList<>(List.of(5.5,6.5));
        BooleanObject booleanObject = new BooleanObject();
    }

    @Test
    @DisplayName("Сериализует объекты с ссылками на объекты своего типа корректно")
    void toJsonShouldSerializeCyclicReferences() {
        ClassWithCyclicReference item1 = new ClassWithCyclicReference(true, null);
        ClassWithCyclicReference item2 = new ClassWithCyclicReference(true, item1);
        ClassWithCyclicReference testItem = new ClassWithCyclicReference(true, item2);

        String expected = gson.toJson(testItem);
        String actual = myGson.toJson(testItem);
        assertThat(actual)
                .isEqualTo(expected);
    }

    static class ClassWithCyclicReference {
        boolean b;
        ClassWithCyclicReference next;

        ClassWithCyclicReference(boolean b, ClassWithCyclicReference next) {
            this.b = b;
            this.next = next;
        }
    }

    @ParameterizedTest
    @DisplayName("Выбрасывает исключение при неподдерживаемом типе")
    @MethodSource("unsupportedFieldFactory")
    void toJsonShouldThrowExceptionOnUnsupportedField(Object object) {
        Assertions.assertThatThrownBy(() -> {
            myGson.toJson(object);
        }).isInstanceOf(UnsupportedTypeException.class);
    }

    private static Stream<Object> unsupportedFieldFactory() {
        return Stream.of(
                new Object() {Runnable runnable = () -> {};},
                new RunnableWrapper(),
                new LocaleArrayWrapper(),
                new RunnableListWrapper()
        );
    }

    private static class RunnableWrapper {
        Runnable r = () -> {};
    }

    private static class LocaleArrayWrapper {
        Locale[] array = new Locale[] {Locale.ROOT};
    }

    private static class RunnableListWrapper {
        List<Runnable> listOfObjects = List.of(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

}