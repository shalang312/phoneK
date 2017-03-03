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
	private Paint paint;//����
	private RectF oval;//Բ�δ�С(ͨ��Բ���ⲿ�������δ�С������Բ�δ�С)
	private float proportionPhone=0;//�ֻ��ڴ�ռ���ռС��
	private float proportionSD=0;//�洢���ڴ�ռ���ռС��
	private float piechartAnglePhone=0;//�ֻ��ڴ�ռ�ĽǶ�
	private float piechartAngleSD=0;//�洢���ڴ�ռ�ĽǶ�
	private int phoneColor=0;//�ֻ��ڴ�ռ������ɫ(��ɫ)
	private int sdColor=0;//�洢���ڴ�ռ������ɫ(��ɫ)
	private int baseColor=0;//������ɫ(�ٻ�ɫ)
	//��ʼ��
	public PiechartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint=new Paint();
		phoneColor=context.getResources().getColor(R.color.piechar_phone);
		sdColor=context.getResources().getColor(R.color.piechar_sdcard);
		baseColor=context.getResources().getColor(R.color.piechar_base);
	}
	//������������С,ȷ��Բ�δ�С
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//����ײ㴫��120,120
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int viewWidth=MeasureSpec.getSize(widthMeasureSpec);//120
		int viewHeight=MeasureSpec.getSize(heightMeasureSpec);//120
		oval=new RectF(0, 0, viewWidth, viewHeight);//120*120
		setMeasuredDimension(viewWidth, viewHeight);//���ò����ߴ磬���ظ��ײ�
	}
	/*
	 *f1:�ֻ��ڴ�ռ���ռ���ڴ�С��
	 *f2:�ڴ濨�洢�ռ���ռ���ڴ�С��
	 */
	//���ñ���ͼ�Ƕ�
		public void setPiechartProportion(float f1,float f2){
			proportionPhone=f1;
			proportionSD=f2;
			//Ŀ��Ƕ�
			final float phoneTargetAngle=360*proportionPhone;
			final float sdcardTargetAngle=360*proportionSD;
			//ֱ�ӽ��ֻ�������洢�������趨��Ŀ��Ƕ�
			piechartAnglePhone=phoneTargetAngle;
			piechartAngleSD=sdcardTargetAngle;
			//��ͼ���ػ�һ��
			postInvalidate();
		}
		public void setPiechartProportionWithAnim(float f1,float f2){
			proportionPhone=f1;
			proportionSD=f2;
			final float phoneTargetAngle=360*proportionPhone;
			final float sdcardTargetAngle=360*proportionSD;
			//ͨ��ÿ��+4�ȣ�ʵ����ת����Ч��
			final Timer timer=new Timer();
			final TimerTask timerTask=new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					piechartAnglePhone +=4;
					piechartAngleSD +=4;
					//�ػ����ǵ���ͼView������д�����ߴ磬�����µ���onDraw����������ͼ�Σ�
					postInvalidate();
					if(piechartAnglePhone>=phoneTargetAngle){	
						//������������ĽǶ�>=���ǵ�Ŀ��Ƕȣ��Ͳ�������4��
						piechartAnglePhone=phoneTargetAngle;
					}
					if(piechartAngleSD>=sdcardTargetAngle){
						piechartAngleSD=sdcardTargetAngle;
					}
					//����ɫԲ������ɫԲ����ת��Ŀ��Ƕȣ��رն�ʱ��
					if(piechartAnglePhone==phoneTargetAngle&&piechartAngleSD==sdcardTargetAngle){
						timer.cancel();
					}
				}
			};
			timer.schedule(timerTask, 26, 26);
		}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setAntiAlias(true);//����ữ
		paint.setColor(baseColor);//���ٻ�ɫΪ��ɫ
		//-90 ������ϵ�����Ϸ�
		canvas.drawArc(oval, -90, 360, true, paint);
		//�ֻ��ڴ�ռ���ռ
		paint.setColor(phoneColor);//����ɫ���ֻ��ڴ�ռ���ռ����
		//��-90��ʼɨ���Ķ���
		canvas.drawArc(oval, -90, piechartAnglePhone, true, paint);
		//�ڴ濨�ڴ�ռ���ռ
		paint.setColor(sdColor);
		canvas.drawArc(oval, -90+piechartAnglePhone, piechartAngleSD, true, paint);
	}
	
}
