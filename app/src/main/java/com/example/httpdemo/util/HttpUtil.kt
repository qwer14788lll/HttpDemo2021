package com.example.httpdemo.util

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object HttpUtil {

    /**
     * 原生Get请求
     * @param urlString 要请求的地址
     * @param listener 网络请求回调接口实现类（匿名类）
     */
    fun sendHttpRequest(urlString: String, listener: HttpCallbackListener) {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                //根据网址创建URL对象
                val url = URL(urlString)
                //打开网址连接
                connection = url.openConnection() as HttpURLConnection
                //设置连接超时
                connection.connectTimeout = 8000
                //设置读取超时
                connection.readTimeout = 8000
                //获取网络请求(服务器返回)的输入流
                val input = connection.inputStream
                //对输入流进行读取
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                listener.onFinish(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                listener.onError(e)
            } finally {
                //将连接关闭
                connection?.disconnect()
            }
        }
    }

    /**
     * 原生Post请求
     * @param urlString 要请求的地址
     * @param parameter post参数
     * @param listener 网络请求回调接口实现类（匿名类）
     */
    fun sendHttpRequest(urlString: String, parameter: String, listener: HttpCallbackListener) {
        thread {
            var connection: HttpURLConnection? = null
            try {
                //将读到的数据拼接为字符串
                val response = StringBuilder()
                //根据网址创建URL对象
                val url = URL(urlString)
                //打开网址连接
                connection = url.openConnection() as HttpURLConnection
                //设置连接超时
                connection.connectTimeout = 8000
                //设置读取超时
                connection.readTimeout = 8000
                connection.requestMethod = "POST"
                //准备提交API所需的数据
                val output = DataOutputStream(connection.outputStream)
                //写入参数
                output.writeBytes(parameter)
                //获取网络请求(服务器返回)的输入流
                val input = connection.inputStream
                //对输入流进行读取
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                listener.onFinish(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                listener.onError(e)
            } finally {
                //将连接关闭
                connection?.disconnect()
            }
        }
    }

    /**
     * OkHttp框架的Get请求
     * @param urlString 要请求的地址
     * @param callback 网络请求回调接口实现类（匿名类）
     */
    fun sendOkHttpRequest(urlString: String, callback: okhttp3.Callback) {
        //创建OkHttp客户端对象
        val client = OkHttpClient()
        //创建Request对象，用来发送HTTP请求
        val request = Request.Builder()
            .url(urlString)
            .build()
        //发出网络请求，并回调数据
        client.newCall(request).enqueue(callback)
    }

    /**
     * OkHttp框架的Post请求
     * @param urlString 要请求的地址
     * @param requestBody post参数对象
     * @param callback 网络请求回调接口实现类（匿名类）
     */
    fun sendOkHttpRequest(urlString: String, requestBody: FormBody, callback: okhttp3.Callback) {
        //创建OkHttp客户端对象
        val client = OkHttpClient()
        //创建Request对象，用来发送HTTP请求
        val request = Request.Builder()
            .url(urlString)
            .post(requestBody)
            .build()
        //发出网络请求，并接收回传的数据
        client.newCall(request).enqueue(callback)
    }
}