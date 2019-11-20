package com.example.dumper2

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import com.example.dumper2.PontoUsuario as PontoUsuario1

interface Endpoint {

    @GET("listPoint")
    fun getPosts() : Call<List<PontoUsuario1>>


    @POST("login")
    fun login(@Body auth: Auth): Call<Login>


    @POST ("savePoint")
    fun savePoint(@Body registerPoint: RegisterPoint) : Call <RegisterPointResponse>

    @POST("saveUser")
    fun saveUser(@Body registerUser: RegisterUser) : Call<RegisterUserResponse>

    @POST("dialog")
    fun dialog(@Body dialogFlowPost: DialogFlowPost) : Call<DialogFlowResponse>
}