package com.buzzinate.buzzads.billing.task;

import com.buzzinate.buzzads.domain.AdCampBudget;

/**
 * 
 * @author zyeming
 *
 */
public final class BudgetUtils {

    // 预警比例
    private static final int BUDGET_WARN_PER = 10;
    // 预警预算  
    private static final int BUDGET_WARN_AMOUNT = 10000;
    // 余额预警
    private static final int BALANCE_WARN_AMOUNT = 20000;
    
    private BudgetUtils() { }
    
    public static boolean isBalanceWarning(long balance) {
        return balance < BALANCE_WARN_AMOUNT;
    }
    
    public static boolean isBudgetWarning(AdCampBudget campBudget) {
        boolean dayWarning = false;
        boolean totalWarning = false;
        
        if (campBudget.getMaxBudgetDay() > 0) {
            // 剩余预算同时小于一定值和一定比例，则进行预警；剩余为负数也同样成立
            long remainDay = campBudget.getMaxBudgetDay() - campBudget.getBudgetDay();
            dayWarning = remainDay < BUDGET_WARN_AMOUNT && 
                            (remainDay * 100 / campBudget.getMaxBudgetDay() < BUDGET_WARN_PER);
        }
        
        if (campBudget.getMaxBudgetTotal() > 0) {
            long remainTotal = campBudget.getMaxBudgetTotal() - campBudget.getBudgetTotal();
            totalWarning = remainTotal < BUDGET_WARN_AMOUNT && (
                            remainTotal * 100 / campBudget.getMaxBudgetTotal() < BUDGET_WARN_PER);
        }
        
        return dayWarning || totalWarning;
    }
    
    
}
