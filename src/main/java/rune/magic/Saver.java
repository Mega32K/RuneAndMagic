package rune.magic;

public class Saver<T> {
    private final T v;

    public Saver(T object) {
        v = object;
    }

    public T getV() {
        return v;
    }
}
