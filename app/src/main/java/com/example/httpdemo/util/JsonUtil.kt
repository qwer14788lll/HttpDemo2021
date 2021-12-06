package com.example.httpdemo.util

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
}