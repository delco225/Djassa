package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ProgressBarICS
  extends View
{
  private static final int ANIMATION_RESOLUTION = 200;
  private static final int MAX_LEVEL = 10000;
  private static final int[] android_R_styleable_ProgressBar = { 16843062, 16843063, 16843064, 16843065, 16843066, 16843067, 16843068, 16843069, 16843070, 16843071, 16843039, 16843072, 16843040, 16843073 };
  private AlphaAnimation mAnimation;
  private int mBehavior;
  private Drawable mCurrentDrawable;
  private int mDuration;
  private boolean mInDrawing;
  private boolean mIndeterminate;
  private Drawable mIndeterminateDrawable;
  private Interpolator mInterpolator;
  private long mLastDrawTime;
  private int mMax;
  int mMaxHeight;
  int mMaxWidth;
  int mMinHeight;
  int mMinWidth;
  private boolean mNoInvalidate;
  private boolean mOnlyIndeterminate;
  private int mProgress;
  private Drawable mProgressDrawable;
  private RefreshProgressRunnable mRefreshProgressRunnable;
  Bitmap mSampleTile;
  private int mSecondaryProgress;
  private boolean mShouldStartAnimationDrawable;
  private Transformation mTransformation;
  private long mUiThreadId = Thread.currentThread().getId();
  
  public ProgressBarICS(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1);
    initProgressBar();
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, android_R_styleable_ProgressBar, paramInt1, paramInt2);
    this.mNoInvalidate = true;
    setMax(localTypedArray.getInt(0, this.mMax));
    setProgress(localTypedArray.getInt(1, this.mProgress));
    setSecondaryProgress(localTypedArray.getInt(2, this.mSecondaryProgress));
    boolean bool1 = localTypedArray.getBoolean(3, this.mIndeterminate);
    this.mOnlyIndeterminate = localTypedArray.getBoolean(4, this.mOnlyIndeterminate);
    Drawable localDrawable1 = localTypedArray.getDrawable(5);
    if (localDrawable1 != null) {
      setIndeterminateDrawable(tileifyIndeterminate(localDrawable1));
    }
    Drawable localDrawable2 = localTypedArray.getDrawable(6);
    if (localDrawable2 != null) {
      setProgressDrawable(tileify(localDrawable2, false));
    }
    this.mDuration = localTypedArray.getInt(7, this.mDuration);
    this.mBehavior = localTypedArray.getInt(8, this.mBehavior);
    this.mMinWidth = localTypedArray.getDimensionPixelSize(9, this.mMinWidth);
    this.mMaxWidth = localTypedArray.getDimensionPixelSize(10, this.mMaxWidth);
    this.mMinHeight = localTypedArray.getDimensionPixelSize(11, this.mMinHeight);
    this.mMaxHeight = localTypedArray.getDimensionPixelSize(12, this.mMaxHeight);
    int i = localTypedArray.getResourceId(13, 17432587);
    if (i > 0) {
      setInterpolator(paramContext, i);
    }
    localTypedArray.recycle();
    this.mNoInvalidate = false;
    boolean bool2;
    if (!this.mOnlyIndeterminate)
    {
      bool2 = false;
      if (!bool1) {}
    }
    else
    {
      bool2 = true;
    }
    setIndeterminate(bool2);
  }
  
  private void doRefreshProgress(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    for (;;)
    {
      Drawable localDrawable2;
      try
      {
        float f;
        Drawable localDrawable1;
        if (this.mMax > 0)
        {
          f = paramInt2 / this.mMax;
          localDrawable1 = this.mCurrentDrawable;
          if (localDrawable1 != null)
          {
            boolean bool = localDrawable1 instanceof LayerDrawable;
            localDrawable2 = null;
            if (!bool) {
              break label97;
            }
            localDrawable2 = ((LayerDrawable)localDrawable1).findDrawableByLayerId(paramInt1);
            break label97;
            localDrawable2.setLevel(i);
          }
        }
        else
        {
          f = 0.0F;
          continue;
          localDrawable2 = localDrawable1;
          continue;
        }
        invalidate();
        continue;
        int i = (int)(10000.0F * f);
      }
      finally {}
      label97:
      if (localDrawable2 == null) {}
    }
  }
  
  private void initProgressBar()
  {
    this.mMax = 100;
    this.mProgress = 0;
    this.mSecondaryProgress = 0;
    this.mIndeterminate = false;
    this.mOnlyIndeterminate = false;
    this.mDuration = 4000;
    this.mBehavior = 1;
    this.mMinWidth = 24;
    this.mMaxWidth = 48;
    this.mMinHeight = 24;
    this.mMaxHeight = 48;
  }
  
  private void refreshProgress(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    for (;;)
    {
      try
      {
        if (this.mUiThreadId == Thread.currentThread().getId())
        {
          doRefreshProgress(paramInt1, paramInt2, paramBoolean, true);
          return;
        }
        RefreshProgressRunnable localRefreshProgressRunnable;
        if (this.mRefreshProgressRunnable != null)
        {
          localRefreshProgressRunnable = this.mRefreshProgressRunnable;
          this.mRefreshProgressRunnable = null;
          localRefreshProgressRunnable.setup(paramInt1, paramInt2, paramBoolean);
          post(localRefreshProgressRunnable);
        }
        else
        {
          localRefreshProgressRunnable = new RefreshProgressRunnable(paramInt1, paramInt2, paramBoolean);
        }
      }
      finally {}
    }
  }
  
  private Drawable tileify(Drawable paramDrawable, boolean paramBoolean)
  {
    Object localObject2;
    if ((paramDrawable instanceof LayerDrawable))
    {
      LayerDrawable localLayerDrawable = (LayerDrawable)paramDrawable;
      int i = localLayerDrawable.getNumberOfLayers();
      Drawable[] arrayOfDrawable = new Drawable[i];
      int j = 0;
      if (j < i)
      {
        int m = localLayerDrawable.getId(j);
        Drawable localDrawable = localLayerDrawable.getDrawable(j);
        if ((m == 16908301) || (m == 16908303)) {}
        for (boolean bool = true;; bool = false)
        {
          arrayOfDrawable[j] = tileify(localDrawable, bool);
          j++;
          break;
        }
      }
      localObject2 = new LayerDrawable(arrayOfDrawable);
      for (int k = 0; k < i; k++) {
        ((LayerDrawable)localObject2).setId(k, localLayerDrawable.getId(k));
      }
    }
    if ((paramDrawable instanceof BitmapDrawable))
    {
      Bitmap localBitmap = ((BitmapDrawable)paramDrawable).getBitmap();
      if (this.mSampleTile == null) {
        this.mSampleTile = localBitmap;
      }
      Object localObject1 = new ShapeDrawable(getDrawableShape());
      BitmapShader localBitmapShader = new BitmapShader(localBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
      ((ShapeDrawable)localObject1).getPaint().setShader(localBitmapShader);
      if (paramBoolean) {
        localObject1 = new ClipDrawable((Drawable)localObject1, 3, 1);
      }
      localObject2 = localObject1;
      return localObject2;
    }
    return paramDrawable;
  }
  
  private Drawable tileifyIndeterminate(Drawable paramDrawable)
  {
    if ((paramDrawable instanceof AnimationDrawable))
    {
      AnimationDrawable localAnimationDrawable1 = (AnimationDrawable)paramDrawable;
      int i = localAnimationDrawable1.getNumberOfFrames();
      AnimationDrawable localAnimationDrawable2 = new AnimationDrawable();
      localAnimationDrawable2.setOneShot(localAnimationDrawable1.isOneShot());
      for (int j = 0; j < i; j++)
      {
        Drawable localDrawable = tileify(localAnimationDrawable1.getFrame(j), true);
        localDrawable.setLevel(10000);
        localAnimationDrawable2.addFrame(localDrawable, localAnimationDrawable1.getDuration(j));
      }
      localAnimationDrawable2.setLevel(10000);
      paramDrawable = localAnimationDrawable2;
    }
    return paramDrawable;
  }
  
  private void updateDrawableBounds(int paramInt1, int paramInt2)
  {
    int i = paramInt1 - getPaddingRight() - getPaddingLeft();
    int j = paramInt2 - getPaddingBottom() - getPaddingTop();
    int k;
    int m;
    float f1;
    if (this.mIndeterminateDrawable != null)
    {
      boolean bool1 = this.mOnlyIndeterminate;
      k = 0;
      m = 0;
      if (bool1)
      {
        boolean bool2 = this.mIndeterminateDrawable instanceof AnimationDrawable;
        k = 0;
        m = 0;
        if (!bool2)
        {
          int n = this.mIndeterminateDrawable.getIntrinsicWidth();
          int i1 = this.mIndeterminateDrawable.getIntrinsicHeight();
          f1 = n / i1;
          float f2 = paramInt1 / paramInt2;
          boolean bool3 = f1 < f2;
          k = 0;
          m = 0;
          if (bool3)
          {
            if (f2 <= f1) {
              break label185;
            }
            int i3 = (int)(f1 * paramInt2);
            k = (paramInt1 - i3) / 2;
            i = k + i3;
          }
        }
      }
    }
    for (;;)
    {
      this.mIndeterminateDrawable.setBounds(k, m, i, j);
      if (this.mProgressDrawable != null) {
        this.mProgressDrawable.setBounds(0, 0, i, j);
      }
      return;
      label185:
      int i2 = (int)(paramInt1 * (1.0F / f1));
      m = (paramInt2 - i2) / 2;
      j = m + i2;
      k = 0;
    }
  }
  
  private void updateDrawableState()
  {
    int[] arrayOfInt = getDrawableState();
    if ((this.mProgressDrawable != null) && (this.mProgressDrawable.isStateful())) {
      this.mProgressDrawable.setState(arrayOfInt);
    }
    if ((this.mIndeterminateDrawable != null) && (this.mIndeterminateDrawable.isStateful())) {
      this.mIndeterminateDrawable.setState(arrayOfInt);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    updateDrawableState();
  }
  
  Shape getDrawableShape()
  {
    return new RoundRectShape(new float[] { 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F }, null, null);
  }
  
  public Drawable getIndeterminateDrawable()
  {
    return this.mIndeterminateDrawable;
  }
  
  public Interpolator getInterpolator()
  {
    return this.mInterpolator;
  }
  
  public int getMax()
  {
    try
    {
      int i = this.mMax;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  public int getProgress()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +9 -> 17
    //   11: iconst_0
    //   12: istore_3
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_3
    //   16: ireturn
    //   17: aload_0
    //   18: getfield 103	android/support/v7/internal/widget/ProgressBarICS:mProgress	I
    //   21: istore_3
    //   22: goto -9 -> 13
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	30	0	this	ProgressBarICS
    //   25	4	1	localObject	Object
    //   6	2	2	bool	boolean
    //   12	10	3	i	int
    // Exception table:
    //   from	to	target	type
    //   2	7	25	finally
    //   17	22	25	finally
  }
  
  public Drawable getProgressDrawable()
  {
    return this.mProgressDrawable;
  }
  
  /* Error */
  public int getSecondaryProgress()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifeq +9 -> 17
    //   11: iconst_0
    //   12: istore_3
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_3
    //   16: ireturn
    //   17: aload_0
    //   18: getfield 108	android/support/v7/internal/widget/ProgressBarICS:mSecondaryProgress	I
    //   21: istore_3
    //   22: goto -9 -> 13
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	30	0	this	ProgressBarICS
    //   25	4	1	localObject	Object
    //   6	2	2	bool	boolean
    //   12	10	3	i	int
    // Exception table:
    //   from	to	target	type
    //   2	7	25	finally
    //   17	22	25	finally
  }
  
  public final void incrementProgressBy(int paramInt)
  {
    try
    {
      setProgress(paramInt + this.mProgress);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final void incrementSecondaryProgressBy(int paramInt)
  {
    try
    {
      setSecondaryProgress(paramInt + this.mSecondaryProgress);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (!this.mInDrawing)
    {
      if (verifyDrawable(paramDrawable))
      {
        Rect localRect = paramDrawable.getBounds();
        int i = getScrollX() + getPaddingLeft();
        int j = getScrollY() + getPaddingTop();
        invalidate(i + localRect.left, j + localRect.top, i + localRect.right, j + localRect.bottom);
      }
    }
    else {
      return;
    }
    super.invalidateDrawable(paramDrawable);
  }
  
  public boolean isIndeterminate()
  {
    try
    {
      boolean bool = this.mIndeterminate;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (this.mIndeterminate) {
      startAnimation();
    }
  }
  
  protected void onDetachedFromWindow()
  {
    if (this.mIndeterminate) {
      stopAnimation();
    }
    if (this.mRefreshProgressRunnable != null) {
      removeCallbacks(this.mRefreshProgressRunnable);
    }
    super.onDetachedFromWindow();
  }
  
  /* Error */
  protected void onDraw(android.graphics.Canvas paramCanvas)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial 414	android/view/View:onDraw	(Landroid/graphics/Canvas;)V
    //   7: aload_0
    //   8: getfield 180	android/support/v7/internal/widget/ProgressBarICS:mCurrentDrawable	Landroid/graphics/drawable/Drawable;
    //   11: astore_3
    //   12: aload_3
    //   13: ifnull +145 -> 158
    //   16: aload_1
    //   17: invokevirtual 419	android/graphics/Canvas:save	()I
    //   20: pop
    //   21: aload_1
    //   22: aload_0
    //   23: invokevirtual 304	android/support/v7/internal/widget/ProgressBarICS:getPaddingLeft	()I
    //   26: i2f
    //   27: aload_0
    //   28: invokevirtual 310	android/support/v7/internal/widget/ProgressBarICS:getPaddingTop	()I
    //   31: i2f
    //   32: invokevirtual 423	android/graphics/Canvas:translate	(FF)V
    //   35: aload_0
    //   36: invokevirtual 426	android/support/v7/internal/widget/ProgressBarICS:getDrawingTime	()J
    //   39: lstore 5
    //   41: aload_0
    //   42: getfield 428	android/support/v7/internal/widget/ProgressBarICS:mAnimation	Landroid/view/animation/AlphaAnimation;
    //   45: ifnull +76 -> 121
    //   48: aload_0
    //   49: getfield 428	android/support/v7/internal/widget/ProgressBarICS:mAnimation	Landroid/view/animation/AlphaAnimation;
    //   52: lload 5
    //   54: aload_0
    //   55: getfield 430	android/support/v7/internal/widget/ProgressBarICS:mTransformation	Landroid/view/animation/Transformation;
    //   58: invokevirtual 436	android/view/animation/AlphaAnimation:getTransformation	(JLandroid/view/animation/Transformation;)Z
    //   61: pop
    //   62: aload_0
    //   63: getfield 430	android/support/v7/internal/widget/ProgressBarICS:mTransformation	Landroid/view/animation/Transformation;
    //   66: invokevirtual 442	android/view/animation/Transformation:getAlpha	()F
    //   69: fstore 8
    //   71: aload_0
    //   72: iconst_1
    //   73: putfield 362	android/support/v7/internal/widget/ProgressBarICS:mInDrawing	Z
    //   76: aload_3
    //   77: ldc 195
    //   79: fload 8
    //   81: fmul
    //   82: f2i
    //   83: invokevirtual 191	android/graphics/drawable/Drawable:setLevel	(I)Z
    //   86: pop
    //   87: aload_0
    //   88: iconst_0
    //   89: putfield 362	android/support/v7/internal/widget/ProgressBarICS:mInDrawing	Z
    //   92: invokestatic 447	android/os/SystemClock:uptimeMillis	()J
    //   95: aload_0
    //   96: getfield 449	android/support/v7/internal/widget/ProgressBarICS:mLastDrawTime	J
    //   99: lsub
    //   100: ldc2_w 450
    //   103: lcmp
    //   104: iflt +17 -> 121
    //   107: aload_0
    //   108: invokestatic 447	android/os/SystemClock:uptimeMillis	()J
    //   111: putfield 449	android/support/v7/internal/widget/ProgressBarICS:mLastDrawTime	J
    //   114: aload_0
    //   115: ldc2_w 450
    //   118: invokevirtual 455	android/support/v7/internal/widget/ProgressBarICS:postInvalidateDelayed	(J)V
    //   121: aload_3
    //   122: aload_1
    //   123: invokevirtual 458	android/graphics/drawable/Drawable:draw	(Landroid/graphics/Canvas;)V
    //   126: aload_1
    //   127: invokevirtual 461	android/graphics/Canvas:restore	()V
    //   130: aload_0
    //   131: getfield 463	android/support/v7/internal/widget/ProgressBarICS:mShouldStartAnimationDrawable	Z
    //   134: ifeq +24 -> 158
    //   137: aload_3
    //   138: instanceof 465
    //   141: ifeq +17 -> 158
    //   144: aload_3
    //   145: checkcast 465	android/graphics/drawable/Animatable
    //   148: invokeinterface 468 1 0
    //   153: aload_0
    //   154: iconst_0
    //   155: putfield 463	android/support/v7/internal/widget/ProgressBarICS:mShouldStartAnimationDrawable	Z
    //   158: aload_0
    //   159: monitorexit
    //   160: return
    //   161: astore 9
    //   163: aload_0
    //   164: iconst_0
    //   165: putfield 362	android/support/v7/internal/widget/ProgressBarICS:mInDrawing	Z
    //   168: aload 9
    //   170: athrow
    //   171: astore_2
    //   172: aload_0
    //   173: monitorexit
    //   174: aload_2
    //   175: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	176	0	this	ProgressBarICS
    //   0	176	1	paramCanvas	android.graphics.Canvas
    //   171	4	2	localObject1	Object
    //   11	134	3	localDrawable	Drawable
    //   39	14	5	l	long
    //   69	11	8	f	float
    //   161	8	9	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   71	87	161	finally
    //   2	12	171	finally
    //   16	71	171	finally
    //   87	121	171	finally
    //   121	158	171	finally
    //   163	171	171	finally
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    try
    {
      Drawable localDrawable = this.mCurrentDrawable;
      int i = 0;
      int j = 0;
      if (localDrawable != null)
      {
        j = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, localDrawable.getIntrinsicWidth()));
        i = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, localDrawable.getIntrinsicHeight()));
      }
      updateDrawableState();
      int k = j + (getPaddingLeft() + getPaddingRight());
      int m = i + (getPaddingTop() + getPaddingBottom());
      setMeasuredDimension(resolveSize(k, paramInt1), resolveSize(m, paramInt2));
      return;
    }
    finally {}
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    setProgress(localSavedState.progress);
    setSecondaryProgress(localSavedState.secondaryProgress);
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.progress = this.mProgress;
    localSavedState.secondaryProgress = this.mSecondaryProgress;
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateDrawableBounds(paramInt1, paramInt2);
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (this.mIndeterminate)
    {
      if ((paramInt == 8) || (paramInt == 4)) {
        stopAnimation();
      }
    }
    else {
      return;
    }
    startAnimation();
  }
  
  public void postInvalidate()
  {
    if (!this.mNoInvalidate) {
      super.postInvalidate();
    }
  }
  
  /* Error */
  public void setIndeterminate(boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 119	android/support/v7/internal/widget/ProgressBarICS:mOnlyIndeterminate	Z
    //   6: ifeq +10 -> 16
    //   9: aload_0
    //   10: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   13: ifne +32 -> 45
    //   16: iload_1
    //   17: aload_0
    //   18: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   21: if_icmpeq +24 -> 45
    //   24: aload_0
    //   25: iload_1
    //   26: putfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   29: iload_1
    //   30: ifeq +18 -> 48
    //   33: aload_0
    //   34: aload_0
    //   35: getfield 312	android/support/v7/internal/widget/ProgressBarICS:mIndeterminateDrawable	Landroid/graphics/drawable/Drawable;
    //   38: putfield 180	android/support/v7/internal/widget/ProgressBarICS:mCurrentDrawable	Landroid/graphics/drawable/Drawable;
    //   41: aload_0
    //   42: invokevirtual 401	android/support/v7/internal/widget/ProgressBarICS:startAnimation	()V
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: aload_0
    //   49: aload_0
    //   50: getfield 324	android/support/v7/internal/widget/ProgressBarICS:mProgressDrawable	Landroid/graphics/drawable/Drawable;
    //   53: putfield 180	android/support/v7/internal/widget/ProgressBarICS:mCurrentDrawable	Landroid/graphics/drawable/Drawable;
    //   56: aload_0
    //   57: invokevirtual 405	android/support/v7/internal/widget/ProgressBarICS:stopAnimation	()V
    //   60: goto -15 -> 45
    //   63: astore_2
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_2
    //   67: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	68	0	this	ProgressBarICS
    //   0	68	1	paramBoolean	boolean
    //   63	4	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	16	63	finally
    //   16	29	63	finally
    //   33	45	63	finally
    //   48	60	63	finally
  }
  
  public void setIndeterminateDrawable(Drawable paramDrawable)
  {
    if (paramDrawable != null) {
      paramDrawable.setCallback(this);
    }
    this.mIndeterminateDrawable = paramDrawable;
    if (this.mIndeterminate)
    {
      this.mCurrentDrawable = paramDrawable;
      postInvalidate();
    }
  }
  
  public void setInterpolator(Context paramContext, int paramInt)
  {
    setInterpolator(AnimationUtils.loadInterpolator(paramContext, paramInt));
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    this.mInterpolator = paramInterpolator;
  }
  
  public void setMax(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = 0;
    }
    try
    {
      if (paramInt != this.mMax)
      {
        this.mMax = paramInt;
        postInvalidate();
        if (this.mProgress > paramInt) {
          this.mProgress = paramInt;
        }
        refreshProgress(16908301, this.mProgress, false);
      }
      return;
    }
    finally {}
  }
  
  public void setProgress(int paramInt)
  {
    try
    {
      setProgress(paramInt, false);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  void setProgress(int paramInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   6: istore 4
    //   8: iload 4
    //   10: ifeq +6 -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: iload_1
    //   17: ifge +5 -> 22
    //   20: iconst_0
    //   21: istore_1
    //   22: iload_1
    //   23: aload_0
    //   24: getfield 91	android/support/v7/internal/widget/ProgressBarICS:mMax	I
    //   27: if_icmple +8 -> 35
    //   30: aload_0
    //   31: getfield 91	android/support/v7/internal/widget/ProgressBarICS:mMax	I
    //   34: istore_1
    //   35: iload_1
    //   36: aload_0
    //   37: getfield 103	android/support/v7/internal/widget/ProgressBarICS:mProgress	I
    //   40: if_icmpeq -27 -> 13
    //   43: aload_0
    //   44: iload_1
    //   45: putfield 103	android/support/v7/internal/widget/ProgressBarICS:mProgress	I
    //   48: aload_0
    //   49: ldc 218
    //   51: aload_0
    //   52: getfield 103	android/support/v7/internal/widget/ProgressBarICS:mProgress	I
    //   55: iload_2
    //   56: invokespecial 530	android/support/v7/internal/widget/ProgressBarICS:refreshProgress	(IIZ)V
    //   59: goto -46 -> 13
    //   62: astore_3
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_3
    //   66: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	ProgressBarICS
    //   0	67	1	paramInt	int
    //   0	67	2	paramBoolean	boolean
    //   62	4	3	localObject	Object
    //   6	3	4	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	8	62	finally
    //   22	35	62	finally
    //   35	59	62	finally
  }
  
  public void setProgressDrawable(Drawable paramDrawable)
  {
    if ((this.mProgressDrawable != null) && (paramDrawable != this.mProgressDrawable)) {
      this.mProgressDrawable.setCallback(null);
    }
    for (int i = 1;; i = 0)
    {
      if (paramDrawable != null)
      {
        paramDrawable.setCallback(this);
        int j = paramDrawable.getMinimumHeight();
        if (this.mMaxHeight < j)
        {
          this.mMaxHeight = j;
          requestLayout();
        }
      }
      this.mProgressDrawable = paramDrawable;
      if (!this.mIndeterminate)
      {
        this.mCurrentDrawable = paramDrawable;
        postInvalidate();
      }
      if (i != 0)
      {
        updateDrawableBounds(getWidth(), getHeight());
        updateDrawableState();
        doRefreshProgress(16908301, this.mProgress, false, false);
        doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
      }
      return;
    }
  }
  
  /* Error */
  public void setSecondaryProgress(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 113	android/support/v7/internal/widget/ProgressBarICS:mIndeterminate	Z
    //   6: istore_3
    //   7: iload_3
    //   8: ifeq +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: iload_1
    //   15: ifge +5 -> 20
    //   18: iconst_0
    //   19: istore_1
    //   20: iload_1
    //   21: aload_0
    //   22: getfield 91	android/support/v7/internal/widget/ProgressBarICS:mMax	I
    //   25: if_icmple +8 -> 33
    //   28: aload_0
    //   29: getfield 91	android/support/v7/internal/widget/ProgressBarICS:mMax	I
    //   32: istore_1
    //   33: iload_1
    //   34: aload_0
    //   35: getfield 108	android/support/v7/internal/widget/ProgressBarICS:mSecondaryProgress	I
    //   38: if_icmpeq -27 -> 11
    //   41: aload_0
    //   42: iload_1
    //   43: putfield 108	android/support/v7/internal/widget/ProgressBarICS:mSecondaryProgress	I
    //   46: aload_0
    //   47: ldc 219
    //   49: aload_0
    //   50: getfield 108	android/support/v7/internal/widget/ProgressBarICS:mSecondaryProgress	I
    //   53: iconst_0
    //   54: invokespecial 530	android/support/v7/internal/widget/ProgressBarICS:refreshProgress	(IIZ)V
    //   57: goto -46 -> 11
    //   60: astore_2
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_2
    //   64: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	65	0	this	ProgressBarICS
    //   0	65	1	paramInt	int
    //   60	4	2	localObject	Object
    //   6	2	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	7	60	finally
    //   20	33	60	finally
    //   33	57	60	finally
  }
  
  public void setVisibility(int paramInt)
  {
    if (getVisibility() != paramInt)
    {
      super.setVisibility(paramInt);
      if (this.mIndeterminate)
      {
        if ((paramInt != 8) && (paramInt != 4)) {
          break label36;
        }
        stopAnimation();
      }
    }
    return;
    label36:
    startAnimation();
  }
  
  void startAnimation()
  {
    if (getVisibility() != 0) {
      return;
    }
    if ((this.mIndeterminateDrawable instanceof Animatable))
    {
      this.mShouldStartAnimationDrawable = true;
      this.mAnimation = null;
    }
    for (;;)
    {
      postInvalidate();
      return;
      if (this.mInterpolator == null) {
        this.mInterpolator = new LinearInterpolator();
      }
      this.mTransformation = new Transformation();
      this.mAnimation = new AlphaAnimation(0.0F, 1.0F);
      this.mAnimation.setRepeatMode(this.mBehavior);
      this.mAnimation.setRepeatCount(-1);
      this.mAnimation.setDuration(this.mDuration);
      this.mAnimation.setInterpolator(this.mInterpolator);
      this.mAnimation.setStartTime(-1L);
    }
  }
  
  void stopAnimation()
  {
    this.mAnimation = null;
    this.mTransformation = null;
    if ((this.mIndeterminateDrawable instanceof Animatable))
    {
      ((Animatable)this.mIndeterminateDrawable).stop();
      this.mShouldStartAnimationDrawable = false;
    }
    postInvalidate();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    return (paramDrawable == this.mProgressDrawable) || (paramDrawable == this.mIndeterminateDrawable) || (super.verifyDrawable(paramDrawable));
  }
  
  private class RefreshProgressRunnable
    implements Runnable
  {
    private boolean mFromUser;
    private int mId;
    private int mProgress;
    
    RefreshProgressRunnable(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      this.mId = paramInt1;
      this.mProgress = paramInt2;
      this.mFromUser = paramBoolean;
    }
    
    public void run()
    {
      ProgressBarICS.this.doRefreshProgress(this.mId, this.mProgress, this.mFromUser, true);
      ProgressBarICS.access$102(ProgressBarICS.this, this);
    }
    
    public void setup(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      this.mId = paramInt1;
      this.mProgress = paramInt2;
      this.mFromUser = paramBoolean;
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ProgressBarICS.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgressBarICS.SavedState(paramAnonymousParcel, null);
      }
      
      public ProgressBarICS.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ProgressBarICS.SavedState[paramAnonymousInt];
      }
    };
    int progress;
    int secondaryProgress;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      this.progress = paramParcel.readInt();
      this.secondaryProgress = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.progress);
      paramParcel.writeInt(this.secondaryProgress);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ProgressBarICS
 * JD-Core Version:    0.7.0.1
 */