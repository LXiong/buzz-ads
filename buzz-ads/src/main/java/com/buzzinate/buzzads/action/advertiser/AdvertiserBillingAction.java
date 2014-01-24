package com.buzzinate.buzzads.action.advertiser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.buzzinate.buzzads.action.StatisticsBaseAction;
import com.buzzinate.buzzads.core.service.AdvertiserAccountService;
import com.buzzinate.buzzads.core.service.AdvertiserBalanceService;
import com.buzzinate.buzzads.core.service.AdvertiserBillingService;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdvertiserBilling;
import com.buzzinate.buzzads.enums.AdvertiserBillingType;
import com.buzzinate.common.util.DateTimeUtil;
import com.opensymphony.xwork2.Action;

/**
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 18, 2013 2:43:07 PM
 * 
 */
@Controller
@Scope("request")
public class AdvertiserBillingAction extends StatisticsBaseAction {
    private static final long serialVersionUID = -534291762163954L;
    private static final long MAX_RECHARGE_PRICE = 
                    ConfigurationReader.getLong("ads.max.advertiser.recharge", 10000000L);

    @Autowired
    private AdvertiserBillingService advertiserBillingService;
    @Autowired
    private AdvertiserBalanceService advertiserBalanceService;
    @Autowired
    private AdvertiserAccountService advertiserAccountService;

    private List<AdvertiserBilling> billings;
    private long balance;
    private int adId;
    private Map<Integer, String> advertiserNames;
    private List<AdvertiserBillingType> types = new ArrayList<AdvertiserBillingType>();;
    private String view = "day";

    private String poundage = ConfigurationReader.getString("buzzads.recharge.poundage.rate", "0");
    private DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");


    public String listBillings() {
        if (adId == 0 || !loginHelper.isLoginAsAdmin()) {
            adId = loginHelper.getUserId();
        }
        balance = advertiserBalanceService.getByAdvertiserId(adId).getBalance();

        initDateTimePicker();
        if (types.isEmpty()) {
            types.addAll(Arrays.asList(AdvertiserBillingType.values()));
        }
        if (view.equals("day"))
            billings = advertiserBillingService.listBillingsByDay(adId, dateStart, DateTimeUtil.plusDays(dateEnd, 1),
                    types, page);
        else
            billings = advertiserBillingService.listBillingsByMonth(adId, dateStart, DateTimeUtil.plusDays(dateEnd, 1),
                    types, page);
        return Action.SUCCESS;
    }

    public String listAllBillings() {
        advertiserNames = advertiserAccountService.getAllAdvertiserName();

        initDateTimePicker();
        if (types.isEmpty()) {
            types.addAll(Arrays.asList(AdvertiserBillingType.values()));
        }
        if (adId > 0) {
            if (view.equals("day"))
                billings = advertiserBillingService.listBillingsByDay(adId, dateStart,
                        DateTimeUtil.plusDays(dateEnd, 1), types, page);
            else
                billings = advertiserBillingService.listBillingsByMonth(adId, dateStart,
                        DateTimeUtil.plusDays(dateEnd, 1), types, page);
        } else {
            if (view.equals("day"))
                billings = advertiserBillingService.listBillingsByDay(dateStart, DateTimeUtil.plusDays(dateEnd, 1),
                        types, page);
            else
                billings = advertiserBillingService.listBillingsByMonth(dateStart, DateTimeUtil.plusDays(dateEnd, 1),
                        types, page);
        }
        for (AdvertiserBilling ab : billings) {
            ab.setCompanyName(advertiserNames.get(ab.getAdvertiserId()));
        }

        return Action.SUCCESS;
    }

    public String getBalanceDouble() {
        return doubleformat.format((double) balance / 100);
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<AdvertiserBilling> getBillings() {
        return billings;
    }

    public void setBillings(List<AdvertiserBilling> billings) {
        this.billings = billings;
    }

    public String getTypes() {
        if (types.size() > 1)
            return "10";
        return Integer.valueOf(types.get(0).getCode()).toString();
    }

    public void setTypes(String types) {
        this.types = new ArrayList<AdvertiserBillingType>();
        if (types.equals("10")) {
            this.types.addAll(Arrays.asList(AdvertiserBillingType.values()));
        } else
            this.types.add(AdvertiserBillingType.findByValue(Integer.valueOf(types)));
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getAdId() {
        return Integer.valueOf(adId).toString();
    }

    public void setAdId(String adId) {
        this.adId = Integer.valueOf(adId);
    }

    public Map<Integer, String> getAdvertiserNames() {
        return advertiserNames;
    }

    public void setAdvertiserNames(Map<Integer, String> advertiserNames) {
        this.advertiserNames = advertiserNames;
    }

    public String getPoundage() {
        return poundage;
    }

    public long getMaxRechargePrice() {
        return MAX_RECHARGE_PRICE;
    }
}
