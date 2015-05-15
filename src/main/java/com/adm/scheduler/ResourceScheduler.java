package com.adm.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.adm.scheduler.pool.GatewayPool;

public class ResourceScheduler implements Runnable {

    private int maxRes;

    private GatewayPool pool;

    MessageComparator comparator = new MessageComparator();

    PriorityQueue<Message> queue = new PriorityQueue<Message>(
	    Integer.MAX_VALUE, comparator);

    private List<Message> messages = new ArrayList<Message>();

    public ResourceScheduler(int max) {
	if (max <= 0)
	    throw new IllegalArgumentException();
	this.maxRes = max;
	pool = new GatewayPool(maxRes);
    }

    public void add(Message msg) {
	messages.add(msg);
    }

    @Override
    public void run() {
	while (true) {
	    if (queue.size() != 0) {
		Message msg = queue.remove();
		sendMessage(msg);
	    }
	}
    }

    public void sendMessage(Message msg) {
	synchronized (this) {
	    Gateway gate = null;
	    try {
		gate = pool.get();
		gate.send(msg);
	    } catch (Exception ex) {
		// TODO display some errors
	    } finally {
		if (gate != null)
		    pool.release(gate);
	    }
	}

    }
}
