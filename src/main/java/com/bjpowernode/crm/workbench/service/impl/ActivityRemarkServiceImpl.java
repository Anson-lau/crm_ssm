package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: ActivityRemarkServiceImpl
 * @Description: TODO
 * @Author: Anson
 * @Create: 2021-09-27 01:12
 **/
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource(name = "activityRemarkDao")
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public List<ActivityRemark> getRemarkList(String activityId) {

        List<ActivityRemark> activityRemarks = activityRemarkDao.getRemarkList(activityId);

        return activityRemarks;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(activityRemark);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public ActivityRemark getEditRemark(String id) {

        ActivityRemark activityRemark = activityRemarkDao.getEditRemark(id);

        return activityRemark;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(activityRemark);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;

        int count = activityRemarkDao.deleteActivityRemarkById(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }
}
