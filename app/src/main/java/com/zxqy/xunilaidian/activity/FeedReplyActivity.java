package com.zxqy.xunilaidian.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhihu.matisse.Matisse;
import com.zxqy.xunilaidian.R;
import com.zxqy.xunilaidian.adapter.FeedImgAddAdapter;
import com.zxqy.xunilaidian.base.BaseActivity;
import com.zxqy.xunilaidian.bean.PhotoTokenBean;
import com.zxqy.xunilaidian.entity.FeedBackComtiEntity;
import com.zxqy.xunilaidian.utils.Constants;
import com.zxqy.xunilaidian.utils.HttpUtils;
import com.zxqy.xunilaidian.utils.MessageEvent;
import com.zxqy.xunilaidian.utils.MyLog;
import com.zxqy.xunilaidian.utils.PermissionUtils;
import com.zxqy.xunilaidian.utils.ToastUtils;
import com.zxqy.xunilaidian.utils.UriTool;
import com.zxqy.xunilaidian.utils.ZTMatisseUtil;
import com.zxqy.xunilaidian.utils.httputil.callback.BaseCallback;
import com.zxqy.xunilaidian.view.ForbidEmojiEditText;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 反馈回复
 */
public class FeedReplyActivity extends BaseActivity {
    @BindView(R.id.et_content)
    ForbidEmojiEditText mContentEdit;
    @BindView(R.id.recyclerView_pic)
    RecyclerView mPicRecyclerView;
    private FeedImgAddAdapter mImageAddAdapter;
    private int mCurrentPosition;
    private String feedId;
    private int uplaodIndex;
    public List<String> potos, photoTokens;

    private UploadManager uploadManager;
    private UploadOptions opt;
    long currentTime;
    private String photoName = "";
    private String postfix = ".png";
    private StringBuffer sbPhotos;//图片拼接

    @Override
    public void setRootView() {
        setShowStatusBar(true);
        setContentView(R.layout.feed_reply);
    }


    protected void initView() {
        feedId = getIntent().getStringExtra("feedId");

        mPicRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mImageAddAdapter = new FeedImgAddAdapter(this, 3, null, new FeedImgAddAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                mCurrentPosition = position;
                ZTMatisseUtil.getPhoto(FeedReplyActivity.this, 1, 1113, 115);
            }

            @Override
            public void OnAddItemClick(int count) {
                ZTMatisseUtil.getPhoto(FeedReplyActivity.this, count, 1113, 114);
            }
        });
        mPicRecyclerView.setAdapter(mImageAddAdapter);
    }


    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_back, R.id.tv_comit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_comit:
                // 提交
                String content = mContentEdit.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShortToast("回复内容不能为空哦");
                    return;
                }
                potos = mImageAddAdapter.getDatas();
                /**
                 * 如果图片不为空
                 * 先上传图片
                 */
                if (potos != null && potos.size() > 0) {
                    //先获取图片上传所需的token
                    getPhotoTokens(potos.size());
                } else {//没有选择图片则直接提交反馈
                    submit();
                }
                showLoadDialog("提交中...");
                break;
        }
    }

    /**
     * 开始提交反馈到服务器
     */
    private void submit() {
        if (sbPhotos != null) {
            photoName = sbPhotos.toString();
        }
        MyLog.e("uploadFile", "sbPhotos==" + photoName);
        HttpUtils.getInstance().comitFeedBackReply(
                feedId
                , mContentEdit.getText().toString().trim()
                , photoName
                , new BaseCallback<FeedBackComtiEntity>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        canceloadDialog();
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Response response, FeedBackComtiEntity o) {
                        canceloadDialog();
                        if (o.code == 0 || o.msg.equals("success")) {
                            ToastUtils.showLongToast("回复成功！");
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_FEEDBACK_DETAIL));
                            finish();
                        } else {
                            if (!TextUtils.isEmpty(o.msg)) {
                                ToastUtils.showShortToast(o.msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
                    }
                });
    }
    /**
     * 隐藏加载进度对话框
     */
    public void canceloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    /**
     * 获取图片上传需要的token
     */
    private void getPhotoTokens(int num) {
        HttpUtils.getInstance().getPhotoTokens(num,
                new BaseCallback<PhotoTokenBean>() {
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        ToastUtils.showLongToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Response response, PhotoTokenBean o) {
                        if (o != null && o.data != null && o.data.size() > 0) {
                            MyLog.e("uploadFile", "图片上传token获取成功");
                            //开始上传图片
                            uplaodIndex = 0;
                            sbPhotos = new StringBuffer();
                            photoTokens = o.data;
                            for (String to : photoTokens
                            ) {
                                MyLog.e("uploadFile", "to====" + to);
                            }
                            upload(new File(potos.get(uplaodIndex)));
                        } else {
                            if (!TextUtils.isEmpty(o.msg)) {
                                ToastUtils.showShortToast(o.msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e, String erroMsg) {
                        canceloadDialog();
                        if (!TextUtils.isEmpty(erroMsg)) {
                            ToastUtils.showLongToast(erroMsg);
                        }
                    }
                });
    }

    /**
     * 上传图片到七牛云
     *
     * @param file
     */
    private void upload(File file) {

        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }

        opt = new UploadOptions(null, null, true, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                Log.i("qiniutest", "percent:" + percent);
            }
        }, null);
        currentTime = new Date().getTime();
        MyLog.e("uploadFile", "photoTokens===" + photoTokens.get(uplaodIndex));

        photoName = file.getName();
        if (photoName.indexOf(".") != -1) {//获取文件后缀
            postfix = photoName.substring(photoName.indexOf("."));
        }
        photoName = Constants.QINIU_FILE_KEY_FEED + currentTime + postfix;
        uploadManager.put(file, photoName, photoTokens.get(uplaodIndex),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        if (respInfo.isOK()) {
                            try {
                                Log.e("zw", jsonData.toString() + respInfo.toString());
//                                writeLog("--------------------------------UPTime/ms: " + (endTime - startTime));
                                String fileKey = jsonData.getString("key");
                                String fileHash = jsonData.getString("hash");

                                MyLog.e("uploadFile", "File Key===" + fileKey);
                                MyLog.e("uploadFile", "File Hash===" + fileHash);
                                MyLog.e("uploadFile", "photoName===" + photoName);
                                MyLog.e("uploadFile", "n上传成功===" + uplaodIndex);

                                if (uplaodIndex == potos.size() - 1) {//说明上传完毕
                                    canceloadDialog();
                                    sbPhotos.append(photoName);
                                    submit();//提交反馈
                                } else {//继续上传下一张
                                    uplaodIndex++;
                                    sbPhotos.append(photoName + ",");
                                    upload(new File(potos.get(uplaodIndex)));
                                }
                            } catch (JSONException e) {
                                canceloadDialog();
                            }
                        } else {
                            canceloadDialog();
                            if (!TextUtils.isEmpty(respInfo.error)) {
                                ToastUtils.showLongToast(respInfo.error);
                            }
                            MyLog.e("uploadFile", respInfo.toString());
                        }
                    }

                }, opt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 114:
                    List<Uri> list2 = Matisse.obtainResult(data);
                    if (null != list2 && list2.size() > 0) {
                        List<String> strings = new ArrayList<>();
                        for (Uri uri : list2) {
                            strings.add(UriTool.getFilePathByUri(FeedReplyActivity.this, uri));
                        }
                        mImageAddAdapter.AddDatas(strings);
                    }
                    break;
                case 115:
                    List<Uri> list3 = Matisse.obtainResult(data);
                    if (null != list3 && list3.size() > 0) {
                        mImageAddAdapter.repeleceData(UriTool.getFilePathByUri(FeedReplyActivity.this, list3.get(0)), mCurrentPosition);
                    }
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1113)
            PermissionUtils.onRequestMorePermissionsResult(this, ZTMatisseUtil.PICTURE_PERMISSION,
                    new PermissionUtils.PermissionCheckCallBack() {
                        @Override
                        public void onHasPermission() {
                            ToastUtils.showShortToast("授权成功");
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDown(String... strings) {
                            ToastUtils.showShortToast("授予此权限才能添加照片哦,点击确定继续授权。");
                            PermissionUtils.requestMorePermissions(FeedReplyActivity.this,
                                    ZTMatisseUtil.PICTURE_PERMISSION, 1113);
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDownAndDontAsk(String... strings) {
                            ToastUtils.showShortToast("您已经拒绝授权，无法继续添加照片，点击确定进入设置开启授权");
                            PermissionUtils.toAppSetting(FeedReplyActivity.this);
                        }
                    });
    }


}
