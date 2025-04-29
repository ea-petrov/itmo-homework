package queue;
// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1..k: a'[i] = a[i]

public class ArrayQueue<T> extends AbstractQueue<T> {
    private Object[] elems = new Object[2];
    private int head;

    // Pre: capacity > 0
    // Post: new capacity >= old capacity
    private void ensureCapacity(int capacity) {
        if (capacity > elems.length) {
            Object[] newElements = new Object[elems.length * 2];
            System.arraycopy(elems, head, newElements, 0, elems.length - head);
            System.arraycopy(elems, 0, newElements, elems.length - head, size - (elems.length - head));
            elems = newElements;
            head = 0;
        }
    }

    //Pre: element != null
    //Post: n' = n + 1 && a'[n'] == element && immutable(n)
    @Override
    protected void enqueueImpl(T element) {
        ensureCapacity(size + 1);
        elems[(head + size) % elems.length] = element;
        size++;
    }

    // Pre: n > 0
    // Post: R = a[1] && immutable(n)
    @Override
    protected T elementImpl() {
        return (T) elems[head];
    }

    // Pre: n > 0
    // Post: n' = n - 1 && R = a[1], after a[1] = null && immutable(n')
    @Override
    protected T dequeueImpl() {
        Object element = elems[head];
        elems[head] = null;
        head = (head + 1) % elems.length;
        size--;
        return (T) element;
    }

    // Pre: true
    // Post: n' = 0 && forall a[i] = null;
    @Override
    protected void clearImpl() {
        elems = new Object[2];
        head = 0;
        size = 0;
    }

    // Pre: index >= 0 && index <= n && a[index] != null
    // Post: R = a[index]
    public T get(final int index) {
        return (T) elems[(head + size - 1 - index) % elems.length];

    }// Pre: index >= 0 && index <= n && value != null
    // Post: a[index] = value
    public void set(final int index, final T value) {
        elems[(head + size - 1 - index) % elems.length] = value;
    }
}