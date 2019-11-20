package com.example.dumper2

import android.inputmethodservice.AbstractInputMethodService
import com.google.gson.annotations.SerializedName
import retrofit2.http.FormUrlEncoded
import java.net.PasswordAuthentication

data class PontoUsuario (

    @SerializedName("_id")
    var _id : String,
    @SerializedName("nome")
    var nome : String,
    @SerializedName("latitude")
    var latitude : String,
    @SerializedName("longitude")
    var longitude : String,
    @SerializedName("descricao")
    var descricao : String,
    @SerializedName("userEmail")
    var userEmail : String,
    @SerializedName("validado")
    var validado : Boolean,
    @SerializedName("createdAt")
    var createdAt : String,
    @SerializedName("updatedAt")
    var updatedAt : String,
    @SerializedName("__v")
    var v : Int

)

data class Login (
    @SerializedName("isMatch")
    var isMatch : String,
    @SerializedName("nome")
    var nome : String
)

data class Auth (
    @SerializedName("email")
    var email : String,
    @SerializedName("password")
    var password : String
)

data class RegisterUser (
    @SerializedName("nome")
    var nome : String,
    @SerializedName("email")
    var email : String,
    @SerializedName("password")
    var password: String
)

data class RegisterUserResponse (
    @SerializedName("email")
    var email : String,
    @SerializedName("erro")
    var erro: String
)

data class RegisterPoint (
    @SerializedName("nome")
    var nome : String,
    @SerializedName("latitude")
    var latitude : String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("descricao")
    var descricao: String,
    @SerializedName("userEmail")
    var userEmail: String,
    @SerializedName("validado")
    var validado: Boolean
)

data class RegisterPointResponse (
    @SerializedName("nome")
    var nome : String,
    @SerializedName("latitude")
    var latitude : String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("decricao")
    var descricao: String,
    @SerializedName("userEmail")
    var userEmail: String,
    @SerializedName("validado")
    var validado: Boolean,
    @SerializedName("erro")
    var Erro : String
)

data class DialogFlowPost (
    @SerializedName("text")
    var text: String,
    @SerializedName("sessionId")
    var sessionId: String?,
    @SerializedName("email")
    var email: String?
)

data class DialogFlowResponse (
    @SerializedName("query")
    var query: String,
    @SerializedName("response")
    var response: String,
    @SerializedName("intent")
    var intent: String
)