package site.simpleproject.checkphonespro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mShotSound;
    private int mStreamID;
    ImageView imageLeft, imageRight;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNewSoundPool();

        mAssetManager = getAssets();

        mShotSound = loadSound("shot.ogg");

        imageLeft = findViewById(R.id.imageLeft);
        imageLeft.setOnTouchListener(onTouchListener);

        imageRight = findViewById(R.id.imageRight);
        imageRight.setOnTouchListener(onTouchListener);

        ImageView imageLike = findViewById(R.id.imageLike);
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "site.simpleproject.checkphonespro")));
                } catch (android.content.ActivityNotFoundException asd) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "site.simpleproject.checkphonespro")));
                }
            }
        });
    }

    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    private int playSound(int sound, boolean left) {
        if (sound > 0) {
            if (left) {
                mStreamID = mSoundPool.play(sound, 1, 0, 1, -1, 0.95f);

            } else {
                mStreamID = mSoundPool.play(sound, 0, 1, 1, -1, 0.95f);
            }
        }
            return mStreamID;
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener(){
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            switch (v.getId()) {
                case R.id.imageLeft:
                    if (eventAction == MotionEvent.ACTION_UP) {
                        if (mStreamID > 0) {
                            mSoundPool.stop(mStreamID);
                            imageLeft.setImageResource(R.drawable.headphonesleft);
                        }
                    }
                    if (eventAction == MotionEvent.ACTION_DOWN) {
                        mStreamID = playSound(mShotSound, true);
                        imageLeft.setImageResource(R.drawable.headphonesleftsound);
                    }
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        mSoundPool.stop(mStreamID);
                        imageLeft.setImageResource(R.drawable.headphonesleft);
                    }
                    return true;
                case R.id.imageRight:
                    if (eventAction == MotionEvent.ACTION_UP) {
                        if (mStreamID > 0) {
                            mSoundPool.stop(mStreamID);
                            imageRight.setImageResource(R.drawable.headphonesright);
                        }
                    }
                    if (eventAction == MotionEvent.ACTION_DOWN) {
                        mStreamID = playSound(mShotSound, false);
                        imageRight.setImageResource(R.drawable.headphonesrightsound);
                    }
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        mSoundPool.stop(mStreamID);
                        imageRight.setImageResource(R.drawable.headphonesright);
                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        createNewSoundPool();
        mAssetManager = getAssets();
        mShotSound = loadSound("shot.ogg");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
        mSoundPool = null;
    }


}