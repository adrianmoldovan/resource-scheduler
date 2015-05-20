package com.adm.scheduler.comparator;

import java.util.Comparator;

import com.adm.scheduler.message.Message;

public class GroupIdComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
	int result = (int) (o1.getGroup() - o2.getGroup());
	if (result == 0)// if the same group use the index
	    result = (int) (o1.getIndex() - o2.getIndex());

	return result;
    }

}
