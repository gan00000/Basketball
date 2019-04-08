package com.jiec.basketball.network.base;

/**
 * User: Qiuh
 * Date: 2016-04-12
 * Time: 10:48
 * DESC: 代表网络访问出错
 */
public class AppHttpException extends RuntimeException{

    public AppHttpException(String format, Object... argus){
        super(String.format(format, argus));
    }

}
