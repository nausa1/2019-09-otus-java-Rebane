package ru.otus.hw02;

import java.util.*;
import java.util.function.UnaryOperator;

public class DIYarrayList <T> implements List<T> {
    private final int INIT_SIZE = 8;
    private Object[] array = new Object[INIT_SIZE];
    private int size;

    public DIYarrayList(int initialCapacity) {
        if (initialCapacity >= 0) {
            this.array = new Object[initialCapacity];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    public DIYarrayList()
    {
        this.array = new Object[INIT_SIZE];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    private void enlarge()
    {
        Object[] newArray = new Object[array.length*2];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    @Override
    public boolean add(T t) {
        if (array.length == size)
            enlarge();
        array[size]=t;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
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
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Object[] newArray = new Object[size];
        System.arraycopy(array, 0, newArray, 0, size);
        Arrays.sort(newArray);
        System.arraycopy(newArray, 0, array, 0, size);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if ( index < 0 || index > size)
            throw new IndexOutOfBoundsException(index);

        return (T) array[index];
    }

    @Override
    public T set(int index, T element) {
        if ( 0 <= index && index < size)
            {
                T old = get(index);
                array[index] = element;
                return old;
            }
        else
            throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(int index, T element) {
        if ( 0 <= index && index < size)
        {
            if (array.length == size)
                enlarge();
            for(int i = size; i > index; i--)
                array[i] = array[i-1];
            array[index] = element;
            size++;
        }
        else
            throw new IndexOutOfBoundsException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
}

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    public void print()
    {
        for (int i = 0; i < size; i++) {
            System.out.println(array[i]);
        }
    }

    private class ListItr implements ListIterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        ListItr(int index) {
            cursor = index;
        }

        public boolean hasNext() {
            return cursor != size;
        }

        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            cursor = i + 1;
            return (T) array[lastRet = i];
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            cursor = i;
            return (T) array[lastRet = i];
        }

        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                DIYarrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T e) {
            throw new UnsupportedOperationException();
        }
    }
}
