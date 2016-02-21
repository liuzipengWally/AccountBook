package com.accountbook.entity.sqlite;


/**
 * User实体类，本地不保存密码
 */
public class User{
    private String id;          //主键
    private String username;    //用户名
    private String email;       //邮箱
    private String fid;         //家庭号
    private String actor;       //角色、身份
    private double money;       //个人存款

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
