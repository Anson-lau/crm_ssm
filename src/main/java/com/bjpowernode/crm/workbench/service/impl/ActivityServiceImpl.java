package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ActivityServiceImpl
 * @Description: 實現ActivityService接口
 * @Author: Anson
 * @Create: 2021-09-11 12:37
 **/

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource(name = "activityDao")
    private ActivityDao activityDao;

    @Resource(name = "activityRemarkDao")
    private ActivityRemarkDao activityRemarkDao;


     /**
      * @Author anson
      * @Description: 保存用戶
      * @Date:  2021/9/17 15:38
      * @Param: activity
      * @return: boolean
      **/
    @Override
    public boolean saveActivity(Activity activity) {

        boolean flag = true;

        int count = activityDao.saveActivity(activity);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

     /**
      * @Author anson
      * @Description: 搜索信息
      * @Date:  2021/9/17 15:41
      * @Param: Map<String, Object> map
      * @return: Pagination<Activity>
      **/
    @Override
    public PaginationVo<Activity> pageListByMap(Map<String, Object> map) {

        // 獲取搜索結果集和結果集條數
        List<Activity> dataList = activityDao.getPageList(map);
        int total = activityDao.pageListCount(map);

        // 將結果集和總條數包裝進PaginationVo
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

     /**
      * @Author anson
      * @Description: 獲取活動對象詳情
      * @Date:  2021/9/19 11:15
      * @Param: id
      * @return: Activity
      **/
    @Override
    public Activity getActivity(String id) {

        Activity a = activityDao.getActivity(id);

        return a;
    }

     /**
      * @Author anson
      * @Description: 修改活動對象信息
      * @Date:  2021/9/19 12:07
      * @Param: activity
      * @return: boolean
      **/
    @Override
    public boolean updateActivity(Activity activity) {

        boolean flag = true;

        int count = activityDao.updateActivity(activity);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean deleteActivity(String[] ids) {
        System.out.println("coming into deleteActivity..");

        boolean flag = true;

        int count1 = activityRemarkDao.getCountByAid(ids);
        System.out.println(count1);

        int count2 = activityRemarkDao.deleteActivityRemark(ids);
        System.out.println(count2);

        if (count1 != count2){
            flag = false;
        }

        int count3 = activityDao.deleteActivity(ids);
        System.out.println(count3);

        if (count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity getDetail(String id) {

        Activity activity = activityDao.getDetail(id);

        return activity;
    }

    @Override
    public boolean deleteDetail(String id) {
        boolean flag = true;

        int count1 = activityDao.deleteDetail(id);
        if (count1 != 1){
            flag = false;
        }

        System.out.println("service1: " +flag);

        int count2 = activityRemarkDao.getCountsByAid(id);
        int count3 = activityRemarkDao.deleteActivityRemarkByAid(id);

        if (count2 != count3){
            flag = false;
        }

        System.out.println("count2: " + count2);
        System.out.println("count3: " + count3);
        System.out.println("service2: " + flag);

        return flag;
    }
}
