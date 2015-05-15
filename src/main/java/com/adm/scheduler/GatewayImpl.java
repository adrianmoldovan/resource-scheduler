package com.adm.scheduler;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GatewayImpl implements Gateway {

    final static Logger logger = LogManager.getLogger();

    /**
     * used to simulate external time consuming resource call
     */
    private Random randomGenerator = new Random();

    public void send(Message msg) {

	// simulate long operation
	try {
	    int randomInt = randomGenerator.nextInt(3000);// max 3 sec
	    logger.info("Start sending message " + msg.id() + ", group "
		    + msg.id());
	    Thread.sleep(randomInt);
	    msg.completed();
	} catch (Exception ex) {
	    // do nothing
	}
	
	logger.info("Message " + msg.id() + "from group " + msg.id() + " sent.");
    }
}
