package com.jiangli.commons.client.model

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.methodcore.MethodImplUtil


val NOT_INCLUDE_METHOD = "_x_not_Include_Method_xx_x_"

enum class MethodImplType {
    inf, test, impl,app_remote,app_controller,app_showdoc,aries_controller,aries_jsp,aries_crud_js,aries_selector_js
}
val test_common_signature = "${'$'}{_this_indent}public void test_${'$'}{_this_name}()"
open class MMethod {
    var indent: String? = null //前置空格
    var name: String? = null //方法名
    var nameCN: String? = null //方法中文名
    var variableName: String? = null //变量名
    var signature: String? = null //方法签名
    var body: String? = null //方法体

    constructor() {

    }

    constructor(indent: String?, name: String?, nameCN: String?, variableName: String?, body: String?) {
        this.indent = indent
        this.name = name
        this.nameCN = nameCN
        this.variableName = variableName
        this.body = body

        MethodImplUtil.add(this)
    }

    fun gets(map: MutableMap<Any, Any>, f: String): String {
        try {
            return map[f]?.toString()?:"${'$'}$f${'$'}"
        } catch (e: Exception) {
            return "${'$'}$f${'$'}"
        }
    }

    fun applyImpl(tp: MethodImplType, fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
//        val mmap =  mutableMapOf<Any,Any>()
//        mmap.putAll(map)
        return when (tp) {
            MethodImplType.inf -> inf(fields, map)
            MethodImplType.test -> test(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.impl -> impl(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.app_remote -> app_remote(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.app_controller -> app_controller(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.app_showdoc -> app_showdoc(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.aries_controller -> aries_controller(fields, map, gets(map, "dtoClsName"), gets(map, "serviceName"))
            MethodImplType.aries_jsp -> aries_jsp(fields, map)
            MethodImplType.aries_crud_js -> aries_crud_js(fields, map)
            MethodImplType.aries_selector_js -> aries_selector_js(fields, map)
            else -> NOT_INCLUDE_METHOD
        }
    }

    fun setBaseSignature(): Unit {
//        signature = "${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
    }

    //通用排除的字段
    open fun commonExcludeFields(fields: MutableList<JavaField>): Array<String> {
        val idField = fields.first { it.isPk }.fieldName
        val excludeField = arrayOf(idField, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"),  colNameToCamel("DELETE_PERSON"),  colNameToCamel("CREATE_PERSON"))
        return excludeField
    }

    //   底层  接口
    open fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        setBaseSignature()
        return ";"
    }

    //   底层  测试
    open fun test(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
        map.put("annotation", "\r\n${'$'}{space}@Test")
        map.put("description", "测试")
        return NOT_INCLUDE_METHOD
    }

    //    底层 实现
    open fun impl(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    remote 实现
    open fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
//        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    controller 实现
    open fun app_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
//        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    showdoc 实现
    open fun app_showdoc(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
        signature=""
//        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }


    //    aries_controller 实现
    open fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        setBaseSignature()
        //        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    aries_jsp 实现
    open fun aries_jsp(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        setBaseSignature()
        //        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    aries_crud_js 实现
    open fun aries_crud_js(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        setBaseSignature()
        //        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }

    //    aries_selector_js 实现
    open fun aries_selector_js(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        setBaseSignature()
        //        map.put("annotation", "\r\n${'$'}{space}@Override")
        return NOT_INCLUDE_METHOD
    }
}


