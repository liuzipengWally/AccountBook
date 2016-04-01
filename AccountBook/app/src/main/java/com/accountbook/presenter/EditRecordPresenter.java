package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.accountbook.biz.api.IEditBiz;
import com.accountbook.biz.impl.EditBiz;
import com.accountbook.entity.local.Record;
import com.accountbook.entity.local.Role;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.IEditView;

import java.util.List;

public class EditRecordPresenter {
    private IEditBiz mAddBiz;
    private IEditView mAddView;

    private QuickSimpleIO mSimpleIO;

    public EditRecordPresenter(IEditView addView) {
        this.mAddView = addView;
        this.mAddBiz = new EditBiz();
        this.mSimpleIO = new QuickSimpleIO((Context) addView, "version_sp");
    }

    public void queryRole() {
        mAddBiz.queryRole(new EditBiz.OnQueryRoleListener() {
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
        Record record = mAddView.getRecordInfo();

        if (validateComplete(record)) {

            mAddBiz.saveRecord(record, new EditBiz.OnRecordSaveListener() {
                @Override
                public void saveSuccess() {
                    mSimpleIO.setBoolean("needSync", true);
                    mAddView.saveSuccess();
                }

                @Override
                public void saveFailed() {
                    mAddView.saveFailed();
                }
            });
        }
    }


    private boolean validateComplete(Record record) {
        if (record.getMoney() == 0) {
            mAddView.validateFailed("请填写金额");

            return false;
        }
        return true;
    }

    public void loadAlterRecordDate(final String id) {
        mAddBiz.queryRecord(id, new EditBiz.OnQueryRecordListener() {
            @Override
            public void querySuccess(Record recordAdds) {
                mAddView.loadAlterRecordDate(recordAdds);
            }

            @Override
            public void queryFailed() {
                loadAlterRecordDate(id);
            }
        });
    }

    public void alterRecord() {
        Record record = mAddView.getRecordInfo();
        if (validateComplete(record)) {
            mAddBiz.alterRecord(record, new EditBiz.OnAlterRecordListener() {
                @Override
                public void alterSuccess() {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSimpleIO.setBoolean("needSync", true);
                            mAddView.alterSuccess();
                        }
                    });
                }

                @Override
                public void alterFailed() {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAddView.alterFailed();
                        }
                    });
                }
            });
        }
    }
}
