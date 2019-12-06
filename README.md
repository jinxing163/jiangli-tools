# `常用的开发工具类`


生成目录命令

根据查询得知可以在win系统内使用tree命令来实现：
1.先通过CMD进入到项目目录；
2.使用命令：tree /f > list.txt；
3.之后在list.txt文件内查看生成的目录结构。

#### 目录结构

```
./jiangli-tools/
文件夹 PATH 列表
卷序列号为 C2DA-3EC6
        
├─cm_pkg
│  │  pom.xml
│  │  
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─jiangli
│      │  │              test.txt
│      │  │              
│      │  └─resources
│      │          applicationContext.xml
│      │          log4j.properties
│      │          
│      └─test
│          └─java
│              └─com
│                  └─jiangli
│                          test.txt
│                          
├─commons
│  │  commons.iml
│  │  pom.xml
│  │  
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─com
│  │  │  │      ├─jiangli
│  │  │  │      │  │  test.txt
│  │  │  │      │  │  
│  │  │  │      │  └─commons
│  │  │  │      │          ClazzUtils.java
│  │  │  │      │          DateUtil.java
│  │  │  │      │          FileStringProcesser.java
│  │  │  │      │          FileUtil.java
│  │  │  │      │          NameUtil.java
│  │  │  │      │          NumberUtil.java
│  │  │  │      │          PathUtil.java
│  │  │  │      │          Rnd.java
│  │  │  │      │          
│  │  │  │      └─jinxing
│  │  │  │          └─helper
│  │  │  │                  DateFormatHelper.java
│  │  │  │                  PagedRequester.java
│  │  │  │                  PageHelper.java
│  │  │  │                  
│  │  │  └─resources
│  │  │          applicationContext.xml
│  │  │          log4j.properties
│  │  │          
│  │  └─test
│  │      └─java
│  │          └─com
│  │              └─jiangli
│  │                      test.txt
│  │                      
│  └─target
│      ├─classes
│      │  │  applicationContext.xml
│      │  │  log4j.properties
│      │  │  
│      │  └─com
│      │      ├─jiangli
│      │      │  └─commons
│      │      │          ClazzUtils.class
│      │      │          DateUtil.class
│      │      │          FileStringProcesser.class
│      │      │          FileUtil.class
│      │      │          NameUtil.class
│      │      │          NumberUtil.class
│      │      │          PathUtil$ProjectPath.class
│      │      │          PathUtil.class
│      │      │          Rnd.class
│      │      │          
│      │      └─jinxing
│      │          └─helper
│      │                  DateFormatHelper$Format.class
│      │                  DateFormatHelper$Unit.class
│      │                  DateFormatHelper.class
│      │                  PagedRequester$RecordableHashMap.class
│      │                  PagedRequester$RequestResult.class
│      │                  PagedRequester.class
│      │                  PageHelper.class
│      │                  
│      └─generated-sources
│          └─annotations
├─db-tools
│  │  db-tools.iml
│  │  pom.xml
│  │  
│  ├─src
│  │  ├─main
│  │  │  └─java
│  │  │      └─com
│  │  │          └─jiangli
│  │  │              └─commons
│  │  │                  └─client
│  │  │                      ├─config
│  │  │                      │      PathMap.kt
│  │  │                      │      
│  │  │                      ├─generator
│  │  │                      │      ClassGenerator.kt
│  │  │                      │      FileGenerator.kt
│  │  │                      │      SymbolConvertor.kt
│  │  │                      │      XmlGenerator.kt
│  │  │                      │      
│  │  │                      ├─methodcore
│  │  │                      │      MethodImplUtil.kt
│  │  │                      │      
│  │  │                      ├─methodimpl
│  │  │                      │      count.kt
│  │  │                      │      delete.kt
│  │  │                      │      dynamic_typeMap.kt
│  │  │                      │      get.kt
│  │  │                      │      index.kt
│  │  │                      │      listAll.kt
│  │  │                      │      listByDto.kt
│  │  │                      │      listByDtoPaged.kt
│  │  │                      │      listOfIds.kt
│  │  │                      │      no_java.kt
│  │  │                      │      save.kt
│  │  │                      │      sort.kt
│  │  │                      │      transToOpen.kt
│  │  │                      │      update.kt
│  │  │                      │      
│  │  │                      ├─model
│  │  │                      │      Command.kt
│  │  │                      │      Field.kt
│  │  │                      │      MethodMeta.kt
│  │  │                      │      
│  │  │                      ├─proto
│  │  │                      │  │  BaseServiceImpl.txt
│  │  │                      │  │  PageRecords.java
│  │  │                      │  │  
│  │  │                      │  ├─app_controller
│  │  │                      │  │      import.txt
│  │  │                      │  │      method.txt
│  │  │                      │  │      
│  │  │                      │  └─web_controller
│  │  │                      │          import.txt
│  │  │                      │          jsp_list.txt
│  │  │                      │          js_crud.txt
│  │  │                      │          js_crud_v1.txt
│  │  │                      │          js_selector.txt
│  │  │                      │          method.txt
│  │  │                      │          
│  │  │                      ├─tools
│  │  │                      │  └─mybatis
│  │  │                      │          DbSyncSqlHelper.kt
│  │  │                      │          MybatisGenerator.kt
│  │  │                      │          更新日志.txt
│  │  │                      │          注释命令解析.md
│  │  │                      │          要求.txt
│  │  │                      │          计划.txt
│  │  │                      │          
│  │  │                      └─utils
│  │  │                              DbBaseTool.kt
│  │  │                              Util.kt
│  │  │                              
│  │  └─test
│  │      ├─java
│  │      │  └─com
│  │      │      └─jiangli
│  │      │          └─commons
│  │      │              └─client
│  │      │                  ├─tools
│  │      │                  │  └─mybatis
│  │      │                  │          ClassGeneratorTest.kt
│  │      │                  │          MethodImplUtilTest.kt
│  │      │                  │          StructureGenerator.kt
│  │      │                  │          
│  │      │                  └─utils
│  │      │                          NameUtilTest.java
│  │      │                          UtilTest.kt
│  │      │                          
│  │      └─resources
│  │              applicationContext.xml
│  │              logback-test.xml
│  │              
│  └─target
│      ├─classes
│      │  ├─com
│      │  │  └─jiangli
│      │  │      ├─commons
│      │  │      │  └─client
│      │  │      │      ├─config
│      │  │      │      │      PathMap.class
│      │  │      │      │      PathMapKt.class
│      │  │      │      │      
│      │  │      │      ├─generator
│      │  │      │      │      FileGeneratorKt.class
│      │  │      │      │      SymbolConvertorKt.class
│      │  │      │      │      XmlGeneratorKt$generateMapperXml$$inlined$sortedBy$1.class
│      │  │      │      │      XmlGeneratorKt$generateMapperXml$1.class
│      │  │      │      │      XmlGeneratorKt$generateMapperXml$includes$1.class
│      │  │      │      │      XmlGeneratorKt.class
│      │  │      │      │      
│      │  │      │      ├─methodcore
│      │  │      │      │      MethodImplUtil$add$1.class
│      │  │      │      │      MethodImplUtil.class
│      │  │      │      │      
│      │  │      │      ├─methodimpl
│      │  │      │      │      CountKt$count$1.class
│      │  │      │      │      CountKt.class
│      │  │      │      │      DeleteKt$delete$1.class
│      │  │      │      │      DeleteKt.class
│      │  │      │      │      GetKt$get$1.class
│      │  │      │      │      GetKt.class
│      │  │      │      │      IndexKt$index$1.class
│      │  │      │      │      IndexKt.class
│      │  │      │      │      ListAllKt$listAll$1.class
│      │  │      │      │      ListAllKt.class
│      │  │      │      │      ListByDtoKt$listByDto$1.class
│      │  │      │      │      ListByDtoKt.class
│      │  │      │      │      ListByDtoPagedKt$listByDtoPaged$1.class
│      │  │      │      │      ListByDtoPagedKt.class
│      │  │      │      │      ListOfIdsKt$listOfIds$1.class
│      │  │      │      │      ListOfIdsKt.class
│      │  │      │      │      No_javaKt$no_java$1$aries_crud_js$2.class
│      │  │      │      │      No_javaKt$no_java$1$aries_crud_js$4.class
│      │  │      │      │      No_javaKt$no_java$1$aries_crud_js$s$2.class
│      │  │      │      │      No_javaKt$no_java$1$aries_jsp$1.class
│      │  │      │      │      No_javaKt$no_java$1$aries_jsp$2.class
│      │  │      │      │      No_javaKt$no_java$1$aries_selector_js$1.class
│      │  │      │      │      No_javaKt$no_java$1$aries_selector_js$3.class
│      │  │      │      │      No_javaKt$no_java$1$geSearchBody$s$1.class
│      │  │      │      │      No_javaKt$no_java$1$geTableData$1.class
│      │  │      │      │      No_javaKt$no_java$1.class
│      │  │      │      │      No_javaKt.class
│      │  │      │      │      SaveKt$save$1.class
│      │  │      │      │      SaveKt.class
│      │  │      │      │      SortKt$sort$1.class
│      │  │      │      │      SortKt.class
│      │  │      │      │      TransToOpenKt$transToOpen$1$impl$getMap$1.class
│      │  │      │      │      TransToOpenKt$transToOpen$1$impl$setStr$1.class
│      │  │      │      │      TransToOpenKt$transToOpen$1.class
│      │  │      │      │      TransToOpenKt.class
│      │  │      │      │      typeMapProto.class
│      │  │      │      │      UpdateKt$update$1.class
│      │  │      │      │      UpdateKt.class
│      │  │      │      │      
│      │  │      │      ├─model
│      │  │      │      │      Command.class
│      │  │      │      │      FieldKt$generateFieldsRequestParamExclude$1.class
│      │  │      │      │      FieldKt$generateFieldsRndSetExclude$1.class
│      │  │      │      │      FieldKt$generateFieldsSetExclude$1.class
│      │  │      │      │      FieldKt$generateFieldsShowdocExclude$1.class
│      │  │      │      │      FieldKt.class
│      │  │      │      │      JavaField.class
│      │  │      │      │      MethodImplType.class
│      │  │      │      │      MethodMetaKt.class
│      │  │      │      │      MMethod$WhenMappings.class
│      │  │      │      │      MMethod.class
│      │  │      │      │      NameCommand.class
│      │  │      │      │      QueryInCommand.class
│      │  │      │      │      SelectCommand.class
│      │  │      │      │      SelectOption.class
│      │  │      │      │      SortCommand.class
│      │  │      │      │      
│      │  │      │      ├─proto
│      │  │      │      │      PageRecords.class
│      │  │      │      │      
│      │  │      │      ├─tools
│      │  │      │      │  └─mybatis
│      │  │      │      │          DbSyncSqlHelperKt.class
│      │  │      │      │          Field.class
│      │  │      │      │          Index.class
│      │  │      │      │          Tbl.class
│      │  │      │      │          
│      │  │      │      └─utils
│      │  │      │              DbBaseTool$iterateRepoTableField$1.class
│      │  │      │              DbBaseTool.class
│      │  │      │              Util$getEnv$1.class
│      │  │      │              Util.class
│      │  │      │              
│      │  │      └─doc
│      │  │          └─mybatis
│      │  │                  ClassGeneratorKt.class
│      │  │                  MybatisGeneratorKt$main$1.class
│      │  │                  MybatisGeneratorKt.class
│      │  │                  
│      │  └─META-INF
│      │          db-tools.kotlin_module
│      │          
│      ├─generated-sources
│      │  └─annotations
│      ├─generated-test-sources
│      │  └─test-annotations
│      ├─sql_client_tools
│      │  │  BaseServiceImpl.java
│      │  │  flowers_system.xml
│      │  │  
│      │  ├─app
│      │  │  └─appserver
│      │  │          FlowersSystemController.java
│      │  │          FlowersSystemRemoteService.java
│      │  │          showdoc.txt
│      │  │          
│      │  ├─dto
│      │  │      FlowersSystemDto.java
│      │  │      
│      │  ├─mapper
│      │  │      FlowersSystemMapper.java
│      │  │      
│      │  ├─model
│      │  │      FlowersSystem.java
│      │  │      
│      │  ├─openapi
│      │  │  │  FlowersSystemOpenService.java
│      │  │  │  
│      │  │  ├─dto
│      │  │  │      FlowersSystemOpenDto.java
│      │  │  │      PageRecords.java
│      │  │  │      
│      │  │  └─impl
│      │  │          FlowersSystemOpenServiceImpl.java
│      │  │          
│      │  ├─service
│      │  │  │  FlowersSystemService.java
│      │  │  │  
│      │  │  └─impl
│      │  │          FlowersSystemServiceImpl.java
│      │  │          
│      │  ├─test
│      │  │  ├─mapper
│      │  │  │      FlowersSystemMapperTest.java
│      │  │  │      
│      │  │  ├─openapi
│      │  │  │      FlowersSystemOpenServiceTest.java
│      │  │  │      
│      │  │  └─service
│      │  │          FlowersSystemServiceTest.java
│      │  │          
│      │  └─web
│      │      └─aries
│      │              beans-dubbo-consumer-operation.xml
│      │              FlowersSystemController.java
│      │              FlowersSystemCRUD.js
│      │              FlowersSystemSELECTOR.js
│      │              index.jsp
│      │              
│      └─test-classes
│          │  applicationContext.xml
│          │  logback-test.xml
│          │  
│          ├─com
│          │  └─jiangli
│          │      ├─commons
│          │      │  └─client
│          │      │      ├─tools
│          │      │      │  └─mybatis
│          │      │      │          ClassGeneratorTestKt.class
│          │      │      │          MethodImplUtilTest.class
│          │      │      │          
│          │      │      └─utils
│          │      │              NameUtilTest.class
│          │      │              UtilTest.class
│          │      │              
│          │      └─doc
│          │          └─mybatis
│          │                  Column.class
│          │                  StructureGeneratorKt$main$1.class
│          │                  StructureGeneratorKt.class
│          │                  
│          └─META-INF
│                  db-tools.kotlin_module
│                  
└─junit-extension
    │  junit-extension.iml
    │  pom.xml
    │  
    ├─doc
    │      说明.docx
    │      
    └─src
        ├─main
        │  └─java
        │      └─com
        │          └─jiangli
        │              └─commons
        │                  └─jtest
        │                      └─core
        │                          │  InvokeContext.java
        │                          │  InvokeMethodRecycle.java
        │                          │  InvokeMode.java
        │                          │  RepeatFixedDuration.java
        │                          │  RepeatFixedTimes.java
        │                          │  StatisticsJunitRunner.java
        │                          │  StatisticsSpringJunitRunner.java
        │                          │  
        │                          ├─data
        │                          │  │  DataCollector.java
        │                          │  │  StatisticModel.java
        │                          │  │  
        │                          │  └─handler
        │                          │          ConsolePrintDataHandler.java
        │                          │          DataHandler.java
        │                          │          PrintNothingDataHandler.java
        │                          │          ResultsCollector.java
        │                          │          
        │                          └─group
        │                              │  AvailableGroup.java
        │                              │  CommonGroup.java
        │                              │  GroupInf.java
        │                              │  InvokerGroup.java
        │                              │  
        │                              └─invoker
        │                                      FixedLengthStringGroup.java
        │                                      GroupInvoker.java
        │                                      SingleGroup.java
        │                                      
        └─test
            ├─java
            │  └─com
            │      └─jiangli
            │          └─commons
            │              └─jtest
            │                  └─core
            │                      └─test
            │                              JunitTest.java
            │                              SpringJunitTest.java
            │                              StatisticsJunitTest.java
            │                              
            └─resources
                    applicationContext.xml
                    logback-test.xml
                    

```

