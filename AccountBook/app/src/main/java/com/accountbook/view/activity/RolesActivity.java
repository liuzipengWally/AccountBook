package com.accountbook.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.accountbook.R;
import com.accountbook.entity.local.Role;
import com.accountbook.presenter.RolePresenter;
import com.accountbook.view.adapter.RolesListAdapter;
import com.accountbook.view.api.IRoleView;
import com.accountbook.view.customview.AutoHideFab;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RolesActivity extends AppCompatActivity implements IRoleView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.RoleList)
    RecyclerView mRoleList;
    @Bind(R.id.Refresh)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.Edit_btn)
    AutoHideFab mEditBtn;
    @Bind(R.id.RootLayout)
    CoordinatorLayout mRootLayout;

    private RolePresenter mPresenter;
    private RolesListAdapter mAdapter;

    private List<Role> mRoles;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roles_activity);
        ButterKnife.bind(this);

        init();
        eventBind();
    }

    private void eventBind() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RolesActivity.this);
                builder.setTitle("添加新的家庭成员");
                View view = LayoutInflater.from(RolesActivity.this).inflate(R.layout.role_add_content, null);
                builder.setView(view);
                final EditText roleEdit = (EditText) view.findViewById(R.id.roleEdit);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!roleEdit.getText().toString().equals("")) {
                            Role role = new Role();
                            role.setRole(roleEdit.getText().toString());
                            mPresenter.saveRole(role);
                        } else {
                            Toast.makeText(RolesActivity.this, "家庭成员不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.show();
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.queryRole();
            }
        });
    }

    private void init() {
        mPresenter = new RolePresenter(this);
        setSupportActionBar(mToolbar);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRoleList.setLayoutManager(manager);

        mPresenter.queryRole();
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        mPresenter.queryRole();
    }

    @Override
    public void saveFailed() {
        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadRole(List<Role> roles) {
        mRoles = roles;
        mAdapter = new RolesListAdapter(roles, this);
        mRoleList.setAdapter(mAdapter);
        mRefresh.setRefreshing(false);

        itemEventBind();
    }

    private void itemEventBind() {
        mAdapter.setItemClickListener(new RolesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                mPresenter.deleteRole(mRoles.get(position).getId());
            }
        });
    }

    @Override
    public void deleteSuccess() {
        mAdapter.removeItem(mPosition);
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadFailed() {
        Toast.makeText(this, "载入数据失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteFailed() {
        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
    }
}
