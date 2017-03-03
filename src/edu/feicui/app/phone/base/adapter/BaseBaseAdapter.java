package edu.feicui.app.phone.base.adapter;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//DataType���ⶨ��һ������
public abstract class BaseBaseAdapter<DataType> extends BaseAdapter{
	protected Context context;
	protected LayoutInflater layoutInflater;//���ּ�����
	//���ڴ洢����������������
	private List<DataType> dataList=new ArrayList<DataType>();
	public BaseBaseAdapter(Context context) {
		this.context=context;
		//��ò��ּ���������,���ڽ���item����
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}
	//�������������б�
	public List<DataType> getDataList(){
		return dataList;		
	}
	public void addDataToAdapter(DataType data){
		//���һ������
		dataList.add(data);
	}
	public void addDataToAdapterHead(List<DataType> datas){
		//��Ӷ�����󣨿ɸ���λ����ӣ�
		dataList.addAll(0, datas);
	}
	public void addDataToAdapterEnd(List<DataType> datas){
		//�ڼ���Ԫ�غ�����Ӷ��Ԫ��
		dataList.addAll(dataList.size(), datas);
	}
	public void setDataToAdapter(DataType data){
		//���������Ԫ�أ������һ����Ԫ��
		dataList.clear();
		dataList.add(data);
	}
	public void setDataToAdapter(List<DataType> datas){
		//���������Ԫ�أ�����Ӷ����Ԫ��
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
	//���ݲ�ͬ������Ҫ��д��ͬ��getView����
	public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
