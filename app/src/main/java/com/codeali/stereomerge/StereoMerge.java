package com.codeali.stereomerge;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by Ziggy on 3/7/2019.
 */

enum ResourceLocation{
    PATH, RESOURCE;
}
public class StereoMerge {

    private SoundPool soundPoolLeft, soundPoolRight;
    private Context mContext;
    private int resourceLeft, resourceRight;
    private String leftAudioFilePath, rightAudioFilePath;

    private ResourceLocation resourceLocation;


    public StereoMerge(Context mContext) {
        this.mContext = mContext;
        this.resourceLeft = R.raw.life;
        this.resourceRight = R.raw.to_inf;
        resourceLocation = ResourceLocation.RESOURCE;
        init();
    }

    public StereoMerge(Context mContext, int resourceLeft, int resourceRight) {
        this.mContext = mContext;
        this.resourceLeft = resourceLeft;
        this.resourceRight = resourceRight;
        resourceLocation = ResourceLocation.RESOURCE;
        init();
    }

    public StereoMerge(Context mContext, String leftAudioFilePath, String rightAudioFilePath) {
        this.mContext = mContext;
        this.leftAudioFilePath = leftAudioFilePath;
        this.rightAudioFilePath = rightAudioFilePath;
        resourceLocation = ResourceLocation.PATH;
        init();
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPoolLeft = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();

            soundPoolRight = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            soundPoolLeft = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundPoolRight = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        soundPoolLeft.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 0, 0, 0, 1.0f); //left channel
                //soundPool.play(sampleId, 0, 1.0f, 0, 0, 1.0f); //right channel
            }
        });

        soundPoolRight.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                //soundPool.play(sampleId, 1.0f, 0, 0, 0, 1.0f); //left channel
                soundPool.play(sampleId, 0, 1.0f, 0, 0, 1.0f); //right channel
            }
        });
    }

    public void stop() {
        if (soundPoolLeft != null) {
            soundPoolLeft.release();
        }

        if (soundPoolRight != null) {
            soundPoolRight.release();
        }
    }

    public void start() {
        if (soundPoolLeft != null && soundPoolRight != null) {
            switch (resourceLocation) {
                case PATH:
                    soundPoolLeft.load(leftAudioFilePath, 1);
                    soundPoolRight.load(rightAudioFilePath, 1);
                    break;
                case RESOURCE:
                    soundPoolLeft.load(mContext, resourceLeft, 1);
                    soundPoolRight.load(mContext, resourceRight, 1);
                    break;
                default:
                    System.out.println("NO RESOURCE LOCATION");
                    break;
            }
        }
    }
}
