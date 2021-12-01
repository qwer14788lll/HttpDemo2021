package com.example.httpdemo.util

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class MyHandler : DefaultHandler() {

    private var nodeName = ""
    private lateinit var id: StringBuilder
    private lateinit var name: StringBuilder
    private lateinit var version: StringBuilder
    private lateinit var data: StringBuilder

    override fun startDocument() {
        id = StringBuilder()
        name = StringBuilder()
        version = StringBuilder()
        data = StringBuilder()
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        //记录当前节点的名字
        nodeName = localName
        Log.d("TAG", "uri：$uri")
        Log.d("TAG", "localName：$localName")
        Log.d("TAG", "qName：$qName")
        Log.d("TAG", "attributes：$attributes")
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        when (nodeName) {
            "id" -> id.append(ch, start, length)
            "name" -> name.append(ch, start, length)
            "version" -> version.append(ch, start, length)
        }
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        if (localName == "app") {
            Log.d("TAG", "APP编号：${id.trim()}")
            Log.d("TAG", "APP名称：${name.trim()}")
            Log.d("TAG", "APP版本：${version.trim()}")

            data.append("APP编号：${id.trim()}")
                .append('\n')
                .append("APP名字：${name.trim()}")
                .append('\n')
                .append("APP版本：${version.trim()}")
                .append('\n').append('\n')

            id.setLength(0)
            name.setLength(0)
            version.setLength(0)
        }
    }

    override fun endDocument() {}

    fun getData() = data.toString()
}