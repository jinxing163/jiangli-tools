package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MMethod
import com.jiangli.commons.client.model.NOT_REGISTER
import com.jiangli.commons.client.model.SelectCommand

open class typeMapProto(val javaField: JavaField, override var name: String?) : MMethod("${'$'}{space}", name, "字典表", "", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${javaField.remarkName} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${name}:{}}
""", NOT_REGISTER) {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} Map<${javaField.fieldCls},String> ${name}()"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.impl(fields, map, dtoClsName, serviceName)

        signature = "${'$'}{_this_indent}${'$'}{scope} Map<${javaField.fieldCls},String> ${name}()"

        val implType = map["implType"].toString().toInt()

        if (implType == MethodImplUtil.IMPL_SERVICE) {
            val firstSelect = javaField.commands.first { it is SelectCommand } as SelectCommand

            var putStr = ""
            firstSelect.cmd_list.forEach {
                putStr += "${'$'}{space}${'$'}{space}ret.put(${it.type}, \"${it.name}\");\r\n"
            }


            return """{
${'$'}{space}${'$'}{space}Map<${javaField.fieldCls}, String> ret = new LinkedHashMap<>();

$putStr

${'$'}{space}${'$'}{space}return ret;
${'$'}{space}}""".trimIndent()
        } else if (implType == MethodImplUtil.IMPL_OPEN_SERVICE) {
            return """{
${'$'}{space}${'$'}{space}return $serviceName.$name();
${'$'}{space}}""".trimIndent()
        } else {
            return "{}"
        }
    }


    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)
        signature = """${'$'}{_this_indent}${'$'}{scope} ResponseEntity ${name}()"""

        map.put("annotation", "\r\n${'$'}{space}@RequestMapping(path = \"/${name}\")")

        return """{
        return ResponseEntity.status(HttpStatus.OK).body($serviceName.${name}());
    }"""
    }
}








