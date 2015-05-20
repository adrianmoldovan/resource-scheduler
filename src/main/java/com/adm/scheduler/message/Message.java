package com.adm.scheduler.message;

public interface Message {

    public void completed();
    
    public int getGroup();
    
    public int getId();
    
    public boolean last();
    
    public int getIndex();
    
}
