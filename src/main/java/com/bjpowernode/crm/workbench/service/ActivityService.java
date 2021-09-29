package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface ActivityService {

     /**
      * @Author anson
      * @Description: 添加活動
      * @Date:  2021/9/12 10:56
      * @Param: Activity
      * @return: boolean
      **/
    boolean saveActivity(Activity activity);

     /**
      * @Author anson
      * @Description: 搜索條目
      * @Date:  2021/9/17 15:37
      * @Param: Map<String, Object> map
      * @return: Pagination<Activity>
      **/
    PaginationVo<Activity> pageListByMap(Map<String, Object> map);

     /**
      * @Author anson
      * @Description: 獲取活動對象
      * @Date:  2021/9/19 11:14
      * @Param: id
      * @return: Activity
      **/
    Activity getActivity(String id);

     /**
      * @Author anson
      * @Description: 修改活動對象信息
      * @Date:  2021/9/19 12:06
      * @Param: activity
      * @return: boolean
      **/
    boolean updateActivity(Activity activity);

    boolean deleteActivity(String[] ids);

    Activity getDetail(String id);

    boolean deleteDetail(String id);

}
