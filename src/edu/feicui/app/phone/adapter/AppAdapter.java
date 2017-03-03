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
		//��ʼ��������
		convertView=layoutInflater.inflate(R.layout.layout_showapp_listitem, null);
		//���item��ÿһ���ؼ�
		iv_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
		tv_title=(TextView) convertView.findViewById(R.id.tv_app_lable);
		tv_text=(TextView) convertView.findViewById(R.id.tv_app_packagename);
		tv_version=(TextView) convertView.findViewById(R.id.tv_app_version);
		cb_del=(CheckBox) convertView.findViewById(R.id.cb_del);
		cb_del.setOnCheckedChangeListener(checkedChangelistener);
		//��ȡCheckBoxλ��
		cb_del.setTag(position);
		//��ȡÿһ������
		AppInfo appInfo=getItem(position);
		//�����ݷŵ��ؼ���
		//title(Ӧ�ó�������)
		//����title=������
		String title=appInfo.getPackageInfo().applicationInfo.loadLabel(context.getPackageManager()).toString();
		//����
		String text=appInfo.getPackageInfo().packageName;
		//�汾
		String version=appInfo.getPackageInfo().versionName;
		//��ȡCheckBox������
		boolean isDel=appInfo.isDel();
		//��ȡiconͼ��
		Drawable drawble=appInfo.getPackageInfo().applicationInfo.loadIcon(context.getPackageManager());
		iv_icon.setImageDrawable(drawble);
		tv_title.setText(text);
		tv_text.setText(text);
		tv_version.setText(version);
		cb_del.setChecked(isDel);
		return convertView;
	}	
	//����CheckBox�Ƿ�ѡ
	OnCheckedChangeListener checkedChangelistener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//��ȡCheckBoxλ��
			int position=(Integer) buttonView.getTag();
			getDataList().get(position).setDel(isChecked);
		}
	};
}
