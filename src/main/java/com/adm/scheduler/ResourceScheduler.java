package com.adm.scheduler;

import java.util.HashSet;
import java.util.PriorityQueue;

import com.adm.scheduler.exception.GroupTerminatedException;
import com.adm.scheduler.pool.GatewayPool;

public class ResourceScheduler implements Runnable {

    private int maxRes;

    private GatewayPool pool;

    MessageComparator comparator = new MessageComparator();

    PriorityQueue<Message> queue = new PriorityQueue<Message>(comparator);

    private HashSet<String> canceled = new HashSet<String>();

    private HashSet<String> terminated = new HashSet<String>();

    public ResourceScheduler(int max) {
	if (max <= 0)
	    throw new IllegalArgumentException();
	this.maxRes = max;
	pool = new GatewayPool(maxRes);
    }

    public void add(Message msg) {
	if (msg == null) {
	    throw new IllegalArgumentException();
	}
	if (terminated.contains(msg.getGroup() + "")) {
	    throw new GroupTerminatedException(msg);
	} else if (msg.last()) {
	    terminated.contains(msg.getGroup() + "");
	}
	queue.add(msg);
    }

    public Message getNext() {
	if (queue.size() != 0) {
	    return queue.remove();
	}
	return null;
    }

    public void cancelGroup(long grId) {
	if (!canceled.contains(grId))
	    canceled.add(grId + "");
    }

    public boolean isCancelled(long grId) {
	return canceled.contains(grId);
    }

    public int count() {
	return queue.size();
    }

    public void clear() {
	queue.clear();
    }

    @Override
    public void run() {
	while (true) {
	    sendMessage(getNext());
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
