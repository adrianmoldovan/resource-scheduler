package com.adm.scheduler.gateway;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adm.scheduler.message.Message;

public class GatewayImpl implements Gateway {

    private final static Logger LOGGER = LogManager.getLogger();

    /**
     * used to simulate external time consuming resource call
     */
    private Random randomGenerator = new Random();

    public void send(Message msg) {

	// simulate long operation
	try {
	    int randomInt = randomGenerator.nextInt(3000);// max 3 sec
	    LOGGER.info("Start sending message " + msg.getId() + ", group "
		    + msg.getGroup());
	    Thread.sleep(randomInt);
	    msg.completed();
	} catch (Exception ex) {
	    // do nothing
	}
	
	LOGGER.info("Message " + msg.getId() + " from group " + msg.getGroup() + " sent.");
    }
}
