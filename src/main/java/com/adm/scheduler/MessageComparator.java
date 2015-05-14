package com.adm.scheduler;

import java.util.Comparator;
import java.util.Hashtable;

public class MessageComparator implements Comparator<Message> {
    
    Hashtable<Long, Long> priority = new Hashtable<Long, Long>();

    @Override
    public int compare(Message o1, Message o2) {
	return 0;
    }

}
