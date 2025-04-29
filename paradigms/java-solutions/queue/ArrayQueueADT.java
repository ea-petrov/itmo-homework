package queue;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1..k: a'[i] = a[i]
public class ArrayQueueADT {
    private Object[] elems = new Object[2];
    private int size;
    private int head;
    private int tail;

    //Pre: element != null
    //Post: n' = n + 1 && a'[n'] == element && immutable(n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        ensureCapacity(queue, queue.size + 1);
        queue.elems[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.elems.length;
        queue.size++;
    }

    // Pre: capacity > 0
    // Post: new capacity >= old capacity
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elems.length) {
            Object[] newElems = new Object[queue.elems.length * 2];
            System.arraycopy(queue.elems, queue.head, newElems, 0, queue.elems.length - queue.head);
            System.arraycopy(queue.elems, 0, newElems, queue.elems.length - queue.head, queue.tail);
            queue.elems = newElems;
            queue.head = 0;
            queue.tail = queue.size;
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1 && R = a[1], after a[1] = null && immutable(n')
    public static Object dequeue(ArrayQueueADT queue) {
        Object res = queue.elems[queue.head];
        queue.elems[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elems.length;
        queue.size--;
        return res;
    }

    // Pre: n > 0
    // Post: R = a[1] && immutable(n)
    public static Object element(ArrayQueueADT queue) {
        return queue.elems[queue.head];
    }

    // Pre: true
    // Post: R = (n = 0) && n' = n && immutable(n)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: true
    // Post: n' = 0 && forall a[i] = null;
    public static void clear(ArrayQueueADT queue) {
        queue.elems = new Object[2];
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
    }

    // Pre: index >= 0 && index <= n && a[index] != null
    // Post: R = a[index]
    public static Object get(ArrayQueueADT queue, int index){
        return queue.elems[(queue.head + queue.size - 1 - index) % queue.elems.length];
    }

    // Pre: index >= 0 && index <= n && value != null
    // Post: a[index] = value
    public static void set(ArrayQueueADT queue, int index, Object value){
        queue.elems[(queue.head + queue.size - 1 - index) % queue.elems.length] = value;
    }
}