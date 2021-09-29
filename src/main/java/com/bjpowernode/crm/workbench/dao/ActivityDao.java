package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityDao {

     /**
      * @Author anson
      * @Description: 保存用戶
      * @Date:  2021/9/17 15:44
      * @Param: activity
      * @return: int
      **/
    int saveActivity(Activity activity);

     /**
      * @Author anson
      * @Description: 獲取搜索結果集
      * @Date:  2021/9/17 15:46
      * @Param: Map<String, Object> map
      * @return:List<Activity>
      **/
    List<Activity> getPageList(Map<String, Object> map);

     /**
      * @Author anson
      * @Description: 獲取搜索結果集總條數
      * @Date:  2021/9/17 15:47
      * @Param: Map<String, Object> map
      * @return: int
      **/
    int pageListCount(Map<String, Object> map);

     /**
      * @Author anson
      * @Description: 獲取活動對象
      * @Date:  2021/9/19 11:16
      * @Param: id
      * @return: Activity
      **/
    Activity getActivity(String id);

     /**
      * @Author anson
      * @Description: 修改活動對象信息
      * @Date:  2021/9/19 12:08
      * @Param: activity
      * @return: int
      **/
    int updateActivity(Activity activity);

    int deleteActivity(String[] ids);

    Activity getDetail(String id);

    int deleteDetail(String id);

}
