package com.adm.scheduler.pool;

public interface Pool<T> {

    /*
     * @return one of the pooled objects.
     */
    T get();

    /*
     * @param objcet T to be return back to pool
     */

    void release(T t);

    /**
     * Shuts down the pool. Should release all resources.
     */

    void shutdown();
}
