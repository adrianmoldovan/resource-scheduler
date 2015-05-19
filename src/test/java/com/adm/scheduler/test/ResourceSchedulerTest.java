package com.adm.scheduler.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.adm.scheduler.ResourceScheduler;
import com.adm.scheduler.comparator.ComparatorType;
import com.adm.scheduler.message.Message;
import com.adm.scheduler.message.MessageImpl;

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
    public void testPriorityMessages1() {
	Message msg1 = new MessageImpl(1, 1, false);
	Message msg2 = new MessageImpl(2, 2, false);
	Message msg3 = new MessageImpl(3, 1, false);
	Message msg4 = new MessageImpl(4, 0, false);
	Message msg5 = new MessageImpl(5, 1, false);
	scheduler.clear();
	scheduler.add(msg1);
	scheduler.add(msg2);
	scheduler.add(msg3);
	scheduler.add(msg4);
	scheduler.add(msg5);

	TestCase.assertEquals(5, scheduler.count());
	Message m = scheduler.getNext();
	TestCase.assertEquals(msg1, m);
	m = scheduler.getNext();
	TestCase.assertEquals(msg3, m);
	m = scheduler.getNext();
	TestCase.assertEquals(msg5, m);
	m = scheduler.getNext();
	TestCase.assertEquals(msg2, m);
	m = scheduler.getNext();
	TestCase.assertEquals(msg4, m);
	TestCase.assertEquals(0, scheduler.count());
    }
    
    @Test
    public void testPriorityMessages2() {
	 ResourceScheduler scheduler2 = new ResourceScheduler(4, ComparatorType.MESSAGEID);
	Message msg1 = new MessageImpl(1, 1, false);
	Message msg2 = new MessageImpl(2, 2, false);
	Message msg3 = new MessageImpl(3, 1, false);
	Message msg4 = new MessageImpl(4, 0, false);
	Message msg5 = new MessageImpl(4, 1, false);
	scheduler2.clear();
	scheduler2.add(msg2);
	scheduler2.add(msg1);
	scheduler2.add(msg4);
	scheduler2.add(msg3);
	scheduler2.add(msg5);

	TestCase.assertEquals(5, scheduler2.count());
	Message m = scheduler2.getNext();
	TestCase.assertEquals(msg1, m);
	m = scheduler2.getNext();
	TestCase.assertEquals(msg2, m);
	m = scheduler2.getNext();
	TestCase.assertEquals(msg3, m);
	m = scheduler2.getNext();
	TestCase.assertEquals(msg4, m);
	m = scheduler2.getNext();
	TestCase.assertEquals(msg5, m);
	TestCase.assertEquals(0, scheduler2.count());
    }
    
    @Test
    public void testPriorityMessages3() {
	 ResourceScheduler scheduler3 = new ResourceScheduler(4, ComparatorType.GROUPID);
	Message msg1 = new MessageImpl(1, 1, false);
	Message msg2 = new MessageImpl(2, 2, false);
	Message msg3 = new MessageImpl(3, 1, false);
	Message msg4 = new MessageImpl(4, 0, false);
	Message msg5 = new MessageImpl(5, 3, false);
	Message msg6 = new MessageImpl(6, 1, false);
	scheduler3.clear();
	scheduler3.add(msg1);
	scheduler3.add(msg2);
	scheduler3.add(msg3);
	scheduler3.add(msg4);
	scheduler3.add(msg5);
	scheduler3.add(msg6);

	TestCase.assertEquals(6, scheduler3.count());
	Message m = scheduler3.getNext();
	TestCase.assertEquals(msg4, m);
	m = scheduler3.getNext();
	TestCase.assertEquals(msg1, m);
	m = scheduler3.getNext();
	TestCase.assertEquals(msg3, m);
	m = scheduler3.getNext();
	TestCase.assertEquals(msg6, m);
	m = scheduler3.getNext();
	TestCase.assertEquals(msg2, m);
	m = scheduler3.getNext();
	TestCase.assertEquals(msg5, m);
	TestCase.assertEquals(0, scheduler3.count());
    }
}
