package com.adm.scheduler.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.adm.scheduler.gateway.Gateway;
import com.adm.scheduler.message.MessageImpl;
import com.adm.scheduler.pool.GatewayPool;

public class GatewayTest {

    private GatewayPool pool;

    @Before
    public void setUp() {
	pool = new GatewayPool(5);
    }

    @Test
    public void testSize() {
	 TestCase.assertEquals(5, pool.size());
    }

    @Test
    public void testCreateNew() {
	Gateway gate = pool.createNew();
	TestCase.assertEquals(true, gate != null);
    }

    @Test
    public void testGetRelease() {
	Gateway gate = pool.get();
	TestCase.assertEquals(true, gate != null);
	TestCase.assertEquals(4, pool.size());
	pool.release(gate);
	TestCase.assertEquals(5, pool.size());
    }

    @Test
    public void testSend() {
	// test pool get
	Gateway gate = pool.get();
	TestCase.assertEquals(true, gate != null);
	TestCase.assertEquals(4, pool.size());

	// test message send
	MessageImpl msg = new MessageImpl(1, 1, false);
	TestCase.assertEquals(false, msg.isCompleted());
	gate.send(msg);
	TestCase.assertEquals(true, msg.isCompleted());

	// test pool release
	pool.release(gate);
	TestCase.assertEquals(5, pool.size());
    }

    @Test
    public void testShutdown() {
	TestCase.assertEquals(5, pool.size());
	pool.shutdown();
	TestCase.assertEquals(0, pool.size());
    }
}
