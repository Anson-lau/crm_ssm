package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> getRemarkList(String activityId);

    boolean saveRemark(ActivityRemark activityRemark);

    ActivityRemark getEditRemark(String id);

    boolean updateRemark(ActivityRemark activityRemark);

    boolean deleteRemark(String id);
}
