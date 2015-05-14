package com.adm.scheduler;

public interface Message {
    public void completed();
    
    public long group();
    
    public long id();
}
