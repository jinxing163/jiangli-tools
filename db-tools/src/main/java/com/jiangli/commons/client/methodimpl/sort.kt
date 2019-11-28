package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.nameToMethod
import com.jiangli.commons.client.model.*

var sort = object : MMethod("${'$'}{space}", "sort", "排序", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}
""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        signature = "${'$'}{_this_indent}${'$'}{scope} PageRecords<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer pageIndex,${'$'}{paramAnno_pageSize:}Integer pageSize)"
        return NOT_INCLUDE_METHOD
    }


    val webReqPath = """doSort"""
    override fun aries_controller(fields: MutableList<JavaField>, map: MutableMap<Any, Any>, dtoClsName: String, serviceName: String): String {
        super.aries_controller(fields, map, dtoClsName, serviceName)

        if (!fields.hasCommand(SortCommand::class)) {
            return NOT_INCLUDE_METHOD
        }
        val idField = fields.first { it.isPk}
        val sortField = fields.first { it.commands.any { it is SortCommand } }

        signature = """${'$'}{_this_indent}${'$'}{scope} ResponseEntity $webReqPath(
            ${'$'}{objType} dto,
            @RequestParam(value = "sortIds[]",required = false) List<Long> sortIds,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    )"""

        super.app_controller(fields, map, dtoClsName, serviceName)
        map.put("annotation", "\r\n${'$'}{space}@PostMapping(path = \"/$webReqPath\")")

        return """{
        try {
            if (sortIds!=null && sortIds.size() > 0) {
                ${sortField.fieldCls} start = 0${when(sortField.fieldCls){
                "Long"->"L"
                "Integer"->""
                else -> ""
            }};

                for (Long sortId : sortIds) {
                    ${'$'}{objType} one = new ${'$'}{objType}();
                    one.set${nameToMethod(idField.fieldName)}(sortId);
                    one.set${nameToMethod(sortField.fieldName)}(++start);
                    $serviceName.update(one);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("${map["description"]} $webReqPath 出错,接收到的参数为:" + dto.toString(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
}"""
    }
}





