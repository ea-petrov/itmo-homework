package queue;

public interface Queue<T> {
    void enqueue(T element);
    T dequeue();
    T element();
    int size();
    boolean isEmpty();
    void clear();
    String toStr();
}