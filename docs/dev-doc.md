[返回](../README.md)
# **AutoRestart 开发文档**
- **本文档的内容还处于编写阶段，内容还需要完善**

## **ID类型**
- **这里ID仅是为了方便开发而分配的**

|时间ID|时间类型|
|:-:|:-:|
|1|分钟|
|2|秒|
|3|时|

|任务ID|任务类型|
|:-:|:-:|
|1|以分钟为单为的重启任务|
|2|以秒为单位的重启任务|
|3|无在线玩家时重启|

- **现在只对重启任务使用**

|任务状态ID|状态说明|
|:-:|:-:|
|0|停止|
|1|运行|
|2|暂停|
## **示例代码**
- **这里给出的仅是示例代码并不能保证能够运行，在使用时需要根据需求进行调整**
---
```java
    // 运行以分钟为单位的重启任务**
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(Utils.getRestartUseTime(),1,1);
    }

    //运行以秒为单位的重启任务
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(Utils.getRestartTipTime(),2,2);
    }

    //运行无在线玩家时自动重启的任务
    public void runRestartTimeMinutes() {
        Utils.runRestartTask(3);
    }

    // 取消重启任务
    public void cancel() {
        Utils.cancelTask();
    }
```
## **讲解**
### **TasksUtils**
```
    runRestartTask(int taskType) 运行重启任务
    runRestartTask(int restartTime,int taskType,int timeUnit) 运行重启任务
    runVoteTask(Player vote) 发起投票重启任务
    cancelVoteTask() 取消重启任务
    cancelRestartTask() 取消重启任务
    pauseRestartTask() 暂停任务
    continueRunRestartTask() 继续运行任务
    restartTaskState 重启任务的状态
    voteTaskState 投票任务的状态
```

### **BaseUtils**
```
    getRestartUseTime() 获取重启任务的重启时间
    getRestartTipTime() 获取提示时间,在还剩多少秒的时候通知玩家
    getRemainder(Player player) 获取剩余时间
```

### **VoteUtils**
```
    getApproval(); 获取赞成票数
    getOppose(); 获取反对票数
    getAbstention(); 获取弃权票数
    getApprovalVotes(); 获取需要的赞成票数
    initializedData(CommandSender sender); 投票任务初始化
    processVotingContent(CommandSender sender, String voteContent); 处理投票结果
```
---