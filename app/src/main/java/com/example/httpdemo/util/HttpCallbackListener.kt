package com.example.httpdemo.util

/**
 * 网络请求回调接口
 */
interface HttpCallbackListener {

    /**
     * 请求成功时发生
     * @param response 服务器返回的数据
     */
    fun onFinish(response:String)

    /**
     * 请求失败时发生
     * @param e 程序抛出的异常
     */
    fun onError(e:Exception)
}