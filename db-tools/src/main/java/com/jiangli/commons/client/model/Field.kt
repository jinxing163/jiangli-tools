package com.jiangli.commons.client.model

import com.jiangli.commons.DateUtil
import com.jiangli.commons.Rnd
import com.jiangli.commons.client.generator.*
import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.methodimpl.typeMapProto
import java.sql.DriverManager

/**
 * out/dao/mapper,xml,model,test
 * out/service/service,dto,test
 * out/openapi/openservice,opendto,test
 *
 *
 * @author Jiangli
 * @date 2018/3/1 9:32
 */
fun shouldInputFieldValue(it: JavaField) = !it.nullable && it.defaultValue == null

fun List<JavaField>.getPkField() : JavaField{
   return  this.first { it.isPk==true}
}
fun List<JavaField>.getPkFieldColumn() : String {
   return  getPkField().columnName
}
fun List<JavaField>.getPkFieldName() : String {
   return  getPkField().fieldName
}

data class JavaField(val columnName: String,val columType: String) {
    var initVal: Any?=null
    var isPk: Boolean=false
    lateinit var remark: String
    lateinit var fieldName: String
    lateinit var fieldCls: String
    var nullable: Boolean=false
    var defaultValue: String?=null
    var fieldClsImport: String?=null //import
    var commands: MutableList<Command> = mutableListOf()
    lateinit var remarkName: String  //用来前端展示的名字 一般为中文
    var generateStr: Boolean=false //是否生成前端展示用的字段 一般时间戳转中文，类型转中文会用到

    override fun toString(): String {
        return "JavaField(columnName='$columnName', columType='$columType', initVal=$initVal, isPk=$isPk, remark='$remark', fieldName='$fieldName', fieldCls='$fieldCls', nullable=$nullable, defaultValue='$defaultValue', fieldClsImport=$fieldClsImport)"
    }

    fun copy():JavaField {
        val ret  = JavaField(columnName,columType)

        ret.initVal = this.initVal
        ret.isPk = false
        ret.remark = this.remark
        ret.fieldName = this.fieldName
        ret.fieldCls = this.fieldCls
        ret.nullable = this.nullable
        ret.defaultValue = this.defaultValue
        ret.fieldClsImport = this.fieldClsImport
        ret.remarkName = this.remarkName
        ret.generateStr = this.generateStr

        return ret
    }

}

fun calcFieldInfo(f:JavaField) {
    val columType = f.columType

    f.fieldCls = when (columType.trim()) {
        "INT" -> "Long"
        "INT UNSIGNED" -> "Long"
        "TINYINT" -> "Integer"
        "TINYINT UNSIGNED" -> "Integer"
        "BIGINT" -> "Long"
        "BIT" -> "Integer"
        "SMALLINT" -> "Integer"
        "VARCHAR" -> "String"
        "CHAR" -> "String"
        "TIMESTAMP" -> "Date"
        "DATETIME" -> "Date"
        "TEXT" -> "String"
        "DECIMAL" -> "Double"
        else -> throw Exception("未识别的type $columType")
    }
    f.fieldClsImport = when (columType) {
        "TIMESTAMP" -> "java.util.Date"
        "DATETIME" -> "java.util.Date"
        else -> null
    }

    f.fieldName = colNameToCamel(f.columnName)

}

fun dbFieldsExists(fields: List<JavaField>, dbFieldName: String): Boolean {
    return fields.any {
        it.columnName == dbFieldName
    }
}


//设置随机值
fun generateFieldRndSet(indent: String, varName: String, field: JavaField): String {
    val value=
            if(field.isPk) "1L"
            else
                when(field.fieldCls){
                    "Long"->"${Rnd.getRandomNum(100,1000000)}L"
                    "Double"->"${Rnd.getRandomNum(0,100)}D"
                    "Integer"->"${Rnd.getRandomNum(1,4)}"
                    "String"->""""abcd""""
                    "Date"->"""new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("${DateUtil.getCurrentDate()} 00:00:00")"""
                    else -> ""
                }

    return """${indent}${varName}.${generatePrefixMethod("set",field.fieldName)}($value);"""
}


fun fieldExclude(fields: List<JavaField>, vararg exList:String):List<JavaField> {
    return fields.filter { !exList.contains(it.fieldName) }
}
fun fieldExcludeByColumnName(fields: List<JavaField>, vararg exList:String):List<JavaField> {
    return fields.filter { !exList.contains(it.columnName) }
}

//设置随机值
fun generateFieldsRndSetExclude(indent:String, varName:String, fields: MutableList<JavaField>, vararg exList:String): String {
    val filtered = fields.filter { !exList.contains(it.fieldName) }
    return iterAndAppend(filtered){
        idx, javaField ->
        generateFieldRndSet(indent,varName,javaField)
    }
}

//生成@RequestParam
fun generateFieldsRequestParamExclude(indent:String,  fields: MutableList<JavaField>, vararg exList:String): String {
    val filtered = fields.filter { !exList.contains(it.fieldName) }
    return iterAndAppend(filtered){
        idx, javaField ->
        val hasNext = idx!=filtered.lastIndex
        val required = javaField.nullable.toString()
        """${indent}@RequestParam(value = "${javaField.fieldName}", required = $required) ${javaField.fieldCls} ${javaField.fieldName}${if(hasNext) "," else ""}"""
    }
}
//生成showdoc
fun generateFieldsShowdocExclude(indent:String,  fields: MutableList<JavaField>, vararg exList:String): String {
    val filtered = fields.filter { !exList.contains(it.fieldName) }
    return iterAndAppend(filtered){
        idx, javaField ->
        val hasNext = idx!=filtered.lastIndex
        val required = if(javaField.nullable) "否" else "是"
        """${indent}|${javaField.fieldName} |$required  |${javaField.fieldCls} | ${javaField.remark}    |"""
    }
}
//生成set
fun generateFieldsSetExclude(indent:String, varName:String, fields: MutableList<JavaField>, vararg exList:String): String {
    val filtered = fields.filter { !exList.contains(it.fieldName) }
    return iterAndAppend(filtered){
        idx, javaField ->
        val hasNext = idx!=filtered.lastIndex
        val required = javaField.nullable.toString()
        val setMethodName = generatePrefixMethod("set", javaField.fieldName)
        """${indent}$varName.$setMethodName(${javaField.fieldName});"""
    }
}

fun <T> iterAndAppend(fields: List<T>,con:(idx:Int,T)->String):String {
    val sb = StringBuilder()

    fields.forEachIndexed { index, it ->
        sb.append(con(index, it))
        if(index!=fields.lastIndex)
            sb.append("\r\n")
    }

    return sb.toString()
}

//解析表元数据 获得list
fun queryFieldList(DB_URL: String, DATABASE: String, TBL_NAME: String): MutableList<JavaField> {
    //下面别看了  具体逻辑
    Class.forName("com.mysql.jdbc.Driver")
    val connection = DriverManager.getConnection(DB_URL)

    val metaData = connection.metaData

    //列信息
    val list = mutableListOf<JavaField>()

    val colRs = metaData.getColumns(DATABASE, "%", TBL_NAME, "%")
    while (colRs.next()) {
        val columnName = colRs.getString("COLUMN_NAME")
        val columnType = colRs.getString("TYPE_NAME")
        val datasize = colRs.getInt("COLUMN_SIZE")
        val digits = colRs.getInt("DECIMAL_DIGITS")
        val nullable = colRs.getInt("NULLABLE") //1:可
        val message = colRs.getString("REMARKS")
        val columnDef  = colRs.getString("COLUMN_DEF") // 该列的默认值 当值在单引号内时应被解释为一个字符串
        //        println("$columnName $columnType $datasize $digits $nullable $columnDef")
        //        println(message)
        //        println(colRs.getString("IS_AUTOINCREMENT"))

        val javaField = JavaField(columnName, columnType)
        javaField.remark = message ?: ""
        javaField.nullable = nullable == 1
        javaField.defaultValue = columnDef

//        替换换行符
        javaField.remark = javaField.remark.replace("\r\n", " ")
        javaField.remark = javaField.remark.trim()

        calcFieldInfo(javaField)
        list.add(javaField)

//        解析命令
        parseCommands(javaField)
    }

    //表主键
    val pkRs = metaData.getPrimaryKeys(DATABASE, null, TBL_NAME)
    pkRs.next()
    val pkColName = pkRs.getString("COLUMN_NAME")
    val pkColJavaName = colNameToCamel(pkColName)

    list.filter { it.columnName==pkColName }.forEach { it.isPk=true }
    return list
}

fun parseCommands(javaField: JavaField) {
    val remark = javaField.remark

//    默认截取备注前面部分字符作为显示文字
    val remarkName = untilFirstSymbol(remark)
    javaField.remarkName = remarkName

//    使用默认的数据库字段名
    if (javaField.remarkName.isBlank()) {
        javaField.remarkName = javaField.columnName
    }

//    通过注释解析出命令list
    val groupValues = Regex("##.*?##").findAll(remark)
    groupValues.forEach {
        var cmds = it.groupValues[0]
        cmds  = cmds.replace("##","").trim()

        val split = cmds.split("\\s+".toRegex())

        if (split.size <= 1) {
//            javaField.commands.add(NameCommand(cmds))
        } else {
            val cmd = split[0].toUpperCase()

//            设置名字
            if (cmd.equals("N")) {
                javaField.commands.add(NameCommand(split[1]))
            }

//            下拉框
            if (cmd.equals("SELECT")) {
                val element = SelectCommand(cmd)

                (1..split.lastIndex).forEach {
                    val cmdArgOne = split[it].trim()
                    var pair = splitTextByCommonSymbol(cmdArgOne)

                    //                    按符号解析失败
                    if (pair.second == cmdArgOne) {
                        pair = splitTextByFirstNumber(cmdArgOne)
                    }

                    element.cmd_list.add(SelectOption(pair.first,pair.second))
                }

//                需要生成中文字段
                javaField.generateStr = true

                javaField.commands.add(element)
            }

        }

//        命名命令直接执行
        javaField.commands.forEach {
            if (it is NameCommand) {
                javaField.remarkName = it.name
            }
        }
//        println(cmds)
        //        println(it.groupValues[0])
    }

    processAfterJavaFieldInitialized(javaField)
}

fun processAfterJavaFieldInitialized(javaField: JavaField) {
//    select

    javaField.commands.forEach {

        if (it is SelectCommand) {
            MethodImplUtil.add(typeMapProto(javaField,generateMethodNameOfSelect(javaField) ))
        }
    }

}

fun generateMethodNameOfSelect(javaField:JavaField): String {
    return "typeMapOf"+ nameToMethod(javaField.fieldName)
}


fun generateStringBodyOfField(fieldExclude: List<JavaField>,split:String? = "", function: (JavaField) -> String): String {
    var ret = ""

    fieldExclude.forEachIndexed { index, javaField ->
        ret+=function(javaField)

        if (index< fieldExclude.lastIndex) {
            ret +=split
        }
    }

    return ret
}

