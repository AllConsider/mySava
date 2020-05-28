package com.ecut.demo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class TestActiviti {
    //定义流程引擎
    private ProcessEngine processEngine;
    private IdentityService identityService;
    private RepositoryService repositoryService;
    private TaskService taskService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    @Before
    @Test
    public void initTable()
    {
        processEngine= ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        identityService=processEngine.getIdentityService();
        repositoryService=processEngine.getRepositoryService();
        taskService=processEngine.getTaskService();
        runtimeService=processEngine.getRuntimeService();
        historyService=processEngine.getHistoryService();
    }

    //1.初始化用户和用户组
    @Test
    public void initUser()
    {
        User u1=identityService.newUser("111");
        u1.setFirstName("陈文渊");
        identityService.saveUser(u1);
        User u2=identityService.newUser("222");
        u2.setFirstName("汪汪");
        identityService.saveUser(u2);
    }
    @Test
    public void initGroup()
    {
        Group g=identityService.newGroup("teacher");
        g.setName("教师");
        identityService.saveGroup(g);
        Group g1=identityService.newGroup("dept");
        g1.setName("教务处");
        identityService.saveGroup(g1);
    }
    //2.将用户分组实现
    @Test
    public void initMemberShip()
    {
        identityService.createMembership("111","teacher");
        identityService.createMembership("222","dept");
    }
    //部署流程定义
    //定义流程，部署上去
    @Test
    public void deployee()
    {
        Deployment deployment=repositoryService.createDeployment()
                .addClasspathResource("process/Test6.bpmn")
                .deploy();
    }
    //删除部署
    @Test
    public void remove()
    {
        //根据deploymentID来删除部署，同时清空所有的任务等
        repositoryService.deleteDeployment("27501",true);
    }
    //发布流程申请
    @Test
    public void testTask()
    {
       String applyUserId="张四";
       identityService.setAuthenticatedUserId(applyUserId);
       runtimeService.startProcessInstanceByKey("leave");
    }
    //查询待办事项
    @Test
    public  void queryTask()
    {
        //根据查询人的名字来查询 assign输入自己的名字
        List<Task> tasks=taskService.createTaskQuery().processDefinitionKey("leave")
                .taskAssignee("").list();
        System.out.println(tasks.size()+"********"+taskService.createTaskQuery().count());
        //对申请进行审批 ，互斥网关 同意则为true 如果多个流程名为leave则要用list
        ProcessInstance processInstance=runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .singleResult();
        taskService.addComment(tasks.get(0).getId(),processInstance.getId(),"流程意见：同意");
        System.out.println(tasks.get(0).getId());
        HashMap<String,Object> varib=new HashMap<>();
        //添加网关互斥条件
        varib.put("PE",true);
        taskService.complete(tasks.get(0).getId(),varib);

    }
    //查询已办事项 从历史任务表中中查询
    @Test
    public void queryComplete()
    {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee("陈文渊").finished()
                .list();
        for(HistoricTaskInstance taskInstance:historicTaskInstances)
        {
            System.out.println("历史任务名称："+taskInstance.getName()+"办理人："+taskInstance.getAssignee());
        }

    }

}
