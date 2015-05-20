package com.adm.scheduler.message;

public class MessageImpl implements Message {

    private int id;
    private int groupID;
    private int index;

    private boolean completed = false;
    private boolean last = false;

    public MessageImpl(int id, int group, boolean isLast, int index) {
	this.id = id;
	this.groupID = group;
	this.last = isLast;
	this.index = index;
    }

    @Override
    public void completed() {
	this.completed = true;
    }

    @Override
    public int getGroup() {
	return groupID;
    }

    @Override
    public int getId() {
	return id;
    }
    
    @Override
    public int getIndex() {
	return index;
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
