package com.example.httpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.httpdemo.databinding.ActivityMainBinding
import com.example.httpdemo.util.HttpCallbackListener
import com.example.httpdemo.util.HttpUtil
import com.example.httpdemo.util.JsonUtil
import com.example.httpdemo.util.XmlUtil
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val ip = "10.72.0.3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnGet.setOnClickListener {
//            thread {
//                val data = HttpUtil.getHttpService()
//                    .get("http://$ip:8080/login?username=admin&password=123456")
//                runOnUiThread { mBinding.textView.text = data }
//            }

            val urlString = "http://$ip:8080/login?username=admin&password=123456"

            HttpUtil.sendHttpRequest(urlString,
                object : HttpCallbackListener {
                    override fun onFinish(response: String) {
                        runOnUiThread { mBinding.textView.text = response }
                    }

                    override fun onError(e: Exception) {
                        runOnUiThread { mBinding.textView.text = "网络请求回调" }
                    }
                })
        }

        mBinding.btnPost.setOnClickListener {
//            thread {
//                val data = HttpUtil.getHttpService().post(
//                    "http://$ip:8080/post",
//                    "input=${URLEncoder.encode("测试测试post", "UTF-8")}"
//                )
//                runOnUiThread {
//                    mBinding.textView.text = data
//                }
//            }

            val urlString = "http://$ip:8080/post"
            val param = "input=${URLEncoder.encode("测试测试post", "UTF-8")}"

            HttpUtil.sendHttpRequest(urlString, param, object : HttpCallbackListener {
                override fun onFinish(response: String) {
                    runOnUiThread { mBinding.textView.text = response }
                }

                override fun onError(e: Exception) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnGetOkhttp.setOnClickListener {
//            thread {
//                try {
//                    //创建OkHttp客户端对象
//                    val client = OkHttpClient()
//                    //创建Request对象，用来发送HTTP请求
//                    val request = Request.Builder()
//                        .url("http://$ip:8080/getKotlin?name=张三")
//                        .build()
//                    //发出网络请求，并接收回传的数据
//                    val response = client.newCall(request).execute()
//                    //数据解析出来
//                    val data = response.body?.string()
//                    runOnUiThread {
//                        mBinding.textView.text = data
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }

            val urlString = "http://$ip:8080/getKotlin?name=张三"
            HttpUtil.sendOkHttpRequest(urlString,object :Callback{
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { mBinding.textView.text = response.body?.string() }
                }

                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnPostOkhttp.setOnClickListener { thread {
                try {
                    //创建OkHttp客户端对象
                    val client = OkHttpClient()
                    //构建参数对象
                    val requestBody = FormBody.Builder()
                        .add("id","admin")
                        .add("name","张三")
                        .build()
                    //创建Request对象，用来发送HTTP请求
                    val request = Request.Builder()
                        .url("http://$ip:8080/post/student")
                        .post(requestBody)
                        .build()
                    //发出网络请求，并接收回传的数据
                    val response = client.newCall(request).execute()
                    //数据解析出来
                    val data = response.body?.string()
                    runOnUiThread {
                        mBinding.textView.text = data
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } }

        mBinding.btnXmlPull.setOnClickListener { thread {
                try {
                    //创建OkHttp客户端对象
                    val client = OkHttpClient()
                    //创建Request对象，用来发送HTTP请求
                    val request = Request.Builder()
                        .url("http://$ip:8080/xml/get_data.xml")
                        .build()
                    //发出网络请求，并接收回传的数据
                    val response = client.newCall(request).execute()
                    //数据解析出来
                    val data = response.body?.string()
                    runOnUiThread {
                        mBinding.textView.text = XmlUtil.parseXMLWithPull(data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } }

        mBinding.btnXmlSax.setOnClickListener { thread {
                try {
                    //创建OkHttp客户端对象
                    val client = OkHttpClient()
                    //创建Request对象，用来发送HTTP请求
                    val request = Request.Builder()
                        .url("http://$ip:8080/xml/get_data.xml")
                        .build()
                    //发出网络请求，并接收回传的数据
                    val response = client.newCall(request).execute()
                    //数据解析出来
                    val data = response.body?.string()
                    runOnUiThread {
                        mBinding.textView.text = XmlUtil.parseXMLWithSAX(data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } }

        mBinding.btnJsonJsonObject.setOnClickListener { thread {
            try {
                //创建OkHttp客户端对象
                val client = OkHttpClient()
                //创建Request对象，用来发送HTTP请求
                val request = Request.Builder()
                    .url("http://$ip:8080/json/get_data.json")
                    .build()
                //发出网络请求，并接收回传的数据
                val response = client.newCall(request).execute()
                //数据解析出来
                val data = response.body?.string()
                runOnUiThread {
                    mBinding.textView.text = JsonUtil.parseJsonWithJSONObject(data.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } }

        mBinding.btnJsonGson.setOnClickListener {
            thread {
                try {
                    //创建OkHttp客户端对象
                    val client = OkHttpClient()
                    //创建Request对象，用来发送HTTP请求
                    val request = Request.Builder()
                        .url("http://$ip:8080/json/get_data.json")
                        .build()
                    //发出网络请求，并接收回传的数据
                    val response = client.newCall(request).execute()
                    //数据解析出来
                    val data = response.body?.string()
                    runOnUiThread {
                        mBinding.textView.text = JsonUtil.parseJsonWithGson(data.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}