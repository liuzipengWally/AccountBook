package com.accountbook.entity;

/**
 * Created by liuzipeng on 16/3/21.
 */
public class ChartData {
    private String classify;
    private float percent;
    private String color;

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChartData{");
        sb.append("classify='").append(classify).append('\'');
        sb.append(", percent=").append(percent);
        sb.append(", color='").append(color).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
