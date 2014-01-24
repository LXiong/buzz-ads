package com.buzzinate.buzzads.billing.cpc;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

import com.buzzinate.buzzads.core.bean.CpcTimeSegment;
import com.buzzinate.common.util.DateTimeUtil;

/**
 * 
 * @author zyeming
 *
 */
public class CpcTimeSegmentTest extends UnitilsTestNG {
    
    @Test
    public void testTimeSegment() {
        CpcTimeSegment pts = CpcTimeSegment.getPreviousSegment();
        System.out.println(DateTimeUtil.formatDate(pts.getDay()) + " - " + pts.getSegment());
        
        CpcTimeSegment cts = CpcTimeSegment.getCurrentSegment();
        System.out.println(DateTimeUtil.formatDate(cts.getDay()) + " - " + cts.getSegment());
        
        CpcTimeSegment nts = CpcTimeSegment.getNextSegment(pts);
        System.out.println(DateTimeUtil.formatDate(nts.getDay()) + " - " + nts.getSegment());
        
        Assert.assertTrue(nts.equals(cts));
    }
    
}
