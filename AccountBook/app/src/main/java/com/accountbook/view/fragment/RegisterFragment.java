package com.accountbook.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.accountbook.R;
import com.accountbook.presenter.RegistryPresenter;
import com.accountbook.view.api.IRegistryView;
import com.accountbook.view.customview.ProgressButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterFragment extends Fragment implements IRegistryView {
    @Bind(R.id.reg_usernameInput)
    EditText mRegUsernameInput;
    @Bind(R.id.reg_usernameWrapper)
    TextInputLayout mRegUsernameWrapper;
    @Bind(R.id.reg_passwordInput)
    EditText mRegPasswordInput;
    @Bind(R.id.reg_passwordWrapper)
    TextInputLayout mRegPasswordWrapper;
    @Bind(R.id.reg_passwordConfirmInput)
    EditText mRegPasswordConfirmInput;
    @Bind(R.id.reg_passwordConfirmWrapper)
    TextInputLayout mRegPasswordConfirmWrapper;
    @Bind(R.id.reg_btn)
    ProgressButton mRegBtn;

    private Context mContext;
    private RegistryPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registry_activity, container, false);

        ButterKnife.bind(this, view);
        init();

        return view;
    }

    private void init() {
        mPresenter = new RegistryPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindEvent();
    }

    private void bindEvent() {
        mRegBtn.setOnClickListener(new ProgressButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doRegistry(mContext);
            }
        });
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
    public void showProgress() {
        mRegBtn.showProgress();
    }

    @Override
    public String getRegUsername() {
        return mRegUsernameInput.getText().toString();
    }

    @Override
    public String getRegPassword() {
        return mRegPasswordInput.getText().toString();
    }

    @Override
    public String getRegPasswordConfirm() {
        return mRegPasswordConfirmInput.getText().toString();
    }

    @Override
    public void registerSuccess() {
        mRegBtn.done();
        mRegBtn.setDoneListener(new ProgressButton.OnProgressDoneListener() {
            @Override
            public void done() {
                getActivity().setResult(2);
                getActivity().finish();
            }
        });
    }

    @Override
    public void registerFailed(String message) {
        mRegBtn.error(message);
    }
}
