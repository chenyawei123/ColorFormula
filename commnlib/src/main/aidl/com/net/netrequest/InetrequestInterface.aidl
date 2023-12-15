// InetrequestInterface.aidl
package com.net.netrequest;

// Declare any non-default types here with import statements

interface InetrequestInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean sendMessage(inout byte[] data);//in 表示数据只能由客户端流向服务端； out 表示数据只能由服务端流向客户端；inout 表示数据可在服务端和客户端双向流通。
}