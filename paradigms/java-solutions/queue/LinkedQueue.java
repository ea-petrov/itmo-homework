package queue;

public class LinkedQueue<T> extends AbstractQueue<T> {
    private static class Node<T> {
        T typo;
        Node<T> next;

        Node(T typo) {
            this.typo = typo;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;

    //Pre: element != null
    //Post: n' = n + 1 && a'[n'] == element && immutable(n)
    @Override
    protected void enqueueImpl(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    // Pre: n > 0
    // Post: R = a[1] && immutable(n)
    @Override
    protected T elementImpl() {
        return head.typo;
    }

    // Pre: n > 0
    // Post: n' = n - 1 && R = a[1], after a[1] = null && immutable(n')
    @Override
    protected T dequeueImpl() {
        T result = head.typo;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return result;
    }

    // Pre: true
    // Post: n' = 0 && forall a[i] = null;
    @Override
    protected void clearImpl() {
        head = null;
        tail = null;
        size = 0;
    }
}