package com.adm.scheduler.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.adm.scheduler.Gateway;
import com.adm.scheduler.MessageImpl;
import com.adm.scheduler.pool.GatewayPool;

public class GatewayTest extends TestCase {

    GatewayPool pool;

    @Override
    protected void setUp() throws Exception {
	pool = new GatewayPool(5);
    }

    @Test
    public void testSize() {
	assertEquals(5, pool.size());
    }

    @Test
    public void testCreateNew() {
	Gateway gate = pool.createNew();
	assertEquals(true, gate != null);
    }

    @Test
    public void testGetRelease() {
	Gateway gate = pool.get();
	assertEquals(true, gate != null);
	assertEquals(4, pool.size());
	pool.release(gate);
	assertEquals(5, pool.size());
    }

    @Test
    public void testSend() {
	//test pool get
	Gateway gate = pool.get();
	assertEquals(true, gate != null);
	assertEquals(4, pool.size());
	
	//test message send
	MessageImpl msg = new MessageImpl(1, 1);
	assertEquals(false, msg.isCompleted());
	gate.send(msg);
	assertEquals(true, msg.isCompleted());
	
	//test pool release
	pool.release(gate);
	assertEquals(5, pool.size());
    }
    
    @Test
    public void testShutdown(){
	assertEquals(5, pool.size());
	pool.shutdown();
	assertEquals(0, pool.size());
    }
}
