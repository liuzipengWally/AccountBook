package com.accountbook.view.api;

import com.accountbook.entity.Budget;
import com.accountbook.entity.Record;
import com.accountbook.entity.Role;

import java.util.List;

public interface IEditBudgetView {

    /**
     * 获取要保存的Budget对象
     *
     * @return
     */
    Budget getBudgetInfo();

    /**
     * 验证数据失败
     */
    void validateFailed(String msg);

    /**
     * 添加数据失败
     */
    void saveFailed();

    /**
     * 添加数据成功
     */
    void saveSuccess();

    /**
     * 载入要修改的数据
     */
    void loadAlterBudgetDate(Budget budget);

    /**
     * 修改成功
     */
    void alterSuccess();

    /**
     * 修改失败
     */
    void alterFailed();
}
