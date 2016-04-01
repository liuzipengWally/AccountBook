package com.accountbook.presenter;

import android.content.Context;

import com.accountbook.biz.api.IClassifyBiz;
import com.accountbook.biz.impl.ClassifyBiz;
import com.accountbook.entity.local.Classify;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.view.api.IClassifyView;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public class ClassifyPresenter {
    private IClassifyBiz mClassifyBiz;
    private IClassifyView mClassifyView;

    private QuickSimpleIO mSimpleIO;

    public ClassifyPresenter(IClassifyView classifyView, Context context) {
        this.mClassifyView = classifyView;
        this.mClassifyBiz = new ClassifyBiz();
        this.mSimpleIO = new QuickSimpleIO(context, "version_sp");
    }

    public void loadClassifyData(int type) {
        mClassifyBiz.query(type, new ClassifyBiz.OnQueryClassifyListener() {
            @Override
            public void querySuccess(List<Classify> classifies) {
                mClassifyView.loadData(classifies);
            }

            @Override
            public void queryFailed() {
                mClassifyView.loadFailed();
            }
        });
    }

    public void deleteClassify(final String id) {
        mClassifyBiz.delete(id, new ClassifyBiz.OnDeleteClassifyListener() {
            @Override
            public void deleteSuccess() {
                mSimpleIO.setBoolean("needSync", true);
                mSimpleIO.setInt("classifyVer", mSimpleIO.getInt("classifyVer") + 1);
                mClassifyView.deleteSuccess(id);
            }

            @Override
            public void deleteFailed() {
                mClassifyView.deleteFailed(id);
            }
        });
    }

    public void recoveryClassify(final String id) {
        mClassifyBiz.recovery(id, new ClassifyBiz.OnRecoveryClassifyListener() {
            @Override
            public void recoverySuccess() {
                mClassifyView.recoverySuccess();
            }

            @Override
            public void recoveryFailed() {
                mClassifyView.recoveryFailed(id);
            }
        });
    }
}
