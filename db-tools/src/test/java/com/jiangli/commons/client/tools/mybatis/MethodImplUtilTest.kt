package com.jiangli.commons.client.tools.mybatis

import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.model.MethodImplType
import com.jiangli.commons.client.model.queryFieldList
import org.junit.jupiter.api.Test

/**
 * @author Jiangli
 * @date 2018/7/9 16:57
 */
internal class MethodImplUtilTest {

    @Test
    fun resolve() {
        println(MethodImplUtil.resolve(mapOf(
//                "SPACE" to "xx"
        )))
    }

    @Test
    fun resolveImpl() {
        println(MethodImplUtil.resolveImpl(mapOf(
//                "SPACE" to "xx"
        ),"XXDto","XXService"))
    }

    @Test
    fun resolveImplEx() {
        val DB_URL = "jdbc:mysql://192.168.222.8:3306?user=root&password=ablejava"
        val DATABASE = "db_aries_manage"
        val TBL_NAME = "TBL_WHITE_LIST"

        println(MethodImplUtil.list)
        val fields = queryFieldList(DB_URL, DATABASE, TBL_NAME)

        println(MethodImplUtil.resolveEx(mapOf(
                                "SPACE" to "xx"
        ), MethodImplType.inf, fields))

        println(MethodImplUtil.resolveEx(mapOf(
                                "SPACE" to "xx"
        ), MethodImplType.test,fields))

        println(MethodImplUtil.resolveEx(mapOf(
                                "SPACE" to "xx"
        ), MethodImplType.impl,fields))
    }
}