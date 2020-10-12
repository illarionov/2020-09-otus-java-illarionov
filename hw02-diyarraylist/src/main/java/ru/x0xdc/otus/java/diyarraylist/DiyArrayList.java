package ru.x0xdc.otus.java.diyarraylist;

import java.util.*;

/**
 * Упрощенная версия {@linkplain ArrayList}.
 * @param <T> тип элемента списка
 */
@SuppressWarnings("NullableProblems")
public class DiyArrayList<T> implements List<T> {

    private static final int INITIAL_CAPACITY = 4;

    private Object[] data;

    private int size;

    public DiyArrayList() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; ++i) {
            if (Objects.equals(o, data[i])) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new DiyIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    @Override
    public <T1> T1[] toArray(T1[] dst) {
        if (size > dst.length) {
            return (T1[]) Arrays.copyOf(data, size, dst.getClass());
        }
        System.arraycopy(data, 0, dst, 0, size);
        if (dst.length > size) dst[size] = null;
        return dst;
    }

    @Override
    public boolean add(T t) {
        if (size >= data.length) {
            if (size == Integer.MAX_VALUE) {
                throw new IndexOutOfBoundsException();
            }
            int newCapacity = data.length + (data.length / 2);
            if (newCapacity <= 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            data = Arrays.copyOf(data, newCapacity);
        }
        data[size++] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx < 0) return false;
        remove(idx);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T item: c) changed |= add(item);
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        size = 0;
        data = new Object[INITIAL_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        T result = (T) data[index];
        data[index] = element;
        return result;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        Objects.checkIndex(index, size);
        T item = (T) data[index];
        if (index != size - 1) {
            System.arraycopy(data, index + 1, data, index, size - index - 1);
        }
        size -= 1;

        return item;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; ++i) {
            if (Objects.equals(o, data[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DiyIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        return new DiyIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }

        List<?> that = (List<?>) o;

        if (size != that.size()) {
            return false;
        }

        int idx = 0;
        for (Object item: that) {
            if (!Objects.equals(item, data[idx++])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; ++i) {
            Object item = data[i];
            result = 31 * result + (item == null ? 0 : item.hashCode());
        }
        return result;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        for (int i = 0; i < size; ++i) sj.add(String.valueOf(data[i]));
        return "[" + sj.toString() + "]";
    }

    private class DiyIterator implements ListIterator<T> {

        private int lastIndex;

        public DiyIterator() {
            lastIndex = -1;
        }

        public DiyIterator(int index) {
            lastIndex = index - 1;
        }

        @Override
        public boolean hasNext() {
            return lastIndex + 1 < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return (T) data[++lastIndex];
        }

        @Override
        public boolean hasPrevious() {
            return lastIndex >= 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            return (T) data[lastIndex--];
        }

        @Override
        public int nextIndex() {
            return lastIndex + 1;
        }

        @Override
        public int previousIndex() {
            return lastIndex;
        }

        @Override
        public void set(T t) {
            if (lastIndex == -1) throw new IllegalStateException();
            data[lastIndex] = t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
