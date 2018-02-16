package com.immersionslabs.lcatalog.Utils;

public class BudgetManager {

    private static Integer Current_Value = 0, Total_Budget = 0, Remaining_Budget = 0;

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

}

