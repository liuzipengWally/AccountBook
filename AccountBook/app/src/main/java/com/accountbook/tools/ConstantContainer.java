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

    /**
     * toolbar的默认高度，单位为dp，所以在java代码中还需要转px
     */
    public static final int TOOLBAR_HEIGHT = 56;

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static final int NORMAL_ITEM = 0;
    public static final int USE_DATE_ITEM = 1;

    public static final int EDIT_RECROD_REQUEST = 0;
    public static final int BUDGET_REQUEST = 1;
}
