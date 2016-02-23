package com.accountbook.entity;


import com.avos.avoscloud.AVUser;

public class UserForLeanCloud extends AVUser {

    //主键、用户名、邮箱字段ACUser已经有了
    public static final String FID = "fid";
    public static final String ACTOR = "actor";
    public static final String MONEY = "money";
//    public static final String MODIFIED = "modified";       //修改标记

    public String getFid(){
        return getString(FID);
    }

    public void setFid(String fid){
        put(FID,fid);
    }

    public String getActor(){
        return getString(ACTOR);
    }

    public void setActor(String actor){
        put(ACTOR,actor);
    }

    public double getMoney(){
        return getDouble(MONEY);
    }

    public void setMoney(double money){
        put(MONEY,money);
    }

//    public boolean getModified(){
//        return getBoolean(MODIFIED);
//    }
//
//    public void setModified(boolean modified){
//        put(MODIFIED,modified);
//    }
}
