package com.buzzinate.buzzads.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.buzzinate.common.util.DateTimeUtil;

/**
 * Visualize JS data generation
 *
 * @author John Chen Copyright 2009-2012 Buzzinate Co. Ltd.
 * @since Jul 21, 2012
 */

public final class VisualizeJsUtil {
    
    private VisualizeJsUtil() { }
    
    /**
     * Returns formatted data for visualize js chart plugin
     * 
     * @param list
     * @param yLabel
     * @return
     */
    public static String getVisualizeJsData(List<Object[]> list, String yLabel) {
        String dataDate = "";
        String dataCount = "";
        
        for (Object[] s : list) {
            dataCount += "<td>" + ((Long[]) s[0])[0] + "</td>";
            dataDate += "<th>" + ((String[]) s[2])[0] + "&lt;br/&gt;" + s[1] + "</th>";
        }
        
        return "<thead><tr><td></td>" + dataDate + "</tr></thead><tbody><tr><th>" + yLabel + "</th>" + 
            dataCount + "</tr></tbody>";
    }
    
    /**
     * Returns formatted data for visualize js chart plugin
     * 
     * @param list
     * @param yLabel
     * @return
     */
    public static String getVisualizeJsData(List<Object[]> list, String yLabel, Date dateStart, Date dateEnd) {
        String dataDate = "";
        String dataCount = "";
        Date date = null;
        Date currentDate = dateStart;
        
        for (Object[] s : list) {
            if (s[1] == null) { 
                date = dateStart;
            } else {
                date = (Date) s[1];
            }
            
            // add 0's to the data in the graph to when there is actually data.
            while (date.after(currentDate)) {
                dataCount += "<td>0</td>";
                dataDate += "<th>" + DateTimeUtil.formatDate(currentDate) + "</th>";
                currentDate = DateTimeUtil.subtractDays(currentDate, -1);
            }
            currentDate = DateTimeUtil.subtractDays(currentDate, -1);
            
            dataCount += "<td>" + s[0] + "</td>";
            dataDate += "<th>" + DateTimeUtil.formatDate((Date) s[1]) + "</th>";
        }
        // add 0's to the data in the graph to the end date.
        if (!currentDate.equals(dateEnd)) {
            while (currentDate.compareTo(dateEnd) < 0) {
                dataCount += "<td>0</td>";
                dataDate += "<th>" + DateTimeUtil.formatDate(currentDate) + "</th>";
                currentDate = DateTimeUtil.subtractDays(currentDate, -1);
            }
        }
        return "<thead><tr><td></td>" + dataDate + "</tr></thead><tbody><tr><th>" + yLabel + "</th>" + 
            dataCount + "</tr></tbody>";
    }
    
    /**
     * Returns formatted data for visualize js pie
     * 
     * @param dates
     * @param commissionMap
     * @return
     */
    public static String getVisualizeJsPieData(List<String> dates, Map<String, 
            List<Integer>> commissionMap, String label) {
        String pieDate = "<caption>" + label + "</caption>";
        pieDate += "<thead><tr>";
        pieDate += "<td></td>";
        for (String date : dates) {
            pieDate += "<th>" + date + "</th>";
        }
        pieDate += "</tr></thead>";
        
        pieDate += "<tbody>";
        for (Map.Entry<String, List<Integer>> entry : commissionMap.entrySet()) {
            pieDate += "<tr>";
            pieDate += "<th>" + entry.getKey() + "</th>";
            for (Integer value : entry.getValue()) {
                pieDate += "<td>" + value + "</td>";
            }
            
            pieDate += "</tr>";
        }
        pieDate += "</tbody>";
        return pieDate;
    }
    
    
    /**
     * get the xLabelInterval dynamically (compare to the default xLabelInterval of one month)
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static Object[] getXLabelInterval(Date dateStart, Date dateEnd, int xLabelInterval) {
        int days = new Long((dateEnd.getTime() - dateStart.getTime()) / (24 * 60 * 60 * 1000)).intValue();
        boolean integrate = days % 30 == 0;
        int size = (int) Math.ceil((double) days / 30);
        int interval = xLabelInterval;
        if (size > 1) {
            interval = interval * size;
        }
        return new Object[] {integrate, interval};
    }
}
