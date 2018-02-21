package com.immersionslabs.lcatalog.Utils;

import java.util.ArrayList;

public class BudgetManager {

    private static Long Current_Value = Long.valueOf(0), Total_Budget = Long.valueOf(0), Remaining_Budget = Long.valueOf(0);
    private static ArrayList<String> Articles = new ArrayList<String>();

    public Long getCurrent_Value() {
        return Current_Value;
    }

    public Long getRemaining_Budget() {
        Remaining_Budget = getTotal_Budget() - getCurrent_Value();
        return Remaining_Budget;
    }

    public Long getTotal_Budget() {
        return Total_Budget;
    }

    public void setCurrent_Value(Long current_Value) {
        Current_Value = current_Value;
    }

    public void setRemaining_Budget(Long remaining_Budget) {
        Remaining_Budget = remaining_Budget;
    }

    public void setTotal_Budget(Long total_Budget) {
        Total_Budget = total_Budget;
    }

    public void Add_Articles(String article_id) {
        Articles.add(article_id);
    }

    public void RemoveArticles(String article_id) {
        if (Articles.contains(article_id)) {
            Articles.remove(article_id);
        }

    }

    public boolean IS_ARTICLE_EXISTS(String article_id) {
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

    public ArrayList GET_BUDGET_ARTICLE_IDS() {
        return Articles;
    }


    public void CLEAR_ARRAY_ARTICLES()
    {
        Articles.clear();
    }
}




