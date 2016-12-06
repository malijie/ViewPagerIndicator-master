package com.example.view;

import java.util.List;

import com.example.viewpagerindicator.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {
	private Paint mPaint;// ���������εĻ���
	private Path mPath;// ���ڻ��������εı�
	private int mTriangleWidth;// �����εĿ�
	private int mTriangleHeight;// �����εĸ�
	private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;// �������������εĿ��tab�ױߵı�����������Ļ����
	/**
	 * �����εױߵ������
	 */
	private final int DIMENSION_TRIANGLE_WIDTH_MAX = (int) (getScreenWidth()/3*RADIO_TRIANGLE_WIDTH);
	private int mInitTranslationX;// ��һ�������γ�ʼ����ƫ��λ��
	private int mTranslationX;// �ƶ�ʱ���������ƫ��λ��
	private int mTabVisibleCount;// �ɼ�tab������
	private static final int COUNT_DEFAULT_TAB = 4;// Ĭ�Ͽɼ�tabΪ4��
	private List<String> mTitles;// ���մ��ݹ�����title
	private static final int COLOR_TEXT_NORMAL = Color.parseColor("#FFFFFF");
	private static final int COLOR_TEXT_HIGHLIGHT = Color.parseColor("#FF4CDA0F");

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		// ��ȡ�ɼ�tab������
		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.ViewPagerIndicator);
		mTabVisibleCount = attributes.getInt(
				R.styleable.ViewPagerIndicator_visible_tab_count,
				COUNT_DEFAULT_TAB);
		if (mTabVisibleCount < 0) {
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		}
		// ��������ͷ�
		attributes.recycle();

		// ��ʼ������
		mPaint = new Paint();
		// ��ֹ��Ե���
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffff"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	/**
	 * ����������
	 * ����VIew��������ݣ�ͨ������View.onDraw(canvas)����ʵ��,�����Լ��ĺ���ͨ��dispatchDraw��canvas��ʵ��
	 * 
	 * ���걳����draw���̻����onDraw(Canvas canvas)������Ȼ�����dispatchDraw(Canvas canvas)����,
	 * dispatchDraw
	 * ()��Ҫ�Ƿַ�����������л��ƣ�����ͨ�����������ʱ����д����onDraw()������ֵ��ע�����ViewGroup��������Ļ���
	 * ������û�б���ʱֱ�ӵ��õ���dispatchDraw
	 * ()����,���ƹ���draw()�����������б�����ʱ��͵���draw()��������draw()�����������
	 * dispatchDraw()�����ĵ��á����Ҫ��ViewGroup�ϻ��ƶ�����ʱ��������д����
	 * dispatchDraw()����������onDraw()�����������Զ���һ��Drawable����д����draw(Canvas c)��
	 * getIntrinsicWidth(),getIntrinsicHeight()������Ȼ����Ϊ����
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		/**
		 * save����������Canvas��״̬��save֮�󣬿��Ե���Canvas��ƽ�ơ���������ת�����С��ü��Ȳ�����
		 * 
		 * restore�������ָ�Canvas֮ǰ�����״̬����ֹsave���Canvasִ�еĲ����Ժ����Ļ�����Ӱ�졣
		 * 
		 * save��restoreҪ���ʹ�ã�restore���Ա�save�٣������ࣩܶ�����restore���ô�����save�࣬������Error��
		 */
		canvas.save();

		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
		canvas.drawPath(mPath, mPaint);

		canvas.restore();
	}

	/**
	 * ���������εĴ�С
	 * 
	 * onSizeChanged()�ڿؼ���С�����仯��ʱ�����(�����һ�γ�ʼ���ؼ���ʱ��) ���ֹ����У�
	 * �ȵ�onMeasure����ÿ��child�Ĵ�С�� Ȼ�����onLayout��child���в��֣�
	 * onSizeChanged�������ڲ��ַ����仯ʱ�Ļص���������ӻ�ȥ����onMeasure, onLayout�������²���
	 * onSizeChanged������ʱ����onDraw֮ǰ
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		// w/3Ϊÿ��tab�Ŀ�ȣ�Ŀǰ�ɼ�Ϊ3��
		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);
		//ѡȡ��С����һ����Ϊ��
		mTriangleWidth = Math.min(mTriangleWidth, DIMENSION_TRIANGLE_WIDTH_MAX);
		// ��һ�������ε�ƫ��λ��
		mInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;

		initTriangle();
	}

	/**
	 * ��ʼ��������
	 */
	private void initTriangle() {
		// mTriangleHeight = mTriangleWidth / 2;
		// �������νǶ�����Ϊ30��
		mTriangleHeight = (int) (mTriangleWidth / 2 * Math.tan(Math.PI / 6));

		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
		// �رյ�ǰ��������ɱպ�
		mPath.close();
	}

	/**
	 * �����θ���ViewPager�ƶ�
	 * 
	 * @param position
	 * @param positionOffset
	 */
	public void scroll(int position, float positionOffset) {
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (positionOffset + position));

		/**
		 * �����ƶ�,��tab�����ƶ������һ��ʱ
		 */
		if (position >= (mTabVisibleCount - 2) && positionOffset > 0
				&& getChildCount() > mTabVisibleCount) {

			if (mTabVisibleCount != 1) {
				this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
						+ (int) (tabWidth * positionOffset), 0);
			} else {
				this.scrollTo(position * tabWidth
						+ (int) (tabWidth * positionOffset), 0);
			}
		}

		// λ�÷����ı䣬Ҫ�����ػ�
		/**
		 * invalidate����˼�ǡ�ʹ��Ч������ʵ����ʹ������Ч�� ʹ��ǰ�Ĵ�����Ч��Ŀ�ľ�����Windows֪������������ڸ����»���һ���ˡ�
		 * �����κ�ʱ������ ���� �� ���ƴ��ڵ�ʱ�򣬾Ϳ����ڱ�ĺ�������ɹ��ܴ���֮��Invalidate()һ�¡�OnDraw���Ͼͻᱻ�����ˡ�
		 * ���ǲ�Ҫ��OnDraw, OnPaint����
		 */
		invalidate();
	}

	/**
	 * xml�������֮�󣬻ص��˷���
	 * 
	 * ����ÿ��tab��LayoutParams
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int childCount = getChildCount();
		if (childCount == 0) {
			return;
		}

		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams params = (LayoutParams) view
					.getLayoutParams();
			params.weight = 0;
			params.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(params);
		}
		setItemClickEvent();
	}

	/**
	 * ��ȡ��Ļ�Ŀ��
	 * 
	 * @return
	 */
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * ��̬����tab������
	 * 
	 * @param count
	 */
	public void setVisibleTabCount(int count) {
		mTabVisibleCount = count;
	}

	/**
	 * ��̬����tab
	 * 
	 * @param titles
	 */
	public void setTabItemTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			mTitles = titles;
			for (String title : mTitles) {
				this.addView(generateTextView(title));
			}
			setItemClickEvent();
		}
	}

	

	/**
	 * ����title����tab
	 * 
	 * @param title
	 * @return
	 */
	private View generateTextView(String title) {
		TextView textView = new TextView(getContext());
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.width = getScreenWidth() / mTabVisibleCount;
		textView.setText(title);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView.setTextColor(COLOR_TEXT_NORMAL);
		textView.setLayoutParams(params);
		return textView;
	}

	// ���չ�����ViewPager
	private ViewPager mViewPager;

	/**
	 * �ṩһ���ӿڹ��ⲿViewPagerʹ��
	 * 
	 * @author Administrator
	 * 
	 */
	public interface PageOnChangeListener {
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	public PageOnChangeListener mListener;

	public void setViewPagerOnPageChangeListener(PageOnChangeListener listener) {
		mListener = listener;
	}

	/**
	 * ���ù�����ViewPager
	 * 
	 * @param viewpager
	 * @param position
	 */
	public void setViewPager(ViewPager viewpager, int position) {
		mViewPager = viewpager;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (mListener != null) {
					mListener.onPageSelected(position);
				}
				highLightTextView(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// �����θ���ViewPager�ƶ��ľ�����ǣ�
				// tabWidth*positionOffset+position*tabWidth
				scroll(position, positionOffset);

				if (mListener != null) {
					mListener.onPageScrolled(position, positionOffset,
							positionOffsetPixels);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (mListener != null) {
					mListener.onPageScrollStateChanged(state);
				}

			}
		});
		mViewPager.setCurrentItem(position);
		highLightTextView(position);
	}
	/**
	 * �����������tab
	 * @param position
	 */
	private void highLightTextView(int position){
		resetTextViewColor();
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
		}
	}
	/**
	 * ����tab�ı���ɫ
	 */
	private void resetTextViewColor(){
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}
	/**
	 * ����Tab�ĵ���¼�
	 */
	private void setItemClickEvent(){
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}
}
