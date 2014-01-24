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

@Test(groups = { "dao", "mysql" })
@SpringApplicationContext("test_applicationContext.xml")
@DataSet
@Transactional
public class AdvertiserBillingDaoTest extends UnitilsTestNG {
    
    @TestedObject
    private AdvertiserBillingDao advertiserBillingDao;
    
    @SpringBeanByName
    @InjectInto(property = "sessionFactory") 
    private SessionFactory sessionFactory;
    
    
    @Test
    public void testGetBalances() {

    }
    
    
}
