package com.adm.scheduler;

import java.util.HashSet;
import java.util.PriorityQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adm.scheduler.exception.GroupTerminatedException;
import com.adm.scheduler.gateway.Gateway;
import com.adm.scheduler.message.Message;
import com.adm.scheduler.pool.GatewayPool;

public class ResourceScheduler implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private int maxRes;

    private GatewayPool pool;

    MessageComparator comparator = new MessageComparator();

    PriorityQueue<Message> queue = new PriorityQueue<Message>(comparator);

    private volatile  HashSet<String> canceled = new HashSet<String>();

    private volatile  HashSet<String> terminated = new HashSet<String>();

    private boolean shutdownSignal = false;

    public ResourceScheduler(int max) {
	if (max <= 0)
	    throw new IllegalArgumentException();
	this.maxRes = max;
	pool = new GatewayPool(maxRes);
    }

    public void add(Message msg) {
	synchronized (this) {
	    if (msg == null) {
		throw new IllegalArgumentException();
	    }
	    if (canceled.contains(msg.getGroup() + "")) { // Cancellation
		LOGGER.info("Group " + msg.getGroup() + " cancelled. Message "
			+ msg.getId() + " will be ignored.");
		return;
	    }
	    if (terminated.contains(msg.getGroup() + "")) { // Termination
		throw new GroupTerminatedException(msg);
	    } else if (msg.last()) {
		LOGGER.info("Last message from " + msg.getGroup()
			+ " received.");
		terminated.add(msg.getGroup() + "");
	    }
	    queue.add(msg);
	}
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
	LOGGER.info("Resource Scheduler : START");
	while (true) {
	    if (shutdownSignal)
		break;
	    sendMessage(getNext());
	}
	LOGGER.info("Resource Scheduler : STOP");
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

    public void shutdown() {
	LOGGER.info("Shutdown signal received.");
	shutdownSignal = true;
    }
}
