package edu.feicui.app.phone.base.adapter;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//DataType随意定义一个泛型
public abstract class BaseBaseAdapter<DataType> extends BaseAdapter{
	protected Context context;
	protected LayoutInflater layoutInflater;//布局加载器
	//用于存储适配器里所有数据
	private List<DataType> dataList=new ArrayList<DataType>();
	public BaseBaseAdapter(Context context) {
		this.context=context;
		//获得布局加载器对象,用于解析item布局
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}
	//返回所有数据列表
	public List<DataType> getDataList(){
		return dataList;		
	}
	public void addDataToAdapter(DataType data){
		//添加一个对象
		dataList.add(data);
	}
	public void addDataToAdapterHead(List<DataType> datas){
		//添加多个对象（可根据位置添加）
		dataList.addAll(0, datas);
	}
	public void addDataToAdapterEnd(List<DataType> datas){
		//在集合元素后面添加多个元素
		dataList.addAll(dataList.size(), datas);
	}
	public void setDataToAdapter(DataType data){
		//先清除所有元素，再添加一个新元素
		dataList.clear();
		dataList.add(data);
	}
	public void setDataToAdapter(List<DataType> datas){
		//先清除所有元素，再添加多个新元素
		dataList.clear();
		dataList.addAll(datas);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList==null?0:dataList.size();
	}
	@Override
	public DataType getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItemView(position, convertView, parent);
	}
	//根据不同子类需要重写不同的getView方法
	public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
