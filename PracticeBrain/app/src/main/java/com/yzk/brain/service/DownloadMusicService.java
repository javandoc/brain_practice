package com.yzk.brain.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.yzk.brain.IDownloadInterface;
import com.yzk.brain.log.LogUtil;
import com.yzk.brain.manager.DownLoadManager;
import com.yzk.brain.task.DownloadRunnable;

/**
 * Created by android on 12/1/16.
 */

public class DownloadMusicService extends Service {

    private DownLoadManager downLoadManager;
    private DownloadMusicService mKeepService;


    @Override
    public void onCreate() {
        super.onCreate();
        mKeepService=this;
        downLoadManager = DownLoadManager.getInstance();
//        HermesEventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        HermesEventBus.getDefault().unregister(this);
//        HermesEventBus.getDefault().destroy();
    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onEventThread(BackgroudMusicEvent.DownloadMusicEvent downloadMusicEvent) {

//        LogUtil.e("onevent:"+downloadMusicEvent.musicEntity.name);
////        DownLoadManager.getInstance().startDownLoad(downloadMusicEvent.mMethod, downloadMusicEvent.mMedia_type, downloadMusicEvent.musicEntity, downloadMusicEvent.mCachePath, downloadMusicEvent.mHeaders);
//        DownloadRunnable downloadRunnable = new DownloadRunnable(downloadMusicEvent.mMethod, downloadMusicEvent.mMedia_type, downloadMusicEvent.musicEntity, downloadMusicEvent.mCachePath, downloadMusicEvent.mHeaders);
//        try {
//            DownLoadManager.getInstance().getQueue().put(downloadRunnable);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }




    public class DownloadBinder extends IDownloadInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }



        @Override
        public void downLoadMusic(int method, int media_type, String url, String name, long fileLenght, int version, String cachedPath) throws RemoteException {
            LogUtil.e("download:"+url);
            DownloadRunnable downloadRunnable = new DownloadRunnable(method, media_type, url, name, fileLenght,version,cachedPath);
            try {
                DownLoadManager.getInstance().getQueue().put(downloadRunnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
