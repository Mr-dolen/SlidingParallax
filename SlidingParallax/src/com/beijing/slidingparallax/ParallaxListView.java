package com.beijing.slidingparallax;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.R.anim;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

public class ParallaxListView extends ListView {

	private ImageView parallaxImageView;
	private int maxHeight;
	private int originalHeight;//ԭʼ�ĸ߶�

	public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ParallaxListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxListView(Context context) {
		super(context);
	}

	/**
	 * �����Ӳ�
	 * 
	 * @param parallaxImage
	 */
	public void setParallaxImage(ImageView imageView) {
		parallaxImageView = imageView;
		// ��ȡͼƬ�ĸ߶�
		maxHeight = imageView.getDrawable().getIntrinsicHeight();
		// ����
		parallaxImageView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// ���ȫ�ֵĲ��ּ��������÷�������parallaxImageView�ڸ�VIew��ִ����layout֮��ִ��
					@Override
					public void onGlobalLayout() {
						// һ��������ķ�������������Ƴ�������,��ΪֻҪ��ǰview�Ŀ�߸ı䶼������÷������»ص�
						parallaxImageView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						originalHeight = parallaxImageView.getHeight();
					}
				});
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		//����Ƕ�����ͷ����������ָ������ͷ
		if (deltaY < 0 && isTouchEvent) {
			//1.����deltaY��ImageVIew��height���
			int newHeight = parallaxImageView.getHeight()+Math.abs(deltaY)/3;
			//2.�жϲ�����imageview�ĸ�
			if(newHeight>maxHeight){
				newHeight = maxHeight;
			}
			//3.��newheight���ø�imageview
			ViewGroup.LayoutParams params = parallaxImageView.getLayoutParams();
			params.height = newHeight;
			parallaxImageView.setLayoutParams(params);
		}
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_UP){
			ValueAnimator animator = ValueAnimator.ofInt(parallaxImageView.getHeight(),originalHeight);
			//��������ִ�еĹ���
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					//��ȡ������ǰ��ֵ
					int animatorValue = (Integer) animator.getAnimatedValue();
					//��animatorValue���ø�imageview
					ViewGroup.LayoutParams params = parallaxImageView.getLayoutParams();
					params.height = animatorValue;
					parallaxImageView.setLayoutParams(params);
				}
			});
			//���ûص��Ĳ�ֵ��
			animator.setInterpolator(new OvershootInterpolator(4));//����һ���ٻ�ͷ
			animator.setDuration(350);
			animator.start();
		}
		return super.onTouchEvent(ev);
	}

}
