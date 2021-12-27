package com.example.httpdemo.util

import com.example.httpdemo.modle.App
import com.example.httpdemo.modle.AppItem
import com.example.httpdemo.modle.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

object JsonUtil {
    fun parseJsonWithJSONObject(data: String) =
        try {
            val jsonArray = JSONArray(data)
            val stringBuilder = StringBuilder()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.getString("id")
                val name = jsonObject.getString("name")
                val version = jsonObject.getString("version")

                stringBuilder.append("APP编号：${id}")
                    .append('\n')
                    .append("APP名字：${name}")
                    .append('\n')
                    .append("APP版本：${version}")
                    .append('\n').append('\n')
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace().toString()
        }

    fun parseJsonWithGson(data: String): String {
        val gson = Gson()
        val appList = gson.fromJson(data, App::class.java)
//        val type = object : TypeToken<List<AppItem>>(){}.type
//        val appList = gson.fromJson<List<AppItem>>(data,type)
        val stringBuilder = StringBuilder()
        for (app in appList) {
            stringBuilder.append("APP编号：${app.id}")
                .append('\n')
                .append("APP名字：${app.name}")
                .append('\n')
                .append("APP版本：${app.version}")
                .append('\n').append('\n')
        }
        return stringBuilder.toString()
    }
}