package edu.feicui.app.phone.biz;
import android.os.Build;
public class SystemManager {
	//��ȡ��Ҫ���ֻ�Ӳ����Ϣ���ֻ��ͺţ����������Ƶȣ�
	//�豸Ʒ��
	public static String getPhoneName(){
		return Build.BRAND;
	}
	//�豸�ͺ����ƣ�����L55u��
	public static String getPhoneModelName(){
		//����ֻ��ͺźͰ汾��
		return Build.MODEL+" Android "+getPhoneSystemVersion();		
	}
	//�豸ϵͳ�汾�ţ�4.4.2��
	public static String getPhoneSystemVersion(){
		//���ϵͳ�汾�ַ���
		return Build.VERSION.RELEASE;		
	}
}
