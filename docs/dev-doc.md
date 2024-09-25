[返回](../README.md)
# **AutoRestart 开发文档**
- **本文档的内容还处于编写阶段，内容还需要完善**

## **ID类型**
- **这里ID仅是为了方便开发而分配的**

|时间ID|时间类型|任务ID|任务类型|
|:-:|:-:|:-:|:-:|
|1|分钟|1|以分钟为单为的重启任务|
|2|秒|2|以秒为单位的重启任务|
|3|时|3|无在线玩家时重启|

### **示例代码**
- **这里给出的仅是示例代码，使用时需要根据需求进行调整**
---
#### **运行以分钟为单位的重启任务**
```java
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(Utils.getRestartUseTime(),1);
    }
```
#### **运行以秒为单位的重启任务**
```java
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(Utils.getRestartTipTime(),2);
    }
```
#### **运行无在线玩家时自动重启的任务**
```java
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(3);
    }
```
#### **取消重启任务**
```java
    public void cancel() {
        Utils.cancelTask();
    }
```
#### **讲解**
```
Utils
    - runRestartTask(int type) 运行重启任务
    - runRestartTask(int restartTime,int type) 运行重启任务
    - getRestartUseTime() 获取重启任务的重启时间
    - getRestartTipTime() 获取提示时间,在还剩多少秒的时候通知玩家
    - cancelTask() 取消重启任务
    - getRemainder(Player player) 获取剩余时间
    - runVoteTask() 发起重启任务
    - cancelVoteTask() 取消重启任务
```
---