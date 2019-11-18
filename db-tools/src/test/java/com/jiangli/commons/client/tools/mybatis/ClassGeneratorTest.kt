package com.jiangli.commons.client.tools.mybatis

import com.jiangli.doc.mybatis.generateBaseImplMethod
import com.jiangli.commons.NameUtil

/**
 *
 *
 * @author Jiangli
 * @date 2018/4/8 14:55
 */
fun main(args: Array<String>) {
    val s = StringBuilder()
    val method = generateBaseImplMethod("A", "B")
    println(method)
//    s.append(method)
    s.append("${method}\r\n")
    println(s)


    println(NameUtil.getCamelSplitName("ArmyStudentStudyRecord"))

}