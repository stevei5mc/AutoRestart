# **AutoRestart 自动重启**
[![GitHub License](https://img.shields.io/github/license/stevei5mc/AutoRestart?style=plastic)](LICENSE)
![GitHub top language](https://img.shields.io/github/languages/top/stevei5mc/AutoRestart?style=plastic)
[![GitHub Release](https://img.shields.io/github/v/release/stevei5mc/AutoRestart?style=plastic&color=drak%20green)](https://github.com/stevei5mc/AutoRestart/releases)  
![GitHub Repo stars](https://img.shields.io/github/stars/stevei5mc/AutoRestart?style=plastic)
![GitHub forks](https://img.shields.io/github/forks/stevei5mc/AutoRestart?style=plastic)
![GitHub issues](https://img.shields.io/github/issues/stevei5mc/AutoRestart?style=plastic&color=linkGreen)
![GitHub pull requests](https://img.shields.io/github/issues-pr/stevei5mc/AutoRestart?style=plastic)
## **插件介绍**
### **功能介绍**
- [x] **多语言（根据玩家客户端的语言进行匹配）**
- [x] **播放音效提醒玩家（暂时只支持mc原有的）**
- [x] **取消自动重启任务**
- [x] **暂停任务**
### **任务类型**
- [x] **自动重启**
- [x] **手动重启（手动重启的时间为配置文件中的提示时间）**
- [x] **服务器无人时自动重启**
- [x] **投票重启**
- [x] **定时重启**
### **命令与权限**
<table>
    <tr align="center" valign="center">
        <th>命令</th><th>权限节点</th><th>命令/权限介绍</th><th>权限归属</th>
    </tr>
    <tr>
        <td>/autorestart</td><td>autorestart.admin</td><td>主命令、打开GUI</td><td rowspan="5" align="center" valign="center">OP</td>
    </tr>
    <tr>
        <td>/autorestart reload</td><td>autorestart.admin.reload</td><td>重载配置文件</td>
    </tr>
    <tr>
        <td>/autorestart cancel</td><td>autorestart.admin.cancel</td><td>取消重启任务</td>
    </tr>
    <tr>
      <td>/autorestart pause</td><td>autorestart.admin.pause</td><td>暂停/继续运行重启任务</td>
    </tr>
    <tr>
      <td>/autorestart restart [任务类型] (时间单位) (时间)</td><td>autorestart.admin.restart</td><td>手动重启服务器</td>
    </tr>
    <tr>
      <td>/voterestart</td><td rowspan="5">autorestart.user.vote</td><td>主命令、打开GUI</td><td rowspan="5" align="center" valign="center">player</td>
    </tr>
    <tr>
      <td>/voterestart initiate</td><td>发起投票</td>
    </tr>
    <tr>
      <td>/voterestart vote approval</td><td>投出赞成票</td>
    </tr>
    <tr>
      <td>/voterestart vote oppose</td><td>投出反对票</td>
    </tr>
    <tr>
      <td>/voterestart vote abstention</td><td>投出弃权票</td>
    </tr>
    <tr>
      <td>/voterestart vote veto</td><td>autorestart.admin.vote.veto</td><td>使用一票否决</td><td rowspan="2" align="center" valign="center">OP</td>
    </tr>
    <tr>
      <td></td><td>autorestart.admin.vote.force</td><td>在调试模式中强制发起投票<br>该功能方便开发而设立的(需开启debug模式)</td>
    </tr>
</table>

#### **命令参数讲解**
- **[ ]  内的参数必填**
- **（ ） 内的参数选填**
##### **任务类型**
- `manual` **手动重启**
- `no-players` **无人时重启**
- `scheduled` **定时重启**
##### **时间单位**
- `hour` **小时**
- `minutes` **分钟**
- `seconds` **秒**

### **配置文件介绍**
```yml
#配置文件版本（勿动!!!）
version: 7
#默认语言
default_language: zh_CN
#重启时间(分钟 min)
restart_time: 180
#提示时间(秒 s)
tips_time: 30
#是否在重启前把玩家踢出
kick_player: true
#一些显示的设置
#显示title(包括subtitle)
show_title: true
#底部显示(在物品栏上方)
show_tip: true
#是否播放音效
play_sound: true
sound:
  name: "random.toast"
  volume: 1.0
  pitch: 0.5
#重启前执行的命令(&con为控制台执行 @p 代表玩家名)
runcommand: true
commands:
  - "help"
  - "say hello @p&con"
#发起重启投票的最低人数（最低只能为3）
vote_start_player: 3
#投票的时间限制（最高只能为5）
vote_time: 5
#调试模式
debug: false
#消息前缀
message_prefix: "§l§bAutoRestart §r§7>> "
#本地化语言文件（开启后语言文件才会保存至插件对应文件及其会使用保存至插件文件夹的语言文件）
local_language_flies: false
#自动更新语言文件（更新语言文件是使用覆盖式更新，旧版配置并不会保存）
auto_update_language_files: false
#显示投票状态
prompt_voting_status: true
#显示投票状态的类型
# 0 = ActionBar，1 = tip，2 = popup
prompt_type: 0
```
### **支持的语言**
- **顺序按照支持的顺序排序**
- [x] zh_CN   中文(简体)
- [x] zh_TW   中文(繁體)
- [x] en_US   English (United States)

## **使用方法**
### **注意事项**
- **自动重启还需要脚本的配合才能实现相关脚本在[Actions](https://github.com/stevei5mc/NewTipsVariables/actions)编译完成后会一并给出，你也可以到对应[GITHUB 仓库](https://github.com/stevei5mc/McStartServer)获取，[Releases](https://github.com/stevei5mc/AutoRestart/releases)中也可以获取得到但只会在版本发布后才会有相关脚本(Windows用.bat后缀的脚本，Linux请用.sh后缀的脚本，另外每种系统的脚本都有两种根据你的需求选择)，如果你已经有了相关脚本则可以忽略**
- **本插件的重启时间会受服务器TPS导致重启时间不会按现实的时间进行而是按照服务器TPS的计算的时间进行的**
- **在使用时请不要随意改动语言文件或配置文件的版本号，否则所带来的后果一切自负！！！！！！**
- **默认情况下使用的语言文件为内置的语言文件**

### **脚本使用说明**
- **此内容使用于配套的启动脚本，可配置项已在下方列出**
- **如果你没有相关知识，请不要修改可配置项以外的地方**
- **可配置项是在`=`后面的，例如`jvm_ram_xms="512M"`的`512M`就为可配置项, 注：`""`可加可不加，如果无法运行可尝试加上`""`**

<details>
<summary>Windows</summary>

```bat
::设置用于启动的java版本 (默认填 java，当然你也可以填java的安装路径)
set java_version=java
::服务器名提示
set server_name=test
::设置最小内存
set ram_Xms=512m
::设置最大内存
set ram_Xmx=1024m
::设置开服核心名(开服核心名不用加.jar的后缀名)
set server_jar_name=server
::设置重启等待时间
set restart_time=20
```
</details>

<details>
<summary>Linux</summary>

```sh
ReStart_Time=5s  #等待多少秒后重启要加时间单位
jvm_ram_xms="512M"  #设置最小内存
jvm_ram_xmx="1024M"  #设置最大内存
jar_name="server.jar"  #开服核心的名字记得加.jar后缀名
server_name="testmc"  #服务名(方便维护用的)，比如说是生存服就将testmc改为生存服
```

```sh
ReStart_Time=20s  #等待多少秒后重启要加时间单位
jvm_ram_xms="512M"  #设置最小内存
jvm_ram_xmx="1024M"  #设置最大内存
jar_name="server.jar"  #开服核心的名字记得加.jar后缀名
```
</details>


|步骤|说明|
|:-:|:-|
|1|准备好相关的启动脚本并放置在服务端的根目录下(一般情况下启动脚本是放置在服务端的根目录下)并调整好启动脚本的配置(如果你有相关的启动脚本则无视这一步骤)|
|2|将本插件放进`plugins`文件夹，并确保安装前置插件[MemoriesOfTime-GameCore](https://motci.cn/job/GameCore/)后启动服务器 <br> 安装前置插件[Tips](https://motci.cn/job/Tips/)对接的变量生效（这是可选的）|
|3|如果不使用默认配置则修改好配置文件后重启服务器让新的配置生效（由于自动重启任务的配置读取是在服务器启动阶段进行的，所以无法通过重载配置文件的操作让新的配置完全生效只能通过重启服务器来让新的配置生效）|
## **效果预览**
|![1](docs/image/1.jpg)|![2](docs/image/2.jpg)|![3](docs/image/3.jpg)|
|-|-|-|
|![4](docs/image/4.jpg)|![5](docs/image/5.jpg)|![6](docs/image/6.jpg)|
|![7](docs/image/7.jpg)|![8](docs/image/8.jpg)|![9](docs/image/9.jpg)|
|![10](docs/image/10.jpg)|![11](docs/image/11.jpg)||
## **变量信息**
|变量|介绍|
|:-:|:-:|
|{restart-remainder}|获取重启任务的剩余时间|
|{vote-data}|获取投票重启的数据|

## **开发文档**
- **请在[Releases](https://github.com/stevei5mc/AutoRestart/releases)界面下载`-javadoc.jar`结尾的文件解压后点击`index.html`获取相关文档**
### **Maven**
[![](https://jitpack.io/v/stevei5mc/AutoRestart.svg)](https://jitpack.io/#stevei5mc/AutoRestart/1.0.0-beta6)
```xml
<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
	<groupId>com.github.stevei5mc</groupId>
	<artifactId>AutoRestart</artifactId>
	<version>1.0.0-beta6</version>
</dependency>
```