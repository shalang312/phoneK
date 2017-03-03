package edu.feicui.app.phone.service;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
//�������ַ���
public class MusicServices extends Service{
	//�������ֲ�����
	MediaPlayer mediaPlayer;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//���ڲ���assetsĿ¼
		AssetManager assetManager=getAssets();
		try {
			//��assetsĿ¼�µ�mo.mp3�����ļ�
			AssetFileDescriptor fileDescriptor=assetManager.openFd("mo.mp3");
			mediaPlayer=new MediaPlayer();//��ʼ�����ֲ�����
			mediaPlayer.setDataSource(//�������ļ����ص�mediaPlayer
					fileDescriptor.getFileDescriptor(),//�õ������ļ���Ϣ
					fileDescriptor.getStartOffset(),//�����ļ��Ŀ�ʼƫ������һ��Ĭ�ϴ�0��ʼ
					fileDescriptor.getLength());//�����ļ�����
			mediaPlayer.prepare();//׼��
			mediaPlayer.start();//��ʼ
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
