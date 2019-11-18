package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MMethod
import com.jiangli.commons.client.model.generateFieldsRndSetExclude
import com.jiangli.commons.client.model.test_common_signature

var listByDto = object : MMethod("${'$'}{space}", "listByDto", "条件分页查询", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer offset,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap< Any,  Any>, dtoClsName:String, serviceName:String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer offset,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        super.impl(fields,map, dtoClsName, serviceName)

        val idField = fields.first { it.isPk }.fieldName
        val pkColJavaName = colNameToCamel(idField);
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        return """{
${'$'}{space}${'$'}{space}return transToOpen($serviceName.listByDto(transToDto(dto),offset,pageSize));
${'$'}{space}}"""
    }

    override fun test(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = test_common_signature
        super.test(fields, map, dtoClsName, serviceName)

        val varName = "dto"
        val idField = fields.first { it.isPk }.fieldName
        val idFieldJavaName = colNameToCamel(idField);
        val saveSetStmt = generateFieldsRndSetExclude("${'$'}{space}${'$'}{space}", varName,fields,idField, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"),  colNameToCamel("DELETE_PERSON"))

        return """{
${'$'}{space}${'$'}{space}$dtoClsName dto = new   $dtoClsName();
$saveSetStmt
${'$'}{space}${'$'}{space}List<$dtoClsName> list=  $serviceName.listByDto(dto,0,10);
${'$'}{space}${'$'}{space}System.out.println(list);
${'$'}{space}}"""
    }

    override fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer offset,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        super.app_remote(fields, map, dtoClsName, serviceName)

        return """{
${'$'}{space}${'$'}{space}return $serviceName.listByDto(dto,offset,pageSize);
${'$'}{space}}"""
    }
}





