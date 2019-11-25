package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.nameToMethod
import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.model.*

var transToOpen = object : MMethod("${'$'}{space}", "transToOpen", "转换", "list", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        return NOT_INCLUDE_METHOD
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(List<${'$'}{dtoClsName}> ${'$'}{_this_variableName})"
        super.impl(fields, map, dtoClsName, serviceName)


        val implType = map["implType"].toString().toInt()

        if (implType == MethodImplUtil.IMPL_SERVICE) {
            val fieldsOfSelectCmd = fields.filter {
                it.commands.any { it is SelectCommand }
            }

            if (fieldsOfSelectCmd.isNotEmpty()) {
                var getMap = generateStringBodyOfField(fieldsOfSelectCmd, "\r\n") {

                    val methodName = generateMethodNameOfSelect(it)
                    val displayName = getDisplayNameOfField(it)
                    val orgName = it.fieldName
                    val tp = it.fieldCls

                    """
${'$'}{space}${'$'}{space}     Map<$tp, String> $methodName = $methodName();
            """
                }

                var setStr = generateStringBodyOfField(fieldsOfSelectCmd, "\r\n") {

                    val methodName = generateMethodNameOfSelect(it)
                    val displayName = getDisplayNameOfField(it)
                    val orgName = it.fieldName
                    val tp = it.fieldCls

                    """
${'$'}{space}${'$'}{space}     one.set${nameToMethod(displayName)}($methodName.get(one.get${nameToMethod(orgName)}()));
            """
                }

                return """{
${'$'}{space}${'$'}{space}if (list!=null && list.size() > 0) {
${getMap}
${'$'}{space}${'$'}{space}    for (${'$'}{dtoClsName} one : list) {
${setStr}
${'$'}{space}${'$'}{space}    }
${'$'}{space}${'$'}{space}}

${'$'}{space}${'$'}{space}List<${'$'}{objType}> ret = super.transToOpen(list);

${'$'}{space}${'$'}{space}return ret;
${'$'}{space}${'$'}{space}}"""
            }
        }


        return NOT_INCLUDE_METHOD
    }
}





