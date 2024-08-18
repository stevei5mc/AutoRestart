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
1. **支持多语言（根据玩家客户端的语言进行匹配）**
2. **支持播放音效提醒玩家（暂时只支持mc原有的）**
3. **支持取消自动重启任务**
4. **支持多种重启任务(查看任务类型获取现在支持的重启任务)，更多类型敬请期待**
### **任务类型**
- [x] **定时重启**
- [x] **手动重启（手动重启的时间为配置文件中的提示时间）**
- [x] **服务器无人时自动重启**
### **命令与权限**
|命令|权限节点|命令/权限介绍|默认权限|
|:-:|:-:|:-:|:-:|
|/autorestart|autorestart.admin|主命令、打开GUI|OP|
|/autorestart reload|autorestart.admin.reload|重载配置文件|OP|
|/autorestart cancel|autorestart.admin.cancel|取消重启任务|OP|
|/autorestart restart manual|autorestart.admin.restart|手动重启服务器|OP|
|/autorestart restart no-player|autorestart.admin.restart|在服务器没有玩家在线时自动重启服务器|OP|
- **任务类型介绍：**
- **manual 手动重启时间为配置文件中`tips_time`设定的时间**
- **no-player 在服务器没有玩家在线的时自动候重启服务器**
### **配置文件介绍**
```yml
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
  pitch: 1.0
#重启前执行的命令(&con为控制台执行 @p 代表玩家名)
runcommand: true
commands:
  - "say hello world&con"
  - "help"
  - "say hello @p&con"
```
### **支持的语言**
- **顺序按照支持的顺序排序**
- [x] zh_CN   中文(简体)
- [x] zh_TW   中文(繁體)
- [x] en_US   English (United States)
## **使用方法**
1. **将插件放进`plugins`文件夹，并确保安装[MemoriesOfTime-GameCore](https://motci.cn/job/GameCore/)插件后重启服务器**  
2. **启动(重启)服务器并调整好配置(如何配置请看插件介绍)，并重启服务器**
3. **自动重启还需要脚本的配合才能实现相关脚本在[Actions](https://github.com/stevei5mc/NewTipsVariables/actions)编译完成后会一并给出，你也可以到对应[仓库](https://github.com/stevei5mc/McStartServer)获取，[Releases](https://github.com/stevei5mc/AutoRestart/releases)中也可以获取得到但只会在版本发布后才会有相关脚本(Windows用.bat后缀的脚本，Linux请用.sh后缀的脚本，另外每种系统的脚本都有两个根据你的需求选择)，如果你已经有了相关脚本则可以忽略这一步骤**
## **效果预览**
|![1](docs/image/1.jpg)|![2](docs/image/2.jpg)|
|-|-|
|![3](docs/image/3.jpg)|![4](docs/image/4.jpg)|
|![5](docs/image/5.jpg)|![6](docs/image/6.jpg)|
## **[开发文档](docs/dev-doc.md)**