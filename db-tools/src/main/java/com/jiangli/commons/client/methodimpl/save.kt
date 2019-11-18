package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.model.*

var save = object : MMethod("${'$'}{space}", "save", "新增", "${'$'}{variableName}", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap< Any,  Any>, dtoClsName:String, serviceName:String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        super.impl(fields,map, dtoClsName, serviceName)

        val idField = fields.first { it.isPk }.fieldName
        val pkColJavaName = colNameToCamel(idField);
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        return """{
${'$'}{space}${'$'}{space}$dtoClsName dto=  transToDto(${'$'}{variableName});
${'$'}{space}${'$'}{space}$serviceName.save(dto);
${'$'}{space}${'$'}{space}
${'$'}{space}${'$'}{space}Long saveId = dto.${pkColJavaNameGetter}();
${'$'}{space}${'$'}{space}${'$'}{variableName}.${pkColJavaNameSetter}(saveId);
${'$'}{space}${'$'}{space}return saveId;
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
${'$'}{space}${'$'}{space}$dtoClsName dto=  new $dtoClsName();
$saveSetStmt
${'$'}{space}${'$'}{space}Long ${idFieldJavaName} = $serviceName.save(dto);
${'$'}{space}${'$'}{space}System.out.println(${idFieldJavaName});
${'$'}{space}}"""
    }

    override fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})"
        super.app_remote(fields, map, dtoClsName, serviceName)

        return """{
${'$'}{space}${'$'}{space}return $serviceName.save(${'$'}{_this_variableName});
${'$'}{space}}"""
    }

    val appReqPath = """save"""
    override fun app_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        val excludeField = commonExcludeFields(fields)
        val reqParams = generateFieldsRequestParamExclude("${'$'}{space}${'$'}{space}", fields,*excludeField)
        val reqSetDto = generateFieldsSetExclude("${'$'}{space}${'$'}{space}","save", fields,*excludeField)

        signature = """${'$'}{_this_indent}${'$'}{scope} ResultWrapper $appReqPath(
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "platform", required = false) Integer platform,
$reqParams
    )"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$appReqPath\")")

        val containsCreatePerson: Boolean = dbFieldsExists(fields,"CREATE_PERSON")
        val setIdStmt = generatePrefixMethod("set", fields.first { it.isPk }.fieldName)
        val setCreatePerson = if(containsCreatePerson) """save.setCreatePerson(userId);""" else ""


        return """{
${'$'}{space}${'$'}{space}final String MD_CN = "${'$'}{description}  ${'$'}{_this_nameCN}";

${'$'}{space}${'$'}{space}//assertNotNull(uid, "uid");

${'$'}{space}${'$'}{space}MDCInfoBuilder builder = MDCInfoBuilder.create(Organization.TEACHER, MD + "C")
${'$'}{space}${'$'}{space}        .put("uid", uid);

${'$'}{space}${'$'}{space}Long userId = UserIdHashHelper.hashToLong(uid);
${'$'}{space}${'$'}{space}if (userId == null) {
${'$'}{space}${'$'}{space}    return new ResultWrapper().error(CODE_ILLEGAL_ARGUMENTS);
${'$'}{space}${'$'}{space}}

${'$'}{space}${'$'}{space}ResultWrapper ret = new ResultWrapper();
${'$'}{space}${'$'}{space}try {

${'$'}{space}${'$'}{space}TblWhiteListOpenDto save = new TblWhiteListOpenDto();
$reqSetDto
${'$'}{space}${'$'}{space}
${'$'}{space}${'$'}{space}$setCreatePerson
${'$'}{space}${'$'}{space}Long saveId = tblWhiteListRemoteService.save(save);

${'$'}{space}${'$'}{space}ret.put("id",saveId);

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

        val excludeField = commonExcludeFields(fields)
        val reqParams = generateFieldsShowdocExclude("", fields,*excludeField)

        return """
**简要描述：**

- ${map["description"]}-保存接口

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
$reqParams

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

    val webReqPath = """doSave"""
    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)
        signature = """${'$'}{_this_indent}${'$'}{scope} ResponseEntity $webReqPath(${'$'}{objType} dto)"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$webReqPath\", produces = \"text/plain;charset=utf8\")")

        return """{
        try {
            logger.info("$webReqPath {}", dto);

            //dto.setCreatePerson(getLoginUserId());

            $serviceName.save(dto);

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





