package com.adm.scheduler.comparator;

import java.util.Comparator;

import com.adm.scheduler.message.Message;

public class MessageIdComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
	int result = (int) (o1.getId() - o2.getId());
	if (result == 0)// if the same group use the index
	    result = (int) (o1.getIndex() - o2.getIndex());
	
	return result;
    }

}
