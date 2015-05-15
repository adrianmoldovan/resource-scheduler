package com.adm.scheduler.pool;

public interface ObjectFactory<T> {
    
    
    /**
     * Returns a new instance of an object type T.
     * 
     * @return T an new instance of the object type T
     */
    public abstract T createNew();
}
