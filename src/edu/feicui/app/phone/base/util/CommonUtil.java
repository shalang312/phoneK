package edu.feicui.app.phone.base.util;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CommonUtil {
	//��ȡ����ĺ�������ʱ��
	public static String getStrTime(long filetime){
		if(filetime==0){
			return "δ֪";
		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//��������ת��date���ͣ���ת�ɹ̶���ʽ���ַ���
		String fiString=format.format(new Date(filetime));
		return fiString;		
	}
	public static String getStrTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ftime=format.format(new Date(System.currentTimeMillis()));
		return ftime;		
	}
	//�ж����������Ƿ����
	public static boolean equals(Object a,Object b){
		//��a==b���Ƚϵ��ǵ�ַ������ַһ������ͬһ�����󣬻��߰���=null��a.equals(b)ΪtrueҲ��ͬһ������
		return (a==b)||(a==null?false:a.equals(b));		
	}
	//Decimal С��  #.00��#��ʾȡ������������00��ʾС��ȡ��λ������������
	private static DecimalFormat df=new DecimalFormat("#.00");
	//��λ����
	public static String getFileSize(long filesize){
		StringBuffer mstrbuf=new StringBuffer();
		//filesize��λ��B����ʾ��СС��1K
		if(filesize<1024){
			//���ֱ�ӿ���������ʾ������1023B
			mstrbuf.append(filesize);
			mstrbuf.append(" B");
		}
		else if (filesize<1024*1024) {
			//��byte��λ�����K
			//���ڴ�Bת����Kʱ�����С����������Ҫ����С��
			mstrbuf.append(df.format((double)filesize/1024));
			mstrbuf.append(" K");
		}
		else if (filesize<1024*1024*1024) {
			mstrbuf.append(df.format((double)filesize/1024/1024));
			mstrbuf.append(" M");
		}else {
			mstrbuf.append(df.format((double)filesize/1024/1024/1024));
			mstrbuf.append(" G");
		}
		return mstrbuf.toString();		
	}
}
