package edu.feicui.app.phone.base.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
public class BasePagerAdapter extends PagerAdapter{
	protected Context context; 
	//���ڴ洢��������ͼƬ
	ArrayList<View> viewList=new ArrayList<View>();
	ArrayList<String> tabtitleList=new ArrayList<String>();
	public BasePagerAdapter(Context context) {
		this.context=context;
	}
	public ArrayList<View> getViewList(){
		//���ش洢����imageView��ͼ�ļ���
		return viewList;		
	}
	//�����ͼԪ��(���������ͼ���������Ե��������)
	public void addViewToAdapter(View view){
		viewList.add(view);
	}
	//����ӵ���ͼ����ӱ��⣨����Ŀû�õ���
	public void addTabToAdapter(String title){
		tabtitleList.add(title);
	}
	//���ViewPager��ͼ�ϱ���
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return tabtitleList.get(position);
	}
	//ɾ�������ͼƬ
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//���Ȼ���Ƴ�����ͼ
		//�ײ��ж�ͼƬ�Ƿ��й�ϵ
		View view=viewList.get(position);
		//�ٽ��õ�����ͼ��viewPager�������Ƴ���containerָ�ľ���viewpager����
		container.removeView(view);
	}
	//�൱��getView����(��װ��Դ�Ͳ��ֵķ���)
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//���Ȼ��Ҫ�������ͼ
		View view=viewList.get(position);
		//�ٽ���ͼ����viewpager������
		container.addView(view);
		return view;//������װ�õ�item
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewList.size();
	}
	//�ж��Ƴ������ʱ�Ƿ���ͬһԪ��
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
}
