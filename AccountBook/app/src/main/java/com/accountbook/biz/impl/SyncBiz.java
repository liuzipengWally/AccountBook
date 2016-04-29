package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accountbook.biz.api.ISyncBiz;
import com.accountbook.entity.cloud.BudgetForCloud;
import com.accountbook.entity.cloud.RecordForCloud;
import com.accountbook.entity.cloud.Version;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.QuickSimpleIO;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

public class SyncBiz implements ISyncBiz {
    private SQLiteDatabase mDatabase;
    private QuickSimpleIO mSimpleIO;

    private OnSyncErrorListener mErrorListener;
    private OnSyncVersionListener mVersionListener;
    private OnSyncRecordDoneListener mRecordDoneListener;
    private OnSyncBudgetDoneListener mBudgetDoneListener;

    private long cloudVer;

    public interface OnSyncErrorListener {
        void error(String msg);
    }

    public interface OnSyncVersionListener {
        void done();
    }

    public interface OnSyncRecordDoneListener {
        void done();
    }

    public interface OnSyncBudgetDoneListener {
        void done();
    }

    public interface OnCompareVerListener {
        void CompareDone(boolean result);
    }

    @Override
    public void setOnPostErrorListener(OnSyncErrorListener listener) {
        this.mErrorListener = listener;
    }

    @Override
    public void setOnSyncVersionListener(OnSyncVersionListener listener) {
        this.mVersionListener = listener;
    }

    @Override
    public void setOnSyncRecordDoneListener(OnSyncRecordDoneListener listener) {
        this.mRecordDoneListener = listener;
    }

    @Override
    public void setOnSyncBudgetDoneListener(OnSyncBudgetDoneListener listener) {
        this.mBudgetDoneListener = listener;
    }

    private Context mContext;

    public SyncBiz(Context context) {
        this.mContext = context;
        mDatabase = SQLite.getInstance().getDatabaseObject();
        mSimpleIO = new QuickSimpleIO(mContext, "version_sp");
    }

    @Override
    public void syncRecord() {
        try {
            postRecordAdd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postRecordAdd() throws Exception {
        //objectId为空的代表是新增的，isSave为0的代表是未保存的
        String sql = "select * from record " +
                "where object_id is null and " +
                "isSave = ? and " +
                "available = ?";

        //将符合条件的数据从数据库中查询出来
        final Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.FALSE + "", ConstantContainer.TRUE + ""});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                final RecordForCloud recordForCloud = new RecordForCloud();
                final String id = cursor.getString(cursor.getColumnIndex("_id"));
                recordForCloud.setId(id);
                recordForCloud.setClassifyId(cursor.getString(cursor.getColumnIndex("classify_id")));
                recordForCloud.setRoleId(cursor.getString(cursor.getColumnIndex("role_id")));
                recordForCloud.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                recordForCloud.setBorrowName(cursor.getString(cursor.getColumnIndex("borrow_name")));
                recordForCloud.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                recordForCloud.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
                recordForCloud.setRecordMs(cursor.getLong(cursor.getColumnIndex("record_ms")));
                recordForCloud.setUpdateMs(cursor.getLong(cursor.getColumnIndex("update_ms")));
                recordForCloud.setAvailable(cursor.getInt(cursor.getColumnIndex("available")));
                recordForCloud.setUser(AVUser.getCurrentUser());

                recordForCloud.saveInBackground(new SaveCallback() {//每查一条保存一条到云端
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            if (mErrorListener != null) {
                                mErrorListener.error("上传record表新增数据出错" + e.getMessage());
                            }
                        } else {
                            //保存完毕，将这条数据生成的objectId保存到本地的该条数据中，并更新isSave为True
                            ContentValues values = new ContentValues();
                            values.put("object_id", recordForCloud.getObjectId());
                            values.put("isSave", ConstantContainer.TRUE);
                            // TODO: 16/4/20 这里保存失败
                            int success = mDatabase.update(SQLite.RECORD_TABLE, values, "_id = ?", new String[]{id});
                            Log.i("success", success + "  " + id);
                            if (success <= 0) {
                                if (mErrorListener != null) {
                                    mErrorListener.error("record保存objectId时出错");
                                }
                            }
                            values.clear();
                        }
                    }
                });
            }
        } else {
            Log.i("info", "没有record数据可add");
        }

        cursor.close();

        try {
            postRecordUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postRecordUpdate() throws Exception {
        String sql = "select * from record " +
                "where object_id not null and " +
                "isSave = ?";

        final Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.FALSE + ""});
        AVQuery<RecordForCloud> query = new AVQuery<>("Record");

        if (cursor.getCount() != 0) {
            Log.i("数据", "有未更新数据");
            while (cursor.moveToNext()) {
                final String objectId = cursor.getString(cursor.getColumnIndex("object_id"));
                final String classifyId = cursor.getString(cursor.getColumnIndex("classify_id"));
                final String roleId = cursor.getString(cursor.getColumnIndex("role_id"));
                final long updateMs = cursor.getLong(cursor.getColumnIndex("update_ms"));
                final String account = cursor.getString(cursor.getColumnIndex("account"));
                final String borrow = cursor.getString(cursor.getColumnIndex("borrow_name"));
                final String description = cursor.getString(cursor.getColumnIndex("description"));
                final int money = cursor.getInt(cursor.getColumnIndex("money"));
                final long recordMs = cursor.getLong(cursor.getColumnIndex("record_ms"));
                final int available = cursor.getInt(cursor.getColumnIndex("available"));
                query.getInBackground(objectId, new GetCallback<RecordForCloud>() {
                    @Override
                    public void done(final RecordForCloud recordForCloud, AVException e) {
                        if (e == null) {
                            if (recordForCloud.getUpdateMs() < updateMs) {
                                Log.i("数据", "数据拿到" + money);
                                recordForCloud.setClassifyId(classifyId);
                                recordForCloud.setRoleId(roleId);
                                recordForCloud.setAccount(account);
                                recordForCloud.setBorrowName(borrow);
                                recordForCloud.setDescription(description);
                                recordForCloud.setMoney(money);
                                recordForCloud.setRecordMs(recordMs);
                                recordForCloud.setUpdateMs(updateMs);
                                recordForCloud.setAvailable(available);
                                recordForCloud.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e != null) {
                                            if (mErrorListener != null) {
                                                mErrorListener.error("上传record表更新出错" + e.getMessage());
                                            }
                                        } else {
                                            ContentValues values = new ContentValues();
                                            values.put("isSave", ConstantContainer.TRUE);
                                            int success = mDatabase.update(SQLite.RECORD_TABLE, values, "object_id = ?", new String[]{objectId});
                                            if (success == 0) {
                                                if (mErrorListener != null) {
                                                    mErrorListener.error("record更新保存状态时出错");
                                                }
                                            }
                                            values.clear();
                                        }
                                    }
                                });
                            }
                        } else {
                            if (mErrorListener != null) {
                                mErrorListener.error("update时获取record数据出错" + e.getMessage());
                            }
                        }
                    }
                });
            }
            cursor.close();
        } else {
            Log.i("查询", "record没有数据可update");
        }

        //对比完版本号，如果是true，表示版本号相同，那么同步到此为止，因为如果两端版本号是一样的，证明云端和本地是一致的
        //如果为false，那只会是一种情况，云端的版本号更高，这就表示云端有我们本地没有的数据，此时我们将现在云端的整张表下载下来下来
        compareVersion("recordVer", new OnCompareVerListener() {
            @Override
            public void CompareDone(boolean result) {
                if (!result) {
                    Log.i("需要下载", "record");
                    try {
                        downloadRecord();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mRecordDoneListener != null) {
                        mRecordDoneListener.done();
                    }
                }
            }
        });
    }

    private void downloadRecord() throws Exception {
        SQLite.getInstance().clearData(SQLite.RECORD_TABLE); //先清空整张表
        AVQuery<RecordForCloud> query = new AVQuery<>("Record");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<RecordForCloud>() { //到云端将该用户下这张表的数据全部查询出来
            @Override
            public void done(List<RecordForCloud> list, AVException e) {
                if (e == null) {
                    ContentValues values = new ContentValues();  //之后循环保存
                    for (int i = 0; i < list.size(); i++) {
                        values.put("_id", list.get(i).getId());
                        values.put("object_id", list.get(i).getObjectId());
                        values.put("classify_id", list.get(i).getClassifyId());
                        values.put("role_id", list.get(i).getRoleId());
                        values.put("account", list.get(i).getAccount());
                        values.put("borrow_name", list.get(i).getBorrowName());
                        values.put("description", list.get(i).getDescription());
                        values.put("money", list.get(i).getMoney());
                        values.put("record_ms", list.get(i).getRecordMs());
                        values.put("update_ms", list.get(i).getUpdateMs());
                        values.put("available", list.get(i).getAvailable());
                        values.put("isSave", ConstantContainer.TRUE);

                        long successNum = mDatabase.insert(SQLite.RECORD_TABLE, null, values);
                        values.clear();
                        if (successNum == 0) {
                            if (mErrorListener != null) {
                                mErrorListener.error("下载record数据出错" + e.getMessage());
                            }
                        }
                    }
                    Log.i("end", "record同步结束");
                    if (mRecordDoneListener != null) {
                        mRecordDoneListener.done();
                    }
                } else {
                    if (mErrorListener != null) {
                        mErrorListener.error("下载record数据出错" + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void syncRole() {
        // TODO: 16/4/2 用户角色因不影响业务主线，暂不实现同步
    }

    @Override
    public void syncClassify() {
        // TODO: 16/4/2 分类因不影响业务主线，暂不实现同步
    }

    @Override
    public void syncBudget() {
        try {
            postBudgetAdd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postBudgetAdd() throws Exception {
        //objectId为空的代表是新增的，isSave为0的代表是未保存的
        String sql = "select * from budget " +
                "where object_id is null and " +
                "isSave = ? and " +
                "available = ?";

        //将符合条件的数据从数据库中查询出来
        final Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.FALSE + "", ConstantContainer.TRUE + ""});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                final BudgetForCloud budgetForCloud = new BudgetForCloud();
                final String id = cursor.getString(cursor.getColumnIndex("_id"));
                budgetForCloud.setId(id);
                budgetForCloud.setClassifyId(cursor.getString(cursor.getColumnIndex("classify_id")));
                budgetForCloud.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                budgetForCloud.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
                budgetForCloud.setStartTime(cursor.getLong(cursor.getColumnIndex("start_date")));
                budgetForCloud.setEndTime(cursor.getLong(cursor.getColumnIndex("end_date")));
                budgetForCloud.setUpdateMs(cursor.getLong(cursor.getColumnIndex("update_ms")));
                budgetForCloud.setAvailable(cursor.getInt(cursor.getColumnIndex("available")));
                budgetForCloud.setUser(AVUser.getCurrentUser());

                budgetForCloud.saveInBackground(new SaveCallback() {//每查一条保存一条到云端
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            if (mErrorListener != null) {
                                mErrorListener.error("上传budget表新增数据出错" + e.getMessage());
                            }
                        } else {
                            //保存完毕，将这条数据生成的objectId保存到本地的该条数据中，并更新isSave为True
                            ContentValues values = new ContentValues();
                            values.put("object_id", budgetForCloud.getObjectId());
                            values.put("isSave", ConstantContainer.TRUE);
                            int success = mDatabase.update(SQLite.BUDGET_TABLE, values, "_id = ?", new String[]{id});
                            if (success == 0) {
                                if (mErrorListener != null) {
                                    mErrorListener.error("budget保存objectId时出错");
                                }
                            }
                            values.clear();
                        }
                    }
                });
            }
        } else {
            Log.i("info", "没有budget数据可add");
        }
        cursor.close();

        try {
            postBudgetUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postBudgetUpdate() throws Exception {
        String sql = "select * from budget " +
                "where object_id not null and " +
                "isSave = ?";

        final Cursor cursor = mDatabase.rawQuery(sql, new String[]{ConstantContainer.FALSE + ""});
        AVQuery<BudgetForCloud> query = new AVQuery<>("Budget");

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                final String objectId = cursor.getString(cursor.getColumnIndex("object_id"));
                final String classifyId = cursor.getString(cursor.getColumnIndex("classify_id"));
                final long updateMs = cursor.getLong(cursor.getColumnIndex("update_ms"));
                final String description = cursor.getString(cursor.getColumnIndex("description"));
                final int money = cursor.getInt(cursor.getColumnIndex("money"));
                final long startTime = cursor.getLong(cursor.getColumnIndex("start_date"));
                final long endTime = cursor.getLong(cursor.getColumnIndex("end_date"));
                final int available = cursor.getInt(cursor.getColumnIndex("available"));

                query.getInBackground(objectId, new GetCallback<BudgetForCloud>() {
                    @Override
                    public void done(final BudgetForCloud budgetForCloud, AVException e) {
                        if (budgetForCloud.getUpdateMs() < updateMs) {
                            budgetForCloud.setClassifyId(classifyId);
                            budgetForCloud.setDescription(description);
                            budgetForCloud.setMoney(money);
                            budgetForCloud.setStartTime(startTime);
                            budgetForCloud.setEndTime(endTime);
                            budgetForCloud.setUpdateMs(updateMs);
                            budgetForCloud.setAvailable(available);
                            budgetForCloud.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e != null) {
                                        if (mErrorListener != null) {
                                            mErrorListener.error("上传budget表更新出错" + e.getMessage());
                                        }
                                    } else {
                                        //保存完毕，将这条数据生成的objectId保存到本地的该条数据中，并更新isSave为True
                                        ContentValues values = new ContentValues();
                                        values.put("isSave", ConstantContainer.TRUE);
                                        int success = mDatabase.update(SQLite.BUDGET_TABLE, values, "object_id = ?", new String[]{objectId});
                                        if (success == 0) {
                                            if (mErrorListener != null) {
                                                mErrorListener.error("budget更新保存状态时出错");
                                            }
                                        }
                                        values.clear();
                                    }
                                }
                            });
                        }
                    }
                });
            }
            cursor.close();
        } else {
            Log.i("查询", "没有budget数据可up");
        }

        //对比完版本号，如果是true，表示版本号相同，那么同步到此为止，因为如果两端版本号是一样的，证明云端和本地是一致的
        //如果为false，那只会是一种情况，云端的版本号更高，这就表示云端有我们本地没有的数据，此时我们将现在云端的整张表下载下来下来
        compareVersion("budgetVer", new OnCompareVerListener() {
            @Override
            public void CompareDone(boolean result) {
                if (!result) {
                    Log.i("需要下载", "budget");
                    downloadBudget();
                } else {
                    if (mBudgetDoneListener != null) {
                        mBudgetDoneListener.done();
                    }
                }
            }
        });
    }

    private void downloadBudget() {
        SQLite.getInstance().clearData(SQLite.BUDGET_TABLE); //先清空整张表
        AVQuery<BudgetForCloud> query = new AVQuery<>("Budget");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<BudgetForCloud>() { //到云端将该用户下这张表的数据全部查询出来
            @Override
            public void done(List<BudgetForCloud> list, AVException e) {
                if (e == null) {
                    ContentValues values = new ContentValues();  //之后循环保存
                    for (int i = 0; i < list.size(); i++) {
                        values.put("_id", list.get(i).getId());
                        values.put("object_id", list.get(i).getObjectId());
                        values.put("classify_id", list.get(i).getClassifyId());
                        values.put("description", list.get(i).getDescription());
                        values.put("money", list.get(i).getMoney());
                        values.put("start_date", list.get(i).getStartTime());
                        values.put("end_date", list.get(i).getEndTime());
                        values.put("update_ms", list.get(i).getUpdateMs());
                        values.put("available", list.get(i).getAvailable());
                        values.put("isSave", ConstantContainer.TRUE);

                        long successNum = mDatabase.insert(SQLite.BUDGET_TABLE, null, values);
                        values.clear();
                        if (successNum == 0) {
                            if (mErrorListener != null) {
                                mErrorListener.error("下载budget数据出错" + e.getMessage());
                            }
                        }
                    }

                    if (mBudgetDoneListener != null) {
                        mBudgetDoneListener.done();
                    }
                } else {
                    if (mErrorListener != null) {
                        mErrorListener.error("下载budget数据出错" + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void syncVersion() {
        //先通过本地的AVUser对象的objectId，获取到对应的云端User
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.getInBackground(AVUser.getCurrentUser().getObjectId(), new GetCallback<AVUser>() {
            @Override
            public void done(final AVUser avUser, AVException e) {
                if (e == null) {
                    if (avUser.getAVObject("Version") == null) { //查询到后，如果该User未关联Version表，那么代表该用户是第一次使用
                        postVersion(avUser);   //那么将本地的Version数据上传至云端
                    } else {
                        downloadVersion((Version) avUser.getAVObject("Version"));   //如果有关联，那么代表，云端有旧数据，那么将云端的Version信息下载到本地
                    }

                } else {
                    if (mErrorListener != null) { //如果出现错误 ，将错误信息抛出去
                        mErrorListener.error("获取当前用户出错" + e.getMessage());
                    }
                }
            }
        });
    }

    private void downloadVersion(Version version) {
        //下载version数据的要点，本地数据必须是云端数据-1，这样才能不影响后续同步数据，否则会无从对比数据
        mSimpleIO.setLong("budgetVer", version.getBudgetVer() - 1);
        mSimpleIO.setLong("classifyVer", version.getClassifyVer() - 1);
        mSimpleIO.setLong("recordVer", version.getRecordVer() - 1);
        mSimpleIO.setLong("roleVer", version.getRoleVer() - 1);

        if (mVersionListener != null) {
            mVersionListener.done();
        }
    }

    private void postVersion(final AVUser avUser) {
        final Version version = new Version();
        version.setBudgetVer(mSimpleIO.getLong("budgetVer"));
        version.setClassifyVer(mSimpleIO.getLong("classifyVer"));
        version.setRecordVer(mSimpleIO.getLong("recordVer"));
        version.setRoleVer(mSimpleIO.getLong("roleVer"));

        version.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    if (mErrorListener != null) {
                        mErrorListener.error("保存版本号出错" + e.getMessage());
                    }
                } else {
                    avUser.put("Version", version); //关联Version表到当前登录的这个用户
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                if (mErrorListener != null) {
                                    mErrorListener.error("关联版本号到用户名出错" + e.getMessage());
                                }
                            } else {
                                if (mVersionListener != null) {
                                    mVersionListener.done();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 工具类，用于对比本地与云端的版本号是否相等
     *
     * @param Key 本地版本号的key
     * @return 相等返回true，否则返回false
     */
    private void compareVersion(final String Key, final OnCompareVerListener compareVerListener) {
        final long localVer = mSimpleIO.getLong(Key);

        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.getInBackground(AVUser.getCurrentUser().getObjectId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e != null) {
                    if (mErrorListener != null) {
                        mErrorListener.error("获取用户错误" + e.getMessage());
                    }
                } else {
                    Version subVersion = avUser.getAVObject("Version");
                    AVQuery<Version> verQuery = new AVQuery<>("Version");
                    verQuery.getInBackground(subVersion.getObjectId(), new GetCallback<Version>() {
                        @Override
                        public void done(Version version, AVException e) {
                            long cloudVer = version.getLong(Key);
                            Log.i("版本号", cloudVer + "");
                            compareVerListener.CompareDone(localVer == cloudVer ? true : false);
                            if (localVer != cloudVer) {
                                cloudVer += 1;
                                version.put(Key, cloudVer);
                                mSimpleIO.setLong(Key, cloudVer);
                                version.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e != null) {
                                            if (mErrorListener != null) {
                                                mErrorListener.error("更新版本信息出错" + e.getMessage());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
