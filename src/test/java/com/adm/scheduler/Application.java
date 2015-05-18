package com.adm.scheduler;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adm.scheduler.message.Message;
import com.adm.scheduler.message.MessageImpl;

public class Application {
    private final static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
	LOGGER.info("Starting Resource Scheduler demo application.");

	ResourceScheduler scheduler = new ResourceScheduler(4);

	// startup messages;
	Message msg1 = new MessageImpl(1, 2, false);
	scheduler.add(msg1);
	Message msg2 = new MessageImpl(2, 3, false);
	scheduler.add(msg2);
	Message msg3 = new MessageImpl(3, 1, false);
	scheduler.add(msg3);
	Thread thread = new Thread(scheduler);
	thread.start();
	scheduler.cancelGroup(1);
	
	Message msg4 = new MessageImpl(4, 3, true); // last message from group 3
	scheduler.add(msg4);
	
	
	Random randomGenerator = new Random();
	for (int i = 5; i <= 20; i++) {
	    int randomId = randomGenerator.nextInt(4); // max 4 groups
	    Message msg = new MessageImpl(i, randomId, false);
	    scheduler.add(msg);
	}
	

	try {
	    Thread.sleep(15 * 1000);
	} catch (Exception ex) {
	    //do nothing
	}
	scheduler.shutdown(); // send application termination after 15 sec	

    }

}
