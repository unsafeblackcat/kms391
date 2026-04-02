# kms391
Korea MapleStory v391 服务端代码

# ~~暂停!~~ 放弃!! 祝大家玩的开心!!!
通过网盘分享的文件：kms391   
链接: https://pan.baidu.com/s/1cjp1JTTJlEaX93iEg5bhaA?pwd=dnkm 提取码: dnkm    
--来自百度网盘超级会员v7的分享   

## 03.30
彻底放弃了。尝试把服务端的 server\wz\Quest.wz 反推回 Korea.MapleStory.v391\Data\Quest后客户端会启动失败。

对比下台服268的客户端任务目录MapleStory.v268\Data\Quest\_Canvas, 应该是缺失了这个文件夹以及里面的内容。导致部分数据不完整让客户端启动失败。

本来想着用kms379的客户端中的Quest覆盖到kms391。结果379也是一个卵样发布出来的客户端也没有任务。甚至找网上一个韩国人开服的kms379私服，他们的客户端中也没有Quest。真是操蛋了不知道发布者为啥要把任务给删掉。。。。。

这个端算是彻底放弃了。    
![](img/tq.gif)

## 03.29
因为韩服客户端中没有Quest.wz, 所以玩家做不了任何任务。

虽然根据群内大佬说可以通过Server端的xml转回Quest.wz

我实在太懒了, 也没有人让我白嫖。

经过考虑，准备投入到TMS268上去折腾。 TMS268是25年2月的端，对应的是KMS392版本。

## 遗留问题
1. 虽然可以使用聊天消息输入中文，但是接收方很大概率收到消息乱码
2. 韩服客户端没有任务WZ文件，需要根据上面的“方法”去尝试去做转换。  

# 服务端部署
1. 安装JDK21: https://www.oracle.com/java/technologies/downloads/#java21
2. 安装mariadb-11.8.6: https://mariadb.org/download/?t=mariadb&o=true&p=mariadb&r=11.8.6&os=windows&cpu=x86_64&pkg=msi&mirror=yamagata-university
3. 安装Navicat: https://www.navicat.com.cn/download/navicat-premium-lite
4. Navicat连接mariadb的数据库
5. 新建数据库名称 kms391, 字符集 utf8mb4 utf-8 unicode 
6. kms391数据库, 运行sql文件, 选择: server/sql/kms391.sql 
7. 打开server\Properties\world.properties
8. 修改world.gateway.ip=当前机器IP
9. 修改channel.net.interface=当前机器IP
10. start.bat 启动服务端
11. stop.bat  停止服务端

# 客户端部署
1. 解压Korea.MapleStory.v391客户端. 游戏客户端路径中不要带有中文!
2. 将MapleStoryEx.dll, MapleStoryStart.exe和config.ini放置Korea.MapleStory.v391客户端根目录.
3. 记事本打开Korea.MapleStory.v391\config.ini
4. ip=127.0.0.1,修改为服务端IP, 如果是当前机器安装的服务端和数据库则跳过步骤.
5. MapleStoryStart.exe启动游戏

# 服务端构建和打包命令
1. 文件 -> 项目结构 -> 项目 -> SDK: 21
2. idea打开工程 -> 重新加载所有Maven项目 -> 同步所有Maven项目
3. 执行Mave目标: ：mvn clean package dependency:copy-dependencies -DincludeScope=runtime
4. release.pack.bat 打包服务端发布需要的所有文件

# 来源 

## server
原始来源: https://forum.ragezone.com/threads/kms-v391-source-client.1232393/         

覆盖来源: https://forum.ragezone.com/threads/maplestory-kms-391-6th-job-skills-new-map-tao-yuan-new-classes-lien-kali-shanghua-yin-yue-class-updates-one-key-launcher-no-virtual-m.1262437/       

## MapleStoryEx.dll
https://github.com/InWILL/Locale_Remulator
