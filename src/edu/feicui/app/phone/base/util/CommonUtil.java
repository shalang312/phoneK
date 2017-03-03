package edu.feicui.app.phone.base.util;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class CommonUtil {
	//获取传入的毫秒数的时间
	public static String getStrTime(long filetime){
		if(filetime==0){
			return "未知";
		}
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//毫秒数先转成date类型，再转成固定格式的字符串
		String fiString=format.format(new Date(filetime));
		return fiString;		
	}
	public static String getStrTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ftime=format.format(new Date(System.currentTimeMillis()));
		return ftime;		
	}
	//判断两个对象是否相等
	public static boolean equals(Object a,Object b){
		//当a==b，比较的是地址，当地址一样就是同一个对象，或者啊！=null，a.equals(b)为true也是同一个对象
		return (a==b)||(a==null?false:a.equals(b));		
	}
	//Decimal 小数  #.00中#表示取到所有整数，00表示小数取两位，且四舍五入
	private static DecimalFormat df=new DecimalFormat("#.00");
	//单位换算
	public static String getFileSize(long filesize){
		StringBuffer mstrbuf=new StringBuffer();
		//filesize单位是B，表示大小小于1K
		if(filesize<1024){
			//这个直接可以整数表示，例如1023B
			mstrbuf.append(filesize);
			mstrbuf.append(" B");
		}
		else if (filesize<1024*1024) {
			//将byte单位换算成K
			//由于从B转换到K时会产生小数，所以需要处理小数
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
