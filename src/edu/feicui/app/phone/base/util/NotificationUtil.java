package edu.feicui.app.phone.base.util;
import edu.feicui.app.phone.activity.HomeActivity;
import edu.feicui.app.phone.activity.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;
public class NotificationUtil {
	private static NotificationManager manager;
	private static Notification notification;
	public static final int NOTIFI_APPICON_ID=1;
	public static boolean isOpenNotification(Context context){
		//��ס״̬
		SharedPreferences preferences=context.getSharedPreferences("notifi", context.MODE_PRIVATE);
		//ֻ��Ϊtrueʱ�����ܴ�Notification
		//��һ��Ĭ��ֵΪtrue
		return preferences.getBoolean("open", true);		
	}
	public static void setOpenNotification(Context context,boolean open){
		SharedPreferences preferences=context.getSharedPreferences("notifi", context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		//open(��ΪisChecked)�����Ƿ�ı��˵�״̬����true����false
		editor.putBoolean("open", open);
		editor.commit();
	}
	//���Notification
	public static void cancelAppIconNotification(Context context){
		if(manager==null){
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);			
		}
		manager.cancel(NOTIFI_APPICON_ID);
	}
	//��ʾ֪ͨ
	public static void showAppIconNotification(Context context){
		if(notification==null){
			notification=new Notification();
		}
		notification.icon=R.drawable.ic_launcher_1;
		notification.tickerText="��֪ͨ";
		notification.when=System.currentTimeMillis();
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		//�Զ����Լ���Notification��ͼ
		RemoteViews contentView=new RemoteViews(context.getPackageName(), R.layout.layout_notification_appicon);
		//���Զ��岼�ַ���֪ͨ��
		notification.contentView=contentView;
		Intent intent=new Intent(context, HomeActivity.class);
		//�����������
		//֪ͨ���е���ͼ����
		PendingIntent contentIntent=PendingIntent.getActivity(context, 1, intent, 1);
		notification.contentIntent=contentIntent;
		if(manager==null){
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);			
		}
		manager.notify(NOTIFI_APPICON_ID, notification);//����֪ͨ
	}
}
