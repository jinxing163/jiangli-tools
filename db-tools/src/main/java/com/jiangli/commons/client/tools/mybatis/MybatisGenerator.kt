package com.jiangli.doc.mybatis

import com.jiangli.commons.FileUtil
import com.jiangli.commons.NameUtil.getCamelSplitName
import com.jiangli.commons.PathUtil
import com.jiangli.commons.client.config.PathMap
import com.jiangli.commons.client.generator.*
import com.jiangli.commons.client.methodcore.MethodImplUtil
import com.jiangli.commons.client.model.MethodImplType
import com.jiangli.commons.client.model.NOT_INCLUDE_METHOD
import com.jiangli.commons.client.model.queryFieldList
import com.jiangli.commons.client.utils.DbBaseTool
import com.jiangli.commons.client.utils.Util
import java.io.File
import java.sql.DriverManager



var OVERWRITE_OK_BTN:Boolean?=null
//var OVERWRITE_OK_BTN:Boolean?=true

fun main(args: Array<String>) {
    /////////////START-OF-CONFIG//////////////////
    //aries
    val DB_URL = "jdbc:mysql://192.168.222.8:3306?user=root&password=ablejava"

//    val TBL_NAME = "TBL_DAILY_PUSH"
//    val TBL_NAME = "TBL_COMMON_CATEGORY"
//    val TBL_NAME = "TBL_COMMON_CATEGORY_ITEMS"
//    val TBL_NAME = "TBL_COMPANY"
    val TBL_NAME = "TBL_APPLY_OPERATOR"
//    val TBL_NAME = "TBL_MENU"
//    val TBL_NAME = "TBL_USER"

//    驼峰式 TBL_USER -> TblUser
    val JAVA_NAME = tblNameToCamel(TBL_NAME)
//    或写死
//    val JAVA_NAME = "AnswerResult"

    //     * 配置正确的aries-server项目后，生成的java、sql文件会自动拷贝至相应项目路径
    //     * 配置不正确也没关系，在本项目的target/sql_client_tools能找到生成文件，手动拷贝到项目路径即可
//        val ARIES_SERVER_SRC_PATH = "E:\\idea_zhishi_workSpace_master\\aries-survey"
//        val ARIES_SERVER_SRC_PATH = "D:\\create_mvc\\outpath"
    val ARIES_SERVER_SRC_PATH = "E:\\idea_zhishi_workSpace\\aries-server"
//    val ARIES_SERVER_SRC_PATH = "C:\\projects\\aries-server12312"
    //    val ARIES_SERVER_SRC_PATH = "C:\\projects\\aries-live-api-server"
    //    val ARIES_SERVER_SRC_PATH = "C:\\projects\\org-server"
    //////////////////////////////////////////////
    //////////////////////////////////////////////
    ///////////////END-OF-CONFIG//////////////////

//    默认取表名的驼峰命名名字 TBL_WHITE_LIST  -> TblWhiteList

    //下面别看了  具体逻辑
    Class.forName("com.mysql.jdbc.Driver")
    val connection = DriverManager.getConnection(DB_URL)

    println("java文件中的dto、service等命名前缀——${JAVA_NAME}")

    var count = 1
    var matchedRepo = mutableListOf<String>()
    var matchedRemark = mutableListOf<String>()
    println("正在匹配表名:$TBL_NAME")
    DbBaseTool.iterateRepoTable(DB_URL){ metaData, databaseName, tableName, fullName ->
        if (tableName == TBL_NAME) {
            println("${count} $fullName")
            matchedRepo.add(databaseName)
            count ++
            metaData.userName

            val st = connection.createStatement()
            val executeQuery = st.executeQuery("""select TABLE_COMMENT FROM `information_schema`.`TABLES` where TABLE_SCHEMA='$databaseName' AND TABLE_NAME='$TBL_NAME';""")
            while (executeQuery.next()) {
                matchedRemark.add(executeQuery.getString(1))
            }
            //            metaData.
        }
    }
    println()


//    变量区
    var DATABASE:String?=null
    var PKG:String?=null
    var DESC:String?=null
    var num:Int? = null
    when (matchedRemark.size) {
        0 ->  {
            println("匹配不到表!!结束")
            return
        }
        1 ->  {
            num = 1
            println("匹配到唯一表 ${matchedRepo[num!!-1]}.$TBL_NAME")
        }
        else ->  {
            println("匹配到多表,请输入数字选择一个库:")

            num =Integer.valueOf(readLine()!!.trim())


        }
    }
    DATABASE = matchedRepo[num!!-1]
    DESC = matchedRemark[num!!-1]
    println("您的选择为$num ${DATABASE}.$TBL_NAME ")
    if (DESC == null) {
        DESC = ""
    }
    println("表注释:$DESC")

    PKG = PathMap.repoToPkgMap[DATABASE]
    if (PKG == null) {
        println("PathMap中没有配置库——${DATABASE}对应的包名")
        return
    } else {
        println("匹配到${DATABASE}对应的包名${PKG}")
    }

    val lastPkgName = PathMap.getLastPkgName(DATABASE)
    println("扩展包名 ${lastPkgName}")
    //    return

    println("--------------生成中------------------")

//
    var finalRootDir:File?=null
    var finalSrcDir:File?=null
    var finalResourcesDir:File?=null
    var finalResourcesMapperDir:File?=null
    var finalTestDir:File?=null
    var finalSrcPkgDir:File?=null
    var finalTestPkgDir:File?=null

//    项目目录
    val javaRootDir = File(ARIES_SERVER_SRC_PATH)
    if (javaRootDir.exists()) {
        println("项目代码路径 ${javaRootDir.existsStr()}：$javaRootDir")

        val pom = File(javaRootDir, "pom.xml")
        if (pom.exists()) {
            println("项目pom.xml路径 ${pom.existsStr()}：$pom")
            finalRootDir = javaRootDir
            val split = PKG.split(".").toTypedArray()
            val lastOne = split[split.lastIndex]

            //            src/main/java
            finalSrcDir = File(PathUtil.buildPath(javaRootDir.absolutePath,true,"src","main","java"))
            println("java-src目录  ${finalSrcDir.existsStr()}：$finalSrcDir")

            //            src/main/resources
            finalResourcesDir = File(PathUtil.buildPath(javaRootDir.absolutePath,true,"src","main","resources"))
            println("java-src-resources目录  ${finalResourcesDir.existsStr()}：$finalResourcesDir")

            //            src/main/resources/mapper/pkg
            finalResourcesMapperDir = File(PathUtil.buildPath(finalResourcesDir.absolutePath,true,"mapper",lastOne))
            println("java-src-resources-mapper目录  ${finalResourcesMapperDir.existsStr()}：$finalResourcesMapperDir")

            //            src/test/java
            finalTestDir = File(PathUtil.buildPath(javaRootDir.absolutePath,true,"src","test","java"))
            println("java-test目录  ${finalTestDir.existsStr()}：$finalTestDir")

            //            src/main/java/com/zhishi/forum/pkg
            finalSrcPkgDir = File(PathUtil.buildPath(finalSrcDir.absolutePath,true,*split))
            println("java-src-pkg目录  ${finalSrcPkgDir.existsStr()}：$finalSrcPkgDir")

            //            src/test/java/com/zhishi/forum/pkg
            finalTestPkgDir = File(PathUtil.buildPath(finalTestDir.absolutePath,true,*split))
            println("java-test-pkg目录  ${finalTestPkgDir.existsStr()}：$finalTestPkgDir")
        }
    }


//    临时目录
    val targetDir = Util.getCurJarParentDir()
    val destDir = File(targetDir,"sql_client_tools")
    destDir.mkdirs()
    val OUTPUTPATH = destDir.absolutePath
    println("临时文件生成至： $OUTPUTPATH")


//    println(Util.getCurJarParentDir())
//    println(Util.getCurrentJar())

    //remove
    FileUtil.deleteUnderDir(OUTPUTPATH)

    //fixed
    val list = queryFieldList(DB_URL, DATABASE, TBL_NAME)
    val pkColJavaName = list.first { it.isPk }.fieldName


    /////////////START-OF-接口层//////////////////
    //pojo
    val modelName = JAVA_NAME
    val modelPkg = "${PKG}.model"
    val modelCls = "$modelPkg.$modelName"
    val modelJava = generateFile(generateCls(modelPkg, "$DESC model", modelName, list, IMPORT_COMMON_DTO), OUTPUTPATH, "model", "$modelName.java")
    tryCopy(modelJava,finalSrcPkgDir,"model", "$modelName.java")

    val dtoName = "${JAVA_NAME}Dto"
    val dtoPkg = "${PKG}.dto"
    val dtoCls = "$dtoPkg.$dtoName"
    val dtoJava = generateFile(generateCls(dtoPkg, "$DESC dto", dtoName, list, IMPORT_COMMON_DTO), OUTPUTPATH, "dto", "$dtoName.java")
    tryCopy(dtoJava,finalSrcPkgDir,"dto", "$dtoName.java")


    val openDtoName = "${JAVA_NAME}OpenDto"
    val openDtoPkg = "${PKG}.openapi.dto"
    val openDtoCls = "$openDtoPkg.$openDtoName"
    val pageRecordsCls = "$openDtoPkg.PageRecords"
    val openDtoJava = generateFile(generateCls(openDtoPkg, "$DESC open dto", openDtoName, list, IMPORT_COMMON_DTO, FIELD_SERIAL, IMPL_SERIAL), OUTPUTPATH, "openapi", "dto", "$openDtoName.java")
    tryCopy(openDtoJava,finalSrcPkgDir,"openapi","dto", "$openDtoName.java")

    //interface
    //mapper
    val mapperClsName = "${JAVA_NAME}Mapper"
    val mapperPkg = "${PKG}.mapper"
    val mapperCls = "$mapperPkg.$mapperClsName"
    val mapperJava = generateFile(generateInterface(mapperPkg, DESC, mapperClsName, false, modelName, pend(IMPORT_MAPPER, modelCls), ANNO_MAPPER, mutableListOf(), mutableMapOf("impl.listByDtoPaged" to NOT_INCLUDE_METHOD)), OUTPUTPATH, "mapper", "$mapperClsName.java")
    tryCopy(mapperJava,finalSrcPkgDir,"mapper","$mapperClsName.java")

    //service
    val serviceClsName = "${JAVA_NAME}Service"
    val servicePkg = "${PKG}.service"
    val serviceCls = "$servicePkg.$serviceClsName"
    val serviceJava = generateFile(generateInterface(servicePkg, DESC, serviceClsName, true, dtoName, pend(IMPORT_SERVICE, dtoCls), mutableListOf(), mutableListOf(), mutableMapOf("impl.listByDtoPaged" to NOT_INCLUDE_METHOD)), OUTPUTPATH, "service", "$serviceClsName.java")
    tryCopy(serviceJava,finalSrcPkgDir,"service","$serviceClsName.java")

    //openapi
    val openServiceClsName = "${JAVA_NAME}OpenService"
    val openapiPkg = "${PKG}.openapi"
    val openapiCls = "$openapiPkg.$openServiceClsName"
    val openServiceJava = generateFile(generateInterface(openapiPkg, DESC, openServiceClsName, true, openDtoName, pend(IMPORT_OPENAPI, openDtoCls, pageRecordsCls), mutableListOf(), mutableListOf(), mutableMapOf()), OUTPUTPATH, "openapi", "$openServiceClsName.java")
    tryCopy(openServiceJava,finalSrcPkgDir,"openapi","$openServiceClsName.java")

    //impl
    //dao
    val xmlImpl = generateFile(
            generateMapperXml(TBL_NAME, PKG, JAVA_NAME, list)
            , OUTPUTPATH
            , "${getCamelSplitName(JAVA_NAME)}.xml"
    )
    tryCopy(xmlImpl,finalResourcesMapperDir,"${getCamelSplitName(JAVA_NAME)}.xml")

    //service impl
    val serviceImplPkg = "${PKG}.service.impl"
    val serviceSuperCls = "BaseServiceImpl<$modelName,$dtoName>"
    val serviceImplMap = mapOf(
            "space" to SPACE
            ,"variableName" to nameToCamel(dtoName)
            ,"description" to DESC
            ,"scope" to "public"
            ,"objType" to dtoName
            ,"pkColJavaName" to pkColJavaName
            ,"impl.listByDtoPaged" to NOT_INCLUDE_METHOD

            ,"dtoClsName" to modelName
            ,"serviceName" to nameToCamel(mapperClsName)
    )
    val serviceImplJava = generateFile(
            generateCls(
                    serviceImplPkg
                    , "$DESC Service实现"
                    , "${serviceClsName}Impl"
                    , null
                    , pend(IMPORT_SERVICE_IMPL, serviceCls, modelCls, dtoCls, mapperCls,"${serviceImplPkg}.BaseServiceImpl")
                    , pend(mutableListOf(), autowiredField(mapperClsName))
                    , arrayListOf(serviceClsName)
                    , serviceSuperCls
                    , arrayListOf("@Service")
                    , pend(mutableListOf()
                    , generateBaseImplMethod(modelName, dtoName)
                    , MethodImplUtil.resolveEx(serviceImplMap, MethodImplType.impl, list))), OUTPUTPATH
            , "service"
            ,"impl"
            , "${serviceClsName}Impl.java"
    )
    tryCopy(serviceImplJava,finalSrcPkgDir,"service","impl","${serviceClsName}Impl.java")

    //openapi impl
    val openserviceImplPkg = "${PKG}.openapi.impl"
    val openserviceSuperCls = "BaseServiceImpl<$dtoName,$openDtoName>"
    val openserviceImplMap = mapOf(
            "space" to SPACE
            ,"variableName" to nameToCamel(openDtoName)
            ,"description" to DESC
            ,"scope" to "public"
            ,"pkColJavaName" to pkColJavaName
            ,"objType" to openDtoName

            ,"dtoClsName" to dtoName
            ,"serviceName" to nameToCamel(serviceClsName)
    )
    val openServiceImplJava = generateFile(
            generateCls(
                    openserviceImplPkg,
                    "$DESC OpenService实现",
                    "${openServiceClsName}Impl",
                    null,
                    pend(IMPORT_OPENAPI_IMPL,openapiCls,dtoCls,openDtoCls,serviceCls,pageRecordsCls,"${serviceImplPkg}.BaseServiceImpl"),
                    pend(mutableListOf(),autowiredField(serviceClsName)), arrayListOf(openServiceClsName),
                    openserviceSuperCls,
                    arrayListOf(
                            """@org.springframework.stereotype.Component("${openServiceClsName}Impl")""",
                            """@com.alibaba.dubbo.config.annotation.Service(interfaceClass = $openServiceClsName.class,version = "1.0.0")"""
                    ),
                    pend(mutableListOf(),generateBaseImplMethod(dtoName,openDtoName),MethodImplUtil.resolveEx(openserviceImplMap, MethodImplType.impl, list))
            ),
            OUTPUTPATH,
            "openapi","impl","${openServiceClsName}Impl.java"
    )
    tryCopy(openServiceImplJava,finalSrcPkgDir,"openapi","impl","${openServiceClsName}Impl.java")

    //BaseServiceImpl.java
    val BaseServiceImplJava = generateFile(
            changePkg(
                    getProtoFileBody("BaseServiceImpl.txt")
            ,"package $serviceImplPkg;"
            ),
            OUTPUTPATH,
            "BaseServiceImpl.java"
    )
    tryCopy(BaseServiceImplJava,finalSrcPkgDir,"service","impl","BaseServiceImpl.java")

//  PageRecords
    val PageRecordsJava = generateFile(changePkg(
            getProtoFileBody("PageRecords.java"),"package $openDtoPkg;"),
            OUTPUTPATH,"openapi","dto",
            "PageRecords.java"
    )
    tryCopy(PageRecordsJava,finalSrcPkgDir,"openapi","dto","PageRecords.java")

    //test
    //mapper
    val mapperTestMap = mapOf(
            "space" to SPACE
            ,"description" to DESC
            ,"impl.listByDtoPaged" to NOT_INCLUDE_METHOD

            ,"dtoClsName" to modelName
            ,"serviceName" to nameToCamel(mapperClsName)
    )
    val mapperTestJava = generateFile(
            generateCls(
                    mapperPkg,
                    "$DESC mapper 测试用例",
                    "${mapperClsName}Test",
                    null,
                    pendNew(IMPORT_TEST_COMMON,modelCls,mapperCls), pend(mutableListOf(),autowiredField(mapperClsName)),
                    null,
                    "BaseTest",
                    null,
                    pend(mutableListOf(),MethodImplUtil.resolveEx(mapperTestMap, MethodImplType.test,list))
            ),
            OUTPUTPATH,"test","mapper","${mapperClsName}Test.java"
    )
    tryCopy(mapperTestJava,finalTestPkgDir,"mapper","${mapperClsName}Test.java")

    //service
    val serviceTestMap = mapOf(
            "space" to SPACE
            ,"description" to DESC
            ,"impl.listByDtoPaged" to NOT_INCLUDE_METHOD

            ,"dtoClsName" to dtoName
            ,"serviceName" to nameToCamel(serviceClsName)
    )
    val serviceTestJava = generateFile(
            generateCls(
                    servicePkg,
                    "$DESC service 测试用例",
                    "${serviceClsName}Test",
                    null,
                    pendNew(IMPORT_TEST_COMMON,dtoCls,serviceCls), pend(mutableListOf(),autowiredField(serviceClsName)),
                    null,
                    "BaseTest",
                    null,
                    pend(mutableListOf(),MethodImplUtil.resolveEx(serviceTestMap, MethodImplType.test, list))
            ),
            OUTPUTPATH,"test","service","${serviceClsName}Test.java"
    )
    tryCopy(serviceTestJava,finalTestPkgDir,"service","${serviceClsName}Test.java")

    //openapi
    val openapiTestMap = mapOf(
            "space" to SPACE
            ,"description" to DESC

            ,"dtoClsName" to openDtoName
            ,"serviceName" to nameToCamel(openServiceClsName)
    )
    val openTestJava = generateFile(
            generateCls(
                    openapiPkg,
                    "$DESC open service 测试用例",
                    "${openServiceClsName}Test",
                    null,
                    pendNew(IMPORT_TEST_COMMON,openDtoCls,openapiCls,pageRecordsCls), pend(mutableListOf(),autowiredField(openServiceClsName)),
                    null,
                    "BaseTest",
                    null,
                    pend(mutableListOf(),MethodImplUtil.resolveEx(openapiTestMap, MethodImplType.test, list))
            ),
            OUTPUTPATH,"test","openapi","${openServiceClsName}Test.java"
    )
    tryCopy(openTestJava,finalTestPkgDir,"openapi","${openServiceClsName}Test.java")
    /////////////END-OF-接口层//////////////////


    /////////////START-OF-app对外接口层//////////////////

//    appserver remoteservice
    val appServerRemotePkg = "com.zhihuishu.aries.remote.${lastPkgName}"
    val appServerRemoteClass = "${JAVA_NAME}RemoteService"
    val appServerRemoteSuperCls = null
    val appServerRemoteImplMap = mapOf(
            "space" to SPACE
            ,"scope" to "public"
            ,"description" to DESC

            ,"variableName" to nameToCamel(openDtoName)
            ,"objType" to openDtoName

            ,"serviceName" to nameToCamel(openServiceClsName)
    )
    val appServerRemoteImplJava = generateFile(
            generateCls(
                    appServerRemotePkg
                    , "$DESC 服务调用"
                    , appServerRemoteClass
                    , null
                    , pend(IMPORT_APP_REMOTE_SERVICE_IMPL, pageRecordsCls, openDtoCls, openapiCls)
                    , pend(mutableListOf(), referrenceField(openServiceClsName))
                    , arrayListOf()
                    , appServerRemoteSuperCls
                    , arrayListOf("@Service")
                    , pend(
                        mutableListOf()
                        , MethodImplUtil.resolveEx(appServerRemoteImplMap, MethodImplType.app_remote,list)
                    )
            ), OUTPUTPATH
            , "app"
            , "appserver"
            , "$appServerRemoteClass.java"
    )

//    appserver controller
    val appServerControllerPkg = "com.zhihuishu.aries.api.${lastPkgName}"
    val appServerControllerClass = "${JAVA_NAME}Controller"
    val appServerControllerSuperCls = "GenericController"
    val appServerControllerImplMap = mapOf(
            "space" to SPACE
            ,"scope" to "public"
            ,"description" to DESC

            ,"variableName" to nameToCamel(openDtoName)
            ,"objType" to openDtoName

            ,"serviceName" to nameToCamel(appServerRemoteClass)
    )
    val appServerControllerImplJava = generateFile(
            generateCls(
                    appServerControllerPkg
                    , "$DESC controller"
                    , appServerControllerClass
                    , null
                    , pend(mutableListOf(), pageRecordsCls, openDtoCls,getProtoFileBody("app_controller\\import.txt"))
                    , pend(mutableListOf(), autowiredField(appServerRemoteClass), autowiredField("CompanyRemoteService"), autowiredField("UserRemoteService"),"private  static final String MD = \"w_l_\""," public static final String[] FILTERED_KEY = {\"userId\", \"viewUserId\"}")
                    , arrayListOf()
                    , appServerControllerSuperCls
                    , arrayListOf("@RestController","@RequestMapping(\"/${nameToCamel(JAVA_NAME)}\")")
                    , pend(
                        mutableListOf()
                        , MethodImplUtil.resolveEx(appServerControllerImplMap, MethodImplType.app_controller,list)
                        ,getProtoFileBody("app_controller\\method.txt")
                    )
            ), OUTPUTPATH
            , "app"
            , "appserver"
            , "$appServerControllerClass.java"
    )

//    showdoc
    val showdocMap = mapOf(
            "description" to DESC
            ,"path" to nameToCamel(JAVA_NAME)
    )
    generateFile(
            MethodImplUtil.resolveEx(showdocMap, MethodImplType.app_showdoc,list)
            , OUTPUTPATH
            , "app"
            , "appserver"
            , "showdoc.txt"
    )

    /////////////END-OF-app对外接口层//////////////////

    /////////////START-OF-web层//////////////////

//    生成dubbo引用xml
    generateFile(
            generateDubboReferrenceXml(nameToCamel(openServiceClsName), openapiCls,DESC)
            , OUTPUTPATH
            , "web"
            , "aries"
            , "beans-dubbo-consumer-$lastPkgName.xml"
    )

//controller
    val webControllerPkg = "com.zhihuishu.aries.web.${lastPkgName}.controller"
    val webControllerClass = "${JAVA_NAME}Controller"
    val webControllerSuperCls = "GlobalController"
    val webControllerImplMap = mapOf(
            "space" to SPACE
            ,"scope" to "public"
            ,"description" to DESC
            ,"path" to nameToCamel(JAVA_NAME)
            ,"lastPkgName" to lastPkgName

            ,"variableName" to nameToCamel(openDtoName)
            ,"objType" to openDtoName

            ,"serviceName" to nameToCamel(openServiceClsName)
            ,"serviceCls" to openServiceClsName
    )
    val webControllerImplJava = generateFile(
            MethodImplUtil.resolveEx(webControllerImplMap, MethodImplType.aries_controller,list)
            , OUTPUTPATH
            , "web"
            , "aries"
            , "$webControllerClass.java"
    )


    val webAssectsMap = mapOf(
            "space" to SPACE
            ,"scope" to "public"
            ,"description" to DESC
            ,"path" to nameToCamel(JAVA_NAME)
            ,"javaName" to JAVA_NAME
            ,"lastPkgName" to lastPkgName
    )
    generateFile(
            MethodImplUtil.resolveEx(webAssectsMap, MethodImplType.aries_jsp,list), OUTPUTPATH
            , "web"
            , "aries"
            , "index.jsp"
    )
    generateFile(
            MethodImplUtil.resolveEx(webAssectsMap, MethodImplType.aries_crud_js,list), OUTPUTPATH
            , "web"
            , "aries"
            , "${webControllerClass}CRUD.js"
    )
    generateFile(
            MethodImplUtil.resolveEx(webAssectsMap, MethodImplType.aries_selector_js,list), OUTPUTPATH
            , "web"
            , "aries"
            , "${webControllerClass}SELECTOR.js"
    )

    /////////////END-OF-web层//////////////////
}



fun File.existsStr(): String {
    val exists = this.exists()
    return if (exists) {
        "\u001b[32m√\u001b[0m"
    } else {
        "\u001b[31mX\u001b[0m"
    }
}