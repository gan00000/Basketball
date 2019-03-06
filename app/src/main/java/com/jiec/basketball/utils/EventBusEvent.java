package com.jiec.basketball.utils;

/**
 * Created by Deng
 * on 2015/11/19 10:52
 * EventBus事件类
 */
public class EventBusEvent {

    public final int status;

    public final Class clazz;

    public final Object data;

    public EventBusEvent(int status, Class clazz,Object data) {
        this.status = status;
        this.clazz = clazz;
        this.data = data;
    }

}
