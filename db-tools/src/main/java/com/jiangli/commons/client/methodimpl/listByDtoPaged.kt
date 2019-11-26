package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MMethod
import com.jiangli.commons.client.model.generateFieldsRndSetExclude
import com.jiangli.commons.client.model.test_common_signature

var listByDtoPaged = object : MMethod("${'$'}{space}", "listByDtoPaged", "条件分页查询（包装）,pageIndex从0开始", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} PageRecords<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer pageIndex,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        return super.inf(fields, map)
    }

    override fun impl(fields: MutableList<JavaField>, map: MutableMap< Any,  Any>, dtoClsName:String, serviceName:String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} PageRecords<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer pageIndex,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        super.impl(fields,map, dtoClsName, serviceName)

        val idField = fields.first { it.isPk }.fieldName
        val pkColJavaName = colNameToCamel(idField);
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        return """{
${'$'}{space}${'$'}{space}long count = $serviceName.count(transToDto(dto));
${'$'}{space}${'$'}{space}PageRecords<${'$'}{objType}> ret = new PageRecords<>();
${'$'}{space}${'$'}{space}ret.setTotalRecords(count);

${'$'}{space}${'$'}{space}pageSize = ensurePageSize(pageSize, 1, 100);
${'$'}{space}${'$'}{space}pageIndex = ensurePageIndex(pageIndex,pageSize,count);

${'$'}{space}${'$'}{space}Integer realRetrieveSize = pageSize + 1;

${'$'}{space}${'$'}{space}if (pageIndex < 0) {
${'$'}{space}${'$'}{space} ret.setRecords(new ArrayList<>());
${'$'}{space}${'$'}{space}} else {
${'$'}{space}${'$'}{space} List<${'$'}{objType}> totalRecords = transToOpen($serviceName.listByDto(transToDto(dto),pageIndex*pageSize, realRetrieveSize));
${'$'}{space}${'$'}{space}if (realRetrieveSize.equals(totalRecords.size())) {
${'$'}{space}${'$'}{space}      ret.setNextId(Long.valueOf(pageIndex+1));
${'$'}{space}${'$'}{space}      totalRecords = new ArrayList<>(totalRecords.subList(0,pageSize));
${'$'}{space}${'$'}{space}}
${'$'}{space}${'$'}{space} ret.setRecords(totalRecords);
${'$'}{space}${'$'}{space}}
${'$'}{space}${'$'}{space}return ret;
${'$'}{space}}"""
    }

    override fun test(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = test_common_signature
        super.test(fields, map, dtoClsName, serviceName)

        val varName = "dto"
        val idField = fields.first { it.isPk }.fieldName
        val idFieldJavaName = colNameToCamel(idField);
        val saveSetStmt = generateFieldsRndSetExclude("${'$'}{space}${'$'}{space}", varName,fields,idField, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"),  colNameToCamel("DELETE_PERSON"))

        return  """{
${'$'}{space}${'$'}{space}$dtoClsName dto = new   $dtoClsName();
$saveSetStmt
${'$'}{space}${'$'}{space}PageRecords<$dtoClsName> list=  $serviceName.listByDtoPaged(dto,0,10);
${'$'}{space}${'$'}{space}System.out.println(list);
${'$'}{space}}"""
    }

    override fun app_remote(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} PageRecords<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer pageIndex,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        super.app_remote(fields, map, dtoClsName, serviceName)

        return """{
${'$'}{space}${'$'}{space}return $serviceName.listByDtoPaged(dto,pageIndex,pageSize);
${'$'}{space}}"""
    }

    val appReqPath = """list"""
    override fun app_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        signature = """${'$'}{_this_indent}${'$'}{scope} ResultWrapper $appReqPath(
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "platform", required = false) Integer platform,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum
    )"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$appReqPath\")")

        return """{
${'$'}{space}${'$'}{space}final String MD_CN = "${'$'}{description}  ${'$'}{_this_nameCN}";

${'$'}{space}${'$'}{space}//assertNotNull(uid, "uid");

${'$'}{space}${'$'}{space}MDCInfoBuilder builder = MDCInfoBuilder.create(Organization.TEACHER, MD + "Q")
${'$'}{space}${'$'}{space}        .put("uid", uid);

${'$'}{space}${'$'}{space}//Long userId = UserIdHashHelper.hashToLong(uid);
${'$'}{space}${'$'}{space}//if (userId == null) {
${'$'}{space}${'$'}{space}//    return new ResultWrapper().error(CODE_ILLEGAL_ARGUMENTS);
${'$'}{space}${'$'}{space}//}

${'$'}{space}${'$'}{space}ResultWrapper ret = new ResultWrapper();
${'$'}{space}${'$'}{space}try {

${'$'}{space}${'$'}{space}    ${'$'}{objType} query = new ${'$'}{objType}();
${'$'}{space}${'$'}{space}    PageRecords<${'$'}{objType}> obj = $serviceName.listByDtoPaged(query, pageNum, pageSize);

${'$'}{space}${'$'}{space}    injectInfo(obj.getRecords());

${'$'}{space}${'$'}{space}    ret.putAll(describeObject(obj, FILTERED_KEY));

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

- ${map["description"]}-列表查询接口

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
|pageSize |是  |string | 每页个数|
|pageNum |否  |string | 当前页码，第1页不用传（或传0），传的必须是nextId返回的结果|

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


    val webReqPath = """doQueryList"""
    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)
        signature = """${'$'}{_this_indent}${'$'}{scope} ResponseEntity $webReqPath(
            ${'$'}{objType} dto,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    )"""
        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$webReqPath\")")

        return """{
        try {
            PageRecords<${'$'}{objType}> rs = $serviceName.listByDtoPaged(dto, pageIndex, pageSize);

            injectInfo(rs.getRecords());

            //logger.warn("${map["description"]} $webReqPath 成功,接收到的参数为:" + dto.toString());

            return ResponseEntity.status(HttpStatus.OK).body(rs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("${map["description"]} $webReqPath 出错,接收到的参数为:" + dto.toString(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
}"""
    }
}





