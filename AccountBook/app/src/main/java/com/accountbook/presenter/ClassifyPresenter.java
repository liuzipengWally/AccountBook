package com.accountbook.presenter;

import com.accountbook.biz.api.IClassifyBiz;
import com.accountbook.biz.impl.ClassifyBiz;
import com.accountbook.entity.Classify;
import com.accountbook.view.api.IClassifyView;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/12.
 */
public class ClassifyPresenter {
    private IClassifyBiz mClassifyBiz;
    private IClassifyView mClassifyView;

    public ClassifyPresenter(IClassifyView classifyView) {
        this.mClassifyView = classifyView;
        this.mClassifyBiz = new ClassifyBiz();
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
}
