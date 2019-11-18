package com.jiangli.commons.client.generator

import com.jiangli.doc.mybatis.SPACE
import com.jiangli.commons.client.model.JavaField
import com.jiangli.commons.client.model.dbFieldsExists

/**
 *
 *
 * @author Jiangli
 * @date 2019/11/6 11:07
 */
fun generateMapperXml(tableName:String,pkg:String,javaName:String,fields:List<JavaField>):String{
    val includes = fields.joinToString(",\r\n") { javaField -> SPACE + SPACE + javaField.columnName }
    val variableName = nameToCamel(javaName)
    val containsDeletePerson: Boolean = dbFieldsExists(fields,"DELETE_PERSON")
    val deleteSetStmt = if (containsDeletePerson) """<if test="deletePerson != null">DELETE_PERSON = #{deletePerson}, </if>""" else ""

    val idField = fields.first { it.isPk }.fieldName
    val idColumn = fields.first { it.isPk }.columnName

    fun mustInput(f: JavaField):Boolean{
        return !f.nullable && f.defaultValue == null
    }

    val updateList = StringBuilder()
    fields.filter { !it.isPk } .forEach {
        //        if(mustInput(it)){
        updateList.append("\r\n$SPACE$SPACE$SPACE<if test=\"${it.fieldName} != null\">${it.columnName}= #{${it.fieldName}}, </if>")
        //        } else {
        //            updateList.append("\r\n$SPACE$SPACE$SPACE<if test=\"${it.fieldName} != null\">${it.columnName}= #{${it.fieldName}}, </if>")
        //        }
    }

    val saveColList = StringBuilder()
    val saveValList = StringBuilder()
    val pageConList = StringBuilder()
    val max_idx =   fields.filter { !it.isPk }.lastIndex

    fields.sortedBy {mustInput(it)}.filter { !it.isPk } .forEachIndexed { idx, it ->
        var suffix = if(idx == max_idx) "" else ","

        //        println("$idx / ${fields.lastIndex}")

        if(mustInput(it)){
            saveColList.append("\r\n$SPACE$SPACE$SPACE${it.columnName}${suffix} ")
            saveValList.append("\r\n$SPACE$SPACE$SPACE#{${it.fieldName}}${suffix} ")
        } else {
            saveColList.append("\r\n$SPACE$SPACE$SPACE<if test=\"${it.fieldName} != null\">${it.columnName}${suffix} </if>")
            saveValList.append("\r\n$SPACE$SPACE$SPACE<if test=\"${it.fieldName} != null\">#{${it.fieldName}}${suffix} </if>")
        }

        pageConList.append("\r\n$SPACE$SPACE<if test=\"dto.${it.fieldName} != null\">AND ${it.columnName} = #{dto.${it.fieldName}} </if>")

    }

    //    println(fields.sortedBy {mustInput(it)})
    //    println(fields)

    return """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${pkg}.mapper.${javaName}Mapper">

    <!-- 表字段 -->
    <sql id="fields">
$includes
    </sql>



    <!-- 根据id查列表 -->
    <select id="listOfIds" resultType="${pkg}.model.${javaName}">
        SELECT <include refid="fields"/>  FROM $tableName WHERE IS_DELETED=0
        AND $idColumn in
        <foreach collection="${variableName}Ids" index="index" item="item"
                 open="(" separator="," close=")">
            #{item}
        </foreach>
        ORDER BY $idColumn DESC
    </select>

    <!-- 查单个 -->
    <select id="get"  resultType="${pkg}.model.${javaName}">
        SELECT <include refid="fields"/>  FROM $tableName WHERE IS_DELETED=0  AND $idColumn = #{${variableName}Id}
    </select>

    <!-- 删除 -->
    <update id="delete" parameterType="${pkg}.model.${javaName}">
        UPDATE $tableName
        <set>
            ${deleteSetStmt}
            IS_DELETED = 1
        </set>
        WHERE $idColumn=#{$idField} AND IS_DELETED=0
    </update>

    <!-- 修改 -->
    <update id="update" parameterType="${pkg}.model.${javaName}">
        UPDATE $tableName
        <set>$updateList
            UPDATE_TIME = CURRENT_TIMESTAMP
        </set>
        WHERE $idColumn=#{$idField} AND IS_DELETED=0
    </update>

    <!-- 保存 -->
    <insert id="save" parameterType="${pkg}.model.${javaName}" keyProperty="$idField" useGeneratedKeys="true">
        INSERT INTO $tableName($saveColList
        ) values ($saveValList
        )
    </insert>

     <!-- 查全部 -->
    <select id="listAll" resultType="${pkg}.model.${javaName}">
        SELECT <include refid="fields"/>  FROM $tableName WHERE IS_DELETED=0
        ORDER BY $idColumn DESC
    </select>

    <!-- 表字段 -->
    <sql id="pageCondition">
        IS_DELETED=0
        $pageConList
    </sql>

   <!-- 分页条件查询 -->
    <select id="listByDto" resultType="${pkg}.model.${javaName}">
        SELECT <include refid="fields"/>  FROM $tableName WHERE
        <include refid="pageCondition" />
        ORDER BY $idColumn DESC
        LIMIT #{offset},#{pageSize}
    </select>
    <!-- 条件count -->
    <select id="count" resultType="Long">
        SELECT COUNT(*)  FROM $tableName WHERE
        <include refid="pageCondition" />
    </select>

</mapper>
"""

    //    <!-- 查列表 -->
    //    <select id="list" resultType="${pkg}.model.${javaName}">
    //    SELECT <include refid="fields"/>  FROM $tableName WHERE IS_DELETED=0  AND COURSE_ID = #{courseId}
    //    ORDER BY CREATE_TIME DESC
    //    </select>


}

fun generateDubboReferrenceXml(serviceName:String,serviceCls:String,desc:String):String{
    return """
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://code.alibabatech.com/schema/dubbo
        http://code.alibabatech.com/schema/dubbo/dubbo.xsd">


    <!--$desc 接口服务-->
	<dubbo:reference id="${serviceName}"  timeout="3000" group="pc" version="1.0.0" interface="$serviceCls"   protocol="dubbo">
	</dubbo:reference>

 </beans>
    """.trimIndent()
}