package edu.feicui.app.phone.activity;
import java.util.ArrayList;
import edu.feicui.app.phone.adapter.TelListAdapter;
import edu.feicui.app.phone.base.BaseActivity;
import edu.feicui.app.phone.biz.DBTelManager;
import edu.feicui.app.phone.entity.TableInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class TelmgrShowActivity extends BaseActivity {
	private ListView listView;
	private TelListAdapter telListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telmgr_show);
		// ȡ��IDXֵ (table1?table2?table3?......)
		// ȡ��titleֵ(ActionBar��ʾ�����绰�����͵绰��...)
		Intent intent = getIntent();
		int idx = intent.getIntExtra("idx", 0);
		String title = intent.getStringExtra("title");
		if (title == null || title.equals("")) {
			title = "�绰�б�";
		}
		// ��ʼ��ActionBar @see super class ActionBarActivity
		initActionBar(title, R.drawable.btn_homeasup_default, -1, clickListener);
		// ������ʾ�绰�б��listview
		listView = (ListView) findViewById(R.id.listView);
		telListAdapter = new TelListAdapter(this);
		listView.setAdapter(telListAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TableInfo tableInfo = telListAdapter.getDataList().get(position);
				long number = tableInfo.getNumber();
				// ͨ����ǰ������λ�ã�ȡ�����绰��Ϣ��ʵ�壬ȡ���绰�����к���
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + number));
				startActivity(intent);
			}
		});
		//
		asyncInitAdapterData(idx);
	}

	private void asyncInitAdapterData(final int idx) {
		final ArrayList<TableInfo> tableInfos = new ArrayList<TableInfo>();
		tableInfos.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				tableInfos.addAll(DBTelManager.readTable("table" + idx));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						telListAdapter.setDataToAdapter(tableInfos);
						telListAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int viewID = v.getId();
			switch (viewID) {
			case R.id.iv_left:
				finish();
				break;
			}
		}
	};
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

}
