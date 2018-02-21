package com.immersionslabs.lcatalog.Utils;

import java.util.ArrayList;

public class BudgetManager {

    private static Long Current_Value = Long.valueOf(0), Total_Budget = Long.valueOf(0), Remaining_Budget = Long.valueOf(0);
    private static ArrayList<String> Articles = new ArrayList<String>();

    public Long BUDGET_GET_CURRENT() {
        return Current_Value;
    }

    public Long BUDGET_GET_REMAINING() {
        Remaining_Budget = BUDGET_GET_TOTAL() - BUDGET_GET_CURRENT();
        return Remaining_Budget;
    }

    public Long BUDGET_GET_TOTAL() {
        return Total_Budget;
    }

    public void BUDGET_SET_CURRENT(Long current_Value) {
        Current_Value = current_Value;
    }

    public void BUDGET_SET_REMAINING(Long remaining_Budget) {
        Remaining_Budget = remaining_Budget;
    }

    public void BUDGET_SET_TOTAL(Long total_Budget) {
        Total_Budget = total_Budget;
    }

    public void BUDGET_ADD_ARTICLE(String article_id) {
        Articles.add(article_id);
    }

    public void BUDGET_REMOVE_ARTICLE(String article_id) {
        if (Articles.contains(article_id)) {
            Articles.remove(article_id);
        }
    }

    public boolean BUDGET_IS_ARTICLE_EXISTS(String article_id) {
        Articles.add("empty");
        boolean flag;
        if (Articles.contains(article_id)) {
            flag = true;
        } else {
            flag = false;
        }
        Articles.remove("empty");
        return flag;
    }

    public ArrayList BUDGET_GET_ARTICLE_IDS() {
        return Articles;
    }

    public void BUDGET_CLEAR_ARRAY_ARTICLES() {
        Articles.clear();
    }
}




