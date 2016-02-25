package com.accountbook.tools;

/**
 * 常量容器，用于放置本项目中会使用到的常量
 */
public class ConstantContainer {

    //// TODO: 16/2/25  下面四个常量分别代表收入，支出，借入，借出四种金钱类型
    /**
     * 收入
     */
    public static final int INCOME = 0;
    /**
     * 支出
     */
    public static final int EXPEND = 1;
    /**
     * 借入
     */
    public static final int BORROW = 2;
    /**
     * 借出
     */
    public static final int LEND = 3;

    //// TODO: 16/2/25 下面两个常量分别代表，主页卡片，主页近期记录的item。是在adapter适配数据的时候，区分View用的
    /**
     * 主页卡片
     */
    public static final int HOME_CARD = 0;
    /**
     * 主页近期记录Item
     */
    public static final int HOME_ITEM = 1;
}
