package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MMethod
import com.jiangli.commons.client.model.generateFieldsRndSetExclude
import com.jiangli.commons.client.model.test_common_signature

var get = object : MMethod("${'$'}{space}", "get", "查询单个", "${'$'}{variableName}Id", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} ${'$'}{objType} ${'$'}{_this_name}(${'$'}{idType} ${'$'}{_this_variableName})"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap< Any,  Any>, dtoClsName:String, serviceName:String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} ${'$'}{objType} ${'$'}{_this_name}(${'$'}{idType} ${'$'}{_this_variableName})"
        super.impl(fields,map, dtoClsName, serviceName)

        val idField = fields.first { it.isPk }.fieldName
        val pkColJavaName = colNameToCamel(idField);
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        return """{
${'$'}{space}${'$'}{space}return transToOpen($serviceName.get(${'$'}{variableName}Id));
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
${'$'}{space}${'$'}{space}$dtoClsName dto=  $serviceName.get(1L);
${'$'}{space}${'$'}{space}System.out.println(dto);
${'$'}{space}}"""
    }

    override fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} ${'$'}{objType} ${'$'}{_this_name}(${'$'}{idType} ${'$'}{_this_variableName})"
        super.app_remote(fields, map, dtoClsName, serviceName)

        return """{
${'$'}{space}${'$'}{space}return $serviceName.get(${'$'}{variableName}Id);
${'$'}{space}}"""
    }


    val appReqPath = """detail"""
    override fun app_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = """${'$'}{_this_indent}${'$'}{scope} ResultWrapper $appReqPath(
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "platform", required = false) Integer platform,
            @RequestParam(value = "id", required = true) Long id
    )"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$appReqPath\")")

        return """{
${'$'}{space}${'$'}{space}final String MD_CN = "${'$'}{description}  ${'$'}{_this_nameCN}";

${'$'}{space}${'$'}{space}//assertNotNull(uid, "uid");

${'$'}{space}${'$'}{space}MDCInfoBuilder builder = MDCInfoBuilder.create(Organization.TEACHER, MD + "q")
${'$'}{space}${'$'}{space}        .put("uid", uid);

${'$'}{space}${'$'}{space}//Long userId = UserIdHashHelper.hashToLong(uid);
${'$'}{space}${'$'}{space}//if (userId == null) {
${'$'}{space}${'$'}{space}//    return new ResultWrapper().error(CODE_ILLEGAL_ARGUMENTS);
${'$'}{space}${'$'}{space}//}

${'$'}{space}${'$'}{space}ResultWrapper ret = new ResultWrapper();
${'$'}{space}${'$'}{space}try {

${'$'}{space}${'$'}{space}TblWhiteListOpenDto obj = tblWhiteListRemoteService.get(id);

${'$'}{space}${'$'}{space}injectInfo(Arrays.asList(obj));

${'$'}{space}${'$'}{space}ret.putAll(describeObject(obj, FILTERED_KEY));

${'$'}{space}${'$'}{space}    //logger.warn(builder, MD_CN + " 成功!" +builder.build());
${'$'}{space}${'$'}{space}} catch (Exception e) {
${'$'}{space}${'$'}{space}    logger.error(builder, MD_CN + " 失败!" + builder.build(), e);
${'$'}{space}${'$'}{space}    return new ResultWrapper().error(CODE_INTERNAL_ERROR);
${'$'}{space}${'$'}{space}}

${'$'}{space}${'$'}{space}return ret;
${'$'}{space}}"""
    }

    override fun app_showdoc(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.app_showdoc(fields, map, dtoClsName, serviceName)

        return """
**简要描述：**

- ${map["description"]}-详情查询接口

**优先级：**
高

**请求URL：**
- `api.g2s.cn/${map["path"]}/$appReqPath`

**请求方式：**
- POST

**参数：**

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|uid |是  |String |用户uid   |
|platform |是  |Integer | 1安卓 2ios    |
|id |是  |string | 业务id|

#实际返回结果
```
{
    "status": "SUCCESS",
    "message": null,
    "currentTime": 1552992314087,
    "rt": {
        "id": 3
    },
    "successful": true
}
```

----------------------------------------------------------------------------------

        """.trimIndent()
    }
}





