package edu.feicui.app.phone.base.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
public class BasePagerAdapter extends PagerAdapter{
	protected Context context; 
	//用于存储三张引导图片
	ArrayList<View> viewList=new ArrayList<View>();
	ArrayList<String> tabtitleList=new ArrayList<String>();
	public BasePagerAdapter(Context context) {
		this.context=context;
	}
	public ArrayList<View> getViewList(){
		//返回存储三个imageView视图的集合
		return viewList;		
	}
	//添加视图元素(如果三张视图不够，可以调方法添加)
	public void addViewToAdapter(View view){
		viewList.add(view);
	}
	//向添加的视图里添加标题（本项目没用到）
	public void addTabToAdapter(String title){
		tabtitleList.add(title);
	}
	//获得ViewPager视图上标题
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return tabtitleList.get(position);
	}
	//删除间隔的图片
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//首先获得移出的视图
		//底层判断图片是否有关系
		View view=viewList.get(position);
		//再将得到的视图从viewPager容器中移出，container指的就是viewpager容器
		container.removeView(view);
	}
	//相当于getView方法(组装资源和布局的方法)
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//首先获得要放入的视图
		View view=viewList.get(position);
		//再将视图放入viewpager容器中
		container.addView(view);
		return view;//返回组装好的item
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewList.size();
	}
	//判断移除或添加时是否是同一元素
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
}
