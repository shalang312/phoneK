package edu.feicui.app.phone.junit;
import edu.feicui.app.phone.biz.AppInfoManager;
import edu.feicui.app.phone.biz.MemoryManager;
import android.content.Context;
import android.test.AndroidTestCase;
//Androidµ¥Ôª²âÊÔ
public class AndroidTest extends AndroidTestCase{
	Context context;
	public AndroidTest(Context context) {
		this.context=context;
	}
	public void AppInfoManagerTest(){
		MemoryManager.getPhoneTotalRamMemory();
	}
	public void MemoryManagerTest(){
		MemoryManager.getPhoneInSDCardPath();
		MemoryManager.getPhoneOutSDCardSize(context);
		MemoryManager.getPhoneOutSDCardFreeSize(context);
		MemoryManager.getPhoneSelfSize();
		MemoryManager.getPhoneSelfFreeSize();
		MemoryManager.getPhoneSelfSDCardSize();
		MemoryManager.getPhoneSelfSDCardFreeSize();
		MemoryManager.getPhoneAllSize();
		MemoryManager.getPhoneAllFreeSize();
		
	}
}
