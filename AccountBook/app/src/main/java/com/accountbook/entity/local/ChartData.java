package com.accountbook.entity.local;

public class ChartData {
    private String classify;
    private String role;
    private float percent;
    private String color;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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
        sb.append(", role='").append(role).append('\'');
        sb.append(", percent=").append(percent);
        sb.append(", color='").append(color).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
