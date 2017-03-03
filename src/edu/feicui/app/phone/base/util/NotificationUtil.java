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
		//记住状态
		SharedPreferences preferences=context.getSharedPreferences("notifi", context.MODE_PRIVATE);
		//只有为true时，才能打开Notification
		//第一次默认值为true
		return preferences.getBoolean("open", true);		
	}
	public static void setOpenNotification(Context context,boolean open){
		SharedPreferences preferences=context.getSharedPreferences("notifi", context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		//open(即为isChecked)代表是否改变了的状态，即true或者false
		editor.putBoolean("open", open);
		editor.commit();
	}
	//清除Notification
	public static void cancelAppIconNotification(Context context){
		if(manager==null){
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);			
		}
		manager.cancel(NOTIFI_APPICON_ID);
	}
	//显示通知
	public static void showAppIconNotification(Context context){
		if(notification==null){
			notification=new Notification();
		}
		notification.icon=R.drawable.ic_launcher_1;
		notification.tickerText="新通知";
		notification.when=System.currentTimeMillis();
		notification.flags=Notification.FLAG_AUTO_CANCEL;
		//自定义自己的Notification视图
		RemoteViews contentView=new RemoteViews(context.getPackageName(), R.layout.layout_notification_appicon);
		//将自定义布局放入通知中
		notification.contentView=contentView;
		Intent intent=new Intent(context, HomeActivity.class);
		//常量随便设置
		//通知特有的意图属性
		PendingIntent contentIntent=PendingIntent.getActivity(context, 1, intent, 1);
		notification.contentIntent=contentIntent;
		if(manager==null){
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);			
		}
		manager.notify(NOTIFI_APPICON_ID, notification);//启动通知
	}
}
