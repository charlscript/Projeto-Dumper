package com.example.dumper2

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import com.example.dumper2.PontoUsuario as PontoUsuario1

interface Endpoint {

    @GET("listPoint")
    fun getPosts() : Call<List<PontoUsuario1>>

    @GET("validatePoint/{id}")
    fun validatePoint(@Path("id") pointId: String): Call<RegisterPointResponse>


    @POST("login")
    fun login(@Body auth: Auth): Call<Login>


    @POST ("savePoint")
    fun savePoint(@Body registerPoint: RegisterPoint) : Call <RegisterPointResponse>

    @POST("saveUser")
    fun saveUser(@Body registerUser: RegisterUser) : Call<RegisterUserResponse>

    @POST("dialog")
    fun dialog(@Body dialogFlowPost: DialogFlowPost) : Call<DialogFlowResponse>

    @POST("createGrupo")
    fun createGrupo(@Body grupo: Grupo): Call<GrupoResponse>

    @POST("adminGroups")
    fun adminGroups(@Body admin: Admin): Call<List<GrupoAdminResponse>>

    @GET("grupos")
    fun grupos(): Call<List<GrupoAdminResponse>>

    @POST("joinGrupo")
    fun joinGrupo(@Body membro: Membro): Call<MembroResponse>

    @POST("findUserGroups")
    fun findUserGroups(@Body user: Admin): Call<List<GrupoAdminResponse>>

    @POST("sendMessage")
    fun sendMessage(@Body messageBody: MessageBody): Call<GrupoAdminResponse>
}