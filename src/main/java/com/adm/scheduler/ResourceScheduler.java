package com.adm.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ResourceScheduler implements Runnable {

    private int maxRes;

    private List<Gateway> idle = new ArrayList<Gateway>();
    private List<Gateway> used = new ArrayList<Gateway>();

    MessageComparator comparator = new MessageComparator();

    PriorityQueue<Message> queue = new PriorityQueue<Message>(
	    Integer.MAX_VALUE, comparator);

    private List<Message> messages = new ArrayList<Message>();

    public ResourceScheduler(int max) {
	if (max <= 0)
	    throw new IllegalArgumentException();

	this.maxRes = max;
	for (int i = 0; i < maxRes; i++) {
	    idle.add(new GatewayImpl());
	}
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
	    if (idle.size() > 0) {
		Gateway gate = idle.remove(0);
		used.add(gate);
		gate.send(msg);
		used.remove(gate);
		idle.add(gate);
		notifyAll();
	    } else {
		try {
		    wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}

    }
}
