package com.jiangli.commons.client.config

import com.jiangli.commons.client.config.PathMap.getLastPkgName
import com.jiangli.commons.client.generator.nameToMethod

/**
 *
 *
 * @author Jiangli
 * @date 2019/11/4 16:57
 */

object PathMap{
    val repoToPkgMap = mutableMapOf<String,String>()

    init {
        repoToPkgMap.put("db_aries_forum","com.zhishi.forum.forum")
        repoToPkgMap.put("db_org_menu","com.zhishi.org.menu")
        repoToPkgMap.put("db_aries_company","com.zhihuishu.aries.company")
        repoToPkgMap.put("db_aries_operation","com.zhihuishu.aries.operation")
        repoToPkgMap.put("db_aries_user","com.zhihuishu.aries.user")
        repoToPkgMap.put("db_aries_manage","com.zhihuishu.aries.common")
        repoToPkgMap.put("db_org_course","com.zhishi.org.course")
        repoToPkgMap.put("db_aries_erp","com.zhishi.aries.erp")
        repoToPkgMap.put("db_aries_run","com.zhihuishu.aries.run")
        repoToPkgMap.put("db_aries_base","com.zhihuishu.base")
        repoToPkgMap.put("db_aries_base_live","com.zhishi.live.datasource")
        repoToPkgMap.put("db_aries_class_tools","com.zhihuishu.aries.chat.datasource")
        repoToPkgMap.put("db_aries_survey","com.zhishi.aries.survey")
        repoToPkgMap.put("db_aries_log","com.zhishi.aries.log")
        repoToPkgMap.put("db_aries_study","com.zhihuishu.aries.study")
        repoToPkgMap.put("db_aries_live","com.zhihuishu.aries.liveRoom.datasource")
        repoToPkgMap.put("db_aries_discuss","com.zhihuishu.aries.discuss")
        repoToPkgMap.put("db_aries_interaction","com.zhihuishu.aries.interaction")
    }

    fun getLastPkgName(repo:String):String {
        val charTimes = getCharTimes(repo, '_')
        val split = repo.split("_")
        if (charTimes == 2) {
            return split[2]
        } else {
            var ret = ""
            val subList = split.subList(2, split.size)

            subList.forEachIndexed { index, s ->
                if (index>0) {
                    ret+= nameToMethod(s)
                } else {
                    ret+=s
                }
            }
            return ret
        }

    }
    fun getCharTimes(repo:String,p:Char):Int {
        var t = 0
        for (c in repo) {
            if (c==p) {
                t++
            }
        }
        return t
    }

}

fun main(args: Array<String>) {
    println(getLastPkgName("db_aries_class_tools"))
    println(getLastPkgName("db_org_menu"))
}

