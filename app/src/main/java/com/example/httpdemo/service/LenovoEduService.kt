package com.example.httpdemo.service

import com.example.httpdemo.modle.SCHospital
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LenovoEduService {
    @GET("SCHospital/getAll")
    fun getSCHospitalAll(@Query("userId") userId: String): Call<SCHospital>

    @GET("SCHospital/getInfo")
    fun getSCHospitalInfo(
        @Query("userId") userId: String,
        @Query("id") id: Int
    ): Call<SCHospital>
}