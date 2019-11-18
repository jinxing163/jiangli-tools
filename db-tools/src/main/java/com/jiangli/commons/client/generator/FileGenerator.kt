package com.jiangli.commons.client.generator

import com.jiangli.commons.PathUtil
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 *
 *
 * @author Jiangli
 * @date 2019/11/6 11:07
 */
fun generateFile(body:String, vararg path:String): File {
    val absPath = concatPath(*path)

    PathUtil.ensureFilePath(absPath)

    val fileOutputStream = FileOutputStream(absPath)
    IOUtils.write(body, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()

    return File(absPath)
}

fun getProtoFileBody(fileName: String):String {
    return IOUtils.toString(FileInputStream(PathUtil.getSRCFileRelative(com.jiangli.commons.client.proto.PageRecords::class.java, fileName)))
}

fun main(args: Array<String>) {
//    println(getProtoFileBody("web_controller_import.txt"))
    println(getProtoFileBody("app_controller\\import.txt"))
}
