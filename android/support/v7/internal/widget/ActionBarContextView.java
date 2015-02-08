package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.view.ActionMode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarContextView
  extends AbsActionBarView
{
  private static final String TAG = "ActionBarContextView";
  private View mClose;
  private View mCustomView;
  private Drawable mSplitBackground;
  private CharSequence mSubtitle;
  private int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private boolean mTitleOptional;
  private int mTitleStyleRes;
  private TextView mTitleView;
  
  public ActionBarContextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.actionModeStyle);
  }
  
  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionMode, paramInt, 0);
    setBackgroundDrawable(localTypedArray.getDrawable(3));
    this.mTitleStyleRes = localTypedArray.getResourceId(1, 0);
    this.mSubtitleStyleRes = localTypedArray.getResourceId(2, 0);
    this.mContentHeight = localTypedArray.getLayoutDimension(0, 0);
    this.mSplitBackground = localTypedArray.getDrawable(4);
    localTypedArray.recycle();
  }
  
  private void initTitle()
  {
    int i = 8;
    if (this.mTitleLayout == null)
    {
      LayoutInflater.from(getContext()).inflate(R.layout.abc_action_bar_title_item, this);
      this.mTitleLayout = ((LinearLayout)getChildAt(-1 + getChildCount()));
      this.mTitleView = ((TextView)this.mTitleLayout.findViewById(R.id.action_bar_title));
      this.mSubtitleView = ((TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle));
      if (this.mTitleStyleRes != 0) {
        this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes);
      }
      if (this.mSubtitleStyleRes != 0) {
        this.mSubtitleView.setTextAppearance(getContext(), this.mSubtitleStyleRes);
      }
    }
    this.mTitleView.setText(this.mTitle);
    this.mSubtitleView.setText(this.mSubtitle);
    int j;
    int k;
    label166:
    TextView localTextView;
    if (!TextUtils.isEmpty(this.mTitle))
    {
      j = 1;
      if (TextUtils.isEmpty(this.mSubtitle)) {
        break label232;
      }
      k = 1;
      localTextView = this.mSubtitleView;
      if (k == 0) {
        break label237;
      }
    }
    label232:
    label237:
    for (int m = 0;; m = i)
    {
      localTextView.setVisibility(m);
      LinearLayout localLinearLayout = this.mTitleLayout;
      if ((j != 0) || (k != 0)) {
        i = 0;
      }
      localLinearLayout.setVisibility(i);
      if (this.mTitleLayout.getParent() == null) {
        addView(this.mTitleLayout);
      }
      return;
      j = 0;
      break;
      k = 0;
      break label166;
    }
  }
  
  public void closeMode()
  {
    if (this.mClose == null) {
      killMode();
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.MarginLayoutParams(-1, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getSubtitle()
  {
    return this.mSubtitle;
  }
  
  public CharSequence getTitle()
  {
    return this.mTitle;
  }
  
  public boolean hideOverflowMenu()
  {
    if (this.mActionMenuPresenter != null) {
      return this.mActionMenuPresenter.hideOverflowMenu();
    }
    return false;
  }
  
  public void initForMode(final ActionMode paramActionMode)
  {
    if (this.mClose == null)
    {
      this.mClose = LayoutInflater.from(getContext()).inflate(R.layout.abc_action_mode_close_item, this, false);
      addView(this.mClose);
    }
    MenuBuilder localMenuBuilder;
    ViewGroup.LayoutParams localLayoutParams;
    for (;;)
    {
      this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramActionMode.finish();
        }
      });
      localMenuBuilder = (MenuBuilder)paramActionMode.getMenu();
      if (this.mActionMenuPresenter != null) {
        this.mActionMenuPresenter.dismissPopupMenus();
      }
      this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
      this.mActionMenuPresenter.setReserveOverflow(true);
      localLayoutParams = new ViewGroup.LayoutParams(-2, -1);
      if (this.mSplitActionBar) {
        break;
      }
      localMenuBuilder.addMenuPresenter(this.mActionMenuPresenter);
      this.mMenuView = ((ActionMenuView)this.mActionMenuPresenter.getMenuView(this));
      this.mMenuView.setBackgroundDrawable(null);
      addView(this.mMenuView, localLayoutParams);
      return;
      if (this.mClose.getParent() == null) {
        addView(this.mClose);
      }
    }
    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
    this.mActionMenuPresenter.setItemLimit(2147483647);
    localLayoutParams.width = -1;
    localLayoutParams.height = this.mContentHeight;
    localMenuBuilder.addMenuPresenter(this.mActionMenuPresenter);
    this.mMenuView = ((ActionMenuView)this.mActionMenuPresenter.getMenuView(this));
    this.mMenuView.setBackgroundDrawable(this.mSplitBackground);
    this.mSplitView.addView(this.mMenuView, localLayoutParams);
  }
  
  public boolean isOverflowMenuShowing()
  {
    if (this.mActionMenuPresenter != null) {
      return this.mActionMenuPresenter.isOverflowMenuShowing();
    }
    return false;
  }
  
  public boolean isTitleOptional()
  {
    return this.mTitleOptional;
  }
  
  public void killMode()
  {
    removeAllViews();
    if (this.mSplitView != null) {
      this.mSplitView.removeView(this.mMenuView);
    }
    this.mCustomView = null;
    this.mMenuView = null;
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mActionMenuPresenter != null)
    {
      this.mActionMenuPresenter.hideOverflowMenu();
      this.mActionMenuPresenter.hideSubMenus();
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    if ((this.mClose != null) && (this.mClose.getVisibility() != 8))
    {
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
      int n = i + localMarginLayoutParams.leftMargin;
      i = n + positionChild(this.mClose, n, j, k) + localMarginLayoutParams.rightMargin;
    }
    if ((this.mTitleLayout != null) && (this.mCustomView == null) && (this.mTitleLayout.getVisibility() != 8)) {
      i += positionChild(this.mTitleLayout, i, j, k);
    }
    if (this.mCustomView != null) {
      (i + positionChild(this.mCustomView, i, j, k));
    }
    int m = paramInt3 - paramInt1 - getPaddingRight();
    if (this.mMenuView != null) {
      (m - positionChildInverse(this.mMenuView, m, j, k));
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) != 1073741824) {
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"FILL_PARENT\" (or fill_parent)");
    }
    if (View.MeasureSpec.getMode(paramInt2) == 0) {
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
    }
    int i = View.MeasureSpec.getSize(paramInt1);
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i12;
    label297:
    int i13;
    label323:
    label330:
    ViewGroup.LayoutParams localLayoutParams;
    int i6;
    label361:
    int i7;
    label381:
    int i8;
    if (this.mContentHeight > 0)
    {
      j = this.mContentHeight;
      k = getPaddingTop() + getPaddingBottom();
      m = i - getPaddingLeft() - getPaddingRight();
      n = j - k;
      i1 = View.MeasureSpec.makeMeasureSpec(n, -2147483648);
      if (this.mClose != null)
      {
        int i14 = measureChildView(this.mClose, m, i1, 0);
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
        m = i14 - (localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin);
      }
      if ((this.mMenuView != null) && (this.mMenuView.getParent() == this)) {
        m = measureChildView(this.mMenuView, m, i1, 0);
      }
      if ((this.mTitleLayout != null) && (this.mCustomView == null))
      {
        if (!this.mTitleOptional) {
          break label516;
        }
        int i10 = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mTitleLayout.measure(i10, i1);
        int i11 = this.mTitleLayout.getMeasuredWidth();
        if (i11 > m) {
          break label503;
        }
        i12 = 1;
        if (i12 != 0) {
          m -= i11;
        }
        LinearLayout localLinearLayout = this.mTitleLayout;
        if (i12 == 0) {
          break label509;
        }
        i13 = 0;
        localLinearLayout.setVisibility(i13);
      }
      if (this.mCustomView != null)
      {
        localLayoutParams = this.mCustomView.getLayoutParams();
        if (localLayoutParams.width == -2) {
          break label534;
        }
        i6 = 1073741824;
        if (localLayoutParams.width < 0) {
          break label542;
        }
        i7 = Math.min(localLayoutParams.width, m);
        if (localLayoutParams.height == -2) {
          break label549;
        }
        i8 = 1073741824;
        label396:
        if (localLayoutParams.height < 0) {
          break label557;
        }
      }
    }
    int i2;
    label516:
    label534:
    label542:
    label549:
    label557:
    for (int i9 = Math.min(localLayoutParams.height, n);; i9 = n)
    {
      this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec(i7, i6), View.MeasureSpec.makeMeasureSpec(i9, i8));
      if (this.mContentHeight > 0) {
        break label572;
      }
      i2 = 0;
      int i3 = getChildCount();
      for (int i4 = 0; i4 < i3; i4++)
      {
        int i5 = k + getChildAt(i4).getMeasuredHeight();
        if (i5 > i2) {
          i2 = i5;
        }
      }
      j = View.MeasureSpec.getSize(paramInt2);
      break;
      label503:
      i12 = 0;
      break label297;
      label509:
      i13 = 8;
      break label323;
      m = measureChildView(this.mTitleLayout, m, i1, 0);
      break label330;
      i6 = -2147483648;
      break label361;
      i7 = m;
      break label381;
      i8 = -2147483648;
      break label396;
    }
    setMeasuredDimension(i, i2);
    return;
    label572:
    setMeasuredDimension(i, j);
  }
  
  public void setContentHeight(int paramInt)
  {
    this.mContentHeight = paramInt;
  }
  
  public void setCustomView(View paramView)
  {
    if (this.mCustomView != null) {
      removeView(this.mCustomView);
    }
    this.mCustomView = paramView;
    if (this.mTitleLayout != null)
    {
      removeView(this.mTitleLayout);
      this.mTitleLayout = null;
    }
    if (paramView != null) {
      addView(paramView);
    }
    requestLayout();
  }
  
  public void setSplitActionBar(boolean paramBoolean)
  {
    ViewGroup.LayoutParams localLayoutParams;
    if (this.mSplitActionBar != paramBoolean) {
      if (this.mActionMenuPresenter != null)
      {
        localLayoutParams = new ViewGroup.LayoutParams(-2, -1);
        if (paramBoolean) {
          break label94;
        }
        this.mMenuView = ((ActionMenuView)this.mActionMenuPresenter.getMenuView(this));
        this.mMenuView.setBackgroundDrawable(null);
        ViewGroup localViewGroup2 = (ViewGroup)this.mMenuView.getParent();
        if (localViewGroup2 != null) {
          localViewGroup2.removeView(this.mMenuView);
        }
        addView(this.mMenuView, localLayoutParams);
      }
    }
    for (;;)
    {
      super.setSplitActionBar(paramBoolean);
      return;
      label94:
      this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
      localLayoutParams.width = -1;
      localLayoutParams.height = this.mContentHeight;
      this.mMenuView = ((ActionMenuView)this.mActionMenuPresenter.getMenuView(this));
      this.mMenuView.setBackgroundDrawable(this.mSplitBackground);
      ViewGroup localViewGroup1 = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup1 != null) {
        localViewGroup1.removeView(this.mMenuView);
      }
      this.mSplitView.addView(this.mMenuView, localLayoutParams);
    }
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    this.mSubtitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    initTitle();
  }
  
  public void setTitleOptional(boolean paramBoolean)
  {
    if (paramBoolean != this.mTitleOptional) {
      requestLayout();
    }
    this.mTitleOptional = paramBoolean;
  }
  
  public boolean showOverflowMenu()
  {
    if (this.mActionMenuPresenter != null) {
      return this.mActionMenuPresenter.showOverflowMenu();
    }
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarContextView
 * JD-Core Version:    0.7.0.1
 */