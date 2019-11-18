package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.model.*

var delete = object : MMethod("${'$'}{space}", "delete", "删除", "${'$'}{variableName}", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} void ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap< Any,  Any>, dtoClsName:String, serviceName:String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} void ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        super.impl(fields,map, dtoClsName, serviceName)

        val idField = fields.first { it.isPk }.fieldName
        val pkColJavaName = colNameToCamel(idField);
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        return """{
${'$'}{space}${'$'}{space}$dtoClsName dto=  transToDto(${'$'}{variableName});
${'$'}{space}${'$'}{space}$serviceName.delete(dto);
${'$'}{space}}"""
    }

    override fun test(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = test_common_signature
        super.test(fields, map, dtoClsName, serviceName)

        val varName = "dto"
        val idField = fields.first { it.isPk }.fieldName
        val containsDeletePerson: Boolean = dbFieldsExists(fields,"DELETE_PERSON")
        val idFieldJavaName = colNameToCamel(idField);
        val saveSetStmt = generateFieldsRndSetExclude("${'$'}{space}${'$'}{space}", varName,fields,idField, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"),  colNameToCamel("DELETE_PERSON"))
        val setIdStmt = generateFieldRndSet("${'$'}{space}${'$'}{space}", varName,fields.first { it.isPk })
        val setDeletePerson = if(containsDeletePerson) """${'$'}{space}${'$'}{space}dto.setDeletePerson(900L);""" else ""

        return """{
${'$'}{space}${'$'}{space}$dtoClsName dto=  new $dtoClsName();
${setIdStmt}
$setDeletePerson
${'$'}{space}${'$'}{space}$serviceName.delete(dto);
${'$'}{space}}"""
    }

    override fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} void ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        super.app_remote(fields, map, dtoClsName, serviceName)

        return """{
${'$'}{space}${'$'}{space}$serviceName.delete(${'$'}{_this_variableName});
${'$'}{space}}"""
    }

    val appReqPath = """remove"""
    override fun app_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = """${'$'}{_this_indent}${'$'}{scope} ResultWrapper $appReqPath(
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "platform", required = false) Integer platform,
            @RequestParam(value = "id", required = true) Long id
    )"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$appReqPath\")")

        val containsDeletePerson: Boolean = dbFieldsExists(fields,"DELETE_PERSON")
        val setIdStmt = generatePrefixMethod("set", fields.first { it.isPk }.fieldName)
        val setDeletePerson = if(containsDeletePerson) """remove.setDeletePerson(userId);""" else ""


        return """{
${'$'}{space}${'$'}{space}final String MD_CN = "${'$'}{description}  ${'$'}{_this_nameCN}";

${'$'}{space}${'$'}{space}//assertNotNull(uid, "uid");

${'$'}{space}${'$'}{space}MDCInfoBuilder builder = MDCInfoBuilder.create(Organization.TEACHER, MD + "q")
${'$'}{space}${'$'}{space}        .put("uid", uid);

${'$'}{space}${'$'}{space}Long userId = UserIdHashHelper.hashToLong(uid);
${'$'}{space}${'$'}{space}if (userId == null) {
${'$'}{space}${'$'}{space}    return new ResultWrapper().error(CODE_ILLEGAL_ARGUMENTS);
${'$'}{space}${'$'}{space}}

${'$'}{space}${'$'}{space}ResultWrapper ret = new ResultWrapper();
${'$'}{space}${'$'}{space}try {

${'$'}{space}${'$'}{space}TblWhiteListOpenDto remove = new TblWhiteListOpenDto();
${'$'}{space}${'$'}{space}remove.$setIdStmt(id);
${'$'}{space}${'$'}{space}$setDeletePerson
${'$'}{space}${'$'}{space}tblWhiteListRemoteService.delete(remove);

${'$'}{space}${'$'}{space}ret.put("result","ok");

${'$'}{space}${'$'}{space}logger.warn(builder, MD_CN + " 成功!" +builder.build());

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

- ${map["description"]}-删除接口

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
        "result": "ok"
    },
    "successful": true
}
```

----------------------------------------------------------------------------------

        """.trimIndent()
    }

    val webReqPath = """doDelete"""
    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)
        signature = """${'$'}{_this_indent}${'$'}{scope} ResponseEntity $webReqPath(${'$'}{objType} dto)"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$webReqPath\", produces = \"text/plain;charset=utf8\")")

        return """{
        try {
            logger.info("$webReqPath {}", dto);

            //dto.setDeletePerson(getLoginUserId());

            $serviceName.delete(dto);

            logger.warn("${map["description"]} $webReqPath 成功,接收到的参数为:" + dto.toString());

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception e) {
            e.printStackTrace();

           logger.error("${map["description"]} $webReqPath 出错,接收到的参数为:" + dto.toString(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
}"""
    }
}





