package com.example.viewpagerindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VpSimpleFragment extends Fragment {
	private String mTitle;//�����û���������title
	private static final String BUNDLE_TITLE = "title";//����bundle��key

	/**
	 * fragmentһ��ʹ��newInstance����new��ʵ��
	 * 
	 * @param title
	 * @return
	 */
	public static VpSimpleFragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);

		VpSimpleFragment fragment = new VpSimpleFragment();
		/**
		 * �ṩ�ṹ���������Fragment��ֻ����Fragment��������Activity֮ǰ������(��仰����������⣬
		 * setArgument������ʹ�ñ���Ҫ��FragmentTransaction ��commit֮ǰʹ�á� )��Ҳ����˵
		 * ��Ӧ���ڹ���fragment֮�����̵������������ṩ�Ĳ�������fragment destroy ��creation��������
		 * 
		 * 
		 * �ٷ��Ƽ�Fragment.setArguments(Bundle bundle)���ַ�ʽ�����ݲ����������Ƽ�ͨ�����췽��ֱ�������ݲ���
		 * ������Ϊ����Activity���´������������л���ʱ�������¹������������Fragment��ԭ�ȵ�Fragment���ֶ�ֵ����ȫ
		 * ����ʧ������ͨ��Fragment.setArguments(Bundle bundle)�������õ�bundle�ᱣ�����������Ծ���ʹ��
		 * Fragment.setArguments(Bundle bundle)��ʽ�����ݲ���
		 */
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle != null){
			mTitle = bundle.getString(BUNDLE_TITLE);
		}
		//��ʾtitle��fragment��
		TextView textView = new TextView(getActivity());
		textView.setText(mTitle);
		textView.setGravity(Gravity.CENTER);
		
		return textView;
	}
}
