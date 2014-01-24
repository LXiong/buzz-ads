package com.buzzinate.buzzads.core.util;

import java.io.Serializable;

/**
 * 分页数据模型基类。
 * 
 * @author harry
 * 
 */
public class Pagination implements Serializable {
    
    private static final long serialVersionUID = -4155418614801270078L;

    private static final int DEFAULT_PAGE_SIZE = ConfigurationReader.getInt("stats.page.rank.size");
    
    // 排序字段及方式 如： "order asc"
    private String orderStr;

    // 当前页码
    private int pageNum = 1;

    // 每页显示记录数
    private int pageSize = DEFAULT_PAGE_SIZE;

    // 查询出的数据总数
    private int totalRecords;

    // 当前查询返回结果数
    private int returnRecords;

    /**
     * 判断当前页是否为第一页
     * 
     * @return boolean
     */
    public boolean isFirstPage() {
        return pageNum == 1;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }

    public int getPageNum() {
        // style 1 (简单分页)
        if (this.totalRecords == 0 && (this.returnRecords > 0 || pageNum > 1)) {
            return pageNum;
        }
        // style 2
        if (pageNum > getTotalPage()) {
            return getTotalPage();
        }
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * 得到上一页的页数
     * 
     * @return int 如果当前页没有上一页，返回1
     */
    public int getPreviousPage() {
        int back = pageNum - 1;
        if (back <= 0) {
            back = 1;
        }
        return back;
    }

    /**
     * 判断当前页是否为最后一页
     * 
     * @return boolean
     */
    public boolean isLastPage() {
        return this.getTotalPage() == pageNum;
    }

    /**
     * 得到下一页的页数
     * 
     * @return int 如果当前也没有下一页，返回总页数
     */
    public int getNextPage() {
        int next = pageNum + 1;
        if (next > this.getTotalPage()) {
            next = this.getTotalPage();
        }
        return next;
    }

    /**
     * 得到要显示的总页数
     * 
     * @return int
     */
    public int getTotalPage() {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    /**
     * 验证pageNum页数是否合法
     * 
     * @param pageNum
     * @return 验证后的pageNum页数
     */
    public int validateToPage(int to) {
        int pageNumTo = to;
        if (pageNumTo < 1) {
            pageNumTo = 1;
        }
        if (pageNumTo > getTotalPage()) {
            pageNumTo = getTotalPage();
        }
        return pageNumTo;
    }

    /**
     * 构建PageInfo对象数据。 PageInfo的实现类专用，主要是验证当前页数的合法性
     */
    public void validatePageInfo() {
        setPageNum(validateToPage(pageNum));
    }

    /**
     * 得到要检索的结束行数
     * 
     * @return 如果<code>pageNum</code>和<code>pageSize</code>中任何一个小于等于0， 则返回
     *         <code>null</code>,用于在分页查询返回所有的数据（即不分页）。
     */
    public Integer getEndRow() {
        if (pageNum > 0 && pageSize > 0) {
            return Integer.valueOf(pageNum * pageSize);
        }
        return Integer.valueOf(10);
    }

    /**
     * 得到要检索的起始行数
     * 
     * @return 如果<code>pageNum</code>和<code>pageSize</code>中任何一个小于等于0， 则返回
     *         <code>null</code>,用于在分页查询返回所有的数据（即不分页）。
     */
    public Integer getStartRow() {
        if (pageNum > 0 && pageSize > 0) {
            return Integer.valueOf((pageNum - 1) * pageSize);
        }
        return Integer.valueOf(0);
    }

    /**
     * @param value
     */
    protected String getSQLBlurValue(String value) {
        if (value == null) {
            return null;
        }
        return value + '%';
    }

    public int getDesiredPage() {
        return this.pageNum;
    }

    public int getReturnRecords() {
        return returnRecords;
    }

    public void setReturnRecords(int returnRecords) {
        this.returnRecords = returnRecords;
    }

}
