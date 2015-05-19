package com.adm.scheduler.message;

public class MessageImpl implements Message {

    private long id;
    private long groupID;

    private boolean completed = false;
    private boolean last = false;

    public MessageImpl(long id, long group, boolean isLast) {
	this.id = id;
	this.groupID = group;
	this.last = isLast;
    }

    @Override
    public void completed() {
	this.completed = true;
    }

    @Override
    public long getGroup() {
	return groupID;
    }

    @Override
    public long getId() {
	return id;
    }

    public boolean isCompleted() {
	return completed;
    }

    @Override
    public boolean last() {
	return last;
    }

    @Override
    public String toString() {
	return id + " " + groupID;
    }

}
