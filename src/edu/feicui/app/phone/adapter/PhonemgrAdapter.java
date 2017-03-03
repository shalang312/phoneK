package edu.feicui.app.phone.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.app.phone.activity.R;
import edu.feicui.app.phone.base.adapter.BaseBaseAdapter;
import edu.feicui.app.phone.entity.PhoneInfo;
public class PhonemgrAdapter extends BaseBaseAdapter<PhoneInfo>{
	private LayoutInflater layoutInflater;
	public PhonemgrAdapter(Context context) {
		super(context);
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.layout_phonemgr_listitem, null);
		}
		//数据在phoneInfo对象里
		PhoneInfo phoneInfo=getItem(position);
		//获取控件
		ImageView icon=(ImageView) convertView.findViewById(R.id.iv_phonemgr_icon);
		TextView title=(TextView) convertView.findViewById(R.id.tv_phonemgr_title);
		TextView text=(TextView) convertView.findViewById(R.id.tv_phonemgr_text);
		//放置数据
		icon.setImageDrawable(phoneInfo.getIcon());
		title.setText(phoneInfo.getTitle());
		text.setText(phoneInfo.getText());
		//每3次循环一次，切换background背景图片
		switch (position%3) {//position是从0开始
		case 0://加载绿色背景图
			icon.setBackgroundResource(R.drawable.notification_information_progress_green);
			break;
		case 1:
			icon.setBackgroundResource(R.drawable.notification_information_progress_red);
			break;
		case 2:
		default://取余为2或者负数都设置黄色
			icon.setBackgroundResource(R.drawable.notification_information_progress_yellow);
			break;
		}
		return convertView;
	}
	
}
