package com.adm.scheduler.pool;

import com.adm.scheduler.gateway.Gateway;
import com.adm.scheduler.gateway.GatewayImpl;

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
