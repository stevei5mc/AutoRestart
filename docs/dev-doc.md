[返回](../README.md)
# **AutoRestart 开发文档**
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
---
#### **运行以秒为单位的重启任务**
```java
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(Utils.getRestartTipTime(),2);
    }
```
---
#### **运行无在线玩家时自动重启的任务**
```java
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(3);
    }
```
---
#### **取消重启任务**
```java
    public void cancel() {
        Utils.cancelTask();
    }
```
---