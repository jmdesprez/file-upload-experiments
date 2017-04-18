package jm.desprez.feign

import feign.*
import feign.form.FormEncoder
import jm.desprez.model.Hello
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import java.io.File


interface DWService {
    @RequestLine("GET /")
    fun hello(): Hello

    @RequestLine("POST /")
    @Headers("Content-Type: multipart/form-data")
    fun uploadFile(@Param("name") name: String, @Param("file") file: File): Hello
}

fun main(args: Array<String>) {

    if (args.size != 1) {
        println("Please profile an absolute filename to upload")
    }

    val dwService = Feign.builder()
            .encoder(FormEncoder())
            .decoder(JacksonDecoder())
            .target(DWService::class.java, "http://localhost:8080")

    println(dwService.uploadFile("myTest", File(args[0])))
}