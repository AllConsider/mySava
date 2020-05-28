package com.ecut.demo.untilServiceImpl;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

//监听类 执行到该流程的时候会执行这个步骤
public class NameListener1 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //do 通过数据库查询部门领导
        //list为查询出来的部门领导 该部门领导为可以处理该任务的人
        List list=new ArrayList();
        delegateTask.addCandidateUsers(list);//完成多人处理的指定
    }
}
