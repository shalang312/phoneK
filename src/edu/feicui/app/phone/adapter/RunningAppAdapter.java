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
	//��ʾ��ǰ״̬
	private int state=0;
	public static final int STATE_SHOW_USER=0;//��ʾ�û�����
	public static final int STATE_SHOW_ALL=1;//��ʾȫ������	
	public static final int STATE_SHOW_SYS=2;//��ʾϵͳ����	
	//��ȡ��ǰ״̬
	public int getState(){
		return state;		
	}
	public void setState(int state){
		this.state=state;
	}
	public RunningAppAdapter(Context context) {
		super(context);
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		state=STATE_SHOW_USER;//Ĭ��һ��ʼ��ʾ�û�ϵͳ
	}	
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.layout_speedup_listitem, null);
		}
		//Ӧ�ó�������
		TextView tv_lable=(TextView) convertView.findViewById(R.id.tv_app_lable);
		//��ʾ�ڴ��С
		TextView tv_size=(TextView) convertView.findViewById(R.id.tv_app_packagename);
		//ͼ��
		ImageView iv_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
		CheckBox cb_clear=(CheckBox) convertView.findViewById(R.id.cb_clear);
		//��ʾ�ұ�ϵͳ���̵���Ϣ
		TextView tv_message=(TextView) convertView.findViewById(R.id.tv_app_version);
		//��ȡCheckBoxλ��
		cb_clear.setTag(position);
		// ���� CheckBox		
		cb_clear.setOnCheckedChangeListener(checkedChangeListener);
		//��Ӧ�ó������Ʒŵ���Ӧ�ؼ��ϣ����磺ҹ�����棩
		tv_lable.setText(getItem(position).getLableName());
		//��Ӧ�ó�����ռ�ڴ�ŵ���Ӧ�Ŀؼ��ϣ����磺13.20M��
		tv_size.setText("�ڴ�:"+CommonUtil.getFileSize(getItem(position).getSize()));
		//��Ӧ�ó����ͼ��ŵ���Ӧ�Ŀؼ��ϣ����磺ҹ������ͼ�꣩
		iv_icon.setImageDrawable(getItem(position).getIcon());
		//��CheckBox��Ӧ�Ƿ�ѡ�����ݷŵ���Ӧ�Ŀؼ��ϣ����磺ҹ������Ĭ����û��ѡ״̬(false)��
		cb_clear.setChecked(getItem(position).isClear());
		//�����ϵͳ���̰ѡ�ϵͳ���̡��ַ����ŵ���Ӧ�Ŀؼ���,�������ϵͳ���̣���Ϊ�հ�
		tv_message.setText(getItem(position).isSystem()?"ϵͳ����":"");
		return convertView;
	}
	OnCheckedChangeListener checkedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//�Ȼ�ȡCheckBoxλ��
			int position=(Integer) buttonView.getTag();
			getItem(position).setClear(isChecked);
		}
	};
}
