package com.jiangli.commons.client.methodcore

import com.jiangli.commons.ClazzUtils
import com.jiangli.commons.client.generator.colNameToCamel
import com.jiangli.commons.client.generator.generatePrefixMethod
import com.jiangli.commons.client.generator.resolveBodyBySpring
import com.jiangli.commons.client.model.*
import org.springframework.util.PropertyPlaceholderHelper
import java.util.*

/**
 *
 *
 * @author Jiangli
 * @date 2019/11/5 13:35
 */
object MethodImplUtil{
    val IMPL_XML = 0
    val IMPL_SERVICE = 1
    val IMPL_OPEN_SERVICE = 2

    val list = mutableListOf<MMethod>()
    val list_order = listOf("i","s","g","u","d","c","l") // 首字母优先级
    fun getOrder(s:String): Int {
        var ret = list_order.indexOf(s)
        return if(ret < 0) 9999 else ret
    }

    fun add(mt: MMethod): Unit {
        list.add(mt)

        Collections.sort(list,kotlin.Comparator { o1, o2 ->
            val idx1 = getOrder(o1.name!![0].toString())
            val idx2 =  getOrder(o2.name!![0].toString())

            if (idx1.equals(idx2)) {
//                优先级相等的话用字符串排序
                return@Comparator o1.name!!.compareTo(o2.name!!)
            } else {
//                根据优先级排序
                return@Comparator idx1.compareTo(idx2)
            }
        })
    }

    init {
//        val classPath = PathUtil.getClassPath(save.javaClass)
        val clazzName = ClazzUtils.getClazzName("com.jiangli.commons.client.methodimpl", false)
        for (s in clazzName) {
            Class.forName(s)
        }

//        println(classPath)
        //        save::javaClass
//                Class.forName("com.jiangli.commons.client.methodimpl.SaveKt")
    }

    @Deprecated("")
    fun funcList(): List<MMethod> {
        val list = mutableListOf<MMethod>()

        list.add(MMethod("${'$'}{space}", "save", "新增", "${'$'}{variableName}", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "delete", "删除", "${'$'}{variableName}", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} void ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "update", "更新", "${'$'}{variableName}", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} void ${'$'}{_this_name}(${'$'}{objType} ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "get", "查询单个", "${'$'}{variableName}Id", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} ${'$'}{objType} ${'$'}{_this_name}(${'$'}{idType} ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        //        是否有查课程id
        //        list.add(MMethod("${'$'}{space}","list","一对多查询列表","courseId","""
        //${'$'}{_this_indent}/**
        //${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
        //${'$'}{_this_indent}*/${'$'}{annotation}
        //${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{idType} ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
        //"""))

        list.add(MMethod("${'$'}{space}", "listOfIds", "根据id查列表", "${'$'}{variableName}Ids", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}List<${'$'}{idType}> ${'$'}{_this_variableName})${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "listAll", "查询全部", "${'$'}{variableName}Ids", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}()${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "count", "条件count", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} Long ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto)${'$'}{impl.${'$'}{_this_name}:{}}
"""))



        list.add(MMethod("${'$'}{space}", "listByDto", "条件分页查询", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} List<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer offset,${'$'}{paramAnno_pageSize:}Integer pageSize)${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        list.add(MMethod("${'$'}{space}", "listByDtoPaged", "条件分页查询（包装）,pageIndex从0开始", "dto", """
${'$'}{_this_indent}/**
${'$'}{_this_indent}* ${'$'}{description} ${'$'}{_this_nameCN}
${'$'}{_this_indent}*/${'$'}{annotation}
${'$'}{_this_indent}${'$'}{scope} PageRecords<${'$'}{objType}> ${'$'}{_this_name}(${'$'}{paramAnno:}${'$'}{objType} dto, ${'$'}{paramAnno_offset:}Integer pageIndex,${'$'}{paramAnno_pageSize:}Integer pageSize)${'$'}{impl.${'$'}{_this_name}:{}}
"""))

        return list
    }

    @Deprecated("")
    fun resolveInterface(map:Map<out Any,out Any>): String? {
        val  paramMap = mutableMapOf<Any,Any>()
        val lineEnd = ";"
        paramMap.put("impl.save", lineEnd)
        paramMap.put("impl.delete", lineEnd)
        paramMap.put("impl.update", lineEnd)
        paramMap.put("impl.get", lineEnd)
        paramMap.put("impl.list", lineEnd)
        paramMap.put("impl.listOfIds", lineEnd)
        paramMap.put("impl.listAll", lineEnd)
        paramMap.put("impl.count", lineEnd)
        paramMap.put("impl.listByDto", lineEnd)
        paramMap.put("impl.listByDtoPaged", lineEnd)
        //
        paramMap.putAll(map)

        return resolve(paramMap)
    }

    @Deprecated("")
    fun resolveImpl(map:Map<out Any,out Any>, dtoClsName:String, serviceName:String): String {
        val  paramMap = mutableMapOf<Any,Any>()
        paramMap.put("annotation", "\r\n${'$'}{space}@Override")

        val dblSpace = """${'$'}{space}${'$'}{space}"""

        val pkColJavaName = map.get("pkColJavaName")?.toString()?:"id"
        val pkColJavaNameGetter = generatePrefixMethod("get",pkColJavaName)
        val pkColJavaNameSetter = generatePrefixMethod("set",pkColJavaName)

        paramMap.put("impl.save", """{
$dblSpace$dtoClsName dto=  transToDto(${'$'}{variableName});
${'$'}{space}${'$'}{space}$serviceName.save(dto);
${'$'}{space}${'$'}{space}
${'$'}{space}${'$'}{space}Long saveId = dto.${pkColJavaNameGetter}();
${'$'}{space}${'$'}{space}${'$'}{variableName}.${pkColJavaNameSetter}(saveId);
${'$'}{space}${'$'}{space}return saveId;
${'$'}{space}}""")

        paramMap.put("impl.delete", """{
$dblSpace$dtoClsName dto=  transToDto(${'$'}{variableName});
${'$'}{space}${'$'}{space}$serviceName.delete(dto);
${'$'}{space}}""")

        paramMap.put("impl.update", """{
$dblSpace$dtoClsName dto=  transToDto(${'$'}{variableName});
${'$'}{space}${'$'}{space}$serviceName.update(dto);
${'$'}{space}}""")

        paramMap.put("impl.get", """{
${dblSpace}return transToOpen($serviceName.get(${'$'}{variableName}Id));
${'$'}{space}}""")

        paramMap.put("impl.list", """{
${dblSpace}return transToOpen($serviceName.list(courseId));
${'$'}{space}}""")

        paramMap.put("impl.listOfIds", """{
${dblSpace}return transToOpen($serviceName.listOfIds(${'$'}{variableName}Ids));
${'$'}{space}}""")

        paramMap.put("impl.listAll", """{
${dblSpace}return transToOpen($serviceName.listAll());
${'$'}{space}}""")

        paramMap.put("impl.count", """{
${dblSpace}return $serviceName.count(transToDto(dto));
${'$'}{space}}""")

        paramMap.put("impl.listByDto", """{
${dblSpace}return transToOpen($serviceName.listByDto(transToDto(dto),offset,pageSize));
${'$'}{space}}""")

        paramMap.put("impl.listByDtoPaged", """{
${dblSpace}long count = $serviceName.count(transToDto(dto));
${dblSpace}PageRecords<${'$'}{objType}> ret = new PageRecords<>();
${dblSpace}ret.setTotalRecords(count);

${dblSpace}pageSize = ensurePageSize(pageSize, 1, 100);
${dblSpace}pageIndex = ensurePageIndex(pageIndex,pageSize,count);

${dblSpace}Integer realRetrieveSize = pageSize + 1;

${dblSpace}if (pageIndex < 0) {
${dblSpace} ret.setRecords(new ArrayList<>());
${dblSpace}} else {
${dblSpace} List<${'$'}{objType}> totalRecords = transToOpen($serviceName.listByDto(transToDto(dto),pageIndex*pageSize, realRetrieveSize));
${dblSpace}if (realRetrieveSize.equals(totalRecords.size())) {
${dblSpace}      ret.setNextId(Long.valueOf(pageIndex+1));
${dblSpace}      totalRecords = new ArrayList<>(totalRecords.subList(0,pageSize));
${dblSpace}}
${dblSpace} ret.setRecords(totalRecords);
${dblSpace}}
${dblSpace}return ret;
${'$'}{space}}""")

        //        setNextId(Long.valueOf((pageIndex+1) * pageSize));

        paramMap.putAll(map)

        return resolve(paramMap)
    }

    @Deprecated("")
    fun resolveTest(map: Map<out Any, out Any>, dtoClsName: String, serviceName: String, fields: MutableList<JavaField>): String {
        val  paramMap = mutableMapOf<Any,Any>()
        paramMap.put("annotation", "\r\n${'$'}{space}@Test")

        val dblSpace = """${'$'}{space}${'$'}{space}"""
        val containsDeletePerson: Boolean = dbFieldsExists(fields,"DELETE_PERSON")
        val containsCreatePerson: Boolean = dbFieldsExists(fields,"CREATE_PERSON")
        val setDeletePerson = if(containsDeletePerson) """${dblSpace}dto.setDeletePerson(900L);""" else ""
        val idField = fields.first { it.isPk }.fieldName
        val idFieldJavaName = colNameToCamel(idField);

        val varName = "dto"
        val saveSetStmt = generateFieldsRndSetExclude(dblSpace, varName,fields,idField, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"),  colNameToCamel("DELETE_PERSON"))
        val updateSetStmt = generateFieldsRndSetExclude(dblSpace, varName,fields, colNameToCamel("IS_DELETED"), colNameToCamel("CREATE_TIME"), colNameToCamel("UPDATE_TIME"), colNameToCamel("CREATE_PERSON"), colNameToCamel("DELETE_PERSON"))
        val setIdStmt = generateFieldRndSet(dblSpace, varName,fields.first { it.isPk })
        paramMap.put("impl.save", """{
$dblSpace$dtoClsName dto=  new $dtoClsName();
$saveSetStmt
${dblSpace}Long ${idFieldJavaName} = $serviceName.save(dto);
${dblSpace}System.out.println(${idFieldJavaName});
${'$'}{space}}""")


        paramMap.put("impl.delete", """{
$dblSpace$dtoClsName dto=  new $dtoClsName();
${setIdStmt}
$setDeletePerson
${dblSpace}$serviceName.delete(dto);
${'$'}{space}}""")

        paramMap.put("impl.update", """{
$dblSpace$dtoClsName dto=  new $dtoClsName();
${updateSetStmt}
${dblSpace}$serviceName.update(dto);
${'$'}{space}}""")

        paramMap.put("impl.get", """{
$dblSpace$dtoClsName dto=  $serviceName.get(1L);
${dblSpace}System.out.println(dto);
${'$'}{space}}""")

        paramMap.put("impl.listOfIds", """{
${dblSpace}List<$dtoClsName> list=  $serviceName.listOfIds(Arrays.asList(1L,2L,3L,4L));
${dblSpace}System.out.println(list);
${'$'}{space}}""")

        paramMap.put("impl.listAll", """{
${dblSpace}List<$dtoClsName> list=  $serviceName.listAll();
${dblSpace}System.out.println(list);
${'$'}{space}}""")

        paramMap.put("impl.count", """{
${dblSpace}$dtoClsName dto = new   $dtoClsName();
$saveSetStmt
${dblSpace}Long count=  $serviceName.count(dto);
${dblSpace}System.out.println(count);
${'$'}{space}}""")

        paramMap.put("impl.listByDto", """{
${dblSpace}$dtoClsName dto = new   $dtoClsName();
$saveSetStmt
${dblSpace}List<$dtoClsName> list=  $serviceName.listByDto(dto,0,10);
${dblSpace}System.out.println(list);
${'$'}{space}}""")

        paramMap.put("impl.listByDtoPaged", """{
${dblSpace}$dtoClsName dto = new   $dtoClsName();
$saveSetStmt
${dblSpace}PageRecords<$dtoClsName> list=  $serviceName.listByDtoPaged(dto,0,10);
${dblSpace}System.out.println(list);
${'$'}{space}}""")

        paramMap.putAll(map)

        val x =  PropertyPlaceholderHelper("\${","}",":",true)
        val props= Properties()
        props.putAll(paramMap)
        val sb = StringBuilder()
        funcList().forEach {
            val obj = it

            obj.javaClass.declaredFields.forEach {
                try {
                    if (it.name != "body") {
                        it.isAccessible = true
                        props.put("_this_${it.name}",it.get(obj))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val realBody  = x.replacePlaceholders("""
${'$'}{_this_indent}//${'$'}{description} 测试-${'$'}{_this_nameCN}${'$'}{annotation}
${'$'}{_this_indent}public void test_${'$'}{_this_name}() throws Exception${'$'}{impl.${'$'}{_this_name}:{}}
""", props)

            if (realBody.contains(NOT_INCLUDE_METHOD)) {
                return@forEach
            }

            sb.append(realBody)
            sb.append("\r\n")
        }

        return sb.toString()
    }


    @Deprecated("")
    fun resolve(map:Map<Any,Any>): String {
        val x =  PropertyPlaceholderHelper("\${","}",":",true)
        val props= Properties()

        val  defaultMap = mutableMapOf<Any,Any>()
        defaultMap.put("space", "")
        defaultMap.put("description", "")
        defaultMap.put("scope", "")
        defaultMap.put("objType", "")
        defaultMap.put("variableName", "")
        defaultMap.put("idType", "Long")
        defaultMap.put("annotation", "")

        props.putAll(defaultMap)
        props.putAll(map)

        val sb = StringBuilder()

        //        每一个方法 生成实现
        funcList().forEach {
            try { //MMethod
                val obj = it

                //props:   context
                obj.javaClass.declaredFields.forEach {
                    if (it.name != "body") {
                        it.isAccessible = true
                        props.put("_this_${it.name}",it.get(obj))
                    }
                }

                val realBody  = x.replacePlaceholders(obj.body, props)

                //not include
                if (realBody.contains(NOT_INCLUDE_METHOD)) {
                    return@forEach
                }

                sb.append(realBody)
                sb.append("\r\n")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return sb.toString()
    }


    fun resolveEx(map:Map<out Any,out Any>, type: MethodImplType, fields: MutableList<JavaField>): String {
        val props= Properties()

        val  defaultMap = mutableMapOf<Any,Any>()
        defaultMap.put("space", "   ")
        defaultMap.put("description", "")
        defaultMap.put("scope", "")
        defaultMap.put("objType", "")
        defaultMap.put("variableName", "")
        defaultMap.put("idType", "Long")
        defaultMap.put("annotation", "")

        props.putAll(defaultMap)
        props.putAll(map)

        val sb = StringBuilder()

        //        每一个方法 生成实现
        list.forEach {
            props.putIfAbsent("impl.${it.name}",it.applyImpl(type,fields,props))

            //MMethod
            val obj = it
            var realBody:String=obj.body!!
            var times = 1
            //props:   context
            do {
                var javaClass:Class<*> = obj.javaClass
                if (javaClass!= MMethod::class.java) {
                    javaClass = javaClass.superclass
                }
                javaClass.declaredFields.forEach {
                    if (it.name != "body") {
                        it.isAccessible = true
                        try {
                            var value = it.get(obj)
                            if (value == null) {
                                value = ""
                            }
                            props.put("_this_${it.name}", value)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                realBody = resolveBodyBySpring(realBody,props)
            }while (times-->0)


            //not include
            if (realBody.contains(NOT_INCLUDE_METHOD)) {
                return@forEach
            }

            sb.append(realBody)
            sb.append("\r\n")
        }

        return sb.toString()
    }
}