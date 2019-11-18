package com.jiangli.commons.client.utils

import com.jiangli.commons.client.utils.Util.getCurJarParentDir
import com.jiangli.commons.client.utils.Util.getCurrentJar
import org.junit.jupiter.api.Test

/**
 * @author Jiangli
 * @date 2018/7/3 16:43
 */
class UtilTest {

    @Test
    fun test_getBaseJarPath() {
        println(getCurJarParentDir())
        println(getCurrentJar())
    }

}