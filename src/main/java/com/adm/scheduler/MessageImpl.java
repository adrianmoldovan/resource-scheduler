package com.adm.scheduler;

public class MessageImpl implements Message {

    private long id;
    private long groupID;

    private boolean completed = false;

    public MessageImpl(long id, long group) {
	this.id = id;
	this.groupID = group;
    }

    @Override
    public void completed() {
	this.completed = true;
    }

    @Override
    public long group() {
	return groupID;
    }

    @Override
    public long id() {
	return id;
    }

    public boolean isCompleted() {
	return completed;
    }

}
