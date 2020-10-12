package ru.x0xdc.otus.java.diyarraylist;

import java.util.*;

public class Main {
    private static final Random RANDOM = new Random(424242);

    private final List<Integer> source;

    public static void main(String[] args) {
        Main main = new Main(generatePseudorandomList(20));
        main.execute();
    }

    private static List<Integer> generatePseudorandomList(int size) {
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size - 2; ++i) list.add(RANDOM.nextInt(100));
        return Collections.unmodifiableList(list);
    }

    public Main(List<Integer> source) {
        this.source = source;
    }

    private void execute() {
        System.out.println("SRC:                         " + source);

        List<Integer> addAllList = createListAddAll(source.toArray(new Integer[0]));
        System.out.println("Collections.addAll() result: " + addAllList);

        List<Integer> copiedList = createListCopy(source);
        System.out.println("Collections.copy() result:   " + copiedList);

        List<Integer> sortedList = createListSort(source);
        System.out.println("Collections.sort() result:   " + sortedList);
    }
    
    @SafeVarargs
    private <T> List<T> createListAddAll(T... elements) {
        List<T> result = createList();
        Collections.addAll(result, elements);
        return result;
    }
    
    private <T> List<T> createListCopy(List<? extends T> src) {
        List<T> result = createList();
        for (int i = 0; i < src.size(); i++) result.add(null); // ensure minimum size
        Collections.copy(result, src);
        return result;
    }

    private <T extends Comparable<? super T>> List<T> createListSort(List<? extends T> src) {
        List<T> result = createList();
        result.addAll(src);
        Collections.sort(result);
        return result;
    }

    private <T> List<T> createList() {
        return new DiyArrayList<>();
        //return new ArrayList<>();
    }
}
