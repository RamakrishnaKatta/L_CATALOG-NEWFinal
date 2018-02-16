package com.immersionslabs.lcatalog.Utils;

import com.immersionslabs.lcatalog.Article3dViewActivity;

import java.util.ArrayList;

public class BudgetManager
{

    private static Integer Current_Value = 0, Total_Budget = 0, Remaining_Budget = 0;
    private static ArrayList<String> Articles=new ArrayList<String>();




    public Integer getCurrent_Value() {
        return Current_Value;
    }

    public Integer getRemaining_Budget() {
        return Remaining_Budget;
    }

    public Integer getTotal_Budget() {
        return Total_Budget;
    }

    public void setCurrent_Value(Integer current_Value) {
        Current_Value = current_Value;
    }

    public void setRemaining_Budget(Integer remaining_Budget) {
        Remaining_Budget = remaining_Budget;
    }

    public void setTotal_Budget(Integer total_Budget) {
        Total_Budget = total_Budget;
    }

    public void Add_Articles(String article_id)
    {Articles.add(article_id);

    }
    public void RemoveArticles(String article_id)
    {
        if(Articles.contains(article_id))
        {
            Articles.remove(article_id);
        }

    }
    public boolean IS_ARTICLE_EXISTS(String article_id)
    { Articles.add("empty");
        boolean flag;
        if(Articles.contains(article_id))
        {
            flag=true;
        }
        else
        {

                flag=false;

        }
        Articles.remove("empty");
        return flag;


    }
    }




