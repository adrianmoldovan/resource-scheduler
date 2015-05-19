package com.adm.scheduler.comparator;

import java.util.Comparator;

import com.adm.scheduler.message.Message;

public class GroupIdComparator  implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
	return Long.compare(o1.getGroup(), o2.getGroup());
    }

}
