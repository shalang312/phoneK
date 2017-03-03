package edu.feicui.app.phone.view;
import edu.feicui.app.phone.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//����������
//�Զ���View
public class ActionBarView extends LinearLayout{
	//��߰�ť���м��ı����ұ߰�ť
	private ImageView iv_actionbar_right;
	private ImageView iv_actionbar_left;
	private TextView tv_actionbar_title;
	public ActionBarView(Context context, AttributeSet attrs) {		
		super(context, attrs);
		//����Բ��ַ���this��LinearLayout�У�
		inflate(context,R.layout.layout_actionbar , this);
		tv_actionbar_title=(TextView) findViewById(R.id.tv_title);
		iv_actionbar_left=(ImageView) findViewById(R.id.iv_left);
		iv_actionbar_right=(ImageView) findViewById(R.id.iv_right);
	}	
	//�������õ�����ImageView�Ƿ���ʾ�Լ����Ч��
	public void initActionBar(String title,int leftResID,int rightResID,OnClickListener listener){
		tv_actionbar_title.setText(title);
		if(leftResID==-1){
			//����������-1�������ͼƬ����ʾ
			iv_actionbar_left.setVisibility(View.INVISIBLE);
		}
		else{
			//������ʾ�����ͼƬ
			iv_actionbar_left.setImageResource(leftResID);
			//�������ü���
			iv_actionbar_left.setOnClickListener(listener);
		}
		if(rightResID==-1){
			//����������-1�����ұ�ͼƬ����ʾ
			iv_actionbar_right.setVisibility(View.INVISIBLE);
		}
		else{
			//������ʾ�����ͼƬ
			iv_actionbar_right.setImageResource(rightResID);
			//�������ü���
			iv_actionbar_right.setOnClickListener(listener);
		}		
	}
}
