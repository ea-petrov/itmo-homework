package queue;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(k): forall i=1..k: a'[i] = a[i]

public abstract class AbstractQueue<T> implements Queue<T> {
    protected int size;
    protected abstract void enqueueImpl(T element);
    protected abstract T elementImpl();
    protected abstract T dequeueImpl();
    protected abstract void clearImpl();
    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    @Override
    public int size() {
        return size;
    }

    // Pre: true
    // Post: R = (n = 0) && n' = n && immutable(n)
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    // Pre: true
    // Post: R = clearImpl()
    @Override
    public void clear() {
        clearImpl();
    }

    // Pre: true
    // Post: R = dequeueImpl()
    @Override
    public T dequeue() {
        return dequeueImpl();
    }

    // Pre: true
    // Post: R = elementImpl()
    @Override
    public T element() {
        return elementImpl();
    }

    // Pre: true
    // Post: R = enqueueImpl()
    @Override
    public void enqueue(T element) {
        enqueueImpl(element);
    }

    // Pre: Inv && Let
    // Post: R = [head, ..., tail]
    @Override
    public String toStr() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int startSize = size;
        for (int i = 0; i < size; i++) {
            T elem = dequeue();
            sb.append(elem.toString());
            if (i < startSize - 1) {
                sb.append(", ");
            }
            enqueue(elem);
        }
        sb.append("]");
        return sb.toString();
    }
}