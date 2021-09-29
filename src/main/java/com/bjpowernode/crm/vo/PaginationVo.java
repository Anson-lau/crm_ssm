package com.bjpowernode.crm.vo;

import java.util.List;

/**
 * @ClassName: PaginationVo
 * @Description: 包裝T類對象和縂條數
 * @Author: Anson
 * @Create: 2021-09-17 15:11
 **/
public class PaginationVo<T> {

    //  頁面縂條數
    private int total;

    //  返回的搜索結果集
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
