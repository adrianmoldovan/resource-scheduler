package com.adm.scheduler.gateway;

import com.adm.scheduler.message.Message;

public interface Gateway {
    public void send(Message msg);
}
