package com.accountbook.presenter;

import com.accountbook.biz.api.IAddBiz;
import com.accountbook.biz.impl.AddBiz;
import com.accountbook.entity.RecordAdd;
import com.accountbook.entity.Role;
import com.accountbook.view.api.IAddView;

import java.util.List;


/**
 * Created by liuzipeng on 16/3/8.
 */
public class AddRecordPresenter {
    private IAddBiz mAddBiz;
    private IAddView mAddView;

    public AddRecordPresenter(IAddView addView) {
        this.mAddView = addView;
        this.mAddBiz = new AddBiz();
    }

    public void queryRole() {
        mAddBiz.queryRole(new AddBiz.OnQueryRoleListener() {
            @Override
            public void querySuccess(List<Role> roles) {
                mAddView.loadRole(roles);
            }

            @Override
            public void queryFailed() {
                mAddView.loadRoleFailed();
            }
        });
    }

    public void saveRecord() {
        if (validateComplete()) {
            RecordAdd record = new RecordAdd();
            record.setMoney(mAddView.getMoney());
            record.setDescription(mAddView.getDescription());
            record.setBorrowName(mAddView.getBorrowing());
            record.setClassifyId(mAddView.getClassifyId());
            record.setAccount(mAddView.getAccount());
            record.setRoleId(mAddView.getRoleId());
            record.setCreateTime(mAddView.getCreateTime());

            mAddBiz.saveRecord(record, new AddBiz.OnRecordSaveListener() {
                @Override
                public void saveSuccess() {
                    mAddView.saveSuccess();
                }

                @Override
                public void saveFailed() {
                    mAddView.saveFailed();
                }
            });
        }
    }

    private boolean validateComplete() {
        if (mAddView.getMoney() == 0) {
            mAddView.validateFailed("请填写金额");

            return false;
        } else if (mAddView.getClassifyId().equals("")) {
            mAddView.validateFailed("必须选择分类");

            return false;
        }

        return true;
    }
}
