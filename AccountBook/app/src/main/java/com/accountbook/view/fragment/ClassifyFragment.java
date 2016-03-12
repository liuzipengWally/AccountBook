package com.accountbook.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.accountbook.R;
import com.accountbook.entity.Classify;
import com.accountbook.presenter.ClassifyPresenter;
import com.accountbook.view.api.IClassifyView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassifyFragment extends Fragment implements IClassifyView {
    private static final String ARG_TYPE = "type";
    @Bind(R.id.classify_list)
    RecyclerView mClassifyList;
    @Bind(R.id.RootLayout)
    FrameLayout mRootLayout;

    private int mType;

    private Context mContext;
    private View mLayoutView;

    private ClassifyPresenter mPresenter;

    public ClassifyFragment() {
        // Required empty public constructor
    }

    /**
     * fragment的工厂方法，用于构造对象，并且方便传递参数
     *
     * @param classifyType 这一页分类的类别 既 支出，收入，借入，借出
     * @return 构造完成的对象
     */
    public static ClassifyFragment newInstance(int classifyType) {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, classifyType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = mLayoutView = inflater.inflate(R.layout.classify_fragment, container, false);
        ButterKnife.bind(this, view);
        init();

        return view;
    }

    private void init() {
        mPresenter = new ClassifyPresenter(this);
        mPresenter.loadClassifyData(mType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadData(List<Classify> classifies) {

    }

    @Override
    public void loadFailed() {
        Snackbar.make(mRootLayout, "获取分类信息失败，请重试", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadClassifyData(mType);
            }
        }).show();
    }
}
