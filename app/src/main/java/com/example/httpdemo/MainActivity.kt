package com.example.httpdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.httpdemo.databinding.ActivityMainBinding
import com.example.httpdemo.modle.SCHospital
import com.example.httpdemo.service.LenovoEduService
import com.example.httpdemo.util.HttpCallbackListener
import com.example.httpdemo.util.HttpUtil
import com.example.httpdemo.util.JsonUtil
import com.example.httpdemo.util.XmlUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLEncoder

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
                    runOnUiThread { mBinding.textView.text = response.body()?.string() }
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
                    runOnUiThread { mBinding.textView.text = response.body()?.string() }
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
                        mBinding.textView.text = XmlUtil.parseXMLWithPull(response.body()?.string())
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
                        mBinding.textView.text = XmlUtil.parseXMLWithSAX(response.body()?.string())
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
                        mBinding.textView.text = JsonUtil.parseJsonWithJSONObject(response.body()?.string())
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
                        mBinding.textView.text = JsonUtil.parseJsonWithGson(response.body()?.string())
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { mBinding.textView.text = "网络请求失败" }
                }
            })
        }

        mBinding.btnGetRetrofit.setOnClickListener {
            //OkHttp封装的是底层通信的实现，网络请求库
            //Retrofit封装上层接口，网络通信库
            //一款APP所发起的网络请求绝大多数是指向同一个服务器域名的
            //Retrofit
            //1，配置根路径
            //2、配置解析类和请求接口

            val retrofit = Retrofit.Builder()
                .baseUrl("http://smartcity.lenovoedu.com/interface/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //将服务器的子地址给到retrofit对象
            val lenovoEduService = retrofit.create(LenovoEduService::class.java)
//            lenovoEduService.getSCHospitalAll("ODcx")
//                .enqueue(object : retrofit2.Callback<SCHospital> {
//                    override fun onResponse(
//                        call: retrofit2.Call<SCHospital>,
//                        response: retrofit2.Response<SCHospital>
//                    ) {
//                        val stringBuilder = StringBuilder()
//                        val list = response.body()?.data
//                        if (list != null) {
//                            for (data in list)
//                                stringBuilder.append("医院名称：").append(data.name).append('\n')
//                        }
//                        mBinding.textView.text = stringBuilder.toString()
//                    }
//
//                    override fun onFailure(call: retrofit2.Call<SCHospital>, t: Throwable) {
//                        mBinding.textView.text = "网络请求失败"
//                    }
//                })

            lenovoEduService.getSCHospitalInfo("ODcx",1)
                .enqueue(object : retrofit2.Callback<SCHospital> {
                    override fun onResponse(
                        call: retrofit2.Call<SCHospital>,
                        response: retrofit2.Response<SCHospital>
                    ) {
                        val data = response.body()?.data?.get(0)
                        mBinding.textView.text = data?.name.toString()
                    }

                    override fun onFailure(call: retrofit2.Call<SCHospital>, t: Throwable) {
                        mBinding.textView.text = "网络请求失败"
                    }
                })
        }
    }
}