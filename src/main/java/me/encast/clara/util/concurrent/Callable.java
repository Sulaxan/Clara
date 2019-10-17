package me.encast.clara.util.concurrent;

public interface Callable<T> {

    void onCall(T t);
}
