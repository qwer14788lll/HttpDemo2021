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
    private val ip = "10.72.0.14"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnGet.setOnClickListener {
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

        mBinding.btnPostOkhttp.setOnClickListener {
            val urlString = "http://$ip:8080/post/student"

            //构建参数对象
            val requestBody = FormBody.Builder()
                .add("id","admin")
                .add("name","张三")
                .build()

            HttpUtil.sendOkHttpRequest(urlString,requestBody,object :Callback{
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { mBinding.textView.text = response.body?.string() }
                }

                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnXmlPull.setOnClickListener {
            val urlString = "http://$ip:8080/xml/get_data.xml"

            HttpUtil.sendOkHttpRequest(urlString, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        mBinding.textView.text = XmlUtil.parseXMLWithPull(response.body?.string())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnXmlSax.setOnClickListener {
            val urlString = "http://$ip:8080/xml/get_data.xml"

            HttpUtil.sendOkHttpRequest(urlString, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        mBinding.textView.text = XmlUtil.parseXMLWithSAX(response.body?.string())
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnJsonJsonObject.setOnClickListener {
            val urlString = "http://$ip:8080/json/get_data.json"
            HttpUtil.sendOkHttpRequest(urlString,object :Callback{
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        mBinding.textView.text = JsonUtil.parseJsonWithJSONObject(response.body?.string())
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnJsonGson.setOnClickListener {
            val urlString = "http://$ip:8080/json/get_data.json"
            HttpUtil.sendOkHttpRequest(urlString,object :Callback{
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        mBinding.textView.text = JsonUtil.parseJsonWithGson(response.body?.string())
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }
    }
}