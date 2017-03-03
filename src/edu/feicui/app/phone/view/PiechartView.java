package edu.feicui.app.phone.view;
import java.util.Timer;
import java.util.TimerTask;

import edu.feicui.app.phone.activity.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
public class PiechartView extends View{
	private Paint paint;//画笔
	private RectF oval;//圆形大小(通过圆形外部的正方形大小来控制圆形大小)
	private float proportionPhone=0;//手机内存空间所占小数
	private float proportionSD=0;//存储卡内存空间所占小数
	private float piechartAnglePhone=0;//手机内存空间的角度
	private float piechartAngleSD=0;//存储卡内存空间的角度
	private int phoneColor=0;//手机内存空间饼型颜色(蓝色)
	private int sdColor=0;//存储卡内存空间饼型颜色(绿色)
	private int baseColor=0;//基础颜色(橘黄色)
	//初始化
	public PiechartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint=new Paint();
		phoneColor=context.getResources().getColor(R.color.piechar_phone);
		sdColor=context.getResources().getColor(R.color.piechar_sdcard);
		baseColor=context.getResources().getColor(R.color.piechar_base);
	}
	//测量父容器大小,确定圆形大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//例如底层传入120,120
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int viewWidth=MeasureSpec.getSize(widthMeasureSpec);//120
		int viewHeight=MeasureSpec.getSize(heightMeasureSpec);//120
		oval=new RectF(0, 0, viewWidth, viewHeight);//120*120
		setMeasuredDimension(viewWidth, viewHeight);//设置测量尺寸，返回给底层
	}
	/*
	 *f1:手机内存空间所占总内存小数
	 *f2:内存卡存储空间所占总内存小数
	 */
	//设置饼型图角度
		public void setPiechartProportion(float f1,float f2){
			proportionPhone=f1;
			proportionSD=f2;
			//目标角度
			final float phoneTargetAngle=360*proportionPhone;
			final float sdcardTargetAngle=360*proportionSD;
			//直接将手机饼型与存储卡饼型设定成目标角度
			piechartAnglePhone=phoneTargetAngle;
			piechartAngleSD=sdcardTargetAngle;
			//将图像重画一次
			postInvalidate();
		}
		public void setPiechartProportionWithAnim(float f1,float f2){
			proportionPhone=f1;
			proportionSD=f2;
			final float phoneTargetAngle=360*proportionPhone;
			final float sdcardTargetAngle=360*proportionSD;
			//通过每次+4度，实现旋转动画效果
			final Timer timer=new Timer();
			final TimerTask timerTask=new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					piechartAnglePhone +=4;
					piechartAngleSD +=4;
					//重绘我们的视图View（即重写测量尺寸，再重新调用onDraw（）方法画图形）
					postInvalidate();
					if(piechartAnglePhone>=phoneTargetAngle){	
						//如果我们自增的角度>=我们的目标角度，就不再自增4度
						piechartAnglePhone=phoneTargetAngle;
					}
					if(piechartAngleSD>=sdcardTargetAngle){
						piechartAngleSD=sdcardTargetAngle;
					}
					//当蓝色圆弧和绿色圆弧都转到目标角度，关闭定时器
					if(piechartAnglePhone==phoneTargetAngle&&piechartAngleSD==sdcardTargetAngle){
						timer.cancel();
					}
				}
			};
			timer.schedule(timerTask, 26, 26);
		}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setAntiAlias(true);//锯齿柔化
		paint.setColor(baseColor);//以橘黄色为底色
		//-90 是坐标系的正上方
		canvas.drawArc(oval, -90, 360, true, paint);
		//手机内存空间所占
		paint.setColor(phoneColor);//用蓝色画手机内存空间所占比例
		//从-90开始扫过的度数
		canvas.drawArc(oval, -90, piechartAnglePhone, true, paint);
		//内存卡内存空间所占
		paint.setColor(sdColor);
		canvas.drawArc(oval, -90+piechartAnglePhone, piechartAngleSD, true, paint);
	}
	
}
