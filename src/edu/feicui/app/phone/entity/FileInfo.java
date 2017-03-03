package edu.feicui.app.phone.entity;
import java.io.File;
public class FileInfo {
	private File file;
	private boolean isSelect;
	private String fileType;
	private String iconName;
	public FileInfo(File file, String fileType,
			String iconName) {
		super();
		this.file = file;
		isSelect=false;//没有CheckBox可以选择
		this.fileType = fileType;
		this.iconName = iconName;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	@Override
	public String toString() {
		return "[file=" + file + ", isSelect=" + isSelect
				+ ", fileType=" + fileType + ", iconName=" + iconName + "]";
	}
	
}
