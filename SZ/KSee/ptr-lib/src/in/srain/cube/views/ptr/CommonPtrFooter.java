package in.srain.cube.views.ptr;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 下拉刷新底部
 * Created by chenlang on 16/11/18.
 */

public class CommonPtrFooter extends FrameLayout implements PtrUIHandler {


    private ImageView mImageView;
    private String mLastUpdateTimeKey;

    public CommonPtrFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CommonPtrFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonPtrFooter(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_ptr_footer,this);
        mImageView=(ImageView) view.findViewById(R.id.img);
        mImageView.setBackgroundResource(R.drawable.anim_common_dialog_loading01);
        startAnimaiton();
    }

    private void startAnimaiton(){
        AnimationDrawable animation = (AnimationDrawable)mImageView.getBackground();
        animation.start();
    }


    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
