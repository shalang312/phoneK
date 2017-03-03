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
//�ֻ����ٱ���ͼ
public class CleararcView extends View{
	private Paint paint;
	private RectF oval;
	private  final int START_ANGLE=-90;
	private int sweepAngle=0;
	private int state=0;//0: ����  1��ǰ��(�ӽǶ�)
	private int arcColor=0xffff8c00;//Բ����ʼɫ
	//�������ٵ�Խ���ٶ�Խ��
	private int[] back={-6,-6,-10,-10,-10,-12};
	private int backIndex;
	//ǰ��
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
		sweepAngle=angle;//��Ŀ��Ƕȸ�ֵ��sweepAngle
		postInvalidate();
		isRunning=false;//Ĭ��û����
	}
	//����Я�������ĽǶ�
	public void setAngleWithAnim(final int angle){
		if(isRunning){
			return;//������Լ���������ˣ���ô�ٴε��ʱ���Ͳ��ܼ������У��Է����ֻ���
		}
		isRunning=true;
		state=0;//����
		final Timer timer=new Timer();
		final TimerTask timerTask=new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (state) {
				case 0://���ǻ���״̬���Ƕȱ�С����ԭ���ĽǶ��ϼ���
					sweepAngle +=back[backIndex++];
					if(backIndex>=back.length){//������仰���������һ��֮��ÿ�ζ�ȥ-12
						backIndex=back.length-1;//��ֹ�±�Խ��,�±������������ֱ�ӱ�Ϊback.length-1
					}
					postInvalidate();
					if(sweepAngle<=0){
						sweepAngle=0;
						state=1;
						backIndex=0;
					}					
					break;
				case 1://ǰ��
					sweepAngle += gone[goneIndex++];
					if (goneIndex >= gone.length) {
						goneIndex = gone.length - 1;
					}
					postInvalidate();//�ػ�
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
	//Բ�Ĵ�С
	//����
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int viewWidth=MeasureSpec.getSize(widthMeasureSpec);
		int viewHeight=MeasureSpec.getSize(heightMeasureSpec);
		oval=new RectF(0, 0, viewWidth, viewHeight);//����Բ�ķ�Χ��С
		//֪ͨ����������Բ������εĿռ��С
		setMeasuredDimension(viewWidth, viewHeight);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(arcColor);//�����������ٻ�ɫ
		paint.setAntiAlias(true);//�����
		//����תԲ��
		canvas.drawArc(oval, START_ANGLE, sweepAngle, true, paint);
	}	
}
