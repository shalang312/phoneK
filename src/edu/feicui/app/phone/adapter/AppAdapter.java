package edu.feicui.app.phone.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.activity.R.drawable;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.entity.AppInfo;
public class AppAdapter extends BaseBaseAdapter<AppInfo>{
	private LayoutInflater layoutInflater;
	public AppAdapter(Context context) {
		super(context);
		layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}	
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_text;
		TextView tv_version;
		CheckBox cb_del;
		//初始化加载器
		convertView=layoutInflater.inflate(R.layout.layout_showapp_listitem, null);
		//获得item上每一个控件
		iv_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
		tv_title=(TextView) convertView.findViewById(R.id.tv_app_lable);
		tv_text=(TextView) convertView.findViewById(R.id.tv_app_packagename);
		tv_version=(TextView) convertView.findViewById(R.id.tv_app_version);
		cb_del=(CheckBox) convertView.findViewById(R.id.cb_del);
		cb_del.setOnCheckedChangeListener(checkedChangelistener);
		//获取CheckBox位置
		cb_del.setTag(position);
		//获取每一个对象
		AppInfo appInfo=getItem(position);
		//将数据放到控件上
		//title(应用程序名字)
		//比如title=收音机
		String title=appInfo.getPackageInfo().applicationInfo.loadLabel(context.getPackageManager()).toString();
		//包名
		String text=appInfo.getPackageInfo().packageName;
		//版本
		String version=appInfo.getPackageInfo().versionName;
		//获取CheckBox上数据
		boolean isDel=appInfo.isDel();
		//获取icon图标
		Drawable drawble=appInfo.getPackageInfo().applicationInfo.loadIcon(context.getPackageManager());
		iv_icon.setImageDrawable(drawble);
		tv_title.setText(text);
		tv_text.setText(text);
		tv_version.setText(version);
		cb_del.setChecked(isDel);
		return convertView;
	}	
	//监听CheckBox是否勾选
	OnCheckedChangeListener checkedChangelistener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//获取CheckBox位置
			int position=(Integer) buttonView.getTag();
			getDataList().get(position).setDel(isChecked);
		}
	};
}
