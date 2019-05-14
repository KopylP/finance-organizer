package com.kopyl.financeorganaizer;

import java.util.Date;
import java.util.UUID;

public class Expense {
    private UUID mId;
    private double mCost;
    private String mName;
    private Date mDate;
    private boolean mIsComing;
    public Expense()
    {
        mDate = new Date();
        mId = UUID.randomUUID();
        mIsComing = false;
    }
    public Expense(UUID uuid, Date date, String name, double cost, boolean isComing)
    {
        mId = uuid;
        mDate = date;
        mName = name;
        mCost = cost;
        mIsComing = isComing;
    }

    public UUID getId() {
        return mId;
    }


    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isComing() {
        return mIsComing;
    }

    public void setComing(boolean coming) {
        mIsComing = coming;
    }

    public String getFileName() {
        return "IMG_" + mId.toString() + ".jpg";
    }
}
