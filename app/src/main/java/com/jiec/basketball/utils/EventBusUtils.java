package com.jiec.basketball.utils;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by Deng
 * on 2015/11/19 10:39
 * EventBus事件处理工具类
 */
public class EventBusUtils {

    public static void registerEvent(Object obj){
        if(!EventBus.getDefault().isRegistered(obj)){
            EventBus
                    .getDefault()
                    .register(obj);
        }
    }

    public static void unRegisteredEvent(Object obj){
        if(EventBus.getDefault().isRegistered(obj)){
            EventBus
                    .getDefault()
                    .unregister(obj);
        }
    }


}
