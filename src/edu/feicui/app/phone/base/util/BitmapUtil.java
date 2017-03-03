package edu.feicui.app.phone.base.util;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
/**
 * BitmapUtil
 * 
 * @author yuanc
 * 
 */
public class BitmapUtil {
	public static Bitmap loadBitmap(String pathName, SizeMessage sizeMessage) {
		// ���ص�ͼ��Ŀ���С
		int targetw = sizeMessage.getW();
		int targeth = sizeMessage.getH();
		Context context = sizeMessage.getContext();
		Options options = new Options();
		options.inJustDecodeBounds = true; // ��"�߽紦��"
		BitmapFactory.decodeFile(pathName, options);
		int resw = options.outWidth;
		int resh = options.outHeight;
		if (resw <= targetw && resh <= targeth) {
			options.inSampleSize = 1; // ���ü���ʱ����Դ����
		}
		// ��������
		else {
			int scalew = resw / targetw;
			int scaleh = resh / targeth;
			int scale = scalew > scaleh ? scalew : scaleh;
			options.inSampleSize = scale;// ���ü���ʱ����Դ����
		}
		options.inJustDecodeBounds = false;// �ر�"�߽紦��"
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
		return bitmap;
	}
	public static Bitmap loadBitmap(int resID, SizeMessage sizeMessage) {
		// ���ص�ͼ��Ŀ���С
		int targetw = sizeMessage.getW();
		int targeth = sizeMessage.getH();
		Context context = sizeMessage.getContext();
		Options options = new Options();
		options.inJustDecodeBounds = true; // ��"�߽紦��"
		BitmapFactory.decodeResource(context.getResources(), resID, options);
		int resw = options.outWidth;
		int resh = options.outHeight;
		if (resw <= targetw && resh <= targeth) {
			options.inSampleSize = 1; // ���ü���ʱ����Դ����
		}
		// ��������
		else {
			int scalew = resw / targetw;
			int scaleh = resh / targeth;
			int scale = scalew > scaleh ? scalew : scaleh;
			options.inSampleSize = scale;// ���ü���ʱ����Դ����
		}
		options.inJustDecodeBounds = false;// �ر�"�߽紦��"
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);
		return bitmap;
	}

	public static class SizeMessage {
		private int w;
		private int h;
		private Context context;

		/**
		 * ��С��Ϣ
		 * 
		 * @param context
		 * @param isPx
		 *            �Ƿ�Ϊ���ص�λ
		 * @param w
		 * @param h
		 */
		public SizeMessage(Context context, boolean isPx, int w, int h) {
			this.context = context;
			if (!isPx) {
				w = DeviceUtil.dp2px(context, w);
				h = DeviceUtil.dp2px(context, h);
			}
			this.w = w;
			this.h = h;
		}

		public Context getContext() {
			return context;
		}

		public int getW() {
			return w;
		}

		public void setW(int w) {
			this.w = w;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}
	}
}
