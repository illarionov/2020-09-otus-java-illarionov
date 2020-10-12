package ru.x0xdc.otus.java.diyarraylist;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.*;

@DisplayName("DiyArrayList")
class DiyArrayListTest {

    public static <E> List<E> createList() { return new DiyArrayList<>(); }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("new DiyArrayList()")
    void isInstantiatedWithNew() {
        createList();
    }

    @Nested
    @DisplayName(".size()")
    class TestSize {

        @Test
        void newListShouldBeEmpty() {
            var list = createList();
            assertEquals(0, list.size());
        }

        @Test
        void shouldNotBeEmptyAfterAdd() {
            var list = createList();
            list.add(null);
            assertEquals(1, list.size());
        }

        @Test
        void shouldBeEmptyAfterRemove() {
            var list = createList();
            list.add(null);
            list.remove(0);
            assertEquals(0, list.size());
        }
    }

    @Nested
    @DisplayName(".isEmpty(")
    class TestIsEmpty {
        @Test
        void newListShouldBeEmpty() {
            var list = createList();
            assertTrue(list.isEmpty());
        }

        @Test
        void shouldNotBeEmptyAfterAdd() {
            var list = createList();
            list.add(null);
            assertFalse(list.isEmpty());
        }

        @Test
        void shouldBeEmptyAfterRemove() {
            var list = createList();
            list.add(null);
            list.remove(0);
            assertTrue(list.isEmpty());
        }
    }

    @Nested
    @DisplayName(".contains()")
    class TestContains {

        @Test
        void listWithTwoElements() {
            List<Integer> list = createList();
            list.add(0);
            list.add(1);
            assertTrue(list.contains(0));
            assertTrue(list.contains(1));
            assertFalse(list.contains(2));
        }

        @Test
        void listShouldNotContainElementAfterRemove() {
            List<String> list = createList();
            list.add("a");
            list.add("b");
            list.remove(0);
            assertFalse(list.contains("a"));
            assertTrue(list.contains("b"));
        }
    }

    @Nested
    @DisplayName(".iterator()")
    class TestIterator {

        @Test
        void shouldReturnIterator() {
            var list = createList();
            assertNotNull(list.iterator());
        }

    }

    @Nested
    @DisplayName(".toArray()")
    class TestToArray {

        @Test
        void newListShouldReturnEmptyArray() {
            var list = createList();
            assertArrayEquals(new Object[0], list.toArray());
        }

        @Test
        void nonEmptyListShouldReturnArrayOfListItems() {
            List<Integer> list = createList();
            IntStream.rangeClosed(1, 5).boxed().forEach(list::add);
            list.remove(4);
            assertArrayEquals(new Object[]{1, 2, 3, 4}, list.toArray());
        }

    }

    @Nested
    @DisplayName(".toArray(array)")
    class TestToArrayWithArgument {

        @SuppressWarnings("ConstantConditions")
        @Test
        void shouldThrowNpeOnNull() {
            var list = createList();
            assertThrows(NullPointerException.class, () -> list.toArray((Object[]) null));
        }

        @ParameterizedTest(name="[{index}] test list.toArray(new Integer[{0}])")
        @ValueSource(ints = {0, 4})
        void shouldReturnNewArrayWhenDstNotBigEnough(int dstSize) {
            List<Integer> list = createList();
            Integer[] expected = new Integer[] {1,2,3,4,5};
            list.addAll(Arrays.asList(expected));
            Integer[] dst = new Integer[dstSize];

            Integer[] result = list.toArray(dst);

            assertArrayEquals(expected, result);
            assertNotSame(result, dst);
        }

        @Test
        void emptyListShouldReturnSameEmptyArrayArgument() {
            List<Integer> list = createList();
            list.add(42);
            list.remove(0);
            Integer[] dst = new Integer[0];

            Integer[] result = list.toArray(dst);
            assertSame(result, dst);
        }

        @Test
        void shouldReturnSameArrayWhenDstBigEnough() {
            List<Integer> list = createList();
            Integer[] expected = new Integer[] {1,2,3,4,5};
            list.addAll(Arrays.asList(expected));
            Integer[] dst = new Integer[5];

            Integer[] result = list.toArray(dst);

            assertArrayEquals(expected, result);
            assertSame(result, dst);
        }

        @Test
        void shouldPadResultWithNullIfEnoughRoom() {
            List<Integer> list = createList();
            IntStream.rangeClosed(1,5).forEach(list::add);
            Integer[] dst = new Integer[] {0,0,0,0,0,0,0,0};

            Integer[] result = list.toArray(dst);

            assertArrayEquals(new Integer[] {1,2,3,4,5,null,0,0}, result);
            assertSame(result, dst);
        }
    }

    @Nested
    @DisplayName(".add()")
    class TestAdd {

        @Test
        void testGrowArray() {
            List<Integer> list = createList();

            for (int value = 0; value < 1000; value++) {
                boolean added = list.add(value);
                assertTrue(added);
            }
            assertEquals(999, list.get(999));
        }
    }

    @Nested
    @DisplayName(".remove(Object)")
    class TestRemoveObject {

        class TestData {
            final List<Integer> source;
            final Object removedElement;
            final List<Integer> expectedResult;
            final boolean expectedChanged;

            private TestData(List<Integer> source, Object removedElement, List<Integer> expectedResult, boolean expectedChanged) {
                this.source = source;
                this.removedElement = removedElement;
                this.expectedResult = expectedResult;
                this.expectedChanged = expectedChanged;
            }
        }

        @TestFactory
        Stream<DynamicTest> testRemoveElement() {
            return Stream.of(
                    new TestData(emptyList(), 1, emptyList(), false),
                    new TestData(emptyList(), null, emptyList(), false),
                    new TestData(singletonList(1), 1, emptyList(), true),
                    new TestData(List.of(1, 2, 3, 4), 5, List.of(1, 2, 3, 4), false),
                    new TestData(List.of(1, 2, 3, 4, 5), 1, List.of(2, 3, 4, 5), true),
                    new TestData(List.of(1, 2, 3, 4, 5), 2, List.of(1, 3, 4, 5), true),
                    new TestData(List.of(1, 2, 3, 4, 5), 5, List.of(1, 2, 3, 4), true)
            ).map(testData -> {
                String name = "remove `" + testData.removedElement + "` from list " + testData.source;
                return dynamicTest(name, () -> {
                    var list = createList();
                    list.addAll(testData.source);

                    boolean changed = list.remove(testData.removedElement);

                    assertEquals(testData.expectedChanged, changed);
                    assertEquals(testData.expectedResult, list);
                });
            });
        }
    }

    @Nested
    @DisplayName(".addAll()")
    class TestAddAll {
        @Test
        void shouldAddAllElementsInOrder() {
            List<Integer> list = createList();
            boolean changed = list.addAll(List.of(1, 2,3,4,5));

            assertEquals(List.of(1,2,3,4,5), list);
            assertTrue(changed);
        }
    }

    @Nested
    @DisplayName(".clear()")
    class TestClear {
        @Test
        void testClear() {
            List<Integer> list = createList();
            list.add(1);
            assertFalse(list.isEmpty());

            list.clear();
            //noinspection ConstantConditions
            assertTrue(list.isEmpty());
        }
    }

    @Nested
    @DisplayName(".get()")
    class TestGet {
        @Test
        void shouldReturnAddedValue() {
            List<Integer> list = createList();
            list.add(42);
            assertEquals(42, list.get(0));
        }

        @Test
        void emptyListShouldThrowOutOfBoundsException() {
            var list = createList();
            //noinspection ResultOfMethodCallIgnored
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        }

        @ParameterizedTest(name = "[{index}] list.get({0}) should throw OOB")
        @ValueSource(ints = {-1, 1})
        void shouldThrowOutOfBoundsExceptionOnBoundaries(int index) {
            var list = createList();
            list.add(null);
            //noinspection ResultOfMethodCallIgnored
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
        }
    }

    @Nested
    @DisplayName(".set()")
    class TestSet {
        @Test
        void testSet() {
            List<Integer> list = createList();
            list.add(42);

            Integer previous = list.set(0, 4242);

            assertEquals(4242, list.get(0));
            assertEquals(42, previous);
        }

        @ParameterizedTest(name = "[{index}] list.set({0}) should throw OOB")
        @ValueSource(ints = {-1, 1})
        void shouldThrowOutOfBoundsExceptionOnBoundaries(int index) {
            var list = createList();
            list.add(null);

            assertThrows(IndexOutOfBoundsException.class, () -> list.set(index, null));
        }
    }

    @Nested
    @DisplayName(".remove(int)")
    class TestRemoveByIndex {

        class TestData {
            final List<Object> source;
            final int removeIndex;
            final Object expectedResult;
            final List<Object> expectedList;

            TestData(List<Object> source, int removeIndex, Object expectedResult, List<Object> expectedList) {
                this.source = source;
                this.removeIndex = removeIndex;
                this.expectedResult = expectedResult;
                this.expectedList = expectedList;
            }
        }

        @TestFactory
        Stream<DynamicTest> testRemoveValue() {
            return Stream.of(
                    new TestData(List.of(1), 0, 1, emptyList()),
                    new TestData(List.of(10,20,30), 0, 10, List.of(20,30)),
                    new TestData(List.of(10,20,30), 1, 20, List.of(10,30)),
                    new TestData(List.of(10,20,30), 2, 30, List.of(10,20))
            ).map(testData -> {
                String displayName = "remove " + testData.removeIndex + "th item from list " + testData.source;
                return DynamicTest.dynamicTest(displayName, () -> {
                    var list = createList();
                    list.addAll(testData.source);

                    Object result = list.remove(testData.removeIndex);

                    assertEquals(testData.expectedResult, result);
                    assertEquals(testData.expectedList, list);
                });
            });
        }

        @ParameterizedTest(name = "[{index}] list.remove({0}) should throw OOB")
        @ValueSource(ints = {-1, 1})
        void shouldThrowOutOfBoundsExceptionOnBoundaries(int index) {
            var list = createList();

            list.add(null);

            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
        }
    }

    @Nested
    @DisplayName(".indexOf()")
    class TestIndexOf {
        class TestData {
            final List<Object> source;
            final Object searchItem;
            final int expectedResult;

            TestData(List<Object> source, Object searchItem, int expectedResult) {
                this.source = source;
                this.searchItem = searchItem;
                this.expectedResult = expectedResult;
            }
        }

        @TestFactory
        Stream<DynamicTest> testIndexOf() {
            return Stream.of(
                    new TestData(emptyList(), null, -1),
                    new TestData(emptyList(), 1, -1),
                    new TestData(singletonList(null), null, 0),
                    new TestData(singletonList(null), 0, -1),
                    new TestData(List.of(1,2,3), 1, 0),
                    new TestData(List.of(1,2,3), 3, 2),
                    new TestData(List.of(1,2,3), 42, -1)
            ).map(testData -> {
                String displayName = "index of element `" + testData.searchItem + "` in list " +
                        testData.source + " should be " + testData.expectedResult;
                return DynamicTest.dynamicTest(displayName, () -> {
                    var list = createList();
                    list.addAll(testData.source);

                    int result = list.indexOf(testData.searchItem);

                    assertEquals(testData.expectedResult, result);
                });
            });
        }
    }

    @Nested
    @DisplayName("List Iterator")
    class TestListIterator {

        @Nested
        @DisplayName("Empty list iterator")
        class TestEmptyListIterator {

            private final List<Object> emptyList = createList();
            ListIterator<Object> iterator;

            @BeforeEach
            void createIterator() {
                iterator = emptyList.listIterator();
            }

            @Test
            void shoutNotHaveNext() {
                assertFalse(iterator.hasNext());
            }

            @Test
            void shouldNotHavePrevious() {
                assertFalse(iterator.hasPrevious());
            }

            @Test
            void shouldThrowNoSuchElementExceptionOnNext() {
                assertThrows(NoSuchElementException.class, () -> iterator.next());
            }

            @Test
            void shouldThrowNoSuchElementExceptionOnPrevious() {
                assertThrows(NoSuchElementException.class, () -> iterator.previous());
            }

            @Test
            void nextIndexShouldBeListSize() {
                assertEquals(emptyList.size(), iterator.nextIndex());
            }

            @Test
            void testPreviousOnBeginning() {
                assertEquals(-1, iterator.previousIndex());
            }

            @Test
            void setShouldThrowIllegalStateException() {
                assertThrows(IllegalStateException.class, () -> iterator.set(42));
            }
        }

        @Nested
        class NonEmptyList {

            List<Integer> list;

            ListIterator<Integer> iterator;

            @BeforeEach
            void createIterator() {
                list = createList();
                Collections.addAll(list, 1, 2, 3);
                iterator = list.listIterator();
            }

            @RepeatedTest(2)
            void testIteratorStatus() {
                assertTrue(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
                assertEquals(0, iterator.nextIndex());
                assertEquals(-1, iterator.previousIndex());
                assertThrows(IllegalStateException.class, () -> iterator.set(42));
                assertThrows(NoSuchElementException.class, () -> iterator.previous());
            }

            @Test
            void testIteratorStatusAfterNext() {
                Integer result = iterator.next();

                assertEquals(1, result);
                assertTrue(iterator.hasNext());
                assertTrue(iterator.hasPrevious());
                assertEquals(1, iterator.nextIndex());
                assertEquals(0, iterator.previousIndex());
            }

            @Test
            void setElementShouldModifyListWhenOnFirstElement() {
                iterator.next();
                iterator.set(10);

                assertEquals(List.of(10, 2,3), list);
            }

            @Test
            void testIteratorStateAfterNextAndPrevious() {
                iterator.next();
                Integer result = iterator.previous();

                assertEquals(1, result);
                assertTrue(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
                assertEquals(0, iterator.nextIndex());
                assertEquals(-1, iterator.previousIndex());
                assertThrows(NoSuchElementException.class, () -> iterator.previous());
            }

            @Test
            void testIteratorStateOnEndOfList() {
                Integer result = -1;
                while (iterator.hasNext()) result = iterator.next();

                assertEquals(3, result);
                assertTrue(iterator.hasPrevious());
                assertEquals(list.size(), iterator.nextIndex());
                assertEquals(2, iterator.previousIndex());
                assertThrows(NoSuchElementException.class, () -> iterator.next());
            }

            @Test
            void testSetLastElement() {
                while (iterator.hasNext()) iterator.next();

                iterator.set(10);
                assertEquals(List.of(1, 2, 10), list);
            }
        }
    }

    @SuppressWarnings("SimplifiableAssertion")
    @Nested
    @DisplayName(".equals(), .hashCode()")
    class TestEqualsHashCode {
        @Test
        void listShouldBeEqualsItself() {
            var list = createList();
            assertEquals(list, list);
            //noinspection EqualsWithItself
            assertTrue(list.equals(list));
            assertEquals(list.hashCode(), list.hashCode());
        }

        @Test
        void emptyListsShouldBeEquals() {
            var list1 = createList();
            var list2 = createList();
            assertTrue(list1.equals(list2));
            assertTrue(list2.equals(list1));
            assertEquals(list2.hashCode(), list1.hashCode());
        }

        @Test
        void equalsShouldNotUseUnusedElementsOfBackingArray() {
            List<Integer> list1 = createList();
            IntStream.rangeClosed(1, 10).forEach(list1::add);
            list1.add(42);
            list1.remove(10);

            List<Integer> list2 = createList();
            IntStream.rangeClosed(1, 10).forEach(list2::add);
            list2.add(4242);
            list2.remove(10);

            assertTrue(list1.equals(list2));
            assertTrue(list2.equals(list1));
            assertEquals(list2.hashCode(), list1.hashCode());
        }

        @Test
        void shouldBeEqualsToAnyOtherList() {
            List<Integer> list1 = createList();
            IntStream.rangeClosed(1, 3).forEach(list1::add);
            List<Integer> list2 = List.of(1,2,3);

            assertTrue(list1.equals(list2));
            assertEquals(list2.hashCode(), list1.hashCode());
        }

        @Test
        void shouldBeEqualTransitive() {
            List<Integer> list1 = createList();
            List<Integer> list2 = createList();
            List<Integer> list3 = createList();
            IntStream.rangeClosed(1, 10).forEach(i -> {
                list1.add(i);
                list2.add(i);
                list3.add(i);
            });

            assertTrue(list1.equals(list2));
            assertTrue(list2.equals(list3));
            assertTrue(list1.equals(list3));

            assertTrue(list3.equals(list2));
            assertTrue(list2.equals(list1));
            assertTrue(list3.equals(list1));

            assertEquals(list1.hashCode(), list3.hashCode());
        }

        @Test
        void shouldNotBeEqualsToNotList() {
            var list1 = createList();
            Integer i = 1;

            //noinspection EqualsBetweenInconvertibleTypes
            assertFalse(list1.equals(i));
        }

        @Test
        void shouldNotBeEqualsToListOfDifferentSize() {
            List<Integer> list1 = createList();
            addAll(list1, 1, 2,3);

            List<Integer> list2 = List.of(1,2);
            assertFalse(list1.equals(list2));
            assertFalse(list2.equals(list1));
        }

        @Test
        void shouldNotBeEqualsDifferentListSameSize() {
            List<Integer> list1 = createList();
            addAll(list1, 1, 2,3);

            List<Integer> list2 = List.of(1,2, 4);
            assertFalse(list1.equals(list2));
            assertFalse(list2.equals(list1));
        }
    }

    @Nested
    class TestToString {

        class TestData {
            final List<Object> testList;
            final String expectedResult;

            TestData(List<Object> testList, String expectedResult) {
                this.testList = testList;
                this.expectedResult = expectedResult;
            }
        }

        @TestFactory
        Stream<DynamicTest> testToString() {
            return Stream.of(
                    new TestData(emptyList(), "[]"),
                    new TestData(List.of(1), "[1]"),
                    new TestData(List.of(1,2), "[1, 2]"),
                    new TestData(List.of(1,2,3), "[1, 2, 3]")
            ).map(testData -> DynamicTest.dynamicTest("testing toString() for list " + testData.expectedResult,
                    () -> {
                        var list = createList();
                        list.addAll(testData.testList);

                        assertEquals(testData.expectedResult, list.toString());
                    }));
        }
    }

    @Nested
    @DisplayName("Test Collections.addAll()")
    class TestCollectionsAddAll {

        @Test
        void emptyListShouldNotChangeList() {
            List<Integer> testList = createList();
            boolean changed = Collections.addAll(testList);
            assertFalse(changed);
        }

        @Test
        void shouldAddAllElementsFromOtherCollection() {
            List<Integer> testList = createList();
            Integer[] expectedData = IntStream.range(1, 100)
                    .boxed()
                    .toArray(Integer[]::new);

            boolean changed = Collections.addAll(testList, expectedData);
            assertThat(testList).containsExactly(expectedData);
            assertTrue(changed);
        }
    }

    @Nested
    @DisplayName("Test Collections.copy()")
    class TestCollectionsCopy {

        @Test
        void shouldThrowIndexOutOfBoundsExceptionOnDstNotBigEnough() {
            List<Integer> testList = createList();
            assertThrows(IndexOutOfBoundsException.class, () -> Collections.copy(testList, singletonList(1)));
        }

        @Test
        void shouldCorrectCopyAllElementsFromOtherCollection() {
            List<Integer> testList = createList();
            List<Integer> expectedData = IntStream.range(1, 100)
                    .peek(i -> testList.add(null))
                    .boxed()
                    .collect(Collectors.toList());
            Collections.copy(testList, expectedData);
            assertThat(testList).containsExactlyElementsOf(expectedData);
        }

        @Test
        void copyShouldUseListIterator() {
            List<Integer> testList = spy(createList());
            AtomicReference<ListIterator<Integer>> listIterator = new AtomicReference<>();

            when(testList.listIterator()).then((Answer<ListIterator<Integer>>) invocation -> {
                @SuppressWarnings("unchecked")
                ListIterator<Integer> iterator = (ListIterator<Integer>) spy(invocation.callRealMethod());
                listIterator.set(iterator);
                return iterator;
            });

            List<Integer> expectedData = IntStream.range(1, 100)
                    .peek(i -> testList.add(null))
                    .boxed()
                    .collect(Collectors.toList());

            Collections.copy(testList, expectedData);

            verify(testList, times(0)).set(anyInt(), anyInt());
            verify(listIterator.get(), times(expectedData.size())).set(anyInt());
            verify(testList, times(1)).listIterator();
        }
    }
    
    @Test
    void shouldCorrectSortElements(){
        List<Integer> testList = createList();
        List<Integer> expectedData = IntStream.range(1, 100)
                .peek(i -> testList.add(100 - i))
                .boxed()
                .collect(Collectors.toList());

        //noinspection Java8ListSort
        Collections.sort(testList, Comparator.naturalOrder());
        assertThat(testList).containsExactlyElementsOf(expectedData);
    }
}