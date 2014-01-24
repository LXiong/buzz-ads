package com.buzzinate.buzzads.core.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.database.annotations.Transactional;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.buzzinate.buzzads.core.dao.AdvertiserBalanceDao;
import com.buzzinate.buzzads.domain.AdvertiserBalance;

@Test(groups = { "dao", "mysql" })
@SpringApplicationContext("test_applicationContext.xml")
@DataSet
@Transactional
public class AdvertiserBalanceDaoTest extends UnitilsTestNG {
    
    @TestedObject
    private AdvertiserBalanceDao advertiserBalanceDao;
    
    @SpringBeanByName
    @InjectInto(property = "sessionFactory") 
    private SessionFactory sessionFactory;
    
    
    @Test
    public void testGetBalances() {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(3);
        Map<Integer, Long> res = advertiserBalanceDao.getBalances(ids);
        Assert.assertTrue(res.get(1) == 10);
        Assert.assertTrue(res.get(3) == 30);
    }
    
    @Test
    public void testUpdateBalanceForCreditsCreate() {
        advertiserBalanceDao.insertOrUpdateForCredits(4, 100);
        AdvertiserBalance ab = advertiserBalanceDao.read(4);
        Assert.assertTrue(ab.getCreditsTotal() == 100);
        Assert.assertTrue(ab.getBalance() == 100);
    }
    
    @Test
    public void testUpdateBalanceForCreditsUpdate() {
        advertiserBalanceDao.insertOrUpdateForCredits(1, 10);
        AdvertiserBalance ab = advertiserBalanceDao.read(1);
        Assert.assertTrue(ab.getCreditsTotal() == 210);
        Assert.assertTrue(ab.getBalance() == 20);
    }
    
    
    @Test
    public void testUpdateBalanceForDebitsCreate() {
        advertiserBalanceDao.insertOrUpdateForDebits(5, 100);
        AdvertiserBalance ab = advertiserBalanceDao.read(5);
        Assert.assertTrue(ab.getDebitsTotal() == 100);
        Assert.assertTrue(ab.getBalance() == -100);
    }
    
    @Test
    public void testUpdateBalanceForDebitsUpdate() {
        advertiserBalanceDao.insertOrUpdateForDebits(2, 10);
        AdvertiserBalance ab = advertiserBalanceDao.read(2);
        Assert.assertTrue(ab.getDebitsTotal() == 310);
        Assert.assertTrue(ab.getBalance() == 10);
    }
    
}
