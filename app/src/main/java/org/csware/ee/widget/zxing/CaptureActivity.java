package org.csware.ee.widget.zxing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.csware.ee.R;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.zxing.camera.CameraManager;
import org.csware.ee.widget.zxing.decoding.CaptureActivityHandler;
import org.csware.ee.widget.zxing.decoding.InactivityTimer;
import org.csware.ee.widget.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends ActivityBase implements Callback {

    @InjectView(R.id.fragCamera)
    FrameLayout fragCamera;
    @InjectView(R.id.LinJurisdiction)
    LinearLayout LinJurisdiction;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    //    private Button cancelScanButton;
    TopActionBar topBar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        ButterKnife.inject(this);

        PackageManager pkm = getPackageManager();
//        PackageManager packManager = this.getPackageManager();
//        List<ApplicationInfo> infos = packManager.getInstalledApplications(PackageManager.GET_ACTIVITIES);
//        PackageInfo packInfo = null;
//        try {
//            packInfo = packManager.getPackageInfo(this.getApplicationInfo().packageName, PackageManager.GET_PERMISSIONS);
//            String permissons [] = packInfo.requestedPermissions;
////获取该app的所有权限
//            int length = permissons.length;
//            for(int i = 0 ;i < length ;i ++){
//                Tracer.d("Capture", permissons[i]);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        boolean has_permission = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.CAMERA));
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission(Manifest.permission.CAMERA, this.getApplicationInfo().packageName));
//        toastFast(has_permission+"");
        if (!has_permission) {
            fragCamera.setVisibility(View.GONE);
            LinJurisdiction.setVisibility(View.VISIBLE);
//            toastFast("对不起，没有拍照权限！");
        }else {
            fragCamera.setVisibility(View.VISIBLE);
            LinJurisdiction.setVisibility(View.GONE);
            CameraManager.init(getApplication());
            viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            topBar = (TopActionBar) findViewById(R.id.topBar);
            topBar.setTitle("扫码支付");
    //        cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
            hasSurface = false;
            inactivityTimer = new InactivityTimer(this);
        }

        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
    }
//    private void insertDummyContactWrapper() {
//        int hasPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
//        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS},
//                    REQUEST_CODE_ASK_PERMISSIONS);
//            return;
//        }
//        insertDummyContact();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pkm = getPackageManager();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", this.getApplicationInfo().packageName));
        if (has_permission) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            decodeFormats = null;
            characterSet = null;

            playBeep = true;
            AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                playBeep = false;
            }
            initBeepSound();
            vibrate = true;
        }
        //quit the scan view
//        cancelScanButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                CaptureActivity.this.finish();
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pkm = getPackageManager();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", this.getApplicationInfo().packageName));
        if (has_permission) {
            if (handler != null) {
                handler.quitSynchronously();
                handler = null;
            }
            CameraManager.get().closeDriver();
        }
    }

    @Override
    protected void onDestroy() {
        PackageManager pkm = getPackageManager();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", this.getApplicationInfo().packageName));
        if (has_permission) {
            inactivityTimer.shutdown();
        }
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Tracer.e("Capture Result:", resultString);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}