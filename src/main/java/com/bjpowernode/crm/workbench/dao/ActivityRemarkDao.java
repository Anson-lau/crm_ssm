package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRemarkDao {

    int getCountByAid(String[] ids);

    int deleteActivityRemark(String[] ids);

    List<ActivityRemark> getRemarkList(String activityId);

    int saveRemark(ActivityRemark activityRemark);

    ActivityRemark getEditRemark(String id);

    int updateRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int deleteActivityRemarkByAid(String activityId);

    int getCountsByAid(String activityId);
}
