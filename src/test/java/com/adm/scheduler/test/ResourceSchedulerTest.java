package com.adm.scheduler.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.adm.scheduler.Message;
import com.adm.scheduler.MessageImpl;
import com.adm.scheduler.ResourceScheduler;

public class ResourceSchedulerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ResourceScheduler scheduler;

    @Before
    public void setUp() {
	scheduler = new ResourceScheduler(4);
    }

    @Test
    public void testNok1() {
	exception.expect(IllegalArgumentException.class);
	new ResourceScheduler(0);
    }

    @Test
    public void testNok2() {
	exception.expect(IllegalArgumentException.class);
	new ResourceScheduler(-1);
    }

    @Test
    public void testOk() {
	new ResourceScheduler(2);
    }

    @Test
    public void testAddMessage() {
	exception.expect(IllegalArgumentException.class);
	scheduler.clear();
	scheduler.add(null);
    }

    @Test
    public void testOneMessage() {

	Message msg1 = new MessageImpl(1, 1, false);
	scheduler.clear();
	scheduler.add(msg1);

	TestCase.assertEquals(1, scheduler.count());
	TestCase.assertEquals(msg1, scheduler.getNext());
    }

    @Test
    public void testTwoMessages() {
	Message msg1 = new MessageImpl(1, 1, false);
	Message msg2 = new MessageImpl(1, 2, false);
	scheduler.clear();
	scheduler.add(msg1);
	scheduler.add(msg2);

	TestCase.assertEquals(2, scheduler.count());
	TestCase.assertEquals(msg1, scheduler.getNext());
	TestCase.assertEquals(msg2, scheduler.getNext());
	TestCase.assertEquals(0, scheduler.count());
    }
    
    @Test
    public void testPriorityMessages() {
	Message msg1 = new MessageImpl(1, 1, false);
	Message msg2 = new MessageImpl(2, 2, false);
	Message msg3 = new MessageImpl(3, 1, false);
	scheduler.clear();
	scheduler.add(msg1);
	scheduler.add(msg2);
	scheduler.add(msg3);

	TestCase.assertEquals(3, scheduler.count());
	TestCase.assertEquals(msg1, scheduler.getNext());
	TestCase.assertEquals(msg3, scheduler.getNext());
	TestCase.assertEquals(msg2, scheduler.getNext());
	TestCase.assertEquals(0, scheduler.count());
    }
}
