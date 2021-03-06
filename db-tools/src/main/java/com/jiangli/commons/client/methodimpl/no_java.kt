package com.jiangli.commons.client.methodimpl

import com.jiangli.commons.client.generator.getProtoFileBody
import com.jiangli.commons.client.generator.resolveBodyBySpring
import com.jiangli.commons.client.model.*

var no_java = object : MMethod("${'$'}{space}", "no_java", "no_java", "", """${'$'}{_this_signature}${'$'}{impl.${'$'}{_this_name}:{}}""") {

    override fun inf(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        return NOT_INCLUDE_METHOD
    }

    override fun aries_jsp(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        super.aries_jsp(fields, map)

        var finalmap = cloneMap(map)

        //        搜索栏
        val pkFieldColumn = fields.getPkFieldColumn()
        val commonInputFields = fieldExcludeByColumnName(fields, pkFieldColumn, "IS_DELETED", "CREATE_TIME", "UPDATE_TIME", "CREATE_PERSON", "DELETE_PERSON")
        val commonShowFields = fieldExcludeByColumnName(fields, "IS_DELETED")

        var s = geSearchBody(commonInputFields)
        finalmap.put("searchBody",s)

//        列表头
        s = generateStringBodyOfField(commonShowFields){
            """
                                <th class="">
                                    <div>${it.remarkName}</div>
                                </th>
            """.trimIndent()
        }
        finalmap.put("tableHead",s)

        //        排序按钮
        s = if(fields.hasCommand(SortCommand::class)){
            """<span id="sortBtn" class="layui-btn layui-btn-primary  fr">排序</span>"""
        } else ""
        finalmap.put("sortBtn",s)

//        弹框
        s = generateStringBodyOfField(commonInputFields){
            """
                                <li class="layui-form-item ">
                                    <span class="layui-form-label">${if(shouldInputFieldValue(it)) "<em class=\"start-em\">*</em>" else ""} ${it.remarkName}：</span>
                                    <div class="layui-input-block">
                                        ${geInputRow(it)}
                                        <div class="error-tips"></div>
                                    </div>
                                </li>
            """.trimIndent()
        }
        finalmap.put("dialogRow",s)


        finalmap.put("firstInputFieldName",commonInputFields.first { it.commands.isEmpty() }.fieldName)

        val body = getProtoFileBody("web_controller\\jsp_list.txt")
        return  resolveBodyBySpring(body,finalmap)
    }



    private fun cloneMap(map: MutableMap<Any, Any>): MutableMap<Any, Any> {
        var finalmap = mutableMapOf<Any, Any>()
        finalmap.putAll(map)
        return finalmap
    }


    override fun aries_crud_js(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
         super.aries_crud_js(fields, map)

        var finalmap = cloneMap(map)

//      下拉框选项配置js
        var s = generateStringBodyOfField(
                fields.filter {
                    it.commands.any { it is SelectCommand }
                },"\r\n"){

            val methodName = generateMethodNameOfSelect(it)

            """
        //读取 ${it.remarkName} 下拉框选项
        injectSelectOptions(basePath + relaModulePath +"/$methodName",function () {
            return [getObjOfForm(queryForm,"${it.fieldName}"),getObjOfForm(createform,"${it.fieldName}")];
        },function () {
            return [true,false]
        })
            """
        }
        finalmap.put("selectFieldConfigJs",s)

        //        搜索栏
        val pkFieldName = fields.getPkFieldName()
        val pkFieldColumn = fields.getPkFieldColumn()
        val commonInputFields = fieldExcludeByColumnName(fields, pkFieldColumn, "IS_DELETED", "CREATE_TIME", "UPDATE_TIME", "CREATE_PERSON", "DELETE_PERSON")
        val commonShowFields = fieldExcludeByColumnName(fields, "IS_DELETED")


        //        验证配置
         s = generateStringBodyOfField(fieldExcludeByColumnName(fields.filter { shouldInputFieldValue(it) },pkFieldColumn),",") {
            """
                                {
                                type: "text",
                                name: "${it.fieldName}",
                                whenEmpty: function (${'$'}obj) {
                                    commonError(${'$'}obj, "请输入 ${it.remarkName}");
                                },
                                clear: commonClear
                            }
            """.trimIndent()
        }
        finalmap.put("validateConfig",s)


        //        列表数据
        s = geTableData(commonShowFields)
        finalmap.put("tableData",s)

        //        排序显示
        s = generateStringBodyOfField(commonShowFields.filter {it.fieldCls=="String" },"+ \" / \" +") {
            var valueField = getRealFieldName(it)

            """one.$valueField""".trimIndent()
        }
        if (s.isBlank()) {
            s = "one.$pkFieldName"
        }
        finalmap.put("sortDisplay","${'$'}{$s}")


        val body = getProtoFileBody("web_controller\\js_crud.txt")
        return  resolveBodyBySpring(body,finalmap)
    }



    override fun aries_selector_js(fields: MutableList<JavaField>, map: MutableMap<Any, Any>): String {
        super.aries_selector_js(fields, map)

        var finalmap = cloneMap(map)

        //        搜索栏
        val pkFieldColumn = fields.getPkFieldColumn()
        val commonInputFields = fieldExcludeByColumnName(fields, pkFieldColumn, "IS_DELETED", "CREATE_TIME", "UPDATE_TIME", "CREATE_PERSON", "DELETE_PERSON")
        val commonShowFields = fieldExcludeByColumnName(fields, "IS_DELETED")


        //        搜索头
        var s = geSearchBody(commonInputFields)
        finalmap.put("searchBody",s)


        //        列表头
        s = generateStringBodyOfField(commonShowFields){
            """
                                <th class="">
                                    <div>${it.remarkName}</div>
                                </th>
            """.trimIndent()
        }
        finalmap.put("tableHead",s)


        //      下拉框选项配置js
        s = generateStringBodyOfField(
                fields.filter {
                    it.commands.any { it is SelectCommand }
                },"\r\n"){

            val methodName = generateMethodNameOfSelect(it)

            """
        //读取 ${it.remarkName} 下拉框选项
        injectSelectOptions(basePath + relaModulePath +"/$methodName",function () {
            return [getObjOfForm(queryFormId,"${it.fieldName}")];
        },function () {
            return [true]
        })
            """
        }
        finalmap.put("selectFieldConfigJs",s)

        //        列表数据
        s = geTableData(commonShowFields)
        finalmap.put("tableData",s)


        val body = getProtoFileBody("web_controller\\js_selector.txt")
        return  resolveBodyBySpring(body,finalmap)
    }

    private fun getRealFieldName(it: JavaField): String {
        var valueField = it.fieldName

        //            select型
        if (it.commands.any { it is SelectCommand }) {
            valueField = getDisplayNameOfField(it)
        }
        return valueField
    }

    private fun geInputRow(it: JavaField): String {
        var inputStr = """<input class="layui-input"  name="${it.fieldName}" placeholder="请输入 ${it.remarkName}" type="text">"""
        if (it.commands.any { it is SelectCommand }) {
            inputStr = """
                            <select name="${it.fieldName}">
                            </select>
                    """.trimIndent()
        }
        return inputStr
    }

    private fun geSearchBody(commonInputFields: List<JavaField>): String {
        var s = generateStringBodyOfField(commonInputFields) {
            var inputStr = geInputRow(it)

            """
                        <li class="fl layui-form-item">
                            <span class="layui-form-label">${it.remarkName}：</span>
                            <div class="layui-input-block">
                                $inputStr
                            </div>
                        </li>
                """.trimIndent()
        }
        return s
    }

    private fun geTableData(commonShowFields: List<JavaField>): String {
        return generateStringBodyOfField(commonShowFields) {
            var valueField = getRealFieldName(it)

            """
                             <td>
                                <div class="minWidth80">${'$'}{one.${valueField} || ""}</div>
                            </td>
                """.trimIndent()
        }
    }
}





