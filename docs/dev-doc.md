[返回](../README.md)
# **AutoRestart 开发文档**
- **本文档的内容还处于编写阶段，内容还需要完善**

## **任务类型**
- **这里ID仅是为了方便开发而分配的，并不是实际的任务ID**

|ID|类型|需要参数|备注|
|:-:|:-:|:-:|:-:|
|1|以分钟为单为的重启任务|time||
|2|以秒为单位的重启任务|time||
|3|无在线玩家时重启|||

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
```java
    Utils.runRestartTask(); // 运行重启任务
    Utils.getRestartUseTime(); // 获取重启任务的重启时间
    Utils.getRestartTipTime(); // 获取提示时间（在还剩多少秒的时候通知玩家）
    Utils.cancelTask(); // 取消重启任务
```
---