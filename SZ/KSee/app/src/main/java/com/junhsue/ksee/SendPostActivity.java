package com.junhsue.ksee;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.adapter.viewholder.ImageViewHolder;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.CircleCircleListDTO;
import com.junhsue.ksee.dto.ImageDTO;
import com.junhsue.ksee.dto.SendPostResultDTO;
import com.junhsue.ksee.entity.CircleCircleListEntity;
import com.junhsue.ksee.entity.ImageBean;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.fragment.CircleDetailAllFragment;
import com.junhsue.ksee.fragment.CommunityCircleFragment;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.api.OkHttpQiniu;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.callback.SaveImgGetTokenCallback;
import com.junhsue.ksee.net.callback.SaveImgSuccessCallback;
import com.junhsue.ksee.net.url.RequestUrl;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.BitmapUtil;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.LoginUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.FlowLayout;
import com.junhsue.ksee.view.MultiImageView;
import com.junhsue.ksee.view.RfCommonDialog;
import com.junhsue.ksee.view.SendPostImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SendPostActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener,
        SaveImgGetTokenCallback, SaveImgSuccessCallback {

    /**
     * 删除图片的广播
     */
    public static final String BROAD_ACTION_DELETE_PIC = "delete_picture";

    /**
     * 圈子跳转key
     * JUMP_BY_PARENT ：主界面
     * JUMP_BY_SON ：圈子界面
     * <p>
     * JUMP_BY_ID ：圈子ID
     * JUMP_BY_NAME ：圈子名称
     */
    public static final String JUMP_BY = "jump activity by where";
    public static final int JUMP_BY_PARENT = 0;
    public static final int JUMP_BY_SON = 1;
    public static final String JUMP_CIRCLE_ID = "jump activity by where, ID";
    public static final String JUMP_CIRCLE_NAME = "jump activity by where, NAME";

    private ActionBar mAbr;
    private EditText mEdTitle, mEdDes;
    private TextView mTvTitleSize, mTvCircleTag;
    private FlowLayout mFlowCircleList;
    private SendPostImageView mMultiImageView;
    private CheckBox mCbAnonymous;
    private ImageView mImgCircleListButton;
    private TextView mTvSendPost;
    private RelativeLayout mRlSendPost, mRlCircleListButton;
    private ImageView mImgLoadCircle;
    private LinearLayout mLLCircleList;

    //圈子方式
    private int circle_Type = 0;
    //圈子
    private CircleCircleListEntity mCircleSelecet;
    //圈子id
    private String mCircleID;
    //圈子名称
    private String mCircleName;
    /**
     * 请求识别码
     **/
    private static final int CODE_CAMERA_REQUEST = 0xa1;//本地
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    /**
     * 帖子发布参数
     **/
    private String title, description, posters = "", isAnonymous;

    /**
     * 图片获取成功获取的数组
     */
    private ArrayList<ImageDTO> photos = new ArrayList<>();

    /**
     * 图片上传成功或失败获取的数组
     */
    private ArrayList<ImageDTO> photoSucc = new ArrayList<>();
    private ArrayList<ImageDTO> photoFail = new ArrayList<>();

    /**
     * 是否图片达到最大值
     */
    private int photoSize = 0;

    /**
     * 圈子数据源
     */
    private CircleCircleListDTO mCircleCircleListDTO;
    private int circlelist_index; //选中的圈子下标
    private int max_lines = 2;
    /**
     * 最大图片数量
     */
    private int max_pic = 9;
    private UserInfo mUserInfo;
    private Context mContext;
    private String token;
    private SaveImgGetTokenCallback saveImgGetTokenCallback = this;
    private SaveImgSuccessCallback saveImgSuccessCallback = this;

    /**
     * 当PopupWindow显示或者消失时改变背景色
     */
    private WindowManager.LayoutParams lp;
    private PopupWindow mHeadImgPopWindow;

    //上传图片子线程
    private Handler handlerLoadPic = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String key = msg.getData().getString("key");
                    ToastUtil.getInstance(mContext).setContent(key).setShow();
                    break;
                case 1:
                    if (max_lines < mFlowCircleList.getLines()) {
                        mRlCircleListButton.setVisibility(View.VISIBLE);
                        mRlCircleListButton.callOnClick();
                    } else {
                        mRlCircleListButton.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    String str = "发布中.";
                    int len = (int) ((System.currentTimeMillis() % 1000) / 333);
                    for (int i = 0; i < len; i++) {
                        str += ".";
                    }
                    mTvSendPost.setText(str);
                    mTvSendPost.setCompoundDrawables(null, null, null, null);
                    handlerLoadPic.sendEmptyMessageDelayed(2, 300);
                    break;
                case 3:
                    handlerLoadPic.removeMessages(2);
                    mRlSendPost.setClickable(true);
                    mTvSendPost.setText("发布");
                    photoSucc.clear();
                    photoFail.clear();
                    Drawable image = mContext.getResources().getDrawable(R.drawable.icon_next_white);
                    image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                    mTvSendPost.setCompoundDrawables(null, null, image, null);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        circle_Type = bundle.getInt(JUMP_BY, JUMP_BY_PARENT);
        if (circle_Type == JUMP_BY_SON) {
            mCircleID = bundle.getString(JUMP_CIRCLE_ID);
            mCircleName = bundle.getString(JUMP_CIRCLE_NAME);
            mCircleSelecet = new CircleCircleListEntity();
            mCircleSelecet.id = Integer.parseInt(mCircleID);
            mCircleSelecet.name = mCircleName;
        }
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_send_post;
    }

    @Override
    protected void onInitilizeView() {

        initView();
        initLister();
        initData();

    }

    private void initView() {
        mAbr = (ActionBar) findViewById(R.id.ab_post_title);
        mEdTitle = (EditText) findViewById(R.id.ed_post_title);
        mEdDes = (EditText) findViewById(R.id.ed_post_des);
        mTvTitleSize = (TextView) findViewById(R.id.tv_post_title_size);
        mTvCircleTag = (TextView) findViewById(R.id.tv_post_tag);
        mMultiImageView = (SendPostImageView) findViewById(R.id.multiImagView_post);
        mFlowCircleList = (FlowLayout) findViewById(R.id.flowlayout_post_circle);
        mCbAnonymous = (CheckBox) findViewById(R.id.cb_post_anonymous);
        mImgCircleListButton = (ImageView) findViewById(R.id.img_post_button);
        mTvSendPost = (TextView) findViewById(R.id.tv_post_send);
        mRlSendPost = (RelativeLayout) findViewById(R.id.rl_post_send);
        mRlCircleListButton = (RelativeLayout) findViewById(R.id.rl_post_button);
        mImgLoadCircle = (ImageView) findViewById(R.id.img_post_load_circle);
        mLLCircleList = (LinearLayout) findViewById(R.id.ll_post_circlelist);

        lp = getWindow().getAttributes();
        mUserInfo = UserProfileService.getInstance(this).getCurrentLoginedUser();
        LoginUtils.getHeadImgToken(saveImgGetTokenCallback); //获取服务的token

        //加载拍照按钮
        loadFlowPhotoFirst();
        //判断是否为首页跳转
        if (circle_Type == JUMP_BY_SON) {
            mLLCircleList.setVisibility(View.GONE);
        } else {
            loadCircleList();
        }

    }

    /**
     * 初始化View监听
     */
    private void initLister() {
        mAbr.setOnClickListener(this);
        mRlSendPost.setOnClickListener(this);
        mRlCircleListButton.setOnClickListener(this);
        mImgLoadCircle.setOnClickListener(this);

        mEdTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvTitleSize.setText(String.format("%d", (30 - s.length())) + "字");
                if (s.length() > 30) {
                    mTvTitleSize.setTextColor(getResources().getColor(R.color.red));
                } else {
                    mTvTitleSize.setTextColor(getResources().getColor(R.color.c_black_55626e));
                }
            }
        });

    }


    /**
     * 初始化发布的圈子
     */
    private void initData() {
        if (circle_Type == JUMP_BY_SON) {
            mTvCircleTag.setText(mCircleSelecet.name);
        } else {
            mTvCircleTag.setVisibility(View.GONE);
        }
    }


    /**
     * 加载圈子流数据源
     */
    private void loadCircleList() {
        OKHttpNewSocialCircle.getInstance().getCircleList("1", new RequestCallback<CircleCircleListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
//                loadCircleList();
                mRlSendPost.setVisibility(View.GONE);
                mImgLoadCircle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(CircleCircleListDTO response) {
                mCircleCircleListDTO = response;
                if (mCircleCircleListDTO == null || response.list == null || response.list.size() == 0) {
                    mRlSendPost.setVisibility(View.GONE);
                    mImgLoadCircle.setVisibility(View.VISIBLE);
                    return;
                } else {
                    mRlSendPost.setVisibility(View.VISIBLE);
                    mImgLoadCircle.setVisibility(View.GONE);
                }
                mFlowCircleList.removeAllViews();
                for (int i = 0; i < response.list.size(); i++) {
                    loadFlowCircleList(i);
                }
                handlerLoadPic.sendEmptyMessageDelayed(1, 10);
            }
        });

    }

    /**
     * 加载圈子流列表
     */
    private void loadFlowCircleList(int i) {
        CheckBox cbTag = (CheckBox) getLayoutInflater().inflate(R.layout.item_topic_tag, mFlowCircleList, false);
        cbTag.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_check_circle));
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.rightMargin = 40;
        lp.topMargin = 40;
        cbTag.setLayoutParams(lp);
        cbTag.setTag(i);
        cbTag.setId(i);
        cbTag.setText(mCircleCircleListDTO.list.get(i).name);
        mFlowCircleList.addView(cbTag);

        cbTag.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputUtil.hideSoftInputFromWindow((Activity) mContext);
                if (isChecked) {
                    if (null != mCircleSelecet) {
                        CheckBox cb = (CheckBox) mFlowCircleList.getChildAt(circlelist_index);
                        cb.setChecked(false);
                    }
                    circlelist_index = Integer.parseInt(buttonView.getTag() + "");
                    buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_normal));
                    mCircleSelecet = mCircleCircleListDTO.list.get(Integer.parseInt(buttonView.getTag().toString().trim()));
                    mTvCircleTag.setVisibility(View.VISIBLE);
                    mTvCircleTag.setText(mCircleCircleListDTO.list.get(Integer.parseInt(buttonView.getTag().toString().trim())).name);
                } else {
                    buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_selector));
                    mCircleSelecet = null;
                    circlelist_index = -1;
                    mTvCircleTag.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    /**
     * 加载初始add照片
     */
    private void loadFlowPhotoFirst() {
        ImageDTO photoInfo = new ImageDTO();
        photoInfo.setLocal_drawable(R.drawable.icon_add_picture);
        photos.add(photos.size(), photoInfo);
        addData();
    }

    /**
     * add照片
     */
    private void addData() {
        ArrayList<String> pp = null;
        if (pp == null) {
            pp = new ArrayList<>();
        } else {
            pp.clear();
        }
        for (int i = 0; i < photos.size() - 1; i++) {
            pp.add(photos.get(i).getQiniu_path());
        }
        if (photos != null && photos.size() > 0) {
            photoSize = photos.size() - 1;
            if (photos.size() == 10) {
                photos.remove(9);
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMultiImageView.setList(photos);
                }
            });
            mMultiImageView.setmOnItemClickListener(new SendPostImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    InputUtil.hideSoftInputFromWindow((Activity) mContext);
                    //imagesize是作为loading时的图片size
                    if (photos.get(position).getLocal_drawable() != 0) {
                        showHeadImgPopupWindow();
                        return;
                    }
                    ArrayList<ImageDTO> pp = new ArrayList<ImageDTO>();
                    pp.addAll(photos);
                    if (pp.get(photos.size() - 1).getLocal_drawable() != 0) {
                        pp.remove(photos.size() - 1);
                    }
                    Intent intent2PicDetail = new Intent(mContext, PicPagesActivity.class);
                    Bundle bundle2PicDetail = new Bundle();
                    bundle2PicDetail.putSerializable(PicPagesActivity.PIC_DATA, pp);
                    bundle2PicDetail.putInt(PicPagesActivity.PIC_INDEX, Integer.parseInt((position + 1) + ""));
                    bundle2PicDetail.putString(PicPagesActivity.PIC_FROM, PicPagesActivity.PIC_LOCAL);
                    intent2PicDetail.putExtras(bundle2PicDetail);
                    startActivity(intent2PicDetail);
                }
            });
        }
        pp.clear();
        pp = null;

    }


    /**
     * 修改头像的PopWindow对话框
     */
    private void showHeadImgPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_register2_popupwindow, null);
        mHeadImgPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mHeadImgPopWindow.setContentView(contentView);
        mHeadImgPopWindow.setOnDismissListener(this);

        //显示PopWindow
        View rootView = LayoutInflater.from(this).inflate(R.layout.act_editor, null);
        mHeadImgPopWindow.setOutsideTouchable(true);
        mHeadImgPopWindow.setFocusable(true);
        mHeadImgPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mHeadImgPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mHeadImgPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
    }

    /**
     * 圈子的数据源的收缩
     **/
    private boolean is_close = false;

    @Override
    public void onClick(View v) {
        InputUtil.hideSoftInputFromWindow((Activity) mContext);
        switch (v.getId()) {
            case R.id.ll_left_layout:
                onBackPressed();
                break;
            case R.id.btn_right_one:
                break;
            case R.id.rl_post_button:
                ViewGroup.LayoutParams params = mFlowCircleList.getLayoutParams();
                if (is_close) {
                    is_close = false;
                    mImgCircleListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_post_close));
                    params.height = mFlowCircleList.getCloseHeight(mFlowCircleList.getLines());
                } else {
                    is_close = true;
                    mImgCircleListButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_post_open));
                    params.height = mFlowCircleList.getCloseHeight(max_lines);
                }
                params.height += mContext.getResources().getDimension(R.dimen.dimen_20px);
                mFlowCircleList.setLayoutParams(params);
                break;
            case R.id.rl_post_send:
                title = mEdTitle.getText().toString().trim();
                description = mEdDes.getText().toString().trim();
                if (StringUtils.isBlank(title) || title.length() < 5 || title.length() > 30) {
                    ToastUtil.getInstance(mContext).setContent("请输入5-30字的标题哦").setShow();
                    return;
                }
                if (StringUtils.isBlank(description) || description.length() < 3) {
                    ToastUtil.getInstance(mContext).setContent("帖子描述需要至少3个字符哦").setShow();
                    return;
                }
                if (null == mCircleSelecet) {
                    ToastUtil.getInstance(mContext).setContent("请选择一个圈子进行发布").setShow();
                    return;
                }
                isAnonymous = "0"; //不匿名
                if (mCbAnonymous.isChecked()) {
                    isAnonymous = "1"; //匿名
                }
                if (photoSize != 0) {
//                    for (int i = 0; i < photoSize; i++){
//                        if (photos.get(i).getPath() != null && !photos.get(i).getPath().equals("")){
                    sendPic(0);
//                        }
//                    }
                } else {
                    toSendCircle(title, description, isAnonymous, mCircleSelecet.id + "", posters);
                }
                mRlSendPost.setClickable(false);
                handlerLoadPic.sendEmptyMessage(2);
                break;
            case R.id.img_post_load_circle:
                loadCircleList();
                break;
        }
    }

    public void sendPic(final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(photos.get(i).getPath()))));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
//                final byte[] bitmapByte = bs.toByteArray();
//                final byte[] bitmapByte = BitmapUtil.huoHuaBitmapToByteArray(bitmap,true);
                final byte[] bitmapByte = BitmapUtil.huoHuaStringToByteArray(photos.get(i).getPath(), true);
                final String key = mUserInfo.phonenumber + "_" + System.currentTimeMillis() + "_" + ((int) (Math.random() * 10000));
                photos.get(i).setQiniu_path(RequestUrl.BASE_QINIU_API + key);
                if (null == token){
                    if (!CommonUtils.getIntnetConnect(mContext)){
                        ToastUtil.getInstance(mContext).setContent("网络连接异常").setShow();
                        return;
                    }
                    LoginUtils.getHeadImgToken(saveImgGetTokenCallback); //获取服务的token
                    SystemClock.sleep(100);
                }else {
                    OkHttpQiniu.getInstance().loadImg(mContext, saveImgSuccessCallback, bitmapByte, key, token);
                }
            }
        });

    }

    /**
     * 发布帖子
     *
     * @param title        标题
     * @param des          描述
     * @param is_anonymous 是否匿名
     */
    private void toSendCircle(final String title, final String des, final String is_anonymous, String circle_id, String poster) {
        OKHttpNewSocialCircle.getInstance().sendPost(title, des, is_anonymous, circle_id, poster, new RequestCallback<SendPostResultDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(mContext).setContent("发布失败").setShow();
                handlerLoadPic.sendEmptyMessage(3);
            }

            @Override
            public void onSuccess(SendPostResultDTO response) {
                ToastUtil.getInstance(mContext).setContent("发布成功").setShow();
                handlerLoadPic.sendEmptyMessage(3);
                finish();
                /** 即时更新圈子列表 **/
//                PostDetailEntity entity = new PostDetailEntity();
//                String[] photos = posters.split(",");
//                for (String s :  photos){
//                    entity.posters.add(s);
//                }
//                entity.circle_id = mCircleID;
//                entity.nickname = mUserInfo.nickname;
//                entity.publish_at = System.currentTimeMillis()/1000 - 1;
//                entity.is_approval = false;
//                entity.approvalcount = "0";
//                entity.is_anonymous = mCbAnonymous.isChecked();
//                entity.is_top = false;
//                entity.is_favorite = false;
//                entity.favoritecount = "0";
//                entity.commentcount = "0";
//                entity.avatar = mUserInfo.avatar;
//                entity.title = title;
//                entity.description = des;
                Intent intent = new Intent(Constants.ACTION_ADD_MYPOST);
//                intent.putExtra(Constants.POST_DETAIL_ID,entity);
                sendBroadcast(intent);
            }
        });
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.pop_cancel:
                mHeadImgPopWindow.dismiss();
                break;
            case R.id.pop_camera:  //打开相机
                Intent intentFromCamera = new Intent(this, ImageSelectActivity.class);
                intentFromCamera.putExtra("select", 0);
                intentFromCamera.putExtra("type", 1);
                startActivityForResult(intentFromCamera, CODE_GALLERY_REQUEST);
                mHeadImgPopWindow.dismiss();
                break;
            case R.id.pop_local:  //打开本地相册
                Intent intentFromGallery = new Intent(this, ImageSelectActivity.class);
                intentFromGallery.putExtra("select", 1);
                intentFromGallery.putExtra("type", 1);
                intentFromGallery.putExtra("max",9);
                intentFromGallery.putExtra("isSelect",photoSize);

                startActivityForResult(intentFromGallery, CODE_CAMERA_REQUEST);
                mHeadImgPopWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //头像
                case CODE_GALLERY_REQUEST:
                    ImageBean bean = data.getParcelableExtra("bitmap");
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setSelect(bean.isSelect());
                    imageDTO.setLocal(bean.getIsLocal());
                    imageDTO.setPath(bean.getPath());
                    photos.add(photos.size() - 1, imageDTO);
                    addData();
                    break;
                case CODE_CAMERA_REQUEST:
                    Bundle bundle = data.getExtras();
                    ArrayList<ImageBean> list = bundle.getParcelableArrayList("selectImages");
                    //避免重复添加
//                    if (mMultiImageView.getChildCount() > 0) {
//                        photos.clear();
//                        mMultiImageView.removeAllViews();
//                    }
//                    loadFlowPhotoFirst();
                    for (int i = 0; i < list.size(); i++) {
                        ImageDTO imageDTO2 = new ImageDTO();
                        imageDTO2.setSelect(list.get(i).isSelect());
                        imageDTO2.setLocal(list.get(i).getIsLocal());
                        imageDTO2.setPath(list.get(i).getPath());
                        photos.add(photos.size() - 1, imageDTO2);
                        addData();
                    }
                    break;
            }
        }
    }


    @Override
    public void onDismiss() {
        // 设置背景颜色变暗
        lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 七牛云图片上传成功接口回调
     *
     * @param avatar
     */
    @Override
    public void getavatar(String avatar) {
        for (int i = 0; i < photos.size(); i++) {
            ImageDTO imageDTO = photos.get(i);
            if (imageDTO.getQiniu_path() != null && imageDTO.getQiniu_path().equals(avatar)) {
                imageDTO.setSelect(true);
                photoSucc.add(imageDTO);
                break;
            }
        }
        String msgSuc = "", msgFail = "";
        if (photoFail.size() > 0) {
            msgFail = "\n上传失败 " + photoFail.size() + " / " + photoSize + "张";
        }
        msgSuc = "上传成功 " + photoSucc.size() + " / " + photoSize + "张" + msgFail;

        Message msgLoadPic = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("key", msgSuc);
        msgLoadPic.setData(bundle);
        msgLoadPic.what = 0;
        handlerLoadPic.sendMessage(msgLoadPic);

        sortPhotos();
        if ((photoSucc.size() + photoFail.size() < photoSize)) {
            for (int i = 0; i < photoSucc.size(); i++) {
                if (photos.get(i).getPath().equals(photos.get(photoSucc.size() + photoFail.size()).getPath())) {
                    photos.get(photoSucc.size() + photoFail.size()).setQiniu_path(photos.get(i).getQiniu_path());
                    getavatar(photos.get(i).getQiniu_path());
                    return;
                }
            }

            if (!CommonUtils.getIntnetConnect(mContext)){
                ToastUtil.getInstance(mContext).setContent("网络连接异常").setShow();
                handlerLoadPic.sendEmptyMessage(3);
                return;
            }
            sendPic(photoSucc.size() + photoFail.size());
        }

    }


    /**
     * 七牛云图片上传失败接口回调
     */
    @Override
    public void getavatar_fail(String msg) {

//        for (int i = 0; i < photos.size(); i++) {
//            ImageDTO imageDTO = photos.get(i);
//            if (imageDTO.getQiniu_path() != null && imageDTO.getQiniu_path().equals(msg)) {
//                imageDTO.setSelect(false);
//                photoFail.add(imageDTO);
//                photos.get(i).setLocal_drawable(R.drawable.shi_calendar_def);
//                addData();
//                break;
//            }
//        }

        handlerLoadPic.sendEmptyMessage(3);
        if (!CommonUtils.getIntnetConnect(mContext)){
            ToastUtil.getInstance(mContext).setContent("网络连接异常").setShow();
            return;
        }

//        String msgSuc = "", msgFail = "";
//        if (photoSucc.size() > 0) {
//            msgSuc = "上传成功 " + photoSucc.size() + " / " + photoSize + "张\r\n";
//        }
//        msgFail = msgSuc + "上传失败 " + photoFail.size() + " / " + photoSize + "张";
//
//        Message msgLoadPic = new Message();
//        Bundle bundle = new Bundle();
//        bundle.putString("key", msgFail);
//        msgLoadPic.setData(bundle);
//        msgLoadPic.what = 0;
//        handlerLoadPic.sendMessage(msgLoadPic);

//        sortPhotos();
//        if ((photoSucc.size() + photoFail.size() < photoSize)) {
//            sendPic(photoSucc.size() + photoFail.size());
//        }
    }


    /**
     * 对图片进行排序
     */
    private void sortPhotos() {
        if (photoSucc.size() + photoFail.size() != photoSize) return;
        photoSucc.clear();
        photoSucc.addAll(photos);
        if (photos.size() > photoSize) {
            photoSucc.remove(photoSize);
        }
        if (photoFail != null && photoFail.size() > 0) {
            for (int i = 0; i < photoFail.size(); i++) {
                for (int j = 0; j < photoSucc.size(); j++) {
                    if (photoFail.get(i).getQiniu_path().equals(photoSucc.get(j).getQiniu_path())) {
                        photoSucc.remove(j);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < photoSucc.size(); i++) {
            if (i != 0) {
                posters = posters + ",";
            }
            posters = posters + photoSucc.get(i).getQiniu_path();
        }
        toSendCircle(title, description, isAnonymous, mCircleSelecet.id + "", posters);
    }


    /**
     * 获取七牛云头像上传所需token的接口回调
     *
     * @param token
     */
    @Override
    public void getToken(String token) {
        this.token = token;
        if (photoSize > 0){
            final byte[] bitmapByte = BitmapUtil.huoHuaStringToByteArray(photos.get(0).getPath(), true);
            final String key = photos.get(0).getQiniu_path();
            OkHttpQiniu.getInstance().loadImg(mContext, saveImgSuccessCallback, bitmapByte, key, token);
        }
    }


    /**
     * 删除图片回调
     *
     * @param index
     */
    public void delete_index(int index) {
        photos.remove(index);
        if (photos.size() == (max_pic - 1) && photos.get(max_pic - 2).getLocal_drawable() == 0) {
            loadFlowPhotoFirst();
            return;
        }
        addData();
    }


    /**
     * 接收更新列表消息的广播
     */
    BroadcastReceiver receiverDeletePic = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            switch (action) {
                case BROAD_ACTION_DELETE_PIC:
                    if (bundle != null) {
                        int index = bundle.getInt(PicPagesActivity.PIC_INDEX, -1);
                        if (index != -1) {
                            delete_index(index);
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_DELETE_PIC);
        registerReceiver(receiverDeletePic, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广播
        unregisterReceiver(receiverDeletePic);
        handlerLoadPic.removeMessages(2);

    }


    /**
     * 退出界面弹出框
     */
    private void toBack() {
        RfCommonDialog.Builder builder = new RfCommonDialog.Builder(mContext);
        builder.setTitle("本次退出内容将无法保存,\n是否确定退出？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        RfCommonDialog commonDialog = builder.create();
        commonDialog.setCanceledOnTouchOutside(true);
        commonDialog.setCancelable(true);
        commonDialog.show();

    }


    @Override
    public void onBackPressed() {
        String title = mEdTitle.getText().toString().trim();
        String des = mEdDes.getText().toString().trim();
        if (!StringUtils.isBlank(title) || !StringUtils.isBlank(des) || photoSize != 0) {
            toBack();
        } else {
            super.onBackPressed();
        }
    }

}
