package com.adm.scheduler.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ObjectPool<T> implements ObjectFactory<T>, Pool<T> {

    private static final Logger LOGGER = LogManager.getLogger();

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
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(), e);
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
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }

    @Override
    public void shutdown() {
	// clear the reasources - our case just clear the pool
	objects.clear();
    }

    public int size() {
	return objects.size();
    }

}
