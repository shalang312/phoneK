package edu.feicui.app.phone.view;
import edu.feicui.app.phone.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//动作导航条
//自定义View
public class ActionBarView extends LinearLayout{
	//左边按钮，中间文本，右边按钮
	private ImageView iv_actionbar_right;
	private ImageView iv_actionbar_left;
	private TextView tv_actionbar_title;
	public ActionBarView(Context context, AttributeSet attrs) {		
		super(context, attrs);
		//将相对布局放入this（LinearLayout中）
		inflate(context,R.layout.layout_actionbar , this);
		tv_actionbar_title=(TextView) findViewById(R.id.tv_title);
		iv_actionbar_left=(ImageView) findViewById(R.id.iv_left);
		iv_actionbar_right=(ImageView) findViewById(R.id.iv_right);
	}	
	//用于设置导航条ImageView是否显示以及点击效果
	public void initActionBar(String title,int leftResID,int rightResID,OnClickListener listener){
		tv_actionbar_title.setText(title);
		if(leftResID==-1){
			//如果传入的是-1，则左边图片不显示
			iv_actionbar_left.setVisibility(View.INVISIBLE);
		}
		else{
			//否则显示传入的图片
			iv_actionbar_left.setImageResource(leftResID);
			//并且设置监听
			iv_actionbar_left.setOnClickListener(listener);
		}
		if(rightResID==-1){
			//如果传入的是-1，则右边图片不显示
			iv_actionbar_right.setVisibility(View.INVISIBLE);
		}
		else{
			//否则显示传入的图片
			iv_actionbar_right.setImageResource(rightResID);
			//并且设置监听
			iv_actionbar_right.setOnClickListener(listener);
		}		
	}
}
