package edu.feicui.app.phone.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.base.util.CommonUtil;
import edu.feicui.app.phone.entity.RunningAppInfo;
public class RunningAppAdapter extends BaseBaseAdapter<RunningAppInfo>{
	private LayoutInflater layoutInflater;
	//表示当前状态
	private int state=0;
	public static final int STATE_SHOW_USER=0;//显示用户进程
	public static final int STATE_SHOW_ALL=1;//显示全部进程	
	public static final int STATE_SHOW_SYS=2;//显示系统进程	
	//获取当前状态
	public int getState(){
		return state;		
	}
	public void setState(int state){
		this.state=state;
	}
	public RunningAppAdapter(Context context) {
		super(context);
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		state=STATE_SHOW_USER;//默认一开始显示用户系统
	}	
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.layout_speedup_listitem, null);
		}
		//应用程序名称
		TextView tv_lable=(TextView) convertView.findViewById(R.id.tv_app_lable);
		//显示内存大小
		TextView tv_size=(TextView) convertView.findViewById(R.id.tv_app_packagename);
		//图标
		ImageView iv_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
		CheckBox cb_clear=(CheckBox) convertView.findViewById(R.id.cb_clear);
		//显示右边系统进程的信息
		TextView tv_message=(TextView) convertView.findViewById(R.id.tv_app_version);
		//获取CheckBox位置
		cb_clear.setTag(position);
		// 监听 CheckBox		
		cb_clear.setOnCheckedChangeListener(checkedChangeListener);
		//把应用程序名称放到对应控件上（例如：夜神桌面）
		tv_lable.setText(getItem(position).getLableName());
		//把应用程序锁占内存放到对应的控件上（例如：13.20M）
		tv_size.setText("内存:"+CommonUtil.getFileSize(getItem(position).getSize()));
		//把应用程序的图标放到对应的控件上（例如：夜神桌面图标）
		iv_icon.setImageDrawable(getItem(position).getIcon());
		//把CheckBox对应是否勾选的数据放到对应的控件上（例如：夜神桌面默认是没勾选状态(false)）
		cb_clear.setChecked(getItem(position).isClear());
		//如果是系统进程把“系统进程”字符串放到对应的控件上,如果不是系统进程，则为空白
		tv_message.setText(getItem(position).isSystem()?"系统进程":"");
		return convertView;
	}
	OnCheckedChangeListener checkedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//先获取CheckBox位置
			int position=(Integer) buttonView.getTag();
			getItem(position).setClear(isChecked);
		}
	};
}
