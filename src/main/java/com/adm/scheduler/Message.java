package com.adm.scheduler;

public interface Message {

    public void completed();
    
    public long getGroup();
    
    public long getId();
    
    public boolean last();
    
}
