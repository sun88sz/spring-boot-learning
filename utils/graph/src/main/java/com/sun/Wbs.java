package com.sun;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Task的计算逻辑类
 *
 * @author ryan
 */
public class Wbs {
    private List<TaskNode> taskNodes;//所有任务集合  
    private Set<TaskNode> noPreviousTaskNodes;//没有前置任务的任务集合  
    private Set<TaskNode> noNextTaskNodes;//没有后置任务的任务集合  


    /**
     * @param taskNodes 所有任务集合
     * @return 没有前置任务的任务集合
     */
    public Set<TaskNode> getNoPreviousTasks(List<TaskNode> taskNodes) {
        if (taskNodes != null && taskNodes.size() > 0) {
            for (TaskNode taskNode : taskNodes) {
                if (taskNode != null && taskNode.getPreviousTaskNodes().size() == 0) {
                    this.noPreviousTaskNodes.add(taskNode);
                }
            }
        }
        return this.noPreviousTaskNodes;
    }

    /**
     * @param taskNodes 所有任务集合
     * @return 没有后置任务的任务集合
     */
    public Set<TaskNode> getNoNextTasks(List<TaskNode> taskNodes) {
        if (taskNodes != null && taskNodes.size() > 0) {
            for (TaskNode taskNode : taskNodes) {
                if (taskNode != null && taskNode.getNextTaskNodes().size() == 0) {
                    this.noNextTaskNodes.add(taskNode);
                }
            }
        }
        return this.noNextTaskNodes;
    }


    /**
     * @param taskNodes 所有任务
     * @return 没有前置任务计算完成之后，待计算的任务
     */
    private Set<TaskNode> calculateNoPreviousEarlyTime(List<TaskNode> taskNodes) {
        //  System.out.println("开始调用calculateNoPreviousEarlyTime(List<TaskNode> taskNodes)------");  
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (taskNodes != null && taskNodes.size() > 0) {
            Set<TaskNode> noPreviousTaskNodes = this.getNoPreviousTasks(taskNodes);
            for (TaskNode taskNode : noPreviousTaskNodes) {
                //      System.out.println("没有前置任务的是"+taskNode.getTaskNumber());  
                if (!taskNode.isCalEST() && !taskNode.isCalEFT())
                    taskNode.calculateET();
                calTaskNodes.add(taskNode);
            }
        }
        //  System.out.println("结束调用calculateNoPreviousEarlyTime(List<TaskNode> taskNodes)------");  
        return calTaskNodes;
    }

    /**
     * @param nextTaskNodes 本次待计算的任务
     * @return 下一次待计算的任务
     */
    public Set<TaskNode> calculateEarlyTime(Set<TaskNode> nextTaskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (nextTaskNodes != null && nextTaskNodes.size() > 0) {
            Iterator<TaskNode> it = nextTaskNodes.iterator();
            while (it.hasNext()) {
                TaskNode taskNode = it.next();
                //      System.out.println("本次需要计算的任务是"+taskNode.getTaskNumber());  
                //找到本任务的前置任务看是不是全部计算完成了，  
                List<TaskNode> befTaskNode = taskNode.getPreviousTaskNodes();
                //判断本任务的前置任务是不是完全计算完成的计数器  
                int count = 0;//计数器  
                for (int i = 0; i < befTaskNode.size(); i++) {
                    TaskNode bfTaskNode = befTaskNode.get(i);
                    if (bfTaskNode.isCalEST() && bfTaskNode.isCalEFT())
                        count++;
                }
                //如果全部计算完成，将本任务的后置任务加入到返回列表里  
                if (count == befTaskNode.size()) {
                    taskNode.calculateET();
                    //          System.out.println("已经计算了"+taskNode.getTaskNumber());  
                    calTaskNodes.addAll(taskNode.getNextTaskNodes());
                    //如果没有全部计算完成，把本任务加入到计算列表里  
                } else {
                    calTaskNodes.add(taskNode);
                }
            }
        }
        return calTaskNodes;
    }

    /**
     * @param taskNodes 所有任务
     * @return 没有前置任务的下一层需要计算的任务
     */
    public Set<TaskNode> calNoNextTasksLateTime(List<TaskNode> taskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (taskNodes != null && taskNodes.size() > 0) {
            for (TaskNode taskNode : this.getNoNextTasks(taskNodes)) {
                if (!taskNode.isCalLST() && !taskNode.isCalLFT())
                    taskNode.calculateLT();
                calTaskNodes.add(taskNode);
            }
        }
        return calTaskNodes;
    }

    /**
     * @param nextTaskNodes 本次待计算的任务
     * @return 下一次需要计算的任务
     */
    public Set<TaskNode> calculateLateTime(Set<TaskNode> nextTaskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (nextTaskNodes != null && nextTaskNodes.size() > 0) {
            Iterator<TaskNode> it = nextTaskNodes.iterator();
            while (it.hasNext()) {
                TaskNode taskNode = it.next();
                //找到本任务的后置任务看是不是全部计算完成了，  
                List<TaskNode> nextTaskNode = taskNode.getNextTaskNodes();
                //判断本任务的后置任务是不是完全计算完成的计数器  
                int count = 0;//计数器  
                for (int i = 0; i < nextTaskNode.size(); i++) {
                    TaskNode ntTaskNode = nextTaskNode.get(i);
                    if (ntTaskNode.isCalLST() && ntTaskNode.isCalLFT())
                        count++;
                }
                //如果全部计算完成，将本任务的后置任务加入到返回列表里  
                if (count == nextTaskNode.size()) {
                    taskNode.calculateLT();
                    //          System.out.println("计算的是"+taskNode.getTaskNumber());  
                    calTaskNodes.addAll(taskNode.getPreviousTaskNodes());
                    //如果没有全部计算完成，把本任务加入到计算列表里  
                } else {
                    calTaskNodes.add(taskNode);
                }
            }
        }
        return calTaskNodes;
    }

    /*
     * 计算最早最晚时间
     */
    public void calculateTime() {
        //计算没有前置任务的任务  
        Set<TaskNode> firstTimes = calculateNoPreviousEarlyTime(this.taskNodes);
        //依次分层计算后一层的任务EST，EFT  
        Set<TaskNode> nextTimes = calculateEarlyTime(firstTimes);
        Set<TaskNode> tmpTaskNodes;
        do {
            tmpTaskNodes = nextTimes;
            nextTimes = calculateEarlyTime(tmpTaskNodes);

        } while (nextTimes.size() > 0);

        firstTimes.clear();
        nextTimes.clear();
        tmpTaskNodes.clear();
        //计算没有后置任务的任务  
        firstTimes = calNoNextTasksLateTime(this.taskNodes);
        //依次分层计算后一层的任务的LST  
        nextTimes = calculateLateTime(firstTimes);
        do {
            tmpTaskNodes = nextTimes;
            nextTimes = calculateLateTime(tmpTaskNodes);

        } while (nextTimes.size() > 0);

        //打印计算结果  
        for (TaskNode taskNode : this.taskNodes) {
            System.out.println(taskNode.getTaskNumber() + "tnumber==" + taskNode.getEarlyStartTime() + "est===" + taskNode.getEarlyFinishTime() + "eft===" + taskNode.getLateStartTime() + "lst===" + taskNode.getLateFinishTime() + "lft====" + taskNode.isCriticalPath());
        }
    }


    /**
     * @param taskNodes 所有任务
     * @return 没有前置任务计算完成之后，待计算的任务
     */
    private Set<TaskNode> calculateNoPreviousEarlyTimeArray(List<TaskNode> taskNodes) {
        //  System.out.println("开始调用calculateNoPreviousEarlyTime(List<TaskNode> taskNodes)------");  
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (taskNodes != null && taskNodes.size() > 0) {
            Set<TaskNode> noPreviousTaskNodes = this.getNoPreviousTasks(taskNodes);
            for (TaskNode taskNode : noPreviousTaskNodes) {
                //      System.out.println("没有前置任务的是"+taskNode.getTaskNumber());  
                if (!taskNode.isCalETArray()) {
                    taskNode.calculateETArray();
//                    System.out.println("计算了第一个" + taskNode.getTaskNumber() + "tnumber==" + taskNode.getEarlyStartTime() + "est===" + taskNode.getEarlyFinishTime() + "eft===");

                    calTaskNodes.add(taskNode);
                }
            }
        }
        //  System.out.println("结束调用calculateNoPreviousEarlyTime(List<TaskNode> taskNodes)------");  
        return calTaskNodes;
    }

    /**
     * @param nextTaskNodes 本次待计算的任务
     * @return 下一次待计算的任务
     */
    public Set<TaskNode> calculateEarlyTimeArray(Set<TaskNode> nextTaskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (nextTaskNodes != null && nextTaskNodes.size() > 0) {
            Iterator<TaskNode> it = nextTaskNodes.iterator();
            while (it.hasNext()) {
                TaskNode taskNode = it.next();
                //      System.out.println("本次需要计算的任务是"+taskNode.getTaskNumber());  
                //找到本任务的前置任务看是不是全部计算完成了，  
                List<TaskNode> befTaskNode = taskNode.getPreviousTaskNodes();
                //判断本任务的前置任务是不是完全计算完成的计数器  
                //计数器  
                int count = 0;
                for (int i = 0; i < befTaskNode.size(); i++) {
                    TaskNode bfTaskNode = befTaskNode.get(i);
                    if (bfTaskNode.isCalETArray()) {
                        count++;
                    }
                }
                //如果全部计算完成，将本任务的后置任务加入到返回列表里  
                if (count == befTaskNode.size()) {
                    taskNode.calculateETArray();
                    //          System.out.println("已经计算了"+taskNode.getTaskNumber());  
                    calTaskNodes.addAll(taskNode.getNextTaskNodes());
                    //如果没有全部计算完成，把本任务加入到计算列表里  
                } else {
                    calTaskNodes.add(taskNode);
                }
            }
        }
        return calTaskNodes;
    }

    /**
     * @param taskNodes 所有任务
     * @return 没有前置任务的下一层需要计算的任务
     */
    public Set<TaskNode> calNoNextTasksLateTimeArray(List<TaskNode> taskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<TaskNode>();
        if (taskNodes != null && taskNodes.size() > 0) {
            for (TaskNode taskNode : this.getNoNextTasks(taskNodes)) {
                if (!taskNode.isCalLTArray())
                    taskNode.calculateLTArray();
                calTaskNodes.add(taskNode);
            }
        }
        return calTaskNodes;
    }

    /**
     * @param nextTaskNodes 本次待计算的任务
     * @return 下一次需要计算的任务
     */
    public Set<TaskNode> calculateLateTimeArray(Set<TaskNode> nextTaskNodes) {
        Set<TaskNode> calTaskNodes = new LinkedHashSet<>();
        if (nextTaskNodes != null && nextTaskNodes.size() > 0) {
            Iterator<TaskNode> it = nextTaskNodes.iterator();
            while (it.hasNext()) {
                TaskNode taskNode = it.next();
                //找到本任务的后置任务看是不是全部计算完成了，  
                List<TaskNode> nextTaskNode = taskNode.getNextTaskNodes();
                //判断本任务的后置任务是不是完全计算完成的计数器  
                //计数器  
                int count = 0;

                for (int i = 0; i < nextTaskNode.size(); i++) {
                    TaskNode ntTaskNode = nextTaskNode.get(i);
                    if (ntTaskNode.isCalLTArray()) {
                        count++;
                    }
                }
                //如果全部计算完成，将本任务的后置任务加入到返回列表里  
                if (count == nextTaskNode.size()) {
                    taskNode.calculateLTArray();
                    //          System.out.println("计算的是"+taskNode.getTaskNumber());  
                    calTaskNodes.addAll(taskNode.getPreviousTaskNodes());
                    //如果没有全部计算完成，把本任务加入到计算列表里  
                } else {
                    calTaskNodes.add(taskNode);
                }

            }
        }
        return calTaskNodes;
    }

    /*
     * 计算最早最晚时间
     */
    public void calculateTimeArray() {
        //计算没有前置任务的任务  
        Set<TaskNode> firstTimes = calculateNoPreviousEarlyTimeArray(this.taskNodes);
        //依次分层计算后一层的任务EST，EFT  
        Set<TaskNode> nextTimes = calculateEarlyTimeArray(firstTimes);
        Set<TaskNode> tmpTaskNodes;
        do {
            tmpTaskNodes = nextTimes;
            nextTimes = calculateEarlyTimeArray(tmpTaskNodes);

        } while (nextTimes.size() > 0);

        firstTimes.clear();
        nextTimes.clear();
        tmpTaskNodes.clear();
        //计算没有后置任务的任务  
        firstTimes = calNoNextTasksLateTimeArray(this.taskNodes);
        //依次分层计算后一层的任务的LST  
        nextTimes = calculateLateTimeArray(firstTimes);
        do {
            tmpTaskNodes = nextTimes;
            nextTimes = calculateLateTimeArray(tmpTaskNodes);

        } while (nextTimes.size() > 0);

        //打印计算结果  
        for (int i = 0; i < this.taskNodes.size(); i++) {
            TaskNode taskNode = this.taskNodes.get(i);
            System.out.println(taskNode.getTaskNumber() + "tnumber==" + taskNode.getEarlyStartTimeArray()[0] + "est===" + taskNode.getEarlyFinishTimeArray()[0] + "eft===" + taskNode.getLateStartTimeArray()[0] + "lst===" + taskNode.getLateFinishTimeArray()[0] + "lft");
        }
    }

    public List<TaskNode> getTaskNodes() {
        return taskNodes;
    }

    public void setTaskNodes(List<TaskNode> taskNodes) {
        this.taskNodes = taskNodes;
    }

    public Set<TaskNode> getNoNextTaskNodes() {
        return getNoNextTasks(taskNodes);
    }

    public Set<TaskNode> getNoPreviousTaskNodes() {
        return getNoPreviousTasks(taskNodes);
    }

    /**
     * taskNodes 所有任务集合
     */
    public Wbs(List<TaskNode> taskNodes) {
        super();
        this.taskNodes = taskNodes;
        this.noNextTaskNodes = new LinkedHashSet<>();
        this.noPreviousTaskNodes = new LinkedHashSet<>();
    }

    public static void main(String args[]) {
      String[] l1 = {"FS"};  
      double[] d1 = {15};  
      double[] dl1 = {0};  
      TaskNode t1 = new TaskNode("1","FS",1,0);  
//      String[] l2 = {"FS"};  
//      double[] d2 = {20};  
//      double[] dl2 = {-5};  
//      TaskNode t2 = new TaskNode("2",l2,d2,dl2);  
//      String[] l3 = {"FS"};  
//      double[] d3 = {26};  
//      double[] dl3 = {0};  
//      TaskNode t3 = new TaskNode("3",l3,d3,dl3);  
//      String[] l4 = {"SS"};  
//      double[] d4 = {18};  
//      double[] dl4 = {10};  
//      TaskNode t4 = new TaskNode("4",l4,d4,dl4);  
//      String[] l5 = {"FS"};  
//      double[] d5 = {15};  
//      double[] dl5 = {0};  
//      TaskNode t5 = new TaskNode("5",l5,d5,dl5);  
//      String[] l6 = {"FS"};  
//      double[] d6 = {38};  
//      double[] dl6 = {0};  
//      TaskNode t6 = new TaskNode("6",l6,d6,dl6);  
//      String[] l7 = {"FS"};  
//      double[] d7 = {25};  
//      double[] dl7 = {-5};  
//      TaskNode t7 = new TaskNode("7",l7,d7,dl7);  
//      String[] l8 = {"FS"};  
//      double[] d8 = {15};  
//      double[] dl8 = {5};  
//      TaskNode t8 = new TaskNode("8",l8,d8,dl8);  
//      String[] l9 = {"SS"};  
//      double[] d9 = {18};  
//      double[] dl9 = {20};  
//      TaskNode t9 = new TaskNode("9",l9,d9,dl9);  
//      String[] l10 = {"FS"};  
//      double[] d10 = {30};  
//      double[] dl10 = {0};  
//      TaskNode t10 = new TaskNode("10",l10,d10,dl10);  
//      String[] l11 = {"FS"};  
//      double[] d11 = {28};  
//      double[] dl11 = {5};  
//      TaskNode t11 = new TaskNode("11",l11,d11,dl11);  
//      String[] l12 = {"FS"};  
//      double[] d12 = {140};  
//      double[] dl12 = {0};  
//      TaskNode t12 = new TaskNode("12",l12,d12,dl12);  
//      String[] l13 = {"FS"};  
//      double[] d13 = {18};  
//      double[] dl13 = {-5};  
//      TaskNode t13 = new TaskNode("13",l13,d13,dl13);  
//      String[] l14 = {"SS"};  
//      double[] d14 = {20};  
//      double[] dl14 = {10};  
//      TaskNode t14 = new TaskNode("14",l14,d14,dl14);  
//      String[] l15 = {"FS"};  
//      double[] d15 = {15};  
//      double[] dl15 = {0};  
//      TaskNode t15 = new TaskNode("15",l15,d15,dl15);  
//      String[] l16 = {"FS"};  
//      double[] d16 = {33};  
//      double[] dl16 = {0};  
//      TaskNode t16 = new TaskNode("16",l16,d16,dl16);  
//      String[] l17 = {"FS"};  
//      double[] d17 = {8};  
//      double[] dl17 = {0};  
//      TaskNode t17 = new TaskNode("17",l17,d17,dl17);  
//      String[] l18 = {"FS"};  
//      double[] d18 = {15};  
//      double[] dl18 = {0};  
//      TaskNode t18 = new TaskNode("18",l18,d18,dl18);  
//      String[] l19 = {"FS"};  
//      double[] d19 = {17};  
//      double[] dl19 = {0};  
//      TaskNode t19 = new TaskNode("19",l19,d19,dl19);  
//      String[] l20 = {"FS"};  
//      double[] d20 = {25};  
//      double[] dl20 = {0};  
//      TaskNode t20 = new TaskNode("20",l20,d20,dl20);  

//      TaskNode t1 = new TaskNode("1","FS",15,0);  
//      TaskNode t2 = new TaskNode("2","FS",20,-5);  
//      TaskNode t3 = new TaskNode("3","FS",26,0);  
//      TaskNode t4 = new TaskNode("4","SS",18,10);  
//      TaskNode t5 = new TaskNode("5","FS",15,0);  
//      TaskNode t6 = new TaskNode("6","FS",38,0);  
//      TaskNode t7 = new TaskNode("7","FS",25,-5);  
//      TaskNode t8 = new TaskNode("8","FS",15,5);  
//      TaskNode t9 = new TaskNode("9","SS",18,20);  
//      TaskNode t10 = new TaskNode("10","FS",30,0);  
//      TaskNode t11 = new TaskNode("11","FS",28,5);  
//      TaskNode t12 = new TaskNode("12","FS",140,0);  
//      TaskNode t13 = new TaskNode("13","FS",18,-5);  
//      TaskNode t14 = new TaskNode("14","SS",20,10);  
//      TaskNode t15 = new TaskNode("15","FS",15,0);  
//      TaskNode t16 = new TaskNode("16","FS",33,0);  
//      TaskNode t17 = new TaskNode("17","FS",8,0);  
//      TaskNode t18 = new TaskNode("18","FS",15,0);  
//      TaskNode t19 = new TaskNode("19","FS",17,0);  
//      TaskNode t20 = new TaskNode("20","FS",25,0);  
//        
//      List<TaskNode> tl1 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl2 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl3 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl4 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl5 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl6 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl7 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl8 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl9 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl10 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl11 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl12 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl13 = new LinkedList<TaskNode>();  
//      List<TaskNode> tl14 = new LinkedList<TaskNode>();  
//        
//      List<TaskNode> tl = new LinkedList<TaskNode>();  
//        
//      tl1.add(t1);          
//      tl2.add(t2);                  
//      tl3.add(t4);  
//      tl4.add(t6);  
//      tl5.add(t3);  
//      tl5.add(t5);  
//      tl6.add(t7);  
//      tl6.add(t8);  
//      tl6.add(t9);  
//      tl7.add(t10);  
//      tl8.add(t12);  
//      tl9.add(t13);  
//      tl10.add(t14);  
//      tl11.add(t11);  
//      tl11.add(t15);  
//      tl12.add(t16);  
//      tl13.add(t17);  
//      tl14.add(t18);  
//      tl14.add(t19);  
//        
//      tl.add(t1);  
//      tl.add(t2);  
//      tl.add(t3);  
//      tl.add(t4);  
//      tl.add(t5);  
//      tl.add(t6);  
//      tl.add(t7);  
//      tl.add(t8);  
//      tl.add(t9);  
//      tl.add(t10);  
//      tl.add(t11);  
//      tl.add(t12);  
//      tl.add(t13);  
//      tl.add(t14);  
//      tl.add(t15);  
//      tl.add(t16);  
//      tl.add(t17);  
//      tl.add(t18);  
//      tl.add(t19);  
//      tl.add(t20);  
//        
//  
//      t2.setPreviousTaskNodes(tl1);  
//      t3.setPreviousTaskNodes(tl2);  
//      t4.setPreviousTaskNodes(tl2);  
//      t5.setPreviousTaskNodes(tl3);  
//      t6.setPreviousTaskNodes(tl5);  
//      t7.setPreviousTaskNodes(tl4);  
//      t8.setPreviousTaskNodes(tl4);  
//      t9.setPreviousTaskNodes(tl4);  
//      t10.setPreviousTaskNodes(tl6);  
//      t11.setPreviousTaskNodes(tl7);  
//  
//      t13.setPreviousTaskNodes(tl8);  
//      t14.setPreviousTaskNodes(tl9);  
//      t15.setPreviousTaskNodes(tl10);  
//      t16.setPreviousTaskNodes(tl11);  
//      t17.setPreviousTaskNodes(tl12);  
//      t18.setPreviousTaskNodes(tl13);  
//      t19.setPreviousTaskNodes(tl13);  
//      t20.setPreviousTaskNodes(tl14);  
//      Wbs wbs = new Wbs(tl);  
//      Set<TaskNode> task = wbs.getNoPreviousTaskNodes();  
//      for (TaskNode task2 : task) {  
//          System.out.println("taskNumber=nb="+task2.getTaskNumber());  
//      }  
//      Set<TaskNode> taskt = wbs.getNoNextTaskNodes();  
//      for (TaskNode task2 : taskt) {  
//          System.out.println("taskNumber=na="+task2.getTaskNumber());  
//      }  
//      wbs.calculateTime();  
//      wbs.calculateTimeArray();  


//      long time1 = System.currentTimeMillis();  
//      TaskNode previous = new TaskNode("1","FS",15,0);  
//      List<TaskNode> tls = new LinkedList<TaskNode>();  
//      tls.add(previous);  
//      List<TaskNode> tl = new LinkedList<TaskNode>();  
//      tl.add(previous);  
//      for(int i = 2;i<=2000;i++){  
//          TaskNode tmp = new TaskNode(String.valueOf(i),"FS",15,0);             
//          tmp.setPreviousTaskNodes(tls);  
//          tls = new LinkedList<TaskNode>();  
//          tls.add(tmp);  
//          tl.add(tmp);  
//      }         
//      long time2 = System.currentTimeMillis();  
//      System.out.println("构建用时："+(time2-time1));  
//      Wbs wbs = new Wbs(tl);  
//      wbs.calculateTime();  
//      long time3 = System.currentTimeMillis();  
//      System.out.println("计算用时："+(time3-time2));  

    }
    
    
    
    private void xxx(){

        //数组的计算  
        long time1 = System.currentTimeMillis();
        int arrayLength = 200;
        String[] logicarray = new String[arrayLength];
        double[] delaytimearray = new double[arrayLength];
        double[] durtimearray = new double[arrayLength];

        Random random = new Random();
        for (int i = 0; i < arrayLength; i++) {
            //获得200以内的正整数为执行时间数组赋值  
            durtimearray[i] = Math.abs(random.nextInt()) % 200;
            //获得20以内的整数为浮动时间赋值  
            delaytimearray[i] = Math.abs(random.nextInt() % 20);
            //获得4以内的整数为4种逻辑关系赋值  
            int index = Math.abs(random.nextInt()) % 4;
            switch (index) {
                case 1:
                    logicarray[i] = "SS";
                    break;
                case 2:
                    logicarray[i] = "FF";
                    break;
                case 3:
                    logicarray[i] = "SF";
                    break;
                default:
                    logicarray[i] = "FS";
                    break;
            }
        }

        TaskNode previous = new TaskNode("task1", logicarray, delaytimearray, durtimearray);
        List<TaskNode> tls = new LinkedList<TaskNode>();
        tls.add(previous);
        List<TaskNode> tl = new LinkedList<TaskNode>();
        tl.add(previous);
        for (int i = 2; i <= 5000; i++) {
            String[] logicarrayi = new String[arrayLength];
            double[] delaytimearrayi = new double[arrayLength];
            double[] durtimearrayi = new double[arrayLength];
            Random randomi = new Random();

            for (int ii = 0; ii < arrayLength; ii++) {
                //获得200以内的正整数为执行时间数组赋值  
                durtimearrayi[ii] = Math.abs(randomi.nextInt()) % 200;
                //获得20以内的整数为浮动时间赋值  
                delaytimearrayi[ii] = Math.abs(randomi.nextInt() % 20);
                //获得4以内的整数为4种逻辑关系赋值  
                int index = Math.abs(randomi.nextInt()) % 4;
                switch (index) {
                    case 1:
                        logicarrayi[ii] = "SS";
                        break;
                    case 2:
                        logicarrayi[ii] = "FF";
                        break;
                    case 3:
                        logicarrayi[ii] = "SF";
                        break;
                    default:
                        logicarrayi[ii] = "FS";
                        break;
                }
            }

            TaskNode tmp = new TaskNode("task" + i, logicarrayi, delaytimearrayi, durtimearrayi);
            tmp.setPreviousTaskNodes(tls);
            tls = new LinkedList<TaskNode>();
            int previousCount = Math.abs(randomi.nextInt()) % 8;
            for (int ii = 0; ii < previousCount; ii++) {
                if (ii < tl.size()) {
                    tls.add(tl.get(ii));
                }
            }

            tl.add(tmp);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("构建用时：" + (time2 - time1));
        Wbs wbs = new Wbs(tl);
        wbs.calculateTimeArray();
        long time3 = System.currentTimeMillis();
        System.out.println("计算用时：" + (time3 - time2));

    }
}  
