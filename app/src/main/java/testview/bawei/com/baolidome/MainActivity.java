package testview.bawei.com.baolidome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;

public class MainActivity extends AppCompatActivity {

    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView videoView = null;
    /**
     * 缩略图界面
     */
    private PolyvPlayerPreviewView firstStartView = null;
    /**
     * 视频加载缓冲视图
     */
    private ProgressBar loadingProgress = null;
    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaController mediaController = null;

    private String vid="c538856dde2600e0096215c16592d4d3_c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findIdAndNew();
        initView();
        // 点击按钮播放视屏
        play(vid);
    }
    private void initView(){
//        初始化播放器
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);

        videoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean b, boolean b1) {

                if (videoView.isInPlaybackState() && mediaController != null)
                    if (mediaController.isShowing())
                        mediaController.hide();
                    else
                        mediaController.show();
                Toast.makeText(MainActivity.this,"点击屏幕",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findIdAndNew() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        videoView= (PolyvVideoView) findViewById(R.id.polyv_video_view);
        firstStartView = (PolyvPlayerPreviewView) findViewById(R.id.polyv_player_first_start_view);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);//视屏缓存
        mediaController = (PolyvPlayerMediaController) findViewById(R.id.polyv_player_media_controller);//状态栏


        mediaController.initConfig(viewLayout);
        videoView.setMediaController(mediaController);
        videoView.setPlayerBufferingIndicator(loadingProgress);

//        设置准备前的加载
        videoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                mediaController.preparedView();
            }
        });

    }
    private void  play(final String vid){
        videoView.release();
        firstStartView.hide();
        mediaController.hide();//设置默认隐藏
        loadingProgress.setVisibility(View.GONE);
        firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {//点击缩略图后播放
            @Override
            public void onClickStart() {
                videoView.setVid(vid);
            }
        });
        firstStartView.show(vid);//默认让缩略图显示
    }
// 重新返回时
    @Override
    protected void onResume() {
        super.onResume();
        mediaController.resume();
    }
//    暂停时
    @Override
    protected void onPause() {
        super.onPause();
        mediaController.pause();
    }
//停止加载时
    @Override
    protected void onStop() {
        super.onStop();
    }

    //在销毁方法里面停止播放
    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
        mediaController.disable();
    }
}
