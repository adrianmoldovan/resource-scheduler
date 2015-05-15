package com.adm.scheduler;

import java.util.Comparator;
import java.util.Hashtable;

public class MessageComparator implements Comparator<Message> {

    Hashtable<Long, Long> priority = new Hashtable<Long, Long>();

    @Override
    public int compare(Message o1, Message o2) {
	if (o1.getGroup() == o2.getGroup())
	    return 0;
	if (o1.getGroup() < o2.getGroup())
	    return -1;
	if (o1.getGroup() > o2.getGroup())
	    return 1;
	return 0;
    }

}
