package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.biz.api.OnRegistryListener;
import com.accountbook.entity.User;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.application.MyApplication;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;


public class UserBiz implements IUserBiz {
    private SQLiteDatabase mDatabase;

    public UserBiz() {
        this.mDatabase = SQLite.getInstance().getDatabaseObject();
    }

    /**
     * 处理登录
     *
     * @param context  上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    @Override
    public void login(final Context context, final String username, final String password, final OnLoginListener listener) {

        if (Util.isNetworkAvailable(context)) {

            AVUser.logInInBackground(username, password, new LogInCallback<UserForLeanCloud>() {

                @Override
                public void done(UserForLeanCloud avUser, AVException e) {
                    if (avUser == null) {
                        listener.loginFailed(Util.getLocalizeLeanCloudError(e));
                    } else {
                        if (isExist(avUser.getObjectId()))
                            updateLocal(avUser);
                        else {
                            insertIntoLocal(avUser);
                            //设置到Application中
                            //好像没什么用，先留着
                            MyApplication application = (MyApplication) context.getApplicationContext();
                            application.setUser(parseLocalUser(avUser));
                        }

                        listener.loginSuccess();
                    }

                }
            }, UserForLeanCloud.class);

        } else {
            listener.loginFailed("请联网后重试");
        }
    }


    /**
     * 处理注册
     *
     * @param context  上下文
     * @param username 用户名
     * @param password 密码
     * @param listener 注册结果回调
     */
    @Override
    public void registry(Context context, String username, String password, final OnRegistryListener listener) {
        if (Util.isNetworkAvailable(context)) {
            final UserForLeanCloud user = new UserForLeanCloud();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //没错误表示注册成功
                        listener.registrySuccess();
                        insertIntoLocal(user);
                    } else {
                        listener.registryFailed(Util.getLocalizeLeanCloudError(e));
                    }
                }
            });
        } else listener.registryFailed("请连接网络后重试");
    }

    /**
     * 往本地用户表添加项
     *
     * @param user 从云端获取到的user对象
     */
    public void insertIntoLocal(UserForLeanCloud user) {
        ContentValues values = new ContentValues();
        values.put(SQLite.USER_COLUMN_ID, user.getObjectId());
        values.put(SQLite.USER_COLUMN_USERNAME, user.getUsername());
        values.put(SQLite.USER_COLUMN_EMAIL, user.getEmail());
//        values.put(UserForLeanCloud.FID, user.getFid());
//        values.put(UserForLeanCloud.ACTOR, user.getActor());
//        values.put(UserForLeanCloud.MONEY, user.getMoney());
        mDatabase.insert(SQLite.USER_TABLE, null, values);
    }

    /**
     * 根据云端对象更新本地数据库
     *
     * @param user 云端对象
     */
    public void updateLocal(UserForLeanCloud user) {

        ContentValues values = new ContentValues();
        values.put(SQLite.USER_COLUMN_USERNAME, user.getUsername());
        values.put(SQLite.USER_COLUMN_EMAIL, user.getEmail());
//        values.put(UserForLeanCloud.FID, avUser.getFid());
//        values.put(UserForLeanCloud.ACTOR, avUser.getActor());
//        values.put(UserForLeanCloud.MONEY, avUser.getMoney());
        mDatabase.update(SQLite.USER_TABLE, values, SQLite.USER_COLUMN_ID + " = ?", new String[]{user.getObjectId()});
    }


    /**
     * 确定某个用户是否已经在本地数据库有记录
     *
     * @param userId 用户唯一标识，对应user表主键、leanCloud上的objectId
     * @return 真表示有
     */
    public boolean isExist(String userId) {
        Cursor cursor = mDatabase.query(SQLite.USER_TABLE, new String[]{SQLite.USER_COLUMN_ID}, SQLite.USER_COLUMN_ID + " = ?", new String[]{userId}, null, null, null);
        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();
            return count != 0;
        } else return false;
    }


    /**
     * 由UserForLeanCloud生成UserBean
     *
     * @param user leanCloud的user
     * @return userBean
     */
    public User parseLocalUser(UserForLeanCloud user) {
        User localUser = new User();
        localUser.setId(user.getObjectId());
        localUser.setUsername(user.getUsername());
        localUser.setEmail(user.getEmail());
//        localUser.setActor(user.getActor());
//        localUser.setFid(user.getFid());
//        localUser.setMoney(user.getMoney());
        return localUser;
    }


}
