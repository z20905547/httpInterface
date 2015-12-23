## 文件夹说明

该文件是 base-framework 用于通过 bat 文件执行一些批处理内容的文件夹，主要的目的是通过某些 bat 文件直接一键完成某些功能的作用。

## 文件说明

**install.bat:** 该文件起到初始化工程，执行该文件时，会将 base-framework 写入到本地 m2 库，接着初始化数据库已经创建 project archetype 和运行 项目等操作。

**reset-database.bat** 该文件是为了出现操作错误或删除不该删除的数据时，将 h2 数据重置成初始化状态的作用。