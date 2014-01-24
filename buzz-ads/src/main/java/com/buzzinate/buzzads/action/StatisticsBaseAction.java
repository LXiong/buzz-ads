package com.buzzinate.buzzads.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.buzzinate.buzzads.user.LoginHelper;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.buzzinate.buzzads.analytics.stats.AdBasicStatistic;
import com.buzzinate.buzzads.core.util.AdsDateUtils;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.enums.AvailableStatsEnum;
import com.buzzinate.buzzads.util.DateRangeUtil;
import com.buzzinate.buzzads.util.VisualizeJsUtil;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.TextProvider;

/**
 * 
 * @author johnson
 *
 */
public class StatisticsBaseAction extends ActionSupport implements ServletRequestAware {

    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    
    private static final long serialVersionUID = -6562408430379097695L;
    
    @Autowired
    protected LoginHelper loginHelper;
    protected Date dateStart;
    protected Date dateEnd;
    protected TextProvider textProvider;
    protected HttpServletRequest request;
    protected boolean isRound;
    protected String jsonDataStats = "";
    
    protected int xLabelInterval = ConfigurationReader.getInt("stats.chart.view.xLabelInterval");
    
    // page
    protected Pagination page = new Pagination();
    //快捷查看:1-今天，2-昨天，3-上周，4-前七天，5-本月，6-上月
    protected int queryRange;
    
    public String getJsonDataStats() {
        return jsonDataStats;
    }
    
    public void setJsonDataStats(String jsonDataStats) {
        this.jsonDataStats = jsonDataStats;
    }
    
    public String getDateStart() {
        return DateTimeUtil.formatDate(dateStart);
    }
    
    public void setDateStart(String dateStart) {
        this.dateStart = DateTimeUtil.convertDate(dateStart);
    }
    
    public String getDateEnd() {
        return DateTimeUtil.formatDate(dateEnd);
    }
    
    public void setDateEnd(String dateEnd) {
        this.dateEnd = DateTimeUtil.convertDate(dateEnd);
    }
    
    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
    
    public void setTextProvider(TextProvider textProvider) {
        this.textProvider = textProvider;
    }
 
    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
    
    public int getXLabelInterval() {
        return xLabelInterval;
    }

    public void setXLabelInterval(int x) {
        this.xLabelInterval = x;
    }
    
    public boolean getIsRound() {
        return isRound;
    }

    public void setIsRound(boolean isRound) {
        this.isRound = isRound;
    }

    protected void initDateTimePicker() {
        Date[] validDates = DateRangeUtil.getStartAndEndDates(dateStart, dateEnd, loginHelper.isLoginAsAdmin());
        dateStart = validDates[0];
        dateEnd = validDates[1];
    }
    
    protected void initQuickDateRange() {
        if (queryRange == 1) {
            dateStart = DateTimeUtil.getCurrentDateDay();
            dateEnd = DateTimeUtil.getCurrentDateDay();
        } else if (queryRange == 2) {
            dateStart = DateTimeUtil.subtractDays(DateTimeUtil.getCurrentDateDay(), 1);
            dateEnd = DateTimeUtil.subtractDays(DateTimeUtil.getCurrentDateDay(), 1);
        } else if (queryRange == 3) {
            dateStart = DateTimeUtil.getDateDay(AdsDateUtils.lastWeekFirstDay());
            dateEnd = DateTimeUtil.getDateDay(AdsDateUtils.lastWeekLastDay());
        } else if (queryRange == 4) {
            dateEnd = DateTimeUtil.subtractDays(DateTimeUtil.getCurrentDateDay(), 1);
            dateStart = DateTimeUtil.subtractDays(dateEnd, 6);
        } else if (queryRange == 5) {
            dateStart = DateTimeUtil.convertDate(DateTimeUtil.getFirstDayOfCurrentMonth());
            dateEnd = DateTimeUtil.getCurrentDateDay();
        } else if (queryRange == 6) {
            dateStart = DateTimeUtil.convertDate(AdsDateUtils.lastMonFirstDay());
            dateEnd = DateTimeUtil.convertDate(AdsDateUtils.lastMonLastDay());
        } else if (queryRange == 7) {
            dateStart = AdsDateUtils.getFirstDayOfLastQuater();
            dateEnd = AdsDateUtils.getLastDayOfLastQuater();
        }
        initDateTimePicker();
    }
    
    
    protected void initVisulizeChart() {
        Object[] visulize = VisualizeJsUtil.getXLabelInterval(dateStart, dateEnd, xLabelInterval);
        isRound = (Boolean) visulize[0];
        xLabelInterval = (Integer) visulize[1];
    }
    
    protected List<AdBasicStatistic> addZeroes(List<? extends AdBasicStatistic> list, 
                    Date start, Date end, boolean isDesc) {
        // passed in list is reversed, so need to reverse it first.
        if (isDesc) Collections.reverse(list);
        List<AdBasicStatistic> listReturn = new ArrayList<AdBasicStatistic>();
        Date date = null;
        Date currentDate = dateStart;
        for (AdBasicStatistic item : list) {
            date = item.getDateDay();

            // add 0's to the data to when there is actually data.
            while (date.after(currentDate)) {
                listReturn.add(new AdBasicStatistic(currentDate));
                currentDate = DateTimeUtil.subtractDays(currentDate, -1);
            }
            currentDate = DateTimeUtil.subtractDays(currentDate, -1);

            listReturn.add(item);
        }
        // add 0's to the data to the end date.
        if (!currentDate.equals(dateEnd)) {
            while (currentDate.compareTo(dateEnd) < 0) {
                listReturn.add(new AdBasicStatistic(currentDate));
                currentDate = DateTimeUtil.subtractDays(currentDate, -1);
            }
        }
        
        // return the list in descending order
        Collections.reverse(listReturn);
        return listReturn;
    }
    
    protected String buildDailyStatisticsJsChart(List<? extends AdBasicStatistic> dailyStatistics, 
            Date start, Date end, AvailableStatsEnum statsEnum) {
        List<Object[]> campDaily = new ArrayList<Object[]>();
        if (dailyStatistics != null) {
            for (AdBasicStatistic daily: dailyStatistics) {
                switch (statsEnum) {
                case VIEWS:
                    campDaily.add(new Object[] {daily.getViews(), daily.getDateDay()});
                    break;
                case CLICKS:
                    campDaily.add(new Object[] {daily.getClicks(), daily.getDateDay()});
                    break;
                case CTR:
                    campDaily.add(new Object[] {daily.getCpcClickToView(), daily.getDateDay()});
                    break;
                case CPCCLICKS:
                    campDaily.add(new Object[] {daily.getCpcClickNo(), daily.getDateDay()});
                    break;
                case CPMVIEWS:
                    campDaily.add(new Object[] {daily.getCpmViewNo(), daily.getDateDay()});
                    break;
                default:
                    campDaily.add(new Object[] {daily.getTotalCommissionDouble(), daily.getDateDay()});
                }
            }
        }
        Collections.reverse(campDaily);
        return VisualizeJsUtil.getVisualizeJsData(campDaily, "", start, end);
    }

    public int getQueryRange() {
        return queryRange;
    }

    public void setQueryRange(int queryRange) {
        this.queryRange = queryRange;
    }
}