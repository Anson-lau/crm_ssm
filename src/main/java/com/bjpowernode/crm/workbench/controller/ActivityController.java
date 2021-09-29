package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.util.UIUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ActivityController
 * @Description: 操作activity頁面
 * @Author: Anson
 * @Create: 2021-09-08 15:54
 **/

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource(name = "activityServiceImpl")
    private ActivityService activityService;

    @Resource(name = "activityRemarkServiceImpl")
    private ActivityRemarkService activityRemarkService;

     /**
      * @Author anson
      * @Description: 打開創建表單，獲取表單所有者
      * @Date:  2021/9/11 12:32
      * @Param:
      * @return: List<User>
      **/
    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){

        return userService.getUserList();

    }



     /**
      * @Author anson
      * @Description: 保存用戶信息
      * @Date:  2021/9/17 15:06
      * @Param: activity, request
      * @return: index.jsp
      **/
    @RequestMapping("/save.do")
    @ResponseBody
    public ModelAndView saveUser(Activity activity, HttpServletRequest request){

        System.out.println("coming into saveUser");

        // 獲取id，創建時間
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());

        // 從session獲取創建人
        activity.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = activityService.saveActivity(activity);

        ModelAndView mv = new ModelAndView();

        mv.setViewName("/activity/index");

        return mv;

    }

     /**
      * @Author anson
      * @Description: 將搜索框關鍵詞搜索出來的條目包裝返回結果集
      * @Date:  2021/9/17 15:30
      * @Param: request
      * @return: Pagination<Activity>
      **/
    @RequestMapping("/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> pageList(String name, String owner, String startDate, String endDate,
                                           String pageNo, String pageSize){
        System.out.println("coming into pageList..");

        int pageNoStr = Integer.valueOf(pageNo);
        int pageSizeStr = Integer.valueOf(pageSize);

        // 每頁相隔條數
        int skipCount = (pageNoStr - 1) * pageSizeStr;

        // 將參數包裝成一個map
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSizeStr", pageSizeStr);

        // activityService調用pageList方法，返回dataList，total
        PaginationVo<Activity> vo = activityService.pageListByMap(map);

        return vo;


    }

     /**
      * @Author anson
      * @Description: 獲取用戶及活動詳情
      * @Date:  2021/9/19 11:08
      * @Param: id
      * @return: Map<String, Object> map
      **/
    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
     public Map<String, Object> getUserListAndActivity(String id){
        System.out.println("coming into getUserListAndActivity..");

        Map<String, Object> map = new HashMap<>();

        List<User> uList = userService.getUserList();

        Activity a = activityService.getActivity(id);

        map.put("uList", uList);
        map.put("a", a);

        return map;

    }


     /**
      * @Author anson
      * @Description: 修改活動對象信息
      * @Date:  2021/9/19 12:05
      * @Param: id, activity, request
      * @return: boolean
      **/
     @RequestMapping("/updateActivity.do")
     @ResponseBody
     public boolean updateActivity(Activity activity, HttpServletRequest request){
        System.out.println("coming into updateActivity..");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        boolean flag = activityService.updateActivity(activity);

        return flag;


    }

     /**
      * @Author anson
      * @Description: 刪除對象
      * @Date:  2021/9/24 12:22
      * @Param: ids
      * @return: boolean
      **/
     @RequestMapping("/deleteActivity.do")
     @ResponseBody
     public boolean deleteActivity(@RequestParam("id") String[] ids){
         System.out.println("coming into delete..");

         boolean flag = activityService.deleteActivity(ids);

         return flag;

    }

    @RequestMapping("/detail.do")
    @ResponseBody
    private ModelAndView detail(String id){
        System.out.println("coming into detail..");

        ModelAndView mv = new ModelAndView();

        Activity activity = activityService.getDetail(id);

        mv.addObject("activity", activity);
        mv.setViewName("/activity/detail");

        return mv;

    }

     /**
      * @Author anson
      * @Description: 獲取編輯框信息
      * @Date:  2021/9/26 11:16
      * @Param: id
      * @return: Map<String, Object> map
      **/
    @RequestMapping("/getDetailEditById.do")
    @ResponseBody
    private Map<String, Object> getDetailEditById(String id){
        System.out.println("coming into getDetailEditById..");

        Map<String, Object> map = new HashMap<>();

        //        獲取user的信息和activity的信息
        List<User> userList = userService.getUserList();
        Activity activity = activityService.getActivity(id);

        map.put("userList", userList);
        map.put("activity", activity);

        return map;
    }

    @RequestMapping("/updateDetailEdit.do")
    @ResponseBody
    private boolean updateDetailEdit(Activity activity, HttpServletRequest request){
        System.out.println("coming into updateDetailEditById..");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        boolean flag = activityService.updateActivity(activity);

        return flag;

    }

    @RequestMapping("/deleteDetail.do")
    @ResponseBody
    private boolean deleteDetail(String id){
        System.out.println("coming into deleteDetail..");

        boolean flag = activityService.deleteDetail(id);

        System.out.println("controller: " + flag);

        return flag;
    }

    @RequestMapping("/getRemarkList.do")
    @ResponseBody
    private List<ActivityRemark> getRemarkList(String activityId){
        System.out.println("coming into getRemarkList..");

        List<ActivityRemark> remarkList = activityRemarkService.getRemarkList(activityId);

        return remarkList;

    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    private boolean saveRemark(ActivityRemark activityRemark, HttpServletRequest request){
        System.out.println("coming into saveRemark..");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        activityRemark.setId(id);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);

        boolean flag = activityRemarkService.saveRemark(activityRemark);

        return flag;

    }


    @RequestMapping("/getEditRemark.do")
    @ResponseBody
    private ActivityRemark getEditRemark(String id){
        System.out.println("coming into getEditRemark..");

        ActivityRemark activityRemark = activityRemarkService.getEditRemark(id);

        return activityRemark;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    private boolean updateRemark(ActivityRemark activityRemark, HttpServletRequest request){
        System.out.println("coming into updateRemark..");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";


        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        boolean flag = activityRemarkService.updateRemark(activityRemark);

        return flag;

    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    private boolean deleteRemark(String id){
        System.out.println("coming into deleteRemark..");

        boolean flag = activityRemarkService.deleteRemark(id);

        return flag;
    }



}
