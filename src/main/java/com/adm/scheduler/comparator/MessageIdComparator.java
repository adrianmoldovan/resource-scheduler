package com.adm.scheduler.comparator;

import java.util.Comparator;

import com.adm.scheduler.message.Message;

public class MessageIdComparator  implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
	if (o1.getId() > o2.getId())
	    return 1;
	if (o1.getId() < o2.getId())
	    return -1;
	return 0;
    }

}
