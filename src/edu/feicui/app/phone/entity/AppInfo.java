package edu.feicui.app.phone.entity;
import android.content.pm.PackageInfo;
public class AppInfo {
	//是否写在APP的类
	private PackageInfo packageInfo;
	private boolean isDel;//是否清理（是否被选中）
	public AppInfo(PackageInfo packageInfo){
		this(packageInfo, false);
	}
	public AppInfo(PackageInfo packageInfo, boolean isDel) {
		super();
		this.packageInfo = packageInfo;
		this.isDel = isDel;
	}
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	public boolean isDel() {
		return isDel;
	}
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	@Override
	public String toString() {
		return "[packageInfo=" + packageInfo + ", isDel=" + isDel + "]";
	}
	
}
