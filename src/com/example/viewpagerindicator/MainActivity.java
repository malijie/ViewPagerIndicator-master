package com.example.viewpagerindicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.view.ViewPagerIndicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/**
 * 
 * @author Administrator
 * 
 * 
 *         FragmentActivity is a special activity provided in the Support
 *         Library to handle fragments on system versions older than API level
 *         11. If the lowest system version you support is API level 11 or
 *         higher, then you can use a regular Activity.
 */
public class MainActivity extends FragmentActivity {

	private ViewPager mViewpager;
	private ViewPagerIndicator mViewPagerIndicator;
	private List<String> mTitles = Arrays.asList("����1", "�ղ�2", "�Ƽ�3", "����4",
			"�ղ�5", "�Ƽ�6", "����7", "�ղ�8", "�Ƽ�9");
	private List<VpSimpleFragment> mContents = new ArrayList<VpSimpleFragment>();// װ��ViewPager���ݵ�List
	/**
	 * FragmentPagerAdapter������֪�⣬�����������������ʵ��Fragment��ViewPager������л����л��ģ���ˣ�
	 * ���������ʵ��Fragment�����һ���������ѡ��ViewPager��FragmentPagerAdapterʵ�֡�
	 * FragmentPagerAdapterӵ���Լ��Ļ������
	 * ������ViewPager���ʹ�õ�ʱ�򣬻Ỻ�浱ǰFragment�Լ����һ�����ұ�һ����һ������Fragment����
	 * ����������Fragment
	 * ,��ô��ViewPager��ʼ��֮��3��fragment���������ɣ��м��Fragment������������������ֻ�����һ��
	 * ��������ߵ�Fragment������ʾ״̬
	 * �����ұߵ�Fragment���ڳ������淶Χ���ᱻ���٣����ٴλ����м��Fragment��ʱ�����ұߵ�Fragment�ᱻ�ٴγ�ʼ����
	 */
	private FragmentPagerAdapter mAdapter;// ViewPager������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
		
		//��̬����tab
		mViewPagerIndicator.setVisibleTabCount(4);
		mViewPagerIndicator.setTabItemTitles(mTitles);
		
		mViewpager.setAdapter(mAdapter);
		mViewPagerIndicator.setViewPager(mViewpager, 0);
//		mViewpager.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrolled(int position, float positionOffset,
//					int positionOffsetPixels) {
//				// �����θ���ViewPager�ƶ��ľ�����ǣ�
//				// tabWidth*positionOffset+position*tabWidth
//				mViewPagerIndicator.scroll(position, positionOffset);
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initDatas() {
		mViewpager = (ViewPager) findViewById(R.id.viewpager);
		mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
	}

	/**
	 * ��ʼ������
	 */
	private void initViews() {
		// ����title��ʼ��fragment
		for (String title : mTitles) {
			VpSimpleFragment fragment = VpSimpleFragment.newInstance(title);
			mContents.add(fragment);
		}
		// getFragmentManager();
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mContents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mContents.get(position);
			}
		};
	}
}
