package com.adm.scheduler.exception;

import com.adm.scheduler.message.Message;


public class GroupTerminatedException extends RuntimeException {

    private static final long serialVersionUID = 1102321746276004899L;

    public GroupTerminatedException(Message msg) {
	super("Group " + msg.getGroup() + " is closed." );
    }
}
