package com.accountbook.entity.local;

public class Budget {
    private String id;
    private String classify;
    private String classifyId;
    private int currMoney;
    private int countMoney;
    private long startTime;
    private long endTime;
    private String description;
    private String color;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public int getCurrMoney() {
        return currMoney;
    }

    public void setCurrMoney(int currMoney) {
        this.currMoney = currMoney;
    }

    public int getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(int countMoney) {
        this.countMoney = countMoney;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Budget{");
        sb.append("classify='").append(classify).append('\'');
        sb.append(", classifyId='").append(classifyId).append('\'');
        sb.append(", currMoney=").append(currMoney);
        sb.append(", countMoney=").append(countMoney);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
