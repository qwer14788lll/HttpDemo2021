package com.example.httpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.httpdemo.databinding.ActivityCovid19Binding
import com.example.httpdemo.util.JsonUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread

class COVID19Activity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCovid19Binding
    private val ip = "https://lab.isaaclin.cn/nCoV/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCovid19Binding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.textView.setOnClickListener {
            thread {
                try {
                    //创建OkHttp客户端对象
                    val client = OkHttpClient()
                    //创建Request对象，用来发送HTTP请求
                    val request = Request.Builder()
                        .url("${ip}api/overall")
                        .build()
                    //发出网络请求，并接收回传的数据
                    val response = client.newCall(request).execute()
                    //数据解析出来
                    val data = response.body?.string()
                    runOnUiThread {
                        mBinding.textView.text = paresJson(data.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun paresJson(data: String):String {
        val stringBuilder = StringBuilder()
        val overall = JSONObject(data)
        val results = overall.getJSONArray("results").getJSONObject(0)
        stringBuilder.append("病毒名称:").append(results.getString("note1")).append('\n')
            .append("传染源:").append(results.getString("note2")).append('\n')
            .append("传播途径:").append(results.getString("note3")).append('\n')
            .append("现存确诊人数:").append(results.getInt("currentConfirmedCount")).append('\n')
            .append("累计确诊人数:").append(results.getInt("confirmedCount")).append('\n')
            .append("疑似感染人数:").append(results.getInt("suspectedCount")).append('\n')
            .append("治愈人数:").append(results.getInt("curedCount")).append('\n')
            .append("死亡人数:").append(results.getInt("deadCount")).append('\n')
            .append("重症病例人数:").append(results.getInt("seriousCount")).append('\n')
        return stringBuilder.toString()
    }
}