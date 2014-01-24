package com.buzzinate.buzzads.core.service;

import com.buzzinate.buzzads.core.bean.PublisherSettlementStats;
import com.buzzinate.buzzads.core.dao.PublisherContactInfoDao;
import com.buzzinate.buzzads.core.dao.PublisherSettlementDao;
import com.buzzinate.buzzads.core.util.Pagination;
import com.buzzinate.buzzads.domain.PublisherContactInfo;
import com.buzzinate.buzzads.domain.PublisherSettlement;
import com.buzzinate.buzzads.enums.SettlementStatusEnum;
import com.buzzinate.common.util.DateTimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-12-17
 */
@Service
public class PublisherSettlementService {

    @Autowired
    private PublisherSettlementDao publisherSettlementSettleDao;
    @Autowired
    private PublisherContactInfoDao publisherContactInfoDao;

    @Transactional(value = "buzzads", readOnly = true)
    public List<PublisherSettlement> listByProperties(String searchName, Integer amount, Date month, Pagination page) {
        List<PublisherSettlement> pubSettles = null;
        if (StringUtils.isNotBlank(searchName)) {
            List<PublisherContactInfo> infos = publisherContactInfoDao.findAllUserByEmail(searchName);
            if (infos.size() > 0) {
                List<Integer> userIds = new ArrayList<Integer>();
                for (PublisherContactInfo info : infos) {
                    userIds.add(info.getUserId());
                }
                if (amount == null) {
                    pubSettles = publisherSettlementSettleDao.listByUserMonth(userIds, month, page);
                } else {
                    pubSettles = publisherSettlementSettleDao.listByUserAmountMonth(userIds, amount * 100, month, page);
                }
            } else {
                pubSettles = new ArrayList<PublisherSettlement>();
            }
        } else if (amount != null) {
            pubSettles = publisherSettlementSettleDao.listByAmountMonth(amount * 100, month, page);
        } else {
            pubSettles = publisherSettlementSettleDao.listByMonth(month, page);
        }
        getPublisherInfo(pubSettles);
        return pubSettles;
    }

    /**
     * 符合条件的所有commission之和
     *
     * @param searchName
     * @param amount
     * @param month
     * @return
     */
    public int getTotalCommission(String searchName, Integer amount, Date month) {
        int totalCommission = 0;
        if (StringUtils.isNotBlank(searchName)) {
            List<PublisherContactInfo> infos = publisherContactInfoDao.findAllUserByEmail(searchName);
            if (infos.size() > 0) {
                List<Integer> userIds = new ArrayList<Integer>();
                for (PublisherContactInfo info : infos) {
                    userIds.add(info.getUserId());
                }
                if (amount == null) {
                    totalCommission = publisherSettlementSettleDao.getTotalCommissionByUserMonth(userIds, month);
                } else {
                    totalCommission = publisherSettlementSettleDao.getTotalCommissionByUserAmountMonth(userIds,
                            amount * 100, month);
                }
            }
        } else if (amount != null) {
            totalCommission = publisherSettlementSettleDao.getTotalCommissionByAmountMonth(amount * 100, month);
        } else {
            totalCommission = publisherSettlementSettleDao.getTotalCommissionByMonth(month);
        }
        return totalCommission;
    }

    private void getPublisherInfo(List<PublisherSettlement> pubSettles) {
        if (pubSettles.size() > 0) {
            List<Integer> userIds = new ArrayList<Integer>(pubSettles.size());
            for (PublisherSettlement ps : pubSettles) {
                userIds.add(ps.getUserId());
            }
            List<PublisherContactInfo> publishers = publisherContactInfoDao.findByUserIds(userIds);
            for (PublisherSettlement ps : pubSettles) {
                for (PublisherContactInfo p : publishers) {
                    if (ps.getUserId() == p.getUserId()) {
                        ps.setEmail(p.getEmail());
                        ps.setLinkName(p.getName());
                        ps.setReceiveName(p.getReceiveName());
                        ps.setReceiveBankCode(p.getReceiveBankCode());
                        ps.setReceiveAccount(p.getReceiveAccount());
                    }
                }
            }
        }
    }

    public List<PublisherSettlement> getByUserId(int userId) {
        return publisherSettlementSettleDao.getByUserId(userId);
    }

    public PublisherSettlement getByUserIdAndMonth(int userId, Date month) {
        return publisherSettlementSettleDao.getByUserIdAndMonth(userId, month);
    }

    public void saveOrUpdate(PublisherSettlement publisherSettlement) {
        publisherSettlementSettleDao.saveOrUpdate(publisherSettlement);
    }

    public void insertOrUpdate(int userId, PublisherSettlementStats stats) {
        Date month = DateUtils.truncate(stats.getDay(), Calendar.MONTH);
        publisherSettlementSettleDao.insertOrUpdate(userId, month,
                stats.getCpsNo(), stats.getCpsCommission(), stats.getCpcNo(),
                stats.getCpcCommission(), stats.getCpmNo(), stats.getCpmCommission(), DateTimeUtil.getCurrentDate());
    }

    public int getUnPaidUserCommission(int userId) {
        int comm = 0;
        List<PublisherSettlement> list = getByUserId(userId);
        for (PublisherSettlement ps : list) {
            if (ps.getStatus().equals(SettlementStatusEnum.UNPAID)) {
                comm += ps.getTotalCommission();
            }
        }
        return comm;
    }

    public int getPaidUserCommission(int userId) {
        int comm = 0;
        List<PublisherSettlement> list = getByUserId(userId);
        for (PublisherSettlement ps : list) {
            if (ps.getStatus().equals(SettlementStatusEnum.PAID)) {
                comm += ps.getTotalCommission();
            }
        }
        return comm;
    }

    public int getTotalCommByStatus(SettlementStatusEnum status) {
        return publisherSettlementSettleDao.getTotalCommByStatus(status);
    }
}
