package com.adm.scheduler.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ObjectPool<T> implements ObjectFactory<T>, Pool<T> {

    private int size;

    private boolean shutdown;

    private BlockingQueue<T> objects;

    public ObjectPool(int size) {
	this.size = size;
	shutdown = false;
	init();
    }

    /*
     * create expensive resource
     */
    private void init() {
	objects = new LinkedBlockingQueue<T>();
	for (int i = 0; i < size; i++) {
	    objects.add(createNew());
	}
    }

    @Override
    public T get() {
	{
	    if (!shutdown) {
		T t = null;

		try {
		    t = objects.take();
		    System.err.println("get: " + objects.size());
		} catch (Exception ie) {
		}

		return t;
	    }

	    throw new IllegalStateException("Object pool is already shutdown.");
	}
    }

    @Override
    public void release(T t) {
	try {
	    objects.offer(t);
	    System.err.println("release: " + objects.size());
	} catch (Exception e) {
	    
	}
    }

    @Override
    public void shutdown() {
	//clear the reasources - our case just clear the pool
	objects.clear();
    }
    
    public int size() {
	return objects.size();
    }

}
