package com.adm.scheduler.pool;

import com.adm.scheduler.Gateway;
import com.adm.scheduler.GatewayImpl;

public class GatewayPool extends ObjectPool<Gateway> {

    public GatewayPool(int size) {
	super(size);
    }

    @Override
    public Gateway createNew() {
	// simulate creating expensive resourse
	return new GatewayImpl();
    }

}
