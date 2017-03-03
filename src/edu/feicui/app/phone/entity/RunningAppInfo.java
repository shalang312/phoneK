package edu.feicui.app.phone.entity;
import android.graphics.drawable.Drawable;
public class RunningAppInfo {
	private String packageName;//安装包的包名
	private Drawable icon;//安装包的图标
	private String labelName;//系统包包名（APP名字）
	private boolean isClear;//是否需要清除（CheckBox）
	private boolean isSystem;//是否是系统APP,是的话显示红字
	private long size;//应用大小（版本）
	public RunningAppInfo() {
		// TODO Auto-generated constructor stub
	}
	public RunningAppInfo(String packageName, Drawable icon, String labelName,
			long size) {
		super();
		this.packageName = packageName;
		this.icon = icon;
		this.labelName = labelName;
		this.size = size;
	}
	@Override
	public String toString() {
		return "[packageName=" + packageName + ", icon=" + icon
				+ ", lableName=" + labelName + ", isClear=" + isClear
				+ ", isSystem=" + isSystem + ", size=" + size + "]";
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getLableName() {
		return labelName;
	}
	public void setLableName(String lableName) {
		this.labelName = lableName;
	}
	public boolean isClear() {
		return isClear;
	}
	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
}
