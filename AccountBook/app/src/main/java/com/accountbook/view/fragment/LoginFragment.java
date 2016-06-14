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
import com.accountbook.presenter.LoginPresenter;
import com.accountbook.view.api.ILoginView;
import com.accountbook.view.customview.ProgressButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment implements ILoginView {
    @Bind(R.id.login_usernameInput)
    EditText mLoginUsernameInput;
    @Bind(R.id.login_usernameWrapper)
    TextInputLayout mLoginUsernameWrapper;
    @Bind(R.id.login_passwordInput)
    EditText mLoginPasswordInput;
    @Bind(R.id.login_passwordWrapper)
    TextInputLayout mLoginPasswordWrapper;
    @Bind(R.id.login_btn)
    ProgressButton mLoginBtn;

    private Context mContext;
    private LoginPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        ButterKnife.bind(this, view);
        init();

        return view;
    }

    private void init() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindEvent();
    }

    private void bindEvent() {
        mLoginBtn.setOnClickListener(new ProgressButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doLogin(mContext);
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
    public String getLoginUsername() {
        return mLoginUsernameInput.getText().toString();
    }

    @Override
    public String getLoginPassword() {
        return mLoginPasswordInput.getText().toString();
    }

    @Override
    public void loginSuccess() {
        mLoginBtn.done();
        mLoginBtn.setDoneListener(new ProgressButton.OnProgressDoneListener() {
            @Override
            public void done() {
                getActivity().setResult(2);
                getActivity().finish();
            }
        });
    }

    @Override
    public void loginFailed(String message) {
        mLoginBtn.error(message);
    }

    @Override
    public void showProgress() {
        mLoginBtn.showProgress();
    }

}
