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
//手机加速饼型图
public class CleararcView extends View{
	private Paint paint;
	private RectF oval;
	private  final int START_ANGLE=-90;
	private int sweepAngle=0;
	private int state=0;//0: 回退  1：前进(加角度)
	private int arcColor=0xffff8c00;//圆弧初始色
	//度数较少的越大，速度越快
	private int[] back={-6,-6,-10,-10,-10,-12};
	private int backIndex;
	//前进
	private int[] gone={12,12,12,12,10,10,10,8,8,8,6};
	private int goneIndex;
	private boolean isRunning;
	public CleararcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAngle(360);
		paint=new Paint();
		arcColor=context.getResources().getColor(R.color.clear_arc_color);
	}	
	public void setAngle(final int angle){
		sweepAngle=angle;//把目标角度赋值给sweepAngle
		postInvalidate();
		isRunning=false;//默认没运行
	}
	//设置携带动画的角度
	public void setAngleWithAnim(final int angle){
		if(isRunning){
			return;//如果在以及点击运行了，那么再次点击时，就不能继续运行，以防出现混乱
		}
		isRunning=true;
		state=0;//回退
		final Timer timer=new Timer();
		final TimerTask timerTask=new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (state) {
				case 0://若是回退状态，角度变小即在原来的角度上减少
					sweepAngle +=back[backIndex++];
					if(backIndex>=back.length){//下上这句话，导致最后一次之后每次都去-12
						backIndex=back.length-1;//防止下标越界,下标如果超过，则直接变为back.length-1
					}
					postInvalidate();
					if(sweepAngle<=0){
						sweepAngle=0;
						state=1;
						backIndex=0;
					}					
					break;
				case 1://前进
					sweepAngle += gone[goneIndex++];
					if (goneIndex >= gone.length) {
						goneIndex = gone.length - 1;
					}
					postInvalidate();//重绘
					if (sweepAngle >= angle) {
						sweepAngle = angle;
						timer.cancel();
						goneIndex = 0;
						isRunning = false;
					}
					break;
				default:
					break;
				}
			}
		};
		timer.schedule(timerTask, 24, 24);
	}
	//圆的大小
	//测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int viewWidth=MeasureSpec.getSize(widthMeasureSpec);
		int viewHeight=MeasureSpec.getSize(heightMeasureSpec);
		oval=new RectF(0, 0, viewWidth, viewHeight);//限制圆的范围大小
		//通知父容器，给圆分配矩形的空间大小
		setMeasuredDimension(viewWidth, viewHeight);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(arcColor);//给画笔设置橘黄色
		paint.setAntiAlias(true);//抗锯齿
		//会旋转圆形
		canvas.drawArc(oval, START_ANGLE, sweepAngle, true, paint);
	}	
}
