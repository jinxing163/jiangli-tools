# `常用的开发工具类`


生成目录命令

根据查询得知可以在win系统内使用tree命令来实现：
1.先通过CMD进入到项目目录;
2.使用命令：tree /a > list.txt;
3.之后在list.txt文件内查看生成的目录结构。
help tree：帮助

#### 目录结构

```
./jiangli-tools/
文件夹 PATH 列表
卷序列号为 C2DA-3EC6
+---.idea
|   +---inspectionProfiles
|   \---libraries
+---cm_pkg
|   \---src
|       +---main
|       |   +---java
|       |   |   \---com
|       |   |       \---jiangli
|       |   \---resources
|       \---test
|           \---java
|               \---com
|                   \---jiangli
+---commons
|   +---src
|   |   +---main
|   |   |   +---java
|   |   |   |   \---com
|   |   |   |       +---jiangli
|   |   |   |       |   \---commons
|   |   |   |       \---jinxing
|   |   |   |           \---helper
|   |   |   \---resources
|   |   \---test
|   |       \---java
|   |           \---com
|   |               \---jiangli
|   \---target
|       +---classes
|       |   \---com
|       |       +---jiangli
|       |       |   \---commons
|       |       \---jinxing
|       |           \---helper
|       \---generated-sources
|           \---annotations
+---db-tools
|   +---src
|   |   +---main
|   |   |   \---java
|   |   |       \---com
|   |   |           \---jiangli
|   |   |               \---commons
|   |   |                   \---client
|   |   |                       +---config
|   |   |                       +---generator
|   |   |                       +---methodcore
|   |   |                       +---methodimpl
|   |   |                       +---model
|   |   |                       +---proto
|   |   |                       |   +---app_controller
|   |   |                       |   \---web_controller
|   |   |                       +---tools
|   |   |                       |   \---mybatis
|   |   |                       \---utils
|   |   \---test
|   |       +---java
|   |       |   \---com
|   |       |       \---jiangli
|   |       |           \---commons
|   |       |               \---client
|   |       |                   +---tools
|   |       |                   |   \---mybatis
|   |       |                   \---utils
|   |       \---resources
|   \---target
|       +---classes
|       |   +---com
|       |   |   \---jiangli
|       |   |       +---commons
|       |   |       |   \---client
|       |   |       |       +---config
|       |   |       |       +---generator
|       |   |       |       +---methodcore
|       |   |       |       +---methodimpl
|       |   |       |       +---model
|       |   |       |       +---proto
|       |   |       |       +---tools
|       |   |       |       |   \---mybatis
|       |   |       |       \---utils
|       |   |       \---doc
|       |   |           \---mybatis
|       |   \---META-INF
|       +---generated-sources
|       |   \---annotations
|       +---generated-test-sources
|       |   \---test-annotations
|       +---sql_client_tools
|       |   +---app
|       |   |   \---appserver
|       |   +---dto
|       |   +---mapper
|       |   +---model
|       |   +---openapi
|       |   |   +---dto
|       |   |   \---impl
|       |   +---service
|       |   |   \---impl
|       |   +---test
|       |   |   +---mapper
|       |   |   +---openapi
|       |   |   \---service
|       |   \---web
|       |       \---aries
|       \---test-classes
|           +---com
|           |   \---jiangli
|           |       +---commons
|           |       |   \---client
|           |       |       +---tools
|           |       |       |   \---mybatis
|           |       |       \---utils
|           |       \---doc
|           |           \---mybatis
|           \---META-INF
\---junit-extension
    +---doc
    \---src
        +---main
        |   \---java
        |       \---com
        |           \---jiangli
        |               \---commons
        |                   \---jtest
        |                       \---core
        |                           +---data
        |                           |   \---handler
        |                           \---group
        |                               \---invoker
        \---test
            +---java
            |   \---com
            |       \---jiangli
            |           \---commons
            |               \---jtest
            |                   \---core
            |                       \---test
            \---resources


```

