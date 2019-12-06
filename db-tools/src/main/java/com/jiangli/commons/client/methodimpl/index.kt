package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MMethod
import com.jiangli.commons.client.model.NOT_INCLUDE_METHOD

var index = object : MMethod("${'$'}{space}", "index", "首页", "", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    val webReqPath = """index"""

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        return NOT_INCLUDE_METHOD
    }

    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)
        signature = """${'$'}{_this_indent}${'$'}{scope} ModelAndView index()"""
        map.put("annotation", "\r\n${'$'}{space}@RequestMapping(path = \"/$webReqPath\")")

        return """{
        ModelAndView modelAndView = new ModelAndView("${map["lastPkgName"]}/${map["path"]}/$webReqPath");

        Boolean flag = super.controlJumpByPermission(PermissionEnum.OPERATE_MODULE, modelAndView, super.getLoginUserId());
        //if (!flag) {//没权限
        //    return modelAndView;
        //}

        return modelAndView;
    }"""
    }

}





