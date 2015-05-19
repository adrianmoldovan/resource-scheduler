package com.adm.scheduler.comparator;

import java.util.Comparator;
import java.util.Hashtable;

import com.adm.scheduler.message.Message;

public class MessageComparator implements Comparator<Message> {

    private Hashtable<String, Long> priority = new Hashtable<String, Long>();
    public MessageComparator(Hashtable<String, Long> groups) {
	priority = groups;
    }

    /*
     * Compares its two arguments for order. Returns a negative integer, zero, 
     * or a positive integer as the first argument is less than, equal to, 
     * or greater than the second.
     */
    @Override
    public int compare(Message o1, Message o2) {
	if (priority.get(o1.getGroup()) != null)
	    return 1;
	if (priority.get(o2.getGroup()) != null)
	    return -1;
	return 1;

    }

}
