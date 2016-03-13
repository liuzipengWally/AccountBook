package com.accountbook.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.accountbook.R;
import com.accountbook.entity.Classify;
import com.accountbook.presenter.ClassifyPresenter;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.view.activity.EditClassifyActivity;
import com.accountbook.view.adapter.ClassifyAdapter;
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
    private ClassifyAdapter mAdapter;
    private String[] mDialogOptions = {"编辑", "删除"};
    private int mDeletePosition;
    private Classify mCurrClassify;

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
        mPresenter.loadClassifyData(mType);

        return view;
    }

    private void init() {
        //这个recyclerView因为要显示的是图标，所以以grid方式显示
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        mClassifyList.setLayoutManager(gridLayoutManager);
        mClassifyList.setItemAnimator(new DefaultItemAnimator());

        mPresenter = new ClassifyPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindEvent();
    }

    private void bindEvent() {
        mAdapter.setOnItemClickListener(new ClassifyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int type, String classify, String id) {
                //当item被点击，将这个item的关键数据传回addActivity，并关闭当前activity
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                bundle.putString("classify", classify);
                bundle.putString("id", id);
                intent.putExtras(bundle);

                getActivity().setResult(0, intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(final Classify classify, final int position) {
                //如果是长按item，则弹出对话框，询问用户是要编辑这个分类，还是删除
                if (classify.getType() != ConstantContainer.BORROW && classify.getType() != ConstantContainer.LEND) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.options);
                    builder.setItems(mDialogOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //选择编辑的时候，这个实体数据传递到编辑的Activity
                                    Intent intent = new Intent(getActivity(), EditClassifyActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", classify.getId());
                                    bundle.putString("classify", classify.getClassify());
                                    bundle.putString("color", classify.getColor());
                                    bundle.putInt("iconResId", classify.getIconResId());
                                    bundle.putInt("type", classify.getType());

                                    startActivityForResult(intent, 1, bundle);
                                    break;
                                case 1:
                                    //选择删除的时候，调用Presenter的删除方法，删除该item
                                    mDeletePosition = position;
                                    mCurrClassify = classify;
                                    mPresenter.deleteClassify(classify.getId());
                                    break;
                            }
                        }
                    });

                    builder.show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: 16/3/13 处理添加新分类和修改分类的数据
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
        mAdapter = new ClassifyAdapter(classifies, mContext);
        mClassifyList.setAdapter(mAdapter);
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

    @Override
    public void deleteSuccess(final String id) {
        mAdapter.removeItem(mDeletePosition);
        Snackbar.make(mRootLayout, "删除成功", Snackbar.LENGTH_LONG).setAction("恢复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryClassify(id);
            }
        }).show();
    }

    @Override
    public void deleteFailed(final String id) {
        Snackbar.make(mRootLayout, "删除失败，请重试", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteClassify(id);
            }
        }).show();
    }

    @Override
    public void recoverySuccess() {
        mAdapter.addItem(mDeletePosition, mCurrClassify);
        Snackbar.make(mRootLayout, "已恢复", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void recoveryFailed(final String id) {
        Snackbar.make(mRootLayout, "恢复失败，请重试", Snackbar.LENGTH_LONG).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.recoveryClassify(id);
            }
        }).show();
    }
}
