package jm.desprez.manager

import java.io.File
import java.io.InputStream

class FileManager(val rootPath: File) {
    fun saveFile(input: InputStream, name: String) {
        File(rootPath, name).outputStream().use { output ->
            input.copyTo(output)
        }
    }
}