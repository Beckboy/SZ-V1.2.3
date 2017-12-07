package com.junhsue.ksee;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.utils.ScreenWindowUtil;

import java.io.File;

public class Splash_GuidanceActivity extends BaseActivity implements View.OnClickListener{

  private VideoView videoView;

  String videoPath;


  @Override
  protected void onReceiveArguments(Bundle bundle) {
    videoPath = getIntent().getStringExtra("VideoPath");
  }

  @Override
  protected int setLayoutId() {
    return R.layout.activity_splash__guidance;
  }

  @Override
  protected void onInitilizeView() {

    initView();

  }

  private void initView() {
    videoView = (VideoView) this.findViewById(R.id.id_video);

    findViewById(R.id.txt_wel_login).setOnClickListener(this);
    findViewById(R.id.txt_wel_register).setOnClickListener(this);

    loadData();

  }


  protected void loadData() {
    File file = new File(videoPath);
    if (!file.exists()){
      Trace.i("视频文件不存在");
    }else {
      videoView.setVideoPath(file.getPath());
      int width = ScreenWindowUtil.getScreenWidth(this);
//      int height = ScreenWindowUtil.getScreenHeight(this) - ScreenWindowUtil.getStatusHeight(this);
      int height = ScreenWindowUtil.getScreenHeight(this);
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
          //设置为填充父窗体
          width,
          height);
      //设置布局
//      videoView.setLayoutParams(layoutParams);
      //循环播放
      videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
          mp.setLooping(true);
        }
      });
      videoView.start();
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (videoView != null){
      //释放掉占用的内存
      videoView.suspend();
    }
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    if (null != videoView ){
      loadData();
    }
  }

  @Override
  public void onClick(View v) {
    finish();
    switch(v.getId()){
      case R.id.txt_wel_login:
        startActivity(new Intent(this,LoginActivity.class));
        break;
      case R.id.txt_wel_register:
        startActivity(new Intent(Splash_GuidanceActivity.this,Register1Activity.class));
        break;
    }
  }
}
