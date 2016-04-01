package com.accountbook.entity.local;

public class Classify {
    private String id;
    private String classify;
    private String color;
    private int iconResId;
    private int type;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Classify{");
        sb.append("id='").append(id).append('\'');
        sb.append(", classify='").append(classify).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", iconResId=").append(iconResId);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
