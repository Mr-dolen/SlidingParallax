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
	private int originalHeight;//原始的高度

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
	 * 设置视差
	 * 
	 * @param parallaxImage
	 */
	public void setParallaxImage(ImageView imageView) {
		parallaxImageView = imageView;
		// 获取图片的高度
		maxHeight = imageView.getDrawable().getIntrinsicHeight();
		// 测量
		parallaxImageView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// 添加全局的布局监听器，该方法会在parallaxImageView在父VIew中执行完layout之后执行
					@Override
					public void onGlobalLayout() {
						// 一般监听器的方法用完就立即移除监听器,因为只要当前view的宽高改变都会引起该方法重新回调
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
		//如果是顶部到头，并且是手指触摸到头
		if (deltaY < 0 && isTouchEvent) {
			//1.根据deltaY让ImageVIew的height变高
			int newHeight = parallaxImageView.getHeight()+Math.abs(deltaY)/3;
			//2.判断并限制imageview的高
			if(newHeight>maxHeight){
				newHeight = maxHeight;
			}
			//3.把newheight设置给imageview
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
			//监听动画执行的过程
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					//获取动画当前的值
					int animatorValue = (Integer) animator.getAnimatedValue();
					//把animatorValue设置给imageview
					ViewGroup.LayoutParams params = parallaxImageView.getLayoutParams();
					params.height = animatorValue;
					parallaxImageView.setLayoutParams(params);
				}
			});
			//设置回弹的插值器
			animator.setInterpolator(new OvershootInterpolator(4));//超过一点再回头
			animator.setDuration(350);
			animator.start();
		}
		return super.onTouchEvent(ev);
	}

}
