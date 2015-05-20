package com.adm.scheduler;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.PriorityQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adm.scheduler.comparator.ComparatorType;
import com.adm.scheduler.comparator.GroupIdComparator;
import com.adm.scheduler.comparator.MessageComparator;
import com.adm.scheduler.comparator.MessageIdComparator;
import com.adm.scheduler.gateway.Gateway;
import com.adm.scheduler.message.Message;
import com.adm.scheduler.pool.GatewayPool;

public class ResourceScheduler implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    private int maxRes;

    private GatewayPool pool;

    private volatile HashSet<String> cancelled = new HashSet<String>();

    private volatile HashSet<String> terminated = new HashSet<String>();

    private volatile Hashtable<String, Long> groupsCount = new Hashtable<String, Long>();

    private PriorityQueue<Message> queue = null;

    private boolean shutdownSignal = false;

    public ResourceScheduler(int max) {
	this(max, ComparatorType.NORMAL);
    }

    public ResourceScheduler(int max, ComparatorType type) {
	if (max <= 0)
	    throw new IllegalArgumentException();
	this.maxRes = max;
	pool = new GatewayPool(maxRes);
	createQueue(type);
    }

    public void createQueue(ComparatorType comp) {
	switch (comp) {
	case GROUPID:
	    queue = new PriorityQueue<Message>(100, new GroupIdComparator());
	    break;
	case MESSAGEID:
	    queue = new PriorityQueue<Message>(100, new MessageIdComparator());
	    break;
	case NORMAL:
	default:
	    queue = new PriorityQueue<Message>(100, new MessageComparator(groupsCount));
	    break;
	}
    }

    public void add(Message msg) {
	synchronized (this) {
	    if (msg == null) {
		throw new IllegalArgumentException();
	    }
	    if (cancelled.contains(msg.getGroup() + "")) { // Cancellation
		LOGGER.info("Group " + msg.getGroup() + " cancelled. Message "
			+ msg.getId() + " will be ignored.");
		return;
	    }
	    if (terminated.contains(msg.getGroup() + "")) { // Termination
		LOGGER.error("Group " + msg.getGroup() + " is closed.");
	    } else if (msg.last()) {
		LOGGER.info("Last message from " + msg.getGroup()
			+ " received.");
		terminated.add(msg.getGroup() + "");
	    }
	    Long count = groupsCount.get(msg.getGroup() + "");
	    if (count != null) {
		count++;
	    } else {
		count = new Long(1);
	    }
	    groupsCount.put(msg.getGroup() + "", count);
	    queue.add(msg);
	}
    }

    public Message getNext() {
	synchronized (this) {
	    if (queue.size() != 0) {
		Message msg = queue.poll();
		Long count = groupsCount.get(msg.getGroup() + "");
		if (count != null && count > 0) {
		    count--;
		    if (count <= 0)
			groupsCount.remove(msg.getGroup() + "");
		    else
			groupsCount.put(msg.getGroup() + "", count);
		}
		return msg;
	    }
	    return null;
	}
    }

    public void cancelGroup(long grId) {
	synchronized (this) {
	    if (!cancelled.contains(grId))
		cancelled.add(grId + "");
	}
    }

    public boolean isCancelled(long grId) {
	return cancelled.contains(grId);
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
	if (msg == null)
	    return;
	synchronized (this) {
	    Gateway gate = null;
	    try {
		gate = pool.get();
		gate.send(msg);
	    } catch (Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
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
