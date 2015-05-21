package com.adm.scheduler;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adm.scheduler.comparator.ComparatorType;
import com.adm.scheduler.message.Message;
import com.adm.scheduler.message.MessageImpl;

public class Application {
    private final static Logger LOGGER = LogManager.getLogger();

    private static void displayError() {
	System.out
		.println("ERROR: Wrong parameters. Parameters example: 5 NORMAL");
	System.out.println("First parameter is the number of resources available.");
	System.out.println("Second parameter is the procesing priority: ");
	System.out.println("NORMAL - normal message processing.");
	System.out.println("MESSAGEID - message id ascending priority");
	System.out.println("GROUPID - group id ascending priority");
	System.exit(1); // exit with error
    }

    public static void main(String[] args) {
	LOGGER.info("Starting Resource Scheduler demo application.");

	if (args.length != 2) {
	    displayError();
	}

	String p0 = args[0];
	String p1 = args[1];
	
	int size = 0;
	try {
	    size = Integer.parseInt(p0);
	} catch (Exception e) {
	    displayError();
	}
	
	ComparatorType type = null;
	try {
	    type = ComparatorType.valueOf(p1);
	} catch (Exception e) {
	    displayError();
	}
	if (type == null) { // double check - not necessary
	    displayError();
	}

	ResourceScheduler scheduler = new ResourceScheduler(size, type);

	try {
	    // startup messages;
	    int index = 1;
	    Message msg1 = new MessageImpl(1, 2, false, index++);
	    scheduler.add(msg1);
	    Message msg2 = new MessageImpl(2, 3, false, index++);
	    scheduler.add(msg2);
	    Message msg3 = new MessageImpl(3, 1, false, index++);
	    scheduler.add(msg3);
	    Thread thread = new Thread(scheduler);
	    thread.start();
	    scheduler.cancelGroup(1);

	    Message msg4 = new MessageImpl(4, 3, true, index++); // last message
								 // from group 3
	    scheduler.add(msg4);

	    Random randomGenerator = new Random();
	    for (int i = 5; i <= 20; i++) {
		int randomId = randomGenerator.nextInt(4); // max 4 groups
		Message msg = new MessageImpl(i, randomId, false, index++);
		scheduler.add(msg);
	    }
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	}

	try {
	    Thread.sleep(15 * 1000);
	    LOGGER.info("Closing application after 15 sec.");
	} catch (Exception ex) {
	    LOGGER.error(ex.getMessage(), ex);
	}
	scheduler.shutdown(); // send application termination after 15 sec

    }
}
