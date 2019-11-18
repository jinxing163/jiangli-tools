package com.jiangli.doc.mybatis

import com.jiangli.commons.client.generator.concatPath
import com.jiangli.commons.client.generator.nameToCamel
import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.MethodImplType
import com.jiangli.commons.NameUtil
import com.jiangli.commons.PathUtil
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.swing.JOptionPane

val IMPORT_COMMON_DTO = arrayListOf("java.util.Date")

val FIELD_SERIAL = arrayListOf("private static final long serialVersionUID = 1L", "")
val IMPL_SERIAL = arrayListOf("java.io.Serializable")

val ANNO_MAPPER = arrayListOf("@Repository")

val IMPORT_MAPPER = arrayListOf("java.util.List","org.apache.ibatis.annotations.Param","org.springframework.stereotype.Repository")
val IMPORT_SERVICE = arrayListOf("java.util.List")
val IMPORT_OPENAPI = arrayListOf("java.util.List")

val IMPORT_SERVICE_IMPL = arrayListOf("java.util.List","java.util.Date","org.springframework.beans.factory.annotation.Autowired","org.springframework.stereotype.Service")
val IMPORT_OPENAPI_IMPL = arrayListOf("java.util.List","java.util.ArrayList","java.util.Date","org.springframework.beans.factory.annotation.Autowired")

val IMPORT_TEST_COMMON = arrayListOf("org.junit.Test","org.springframework.beans.factory.annotation.Autowired","org.junit.runner.RunWith","org.springframework.boot.test.context.SpringBootTest","com.zhihuishu.aries.BaseTest","java.util.Arrays","java.util.List","java.text.SimpleDateFormat")

val IMPORT_APP_REMOTE_SERVICE_IMPL = arrayListOf("java.util.List","com.alibaba.dubbo.config.annotation.Reference","org.springframework.stereotype.Service")

val SPACE = "    "


fun autowiredField(clsName: String,va:String? = null):String {
    return annotationField("@Autowired",clsName,va)
}
fun resourceField(clsName: String,va:String? = null):String {
    return annotationField("@Resource",clsName,va)
}
fun referrenceField(clsName: String):String {
    return annotationField("@Reference(version = \"1.0.0\")",clsName)
}
fun annotationField(anno:String,clsName: String,va:String? = null):String {
    val varName = if(va == null ) nameToCamel(clsName) else va

    return "$anno\r\n${SPACE}private $clsName $varName"
}

fun generateCls(pkg:String, desc:String, clsName:String, fields:List<JavaField>?, extraImports:List<String>?= arrayListOf(), extraField:List<String>?= arrayListOf(), implClses:List<String>?= arrayListOf(), superClsName:String?=null, extraAnnos:List<String>?= arrayListOf(), extraMethods:List<String>?= arrayListOf()):String {
    val fieldList =  StringBuilder()
    val importList = StringBuilder()
    val implClsList = StringBuilder()
    val extendsCls = StringBuilder()
    val methodsList = StringBuilder()
    val annoList =  StringBuilder()
    val totalImport = mutableSetOf<String>()

    //import
    extraImports?.forEach { totalImport.add(it) }
    totalImport.forEach {
        importList.append("import $it;\r\n")
    }

    //注解
    extraAnnos?.forEach {
        annoList.append("\r\n${it}")
    }

    //超类
    superClsName?.let{
        extendsCls.append("""extends $it""")
    }

    //实现类
    implClses?.let {
        if (implClses.isNotEmpty()) {
            implClsList.append("implements ")

            it.forEachIndexed { index, s ->
                implClsList.append(s)
                if (index != it.lastIndex) {
                    implClsList.append(",")
                }
            }
        }
    }

    //custom fields first
    extraField?.forEach {
        fieldList.append("${SPACE}${it}${if (it.isNotEmpty()) ";" else ""}\r\n")
    }

    //fields from list
    fields?.forEach {
        fieldList.append("${SPACE}private ${it.fieldCls} ${it.fieldName};//${it.remark}\r\n")

        it.fieldClsImport?.let {
            totalImport.add(it)
        }
    }


    //所有方法
    val totalMethods = mutableListOf<String>()

    //getter & setter
    fields?.let {
        fields.forEach {
            val setter = """
    public void set${NameUtil.getCapitalName(it.fieldName)}(${it.fieldCls} ${it.fieldName}) {
        this.${it.fieldName} = ${it.fieldName};
    }

    public ${it.fieldCls} get${NameUtil.getCapitalName(it.fieldName)}() {
        return this.${it.fieldName};
    }
"""
            totalMethods.add(setter)
        }
    }

    //toString
    fields?.let {
        val toStrPrefix="""
    @Override
    public String toString() {
        return "$clsName{" +
"""
        val toStrSuffix="""
                '}';
    }
"""
        var sb = StringBuilder()
        fields.forEachIndexed{
            idx,it->
                sb.append("""                "${if(idx!=0) "," else ""}${it.fieldName}=" + ${it.fieldName} + """)
                sb.append("\r\n")
        }
        totalMethods.add("$toStrPrefix$sb$toStrSuffix")
    }

    if (extraMethods != null) {
        totalMethods.addAll(extraMethods)
    }

    totalMethods?.let{
        it.forEach {
            methodsList.append("${it}\r\n")
        }
    }

    return """
package $pkg;
$importList

/**
 * $desc
 */$annoList
public class $clsName $extendsCls $implClsList{
$fieldList

$methodsList
}
"""
}

fun appendComment(sb:StringBuilder,txt:String) {
    sb.append("${SPACE}/**\r\n")
    sb.append("${SPACE} * $txt\r\n")
    sb.append("${SPACE} */\r\n")
}
fun appendEnter(sb:StringBuilder) {
    sb.append("${SPACE}\r\n")
}

fun generateInterface(pkg:String, desc:String, clsName:String, useWrap:Boolean?=false, objName:String, extraImports:List<String>?= arrayListOf(), extraAnnos:List<String>?= arrayListOf(), implClses:List<String>?= arrayListOf(), methodCtrl:MutableMap< Any, Any>?= mutableMapOf()):String {
    val annoList =  StringBuilder()
    val importList = StringBuilder()
    val methodsList =  StringBuilder()
    val implClsList = StringBuilder()
    val imported = hashSetOf<String>()

    extraAnnos?.forEach {
        annoList.append("\r\n${it}")
    }

    extraImports?.forEach {
        if (!imported.contains(it)) {
            importList.append("import $it;\r\n")
            imported.add(it)
        }
    }

//    extraMethods?.forEach {
        val variableName = nameToCamel(objName)
        val mapOf = mutableMapOf(
                "space" to SPACE
                ,"variableName" to variableName
                ,"description" to desc
                ,"scope" to ""
                ,"objType" to objName
        )


    //dao
    if (!useWrap!!) {
        mapOf.put("paramAnno","@Param(\"${'$'}{_this_variableName}\") ")
        mapOf.put("paramAnno_offset","@Param(\"offset\") ")
        mapOf.put("paramAnno_pageSize","@Param(\"pageSize\") ")
    }

    methodCtrl!!.putAll(mapOf)

    val method = MethodImplUtil.resolveEx(methodCtrl, MethodImplType.inf, mutableListOf())

    implClses?.let {
        if (implClsList.isNotEmpty()) {
            implClsList.append("extends ")

            it.forEachIndexed { index, s ->
                implClsList.append(s)
                if (index != it.lastIndex) {
                    implClsList.append(",")
                }
            }
        }
    }

    return """
package $pkg;
$importList

/**
 * $desc
 */$annoList
public interface $clsName $implClsList{
$method
}
"""
}


fun generateBaseImplMethod(serviceClsName: String, openServiceName: String): String {
    val methodList =  """
    @Override
    public Class<$serviceClsName> getDtoClass() {
        return $serviceClsName.class;
    }

    @Override
    public Class<$openServiceName> getOpenDtoClass() {
        return $openServiceName.class;
    }
"""
    return methodList
}


fun tryCopy(src: File, destDir: File?, vararg path:String): Boolean {
    if (destDir != null) {
        val absPath = concatPath(destDir.absolutePath,*path)
        PathUtil.ensureFilePath(absPath)

        var copy = false
        val destFile = File(absPath)
        if (!destFile.exists()) {
            destFile.createNewFile()

            copy = true
        } else {
            if (OVERWRITE_OK_BTN == null) {
                println("【WARN】需要确认是否拷贝 $src -> $destDir")
                val res = JOptionPane.showConfirmDialog(null, "项目部分代码已经存在，是否 全部覆盖 或 全部取消覆盖(增量模式)？", "确认", JOptionPane.YES_NO_OPTION)

                OVERWRITE_OK_BTN = res == 0
            }

            if (OVERWRITE_OK_BTN != null && OVERWRITE_OK_BTN!!) {
                println("【WARN】已选择覆盖 $src -> $destDir")
                copy = true
            }
        }

        if (copy) {
            println("copy to...$destFile")
            IOUtils.copyLarge(FileInputStream(src), FileOutputStream(destFile))
        } else {
            println("ignored copy $src to...$destFile")
        }
    }
    return true
}
