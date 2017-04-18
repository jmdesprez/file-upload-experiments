package jm.desprez.retrofit

import jm.desprez.model.Hello
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*
import java.io.File

interface DWService {

    @Multipart
    @POST("/")
    fun uploadFile(@Part("name") name: RequestBody, @Part("file") file: RequestBody): Call<Hello>
}

fun main(args: Array<String>) {

    if(args.size !=1) {
        println("Please profile an absolute filename to upload")
    }

    val retrofit = Retrofit.Builder().baseUrl("http://localhost:8080")
            .addConverterFactory(JacksonConverterFactory.create()).build()!!
    val dwService = retrofit.create(DWService::class.java)

    val toUpload = File(args[0])

    val filename = RequestBody.create(MediaType.parse("text/plain"), toUpload.name)
    val body = RequestBody.create(MediaType.parse("application/octet-stream"), toUpload)

    val call = dwService.uploadFile(filename, body)
    println(call.execute().body())
}