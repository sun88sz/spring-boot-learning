package com.sun;

/**
 * @author : Sun
 * @date : 2018/11/9 15:21
 */

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

/**
 * TASK基础类
 *
 * @author ryan
 */
@Data
public class TaskNode {
    /**
     * 任务编号
     */
    private String taskNumber;
    /**
     * 任务之间的逻辑关系
     */
    private String logic;
    //最早开始时间  
    private double earlyStartTime;
    //最早结束时间  
    private double earlyFinishTime;
    //最晚开始时间 
    private double lateStartTime;
    //最晚结束时间  
    private double lateFinishTime;
    //执行时间  
    private double dut;
    //延迟时间  
    private double delayTime;
    //机动时间  
    private double slack;

    private String[] logicArray;//任务之间的逻辑关系  
    private double[] earlyStartTimeArray;//最早开始时间  
    private double[] earlyFinishTimeArray;//最早结束时间  
    private double[] lateStartTimeArray;//最晚开始时间  
    private double[] lateFinishTimeArray;//最晚结束时间  
    private double[] dutArray;//执行时间  
    private double[] delayTimeArray;//延迟时间  
    private double[] slackArray;//机动时间  

    private boolean isCalEST = false;//是否计算了最早开始时间  
    private boolean isCalEFT = false;//是否计算了最早结束时间  
    private boolean isCalLST = false;//是否计算了最晚开始时间  
    private boolean isCalLFT = false;//是否计算了最晚结束时间  
    private boolean isCalSlack = false;//是否计算了机动时间  

    private boolean isCalETArray = false;//是否计算了最早开始时间  
    private boolean isCalLTArray = false;//是否计算了最晚开始时间  
    private boolean isCalSlackArray = false;//是否计算了机动时间  

    private boolean isCriticalPath = false;//是否是关键路径  

    private List<TaskNode> previousTaskNodes = new LinkedList<>();//前置任务集合  
    private List<TaskNode> nextTaskNodes = new LinkedList<>();//后置任务集合  

    /*
     * 计算最早开始时间
     */
    public void calculateET() {
        if (!this.isCalEST()) {
            //临时存放最早开始时间  
            double est = 0.0d;
            //标记是否执行了逻辑关系中的代码  
            boolean isTmp = false;
            //第一个任务是没有前置任务的，所以其最早开始时间是0  
            if (this.getPreviousTaskNodes().size() == 0) {
                this.earlyStartTime = est;
                this.isCalEST = true;
            } else {
                // ES= max{ES(前)+ Dur(前)+ FTS}  
                if ("FS".equals(logic)) {
                    for (TaskNode previousTaskNode : this.getPreviousTaskNodes()) {
                        if (previousTaskNode.getEarlyFinishTime() > est && previousTaskNode.isCalEFT()) {

                            est = previousTaskNode.getEarlyFinishTime();
                            isTmp = previousTaskNode.isCalEFT();
                        }
                    }
                    est = est + this.getDelayTime();
                }
                // ES= max{ES(前)+ Dur(前) - Dur + FTF}
                else if ("FF".equals(logic)) {
                    for (TaskNode previousTaskNode : this.getPreviousTaskNodes()) {
                        if (previousTaskNode.getEarlyFinishTime() > est && previousTaskNode.isCalEFT()) {
                            est = previousTaskNode.getEarlyFinishTime();
                            isTmp = previousTaskNode.isCalEFT();
                        }
                    }
                    est = est + this.getDelayTime() - this.getDut();
                }
                // ES=max{ ES(前) + STS}  
                else if ("SS".equals(logic)) {
                    for (TaskNode previousTaskNode : this.getPreviousTaskNodes()) {
                        if (previousTaskNode.getEarlyStartTime() > est && previousTaskNode.isCalEST()) {
                            est = previousTaskNode.getEarlyStartTime();
                            isTmp = previousTaskNode.isCalEST();
                        }
                    }
                    est = est + this.getDelayTime();
                }
                // ES=max{ ES(前) - Dur + STF}  
                else if ("SF".equals(logic)) {
                    for (TaskNode previousTaskNode : this.getPreviousTaskNodes()) {
                        if (previousTaskNode.getEarlyStartTime() > est && previousTaskNode.isCalEST()) {
                            est = previousTaskNode.getEarlyStartTime();
                            isTmp = previousTaskNode.isCalEST();
                        }
                    }
                    est = est - this.getDut() + this.getDelayTime();
                }
                if (isTmp) {
                    this.earlyStartTime = est;
                    this.isCalEST = true;
                }
            }
        }
        if (!this.isCalEFT() && this.isCalEST()) {
            this.earlyFinishTime = this.getEarlyStartTime() + this.getDut();
            this.isCalEFT = true;
        }
    }

    /**
     * 计算最晚时间
     */
    public void calculateLT() {
        if (!this.isCalLST()) {
            calculateLT(this.nextTaskNodes, this);
        }
        if (!this.isCalLFT()) {
            calculateLT(this.nextTaskNodes, this);
        }
    }


    /**
     * task的后置任务nextTasks
     */
    public void calculateLT(List<TaskNode> nextTaskNodes, TaskNode taskNode) {
        //临时时间差  
        double tmpSlack = 0.0d;
        //标记
        boolean isTmp = false;
        if (nextTaskNodes.size() == 0) {
            if (taskNode.isCalEST() && taskNode.isCalEFT()) {
                taskNode.lateFinishTime = taskNode.getEarlyFinishTime();
                taskNode.slack = 0.0d;
                taskNode.isCalLFT = true;
                taskNode.isCalSlack = true;
                taskNode.lateStartTime = taskNode.getEarlyStartTime();
                taskNode.slack = 0.0d;
                taskNode.isCalLST = true;
                taskNode.isCalSlack = true;
            }
        } else {
            for (int i = 0; i < nextTaskNodes.size(); i++) {
                TaskNode nextTaskNode = nextTaskNodes.get(i);
                if (!nextTaskNode.isCalLFT()) {
                    return;
                }
                if (!nextTaskNode.isCalLST()) {
                    return;
                }
                if (nextTaskNode.isCalSlack) {
                    // 临时时间间隔  
                    double tmp = tmpSlack;
                    // Slack = min{slack后+ES后-EF-FTS}  
                    if ("FS".equals(nextTaskNode.logic) && nextTaskNode.isCalEST() && taskNode.isCalEFT()) {
                        tmp = nextTaskNode.getSlack() + nextTaskNode.getEarlyStartTime() - taskNode.getEarlyFinishTime() - nextTaskNode.getDelayTime();
                        isTmp = true;
                    }
                    // Slack = min{slack后+EF后-EF-FTF}  
                    else if ("FF".equals(nextTaskNode.logic) && nextTaskNode.isCalEFT() && taskNode.isCalEFT()) {
                        tmp = nextTaskNode.getSlack() + nextTaskNode.getEarlyFinishTime() - taskNode.getEarlyFinishTime() - nextTaskNode.getDelayTime();
                        isTmp = true;
                    }
                    // Slack = min｛slack后+EF后-ES-STF｝  
                    else if ("SF".equals(nextTaskNode.logic) && nextTaskNode.isCalEFT() && taskNode.isCalEST()) {
                        tmp = nextTaskNode.getSlack() + nextTaskNode.getEarlyFinishTime() - taskNode.getEarlyStartTime() - nextTaskNode.getDelayTime();
                        isTmp = true;
                    }
                    // Slack = min{slack后+ES后-ES-STS}  
                    else if ("SS".equals(nextTaskNode.logic) && nextTaskNode.isCalEST() && taskNode.isCalEST()) {
                        tmp = nextTaskNode.getSlack() + nextTaskNode.getEarlyStartTime() - taskNode.getEarlyStartTime() - nextTaskNode.getDelayTime();
                        isTmp = true;
                    }
                    if (i == 0) {
                        tmpSlack = tmp;
                    }
                    if (tmp < tmpSlack) {
                        tmpSlack = tmp;
                    }
                }
            }

        }
        //isTmp标记为true，说明已经经过计算。
        if (isTmp && taskNode.isCalEST() && taskNode.isCalEFT()) {
            taskNode.lateFinishTime = taskNode.getEarlyFinishTime() + tmpSlack;
            taskNode.setSlack(tmpSlack);
            taskNode.isCalLFT = true;
            taskNode.isCalSlack = true;
            taskNode.lateStartTime = taskNode.getEarlyStartTime() + tmpSlack;
            taskNode.setSlack(tmpSlack);
            taskNode.isCalLST = true;
            taskNode.isCalSlack = true;

        }
    }

    /**
     * 计算最早开始和最早结束时间
     */
    public void calculateETArray() {
        if (!this.isCalETArray()) {
            double[] dutArray = this.getDutArray();
            double[] delayTimeArray = this.getDelayTimeArray();
            String[] logicArray = this.getLogicArray();
            double[] earlyStartTimeArray = new double[dutArray.length];
            double[] earlyFinishTimeArray = new double[dutArray.length];
            int ETCount = 0;
            for (int i = 0; i < dutArray.length; i++) {
                this.setDelayTime(delayTimeArray[i]);
                this.setLogic(logicArray[i]);
                this.setDut(dutArray[i]);
                this.calculateET();
                earlyStartTimeArray[i] = this.getEarlyStartTime();
                earlyFinishTimeArray[i] = this.getEarlyFinishTime();
                ETCount++;

            }
            if (ETCount == dutArray.length) {
                this.setEarlyStartTimeArray(earlyStartTimeArray);
                this.setEarlyFinishTimeArray(earlyFinishTimeArray);
                this.setCalETArray(true);
            }
        }
    }

    /**
     * 计算最晚开始和最晚结束时间
     */
    public void calculateLTArray() {
        if (!this.isCalLTArray()) {
            double[] dutArray = this.getDutArray();
            double[] delayTimeArray = this.getDelayTimeArray();
            String[] logicArray = this.getLogicArray();
            double[] lateStartTimeArray = new double[dutArray.length];
            double[] lateFinishTimeArray = new double[dutArray.length];
            int ltCount = 0;
            for (int i = 0; i < dutArray.length; i++) {
                this.setDelayTime(delayTimeArray[i]);
                this.setLogic(logicArray[i]);
                this.setDut(dutArray[i]);
                this.calculateLT();
                lateStartTimeArray[i] = this.getLateStartTime();
                lateFinishTimeArray[i] = this.getLateFinishTime();
                ltCount++;
            }

            if (ltCount == dutArray.length) {
                this.setLateStartTimeArray(lateStartTimeArray);
                this.setLateFinishTimeArray(lateFinishTimeArray);
                this.setCalLTArray(true);
            }
        }
    }

    /**
     * taskNumber 任务编号
     * logic 与前置任务的逻辑关系
     * dut 任务执行时间
     * delayTime 提前滞后时间
     */
    public TaskNode(String taskNumber, String logic, double dut, double delayTime) {
        super();
        this.taskNumber = taskNumber;
        this.logic = logic;
        this.dut = dut;
        this.delayTime = delayTime;
    }

    /**
     * taskNumber 任务编号
     * logic 与前置任务的逻辑关系
     * dut 任务执行时间数组
     * delayTime 提前滞后时间数组
     */
    public TaskNode(String taskNumber, String[] logicArray, double[] dutArray, double[] delayTimeArray) {
        super();
        this.taskNumber = taskNumber;
        this.logicArray = logicArray;
        this.dutArray = dutArray;
        this.delayTimeArray = delayTimeArray;
    }

    public void setPreviousTaskNodes(List<TaskNode> previousTaskNodes) {
        this.previousTaskNodes = previousTaskNodes;
        for (TaskNode taskNode : this.previousTaskNodes) {
            this.getNextTaskNodes().add(this);
        }
    }

    public void setCriticalPath() {
        if (this.isCalLST() && this.isCalEST()) {
            if (this.getLateStartTime() - this.getEarlyStartTime() == 0) {
                this.isCriticalPath = true;
            }
        }
    }

    public void setCriticalPathArray() {
        if (this.isCalLTArray() && this.isCalETArray()) {
            //TODO 待完成  
        }
    }


}  
