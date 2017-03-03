package edu.feicui.app.phone.service;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
//背景音乐服务
public class MusicServices extends Service{
	//背景音乐播放器
	MediaPlayer mediaPlayer;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//用于操作assets目录
		AssetManager assetManager=getAssets();
		try {
			//打开assets目录下的mo.mp3音乐文件
			AssetFileDescriptor fileDescriptor=assetManager.openFd("mo.mp3");
			mediaPlayer=new MediaPlayer();//初始化音乐播放器
			mediaPlayer.setDataSource(//把音乐文件加载到mediaPlayer
					fileDescriptor.getFileDescriptor(),//得到音乐文件信息
					fileDescriptor.getStartOffset(),//音乐文件的开始偏移量，一般默认从0开始
					fileDescriptor.getLength());//音乐文件长度
			mediaPlayer.prepare();//准备
			mediaPlayer.start();//开始
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		mediaPlayer.stop();
		super.onDestroy();
	}
}
