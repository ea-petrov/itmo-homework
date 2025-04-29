package queue;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1..k: a'[i] = a[i]
public class ArrayQueueModule {
    private static Object[] elems = new Object[2];
    private static int size;
    private static int head;
    private static int tail;

    //Pre: element != null
    //Post: n' = n + 1 && a'[n'] == element && immutable(n)
    public static void enqueue(Object element) {
        ensureCapacity(size + 1);
        elems[tail] = element;
        tail = (tail + 1) % elems.length;
        size++;
    }

    // Pre: n > 0
    // Post: n' = n - 1 && R = a[1], after a[1] = null && immutable(n')
    public static Object dequeue() {
        Object element = elems[head];
        elems[head] = null;
        head = (head + 1) % elems.length;
        size--;
        return element;
    }

    // Pre: capacity > 0
    // Post: new capacity >= old capacity
    private static void ensureCapacity(int capacity) {
        if (capacity > elems.length) {
            Object[] newElems = new Object[elems.length * 2];
            System.arraycopy(elems, head, newElems, 0, elems.length - head);
            System.arraycopy(elems, 0, newElems, elems.length - head, tail);
            elems = newElems;
            head = 0;
            tail = size;
        }
    }

    // Pre: n > 0
    // Post: R = a[1] && immutable(n)
    public static Object element() {
        return elems[head];
    }

    // Pre: true
    // Post: R = (n = 0) && n' = n && immutable(n)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' = 0 && forall a[i] = null;
    public static void clear() {
        elems = new Object[2];
        size = 0;
        head = 0;
        tail = 0;
    }

    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    public static int size() {
        return size;
    }

    // Pre: index >= 0 && index <= n && a[index] != null
    // Post: R = a[index]
    public static Object get(final int index){
        return elems[(head + size - 1 - index) % elems.length];

    }

    // Pre: index >= 0 && index <= n && value != null
    // Post: a[index] = value
    public static void set(final int index, Object value){
        elems[(head + size - 1 -index) % elems.length] = value;
    }
}