package android.support.v7.internal.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.string;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuPresenter.Callback;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.view.CollapsibleActionView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.List;

public class ActionBarView
  extends AbsActionBarView
{
  private static final int DEFAULT_CUSTOM_GRAVITY = 19;
  public static final int DISPLAY_DEFAULT = 0;
  private static final int DISPLAY_RELAYOUT_MASK = 31;
  private static final String TAG = "ActionBarView";
  private ActionBar.OnNavigationListener mCallback;
  private Context mContext;
  private ActionBarContextView mContextView;
  private View mCustomNavView;
  private int mDisplayOptions = -1;
  View mExpandedActionView;
  private final View.OnClickListener mExpandedActionViewUpListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      MenuItemImpl localMenuItemImpl = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
      if (localMenuItemImpl != null) {
        localMenuItemImpl.collapseActionView();
      }
    }
  };
  private HomeView mExpandedHomeLayout;
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  private HomeView mHomeLayout;
  private Drawable mIcon;
  private boolean mIncludeTabs;
  private int mIndeterminateProgressStyle;
  private ProgressBarICS mIndeterminateProgressView;
  private boolean mIsCollapsable;
  private boolean mIsCollapsed;
  private int mItemPadding;
  private LinearLayout mListNavLayout;
  private Drawable mLogo;
  private ActionMenuItem mLogoNavItem;
  private final AdapterViewICS.OnItemSelectedListener mNavItemSelectedListener = new AdapterViewICS.OnItemSelectedListener()
  {
    public void onItemSelected(AdapterViewICS<?> paramAnonymousAdapterViewICS, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      if (ActionBarView.this.mCallback != null) {
        ActionBarView.this.mCallback.onNavigationItemSelected(paramAnonymousInt, paramAnonymousLong);
      }
    }
    
    public void onNothingSelected(AdapterViewICS<?> paramAnonymousAdapterViewICS) {}
  };
  private int mNavigationMode;
  private MenuBuilder mOptionsMenu;
  private int mProgressBarPadding;
  private int mProgressStyle;
  private ProgressBarICS mProgressView;
  private SpinnerICS mSpinner;
  private SpinnerAdapter mSpinnerAdapter;
  private CharSequence mSubtitle;
  private int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private ScrollingTabContainerView mTabScrollView;
  private Runnable mTabSelector;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private int mTitleStyleRes;
  private View mTitleUpView;
  private TextView mTitleView;
  private final View.OnClickListener mUpClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      ActionBarView.this.mWindowCallback.onMenuItemSelected(0, ActionBarView.this.mLogoNavItem);
    }
  };
  private boolean mUserTitle;
  Window.Callback mWindowCallback;
  
  public ActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    setBackgroundResource(0);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
    ApplicationInfo localApplicationInfo = paramContext.getApplicationInfo();
    PackageManager localPackageManager = paramContext.getPackageManager();
    this.mNavigationMode = localTypedArray.getInt(2, 0);
    this.mTitle = localTypedArray.getText(0);
    this.mSubtitle = localTypedArray.getText(4);
    this.mLogo = localTypedArray.getDrawable(8);
    if ((this.mLogo != null) || (Build.VERSION.SDK_INT < 9) || ((paramContext instanceof Activity))) {}
    try
    {
      this.mLogo = localPackageManager.getActivityLogo(((Activity)paramContext).getComponentName());
      if (this.mLogo == null) {
        this.mLogo = localApplicationInfo.loadLogo(localPackageManager);
      }
      this.mIcon = localTypedArray.getDrawable(7);
      if (this.mIcon == null) {
        if (!(paramContext instanceof Activity)) {}
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException2)
    {
      try
      {
        this.mIcon = localPackageManager.getActivityIcon(((Activity)paramContext).getComponentName());
        if (this.mIcon == null) {
          this.mIcon = localApplicationInfo.loadIcon(localPackageManager);
        }
        LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
        int i = localTypedArray.getResourceId(14, R.layout.abc_action_bar_home);
        this.mHomeLayout = ((HomeView)localLayoutInflater.inflate(i, this, false));
        this.mExpandedHomeLayout = ((HomeView)localLayoutInflater.inflate(i, this, false));
        this.mExpandedHomeLayout.setUp(true);
        this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        this.mExpandedHomeLayout.setContentDescription(getResources().getText(R.string.abc_action_bar_up_description));
        this.mTitleStyleRes = localTypedArray.getResourceId(5, 0);
        this.mSubtitleStyleRes = localTypedArray.getResourceId(6, 0);
        this.mProgressStyle = localTypedArray.getResourceId(15, 0);
        this.mIndeterminateProgressStyle = localTypedArray.getResourceId(16, 0);
        this.mProgressBarPadding = localTypedArray.getDimensionPixelOffset(17, 0);
        this.mItemPadding = localTypedArray.getDimensionPixelOffset(18, 0);
        setDisplayOptions(localTypedArray.getInt(3, 0));
        int j = localTypedArray.getResourceId(13, 0);
        if (j != 0)
        {
          this.mCustomNavView = localLayoutInflater.inflate(j, this, false);
          this.mNavigationMode = 0;
          setDisplayOptions(0x10 | this.mDisplayOptions);
        }
        this.mContentHeight = localTypedArray.getLayoutDimension(1, 0);
        localTypedArray.recycle();
        this.mLogoNavItem = new ActionMenuItem(paramContext, 0, 16908332, 0, 0, this.mTitle);
        this.mHomeLayout.setOnClickListener(this.mUpClickListener);
        this.mHomeLayout.setClickable(true);
        this.mHomeLayout.setFocusable(true);
        return;
        localNameNotFoundException2 = localNameNotFoundException2;
        Log.e("ActionBarView", "Activity component name not found!", localNameNotFoundException2);
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException1)
      {
        for (;;)
        {
          Log.e("ActionBarView", "Activity component name not found!", localNameNotFoundException1);
        }
      }
    }
  }
  
  private void configPresenters(MenuBuilder paramMenuBuilder)
  {
    if (paramMenuBuilder != null)
    {
      paramMenuBuilder.addMenuPresenter(this.mActionMenuPresenter);
      paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter);
    }
    for (;;)
    {
      this.mActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
      return;
      this.mActionMenuPresenter.initForMenu(this.mContext, null);
      this.mExpandedMenuPresenter.initForMenu(this.mContext, null);
    }
  }
  
  private void initTitle()
  {
    boolean bool1 = true;
    boolean bool2;
    boolean bool3;
    label200:
    int i;
    label217:
    LinearLayout localLinearLayout;
    if (this.mTitleLayout == null)
    {
      this.mTitleLayout = ((LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.abc_action_bar_title_item, this, false));
      this.mTitleView = ((TextView)this.mTitleLayout.findViewById(R.id.action_bar_title));
      this.mSubtitleView = ((TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle));
      this.mTitleUpView = this.mTitleLayout.findViewById(R.id.up);
      this.mTitleLayout.setOnClickListener(this.mUpClickListener);
      if (this.mTitleStyleRes != 0) {
        this.mTitleView.setTextAppearance(this.mContext, this.mTitleStyleRes);
      }
      if (this.mTitle != null) {
        this.mTitleView.setText(this.mTitle);
      }
      if (this.mSubtitleStyleRes != 0) {
        this.mSubtitleView.setTextAppearance(this.mContext, this.mSubtitleStyleRes);
      }
      if (this.mSubtitle != null)
      {
        this.mSubtitleView.setText(this.mSubtitle);
        this.mSubtitleView.setVisibility(0);
      }
      if ((0x4 & this.mDisplayOptions) == 0) {
        break label289;
      }
      bool2 = bool1;
      if ((0x2 & this.mDisplayOptions) == 0) {
        break label294;
      }
      bool3 = bool1;
      View localView = this.mTitleUpView;
      if (bool3) {
        break label305;
      }
      if (!bool2) {
        break label299;
      }
      i = 0;
      localView.setVisibility(i);
      localLinearLayout = this.mTitleLayout;
      if ((!bool2) || (bool3)) {
        break label312;
      }
    }
    for (;;)
    {
      localLinearLayout.setEnabled(bool1);
      addView(this.mTitleLayout);
      if ((this.mExpandedActionView != null) || ((TextUtils.isEmpty(this.mTitle)) && (TextUtils.isEmpty(this.mSubtitle)))) {
        this.mTitleLayout.setVisibility(8);
      }
      return;
      label289:
      bool2 = false;
      break;
      label294:
      bool3 = false;
      break label200;
      label299:
      i = 4;
      break label217;
      label305:
      i = 8;
      break label217;
      label312:
      bool1 = false;
    }
  }
  
  private void setTitleImpl(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    int i;
    LinearLayout localLinearLayout;
    int j;
    if (this.mTitleView != null)
    {
      this.mTitleView.setText(paramCharSequence);
      if ((this.mExpandedActionView != null) || ((0x8 & this.mDisplayOptions) == 0) || ((TextUtils.isEmpty(this.mTitle)) && (TextUtils.isEmpty(this.mSubtitle)))) {
        break label96;
      }
      i = 1;
      localLinearLayout = this.mTitleLayout;
      j = 0;
      if (i == 0) {
        break label101;
      }
    }
    for (;;)
    {
      localLinearLayout.setVisibility(j);
      if (this.mLogoNavItem != null) {
        this.mLogoNavItem.setTitle(paramCharSequence);
      }
      return;
      label96:
      i = 0;
      break;
      label101:
      j = 8;
    }
  }
  
  public void collapseActionView()
  {
    if (this.mExpandedMenuPresenter == null) {}
    for (MenuItemImpl localMenuItemImpl = null;; localMenuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem)
    {
      if (localMenuItemImpl != null) {
        localMenuItemImpl.collapseActionView();
      }
      return;
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ActionBar.LayoutParams(19);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ActionBar.LayoutParams(getContext(), paramAttributeSet);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams == null) {
      paramLayoutParams = generateDefaultLayoutParams();
    }
    return paramLayoutParams;
  }
  
  public View getCustomNavigationView()
  {
    return this.mCustomNavView;
  }
  
  public int getDisplayOptions()
  {
    return this.mDisplayOptions;
  }
  
  public SpinnerAdapter getDropdownAdapter()
  {
    return this.mSpinnerAdapter;
  }
  
  public int getDropdownSelectedPosition()
  {
    return this.mSpinner.getSelectedItemPosition();
  }
  
  public int getNavigationMode()
  {
    return this.mNavigationMode;
  }
  
  public CharSequence getSubtitle()
  {
    return this.mSubtitle;
  }
  
  public CharSequence getTitle()
  {
    return this.mTitle;
  }
  
  public boolean hasEmbeddedTabs()
  {
    return this.mIncludeTabs;
  }
  
  public boolean hasExpandedActionView()
  {
    return (this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null);
  }
  
  public void initIndeterminateProgress()
  {
    this.mIndeterminateProgressView = new ProgressBarICS(this.mContext, null, 0, this.mIndeterminateProgressStyle);
    this.mIndeterminateProgressView.setId(R.id.progress_circular);
    this.mIndeterminateProgressView.setVisibility(8);
    addView(this.mIndeterminateProgressView);
  }
  
  public void initProgress()
  {
    this.mProgressView = new ProgressBarICS(this.mContext, null, 0, this.mProgressStyle);
    this.mProgressView.setId(R.id.progress_horizontal);
    this.mProgressView.setMax(10000);
    this.mProgressView.setVisibility(8);
    addView(this.mProgressView);
  }
  
  public boolean isCollapsed()
  {
    return this.mIsCollapsed;
  }
  
  public boolean isSplitActionBar()
  {
    return this.mSplitActionBar;
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mTitleView = null;
    this.mSubtitleView = null;
    this.mTitleUpView = null;
    if ((this.mTitleLayout != null) && (this.mTitleLayout.getParent() == this)) {
      removeView(this.mTitleLayout);
    }
    this.mTitleLayout = null;
    if ((0x8 & this.mDisplayOptions) != 0) {
      initTitle();
    }
    if ((this.mTabScrollView != null) && (this.mIncludeTabs))
    {
      ViewGroup.LayoutParams localLayoutParams = this.mTabScrollView.getLayoutParams();
      if (localLayoutParams != null)
      {
        localLayoutParams.width = -2;
        localLayoutParams.height = -1;
      }
      this.mTabScrollView.setAllowCollapse(true);
    }
    if (this.mProgressView != null)
    {
      removeView(this.mProgressView);
      initProgress();
    }
    if (this.mIndeterminateProgressView != null)
    {
      removeView(this.mIndeterminateProgressView);
      initIndeterminateProgress();
    }
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks(this.mTabSelector);
    if (this.mActionMenuPresenter != null)
    {
      this.mActionMenuPresenter.hideOverflowMenu();
      this.mActionMenuPresenter.hideSubMenus();
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    addView(this.mHomeLayout);
    if ((this.mCustomNavView != null) && ((0x10 & this.mDisplayOptions) != 0))
    {
      ViewParent localViewParent = this.mCustomNavView.getParent();
      if (localViewParent != this)
      {
        if ((localViewParent instanceof ViewGroup)) {
          ((ViewGroup)localViewParent).removeView(this.mCustomNavView);
        }
        addView(this.mCustomNavView);
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2 - getPaddingTop() - getPaddingBottom();
    if (k <= 0) {
      return;
    }
    HomeView localHomeView;
    label47:
    int i15;
    label126:
    int m;
    label180:
    View localView1;
    label295:
    ActionBar.LayoutParams localLayoutParams1;
    label322:
    int i2;
    label334:
    int i3;
    int i4;
    int i5;
    int i6;
    int i14;
    label420:
    int i7;
    label460:
    int i9;
    if (this.mExpandedActionView != null)
    {
      localHomeView = this.mExpandedHomeLayout;
      if (localHomeView.getVisibility() != 8)
      {
        int i16 = localHomeView.getLeftOffset();
        i += i16 + positionChild(localHomeView, i + i16, j, k);
      }
      if (this.mExpandedActionView == null)
      {
        if ((this.mTitleLayout == null) || (this.mTitleLayout.getVisibility() == 8) || ((0x8 & this.mDisplayOptions) == 0)) {
          break label622;
        }
        i15 = 1;
        if (i15 != 0) {
          i += positionChild(this.mTitleLayout, i, j, k);
        }
      }
      switch (this.mNavigationMode)
      {
      case 0: 
      default: 
        m = paramInt3 - paramInt1 - getPaddingRight();
        if ((this.mMenuView != null) && (this.mMenuView.getParent() == this))
        {
          positionChildInverse(this.mMenuView, m, j, k);
          m -= this.mMenuView.getMeasuredWidth();
        }
        if ((this.mIndeterminateProgressView != null) && (this.mIndeterminateProgressView.getVisibility() != 8))
        {
          positionChildInverse(this.mIndeterminateProgressView, m, j, k);
          m -= this.mIndeterminateProgressView.getMeasuredWidth();
        }
        if (this.mExpandedActionView != null)
        {
          localView1 = this.mExpandedActionView;
          if (localView1 != null)
          {
            ViewGroup.LayoutParams localLayoutParams = localView1.getLayoutParams();
            if (!(localLayoutParams instanceof ActionBar.LayoutParams)) {
              break label764;
            }
            localLayoutParams1 = (ActionBar.LayoutParams)localLayoutParams;
            if (localLayoutParams1 == null) {
              break label770;
            }
            i2 = localLayoutParams1.gravity;
            i3 = localView1.getMeasuredWidth();
            i4 = 0;
            i5 = 0;
            if (localLayoutParams1 != null)
            {
              i += localLayoutParams1.leftMargin;
              m -= localLayoutParams1.rightMargin;
              i5 = localLayoutParams1.topMargin;
              i4 = localLayoutParams1.bottomMargin;
            }
            i6 = i2 & 0x7;
            if (i6 != 1) {
              break label793;
            }
            i14 = (getWidth() - i3) / 2;
            if (i14 >= i) {
              break label777;
            }
            i6 = 3;
            i7 = 0;
            switch (i6)
            {
            case 2: 
            case 4: 
            default: 
              int i8 = i2 & 0x70;
              if (i2 == -1) {
                i8 = 16;
              }
              i9 = 0;
              switch (i8)
              {
              }
              break;
            }
          }
        }
        break;
      }
    }
    for (;;)
    {
      int i10 = localView1.getMeasuredWidth();
      int i11 = i7 + i10;
      int i12 = i9 + localView1.getMeasuredHeight();
      localView1.layout(i7, i9, i11, i12);
      (i + i10);
      if (this.mProgressView == null) {
        break;
      }
      this.mProgressView.bringToFront();
      int i1 = this.mProgressView.getMeasuredHeight() / 2;
      this.mProgressView.layout(this.mProgressBarPadding, -i1, this.mProgressBarPadding + this.mProgressView.getMeasuredWidth(), i1);
      return;
      localHomeView = this.mHomeLayout;
      break label47;
      label622:
      i15 = 0;
      break label126;
      if (this.mListNavLayout == null) {
        break label180;
      }
      if (i15 != 0) {
        i += this.mItemPadding;
      }
      i += positionChild(this.mListNavLayout, i, j, k) + this.mItemPadding;
      break label180;
      if (this.mTabScrollView == null) {
        break label180;
      }
      if (i15 != 0) {
        i += this.mItemPadding;
      }
      i += positionChild(this.mTabScrollView, i, j, k) + this.mItemPadding;
      break label180;
      int n = 0x10 & this.mDisplayOptions;
      localView1 = null;
      if (n == 0) {
        break label295;
      }
      View localView2 = this.mCustomNavView;
      localView1 = null;
      if (localView2 == null) {
        break label295;
      }
      localView1 = this.mCustomNavView;
      break label295;
      label764:
      localLayoutParams1 = null;
      break label322;
      label770:
      i2 = 19;
      break label334;
      label777:
      if (i14 + i3 <= m) {
        break label420;
      }
      i6 = 5;
      break label420;
      label793:
      if (i2 != -1) {
        break label420;
      }
      i6 = 3;
      break label420;
      i7 = (getWidth() - i3) / 2;
      break label460;
      i7 = i;
      break label460;
      i7 = m - i3;
      break label460;
      int i13 = getPaddingTop();
      i9 = (getHeight() - getPaddingBottom() - i13 - localView1.getMeasuredHeight()) / 2;
      continue;
      i9 = i5 + getPaddingTop();
      continue;
      i9 = getHeight() - getPaddingBottom() - localView1.getMeasuredHeight() - i4;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    if (this.mIsCollapsable)
    {
      int i31 = 0;
      for (int i32 = 0; i32 < i; i32++)
      {
        View localView3 = getChildAt(i32);
        if ((localView3.getVisibility() != 8) && ((localView3 != this.mMenuView) || (this.mMenuView.getChildCount() != 0))) {
          i31++;
        }
      }
      if (i31 == 0)
      {
        setMeasuredDimension(0, 0);
        this.mIsCollapsed = true;
        return;
      }
    }
    this.mIsCollapsed = false;
    if (View.MeasureSpec.getMode(paramInt1) != 1073741824) {
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"MATCH_PARENT\" (or fill_parent)");
    }
    if (View.MeasureSpec.getMode(paramInt2) != -2147483648) {
      throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
    }
    int j = View.MeasureSpec.getSize(paramInt1);
    int k;
    int m;
    int i2;
    int i4;
    int i5;
    int i6;
    HomeView localHomeView;
    label284:
    ViewGroup.LayoutParams localLayoutParams2;
    int i28;
    label319:
    int i7;
    label504:
    View localView1;
    label536:
    label549:
    ViewGroup.LayoutParams localLayoutParams1;
    ActionBar.LayoutParams localLayoutParams;
    label580:
    int i12;
    int i14;
    int i15;
    int i16;
    label675:
    int i17;
    label695:
    int i18;
    if (this.mContentHeight > 0)
    {
      k = this.mContentHeight;
      m = getPaddingTop() + getPaddingBottom();
      int n = getPaddingLeft();
      int i1 = getPaddingRight();
      i2 = k - m;
      int i3 = View.MeasureSpec.makeMeasureSpec(i2, -2147483648);
      i4 = j - n - i1;
      i5 = i4 / 2;
      i6 = i5;
      if (this.mExpandedActionView == null) {
        break label887;
      }
      localHomeView = this.mExpandedHomeLayout;
      if (localHomeView.getVisibility() != 8)
      {
        localLayoutParams2 = localHomeView.getLayoutParams();
        if (localLayoutParams2.width >= 0) {
          break label896;
        }
        i28 = View.MeasureSpec.makeMeasureSpec(i4, -2147483648);
        int i29 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
        localHomeView.measure(i28, i29);
        int i30 = localHomeView.getMeasuredWidth() + localHomeView.getLeftOffset();
        i4 = Math.max(0, i4 - i30);
        i5 = Math.max(0, i4 - i30);
      }
      if ((this.mMenuView != null) && (this.mMenuView.getParent() == this))
      {
        i4 = measureChildView(this.mMenuView, i4, i3, 0);
        i6 = Math.max(0, i6 - this.mMenuView.getMeasuredWidth());
      }
      if ((this.mIndeterminateProgressView != null) && (this.mIndeterminateProgressView.getVisibility() != 8))
      {
        i4 = measureChildView(this.mIndeterminateProgressView, i4, i3, 0);
        i6 = Math.max(0, i6 - this.mIndeterminateProgressView.getMeasuredWidth());
      }
      if ((this.mTitleLayout == null) || (this.mTitleLayout.getVisibility() == 8) || ((0x8 & this.mDisplayOptions) == 0)) {
        break label912;
      }
      i7 = 1;
      if (this.mExpandedActionView == null) {}
      switch (this.mNavigationMode)
      {
      default: 
        if (this.mExpandedActionView != null)
        {
          localView1 = this.mExpandedActionView;
          if (localView1 != null)
          {
            localLayoutParams1 = generateLayoutParams(localView1.getLayoutParams());
            if (!(localLayoutParams1 instanceof ActionBar.LayoutParams)) {
              break label1174;
            }
            localLayoutParams = (ActionBar.LayoutParams)localLayoutParams1;
            i12 = 0;
            int i13 = 0;
            if (localLayoutParams != null)
            {
              i12 = localLayoutParams.leftMargin + localLayoutParams.rightMargin;
              i13 = localLayoutParams.topMargin + localLayoutParams.bottomMargin;
            }
            if (this.mContentHeight > 0) {
              break label1180;
            }
            i14 = -2147483648;
            if (localLayoutParams1.height >= 0) {
              i2 = Math.min(localLayoutParams1.height, i2);
            }
            i15 = Math.max(0, i2 - i13);
            if (localLayoutParams1.width == -2) {
              break label1206;
            }
            i16 = 1073741824;
            if (localLayoutParams1.width < 0) {
              break label1214;
            }
            i17 = Math.min(localLayoutParams1.width, i4);
            i18 = Math.max(0, i17 - i12);
            if (localLayoutParams == null) {
              break label1221;
            }
          }
        }
        break;
      }
    }
    int i9;
    label896:
    label912:
    label1174:
    label1180:
    label1206:
    label1214:
    label1221:
    for (int i19 = localLayoutParams.gravity;; i19 = 19)
    {
      if (((i19 & 0x7) == 1) && (localLayoutParams1.width == -1)) {
        i18 = 2 * Math.min(i5, i6);
      }
      localView1.measure(View.MeasureSpec.makeMeasureSpec(i18, i16), View.MeasureSpec.makeMeasureSpec(i15, i14));
      i4 -= i12 + localView1.getMeasuredWidth();
      if ((this.mExpandedActionView == null) && (i7 != 0))
      {
        measureChildView(this.mTitleLayout, i4, View.MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824), 0);
        Math.max(0, i5 - this.mTitleLayout.getMeasuredWidth());
      }
      if (this.mContentHeight > 0) {
        break label1306;
      }
      i9 = 0;
      for (int i10 = 0; i10 < i; i10++)
      {
        int i11 = m + getChildAt(i10).getMeasuredHeight();
        if (i11 > i9) {
          i9 = i11;
        }
      }
      k = View.MeasureSpec.getSize(paramInt2);
      break;
      label887:
      localHomeView = this.mHomeLayout;
      break label284;
      i28 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.width, 1073741824);
      break label319;
      i7 = 0;
      break label504;
      if (this.mListNavLayout == null) {
        break label536;
      }
      if (i7 != 0) {}
      for (int i24 = 2 * this.mItemPadding;; i24 = this.mItemPadding)
      {
        int i25 = Math.max(0, i4 - i24);
        int i26 = Math.max(0, i5 - i24);
        this.mListNavLayout.measure(View.MeasureSpec.makeMeasureSpec(i25, -2147483648), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
        int i27 = this.mListNavLayout.getMeasuredWidth();
        i4 = Math.max(0, i25 - i27);
        i5 = Math.max(0, i26 - i27);
        break;
      }
      if (this.mTabScrollView == null) {
        break label536;
      }
      if (i7 != 0) {}
      for (int i20 = 2 * this.mItemPadding;; i20 = this.mItemPadding)
      {
        int i21 = Math.max(0, i4 - i20);
        int i22 = Math.max(0, i5 - i20);
        this.mTabScrollView.measure(View.MeasureSpec.makeMeasureSpec(i21, -2147483648), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
        int i23 = this.mTabScrollView.getMeasuredWidth();
        i4 = Math.max(0, i21 - i23);
        i5 = Math.max(0, i22 - i23);
        break;
      }
      int i8 = 0x10 & this.mDisplayOptions;
      localView1 = null;
      if (i8 == 0) {
        break label549;
      }
      View localView2 = this.mCustomNavView;
      localView1 = null;
      if (localView2 == null) {
        break label549;
      }
      localView1 = this.mCustomNavView;
      break label549;
      localLayoutParams = null;
      break label580;
      if (localLayoutParams1.height != -2) {}
      for (i14 = 1073741824;; i14 = -2147483648) {
        break;
      }
      i16 = -2147483648;
      break label675;
      i17 = i4;
      break label695;
    }
    setMeasuredDimension(j, i9);
    for (;;)
    {
      if (this.mContextView != null) {
        this.mContextView.setContentHeight(getMeasuredHeight());
      }
      if ((this.mProgressView == null) || (this.mProgressView.getVisibility() == 8)) {
        break;
      }
      this.mProgressView.measure(View.MeasureSpec.makeMeasureSpec(j - 2 * this.mProgressBarPadding, 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), -2147483648));
      return;
      label1306:
      setMeasuredDimension(j, k);
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if ((localSavedState.expandedMenuItemId != 0) && (this.mExpandedMenuPresenter != null) && (this.mOptionsMenu != null))
    {
      SupportMenuItem localSupportMenuItem = (SupportMenuItem)this.mOptionsMenu.findItem(localSavedState.expandedMenuItemId);
      if (localSupportMenuItem != null) {
        localSupportMenuItem.expandActionView();
      }
    }
    if (localSavedState.isOverflowOpen) {
      postShowOverflowMenu();
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if ((this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      localSavedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
    }
    localSavedState.isOverflowOpen = isOverflowMenuShowing();
    return localSavedState;
  }
  
  public void setCallback(ActionBar.OnNavigationListener paramOnNavigationListener)
  {
    this.mCallback = paramOnNavigationListener;
  }
  
  public void setCollapsable(boolean paramBoolean)
  {
    this.mIsCollapsable = paramBoolean;
  }
  
  public void setContextView(ActionBarContextView paramActionBarContextView)
  {
    this.mContextView = paramActionBarContextView;
  }
  
  public void setCustomNavigationView(View paramView)
  {
    if ((0x10 & this.mDisplayOptions) != 0) {}
    for (int i = 1;; i = 0)
    {
      if ((this.mCustomNavView != null) && (i != 0)) {
        removeView(this.mCustomNavView);
      }
      this.mCustomNavView = paramView;
      if ((this.mCustomNavView != null) && (i != 0)) {
        addView(this.mCustomNavView);
      }
      return;
    }
  }
  
  public void setDisplayOptions(int paramInt)
  {
    int i = 8;
    int j = -1;
    boolean bool1 = true;
    boolean bool2;
    label38:
    int k;
    label53:
    boolean bool5;
    label78:
    boolean bool4;
    label121:
    Drawable localDrawable;
    label138:
    label163:
    boolean bool3;
    if (this.mDisplayOptions == j)
    {
      this.mDisplayOptions = paramInt;
      if ((j & 0x1F) == 0) {
        break label371;
      }
      if ((paramInt & 0x2) == 0) {
        break label299;
      }
      bool2 = bool1;
      if ((!bool2) || (this.mExpandedActionView != null)) {
        break label305;
      }
      k = 0;
      this.mHomeLayout.setVisibility(k);
      if ((j & 0x4) != 0)
      {
        if ((paramInt & 0x4) == 0) {
          break label311;
        }
        bool5 = bool1;
        this.mHomeLayout.setUp(bool5);
        if (bool5) {
          setHomeButtonEnabled(bool1);
        }
      }
      if ((j & 0x1) != 0)
      {
        if ((this.mLogo == null) || ((paramInt & 0x1) == 0)) {
          break label317;
        }
        bool4 = bool1;
        HomeView localHomeView = this.mHomeLayout;
        if (!bool4) {
          break label323;
        }
        localDrawable = this.mLogo;
        localHomeView.setIcon(localDrawable);
      }
      if ((j & 0x8) != 0)
      {
        if ((paramInt & 0x8) == 0) {
          break label332;
        }
        initTitle();
      }
      if ((this.mTitleLayout != null) && ((j & 0x6) != 0))
      {
        if ((0x4 & this.mDisplayOptions) == 0) {
          break label343;
        }
        bool3 = bool1;
        label190:
        View localView = this.mTitleUpView;
        if (!bool2)
        {
          if (!bool3) {
            break label349;
          }
          i = 0;
        }
        label208:
        localView.setVisibility(i);
        LinearLayout localLinearLayout = this.mTitleLayout;
        if ((bool2) || (!bool3)) {
          break label354;
        }
        label230:
        localLinearLayout.setEnabled(bool1);
      }
      if (((j & 0x10) != 0) && (this.mCustomNavView != null))
      {
        if ((paramInt & 0x10) == 0) {
          break label360;
        }
        addView(this.mCustomNavView);
      }
      label266:
      requestLayout();
    }
    for (;;)
    {
      if (this.mHomeLayout.isEnabled()) {
        break label378;
      }
      this.mHomeLayout.setContentDescription(null);
      return;
      j = paramInt ^ this.mDisplayOptions;
      break;
      label299:
      bool2 = false;
      break label38;
      label305:
      k = i;
      break label53;
      label311:
      bool5 = false;
      break label78;
      label317:
      bool4 = false;
      break label121;
      label323:
      localDrawable = this.mIcon;
      break label138;
      label332:
      removeView(this.mTitleLayout);
      break label163;
      label343:
      bool3 = false;
      break label190;
      label349:
      i = 4;
      break label208;
      label354:
      bool1 = false;
      break label230;
      label360:
      removeView(this.mCustomNavView);
      break label266;
      label371:
      invalidate();
    }
    label378:
    if ((paramInt & 0x4) != 0)
    {
      this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_up_description));
      return;
    }
    this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_home_description));
  }
  
  public void setDropdownAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    this.mSpinnerAdapter = paramSpinnerAdapter;
    if (this.mSpinner != null) {
      this.mSpinner.setAdapter(paramSpinnerAdapter);
    }
  }
  
  public void setDropdownSelectedPosition(int paramInt)
  {
    this.mSpinner.setSelection(paramInt);
  }
  
  public void setEmbeddedTabView(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if (this.mTabScrollView != null) {
      removeView(this.mTabScrollView);
    }
    this.mTabScrollView = paramScrollingTabContainerView;
    if (paramScrollingTabContainerView != null) {}
    for (boolean bool = true;; bool = false)
    {
      this.mIncludeTabs = bool;
      if ((this.mIncludeTabs) && (this.mNavigationMode == 2))
      {
        addView(this.mTabScrollView);
        ViewGroup.LayoutParams localLayoutParams = this.mTabScrollView.getLayoutParams();
        localLayoutParams.width = -2;
        localLayoutParams.height = -1;
        paramScrollingTabContainerView.setAllowCollapse(true);
      }
      return;
    }
  }
  
  public void setHomeAsUpIndicator(int paramInt)
  {
    this.mHomeLayout.setUpIndicator(paramInt);
  }
  
  public void setHomeAsUpIndicator(Drawable paramDrawable)
  {
    this.mHomeLayout.setUpIndicator(paramDrawable);
  }
  
  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    this.mHomeLayout.setEnabled(paramBoolean);
    this.mHomeLayout.setFocusable(paramBoolean);
    if (!paramBoolean)
    {
      this.mHomeLayout.setContentDescription(null);
      return;
    }
    if ((0x4 & this.mDisplayOptions) != 0)
    {
      this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_up_description));
      return;
    }
    this.mHomeLayout.setContentDescription(this.mContext.getResources().getText(R.string.abc_action_bar_home_description));
  }
  
  public void setIcon(int paramInt)
  {
    setIcon(this.mContext.getResources().getDrawable(paramInt));
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
    if ((paramDrawable != null) && (((0x1 & this.mDisplayOptions) == 0) || (this.mLogo == null))) {
      this.mHomeLayout.setIcon(paramDrawable);
    }
    if (this.mExpandedActionView != null) {
      this.mExpandedHomeLayout.setIcon(this.mIcon.getConstantState().newDrawable(getResources()));
    }
  }
  
  public void setLogo(int paramInt)
  {
    setLogo(this.mContext.getResources().getDrawable(paramInt));
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    this.mLogo = paramDrawable;
    if ((paramDrawable != null) && ((0x1 & this.mDisplayOptions) != 0)) {
      this.mHomeLayout.setIcon(paramDrawable);
    }
  }
  
  public void setMenu(SupportMenu paramSupportMenu, MenuPresenter.Callback paramCallback)
  {
    if (paramSupportMenu == this.mOptionsMenu) {
      return;
    }
    if (this.mOptionsMenu != null)
    {
      this.mOptionsMenu.removeMenuPresenter(this.mActionMenuPresenter);
      this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
    }
    MenuBuilder localMenuBuilder = (MenuBuilder)paramSupportMenu;
    this.mOptionsMenu = localMenuBuilder;
    if (this.mMenuView != null)
    {
      ViewGroup localViewGroup3 = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup3 != null) {
        localViewGroup3.removeView(this.mMenuView);
      }
    }
    if (this.mActionMenuPresenter == null)
    {
      this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
      this.mActionMenuPresenter.setCallback(paramCallback);
      this.mActionMenuPresenter.setId(R.id.action_menu_presenter);
      this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(null);
    }
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-2, -1);
    ActionMenuView localActionMenuView;
    if (!this.mSplitActionBar)
    {
      this.mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
      configPresenters(localMenuBuilder);
      localActionMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      localActionMenuView.initialize(localMenuBuilder);
      ViewGroup localViewGroup2 = (ViewGroup)localActionMenuView.getParent();
      if ((localViewGroup2 != null) && (localViewGroup2 != this)) {
        localViewGroup2.removeView(localActionMenuView);
      }
      addView(localActionMenuView, localLayoutParams);
    }
    for (;;)
    {
      this.mMenuView = localActionMenuView;
      return;
      this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
      this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
      localLayoutParams.width = -1;
      configPresenters(localMenuBuilder);
      localActionMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      if (this.mSplitView != null)
      {
        ViewGroup localViewGroup1 = (ViewGroup)localActionMenuView.getParent();
        if ((localViewGroup1 != null) && (localViewGroup1 != this.mSplitView)) {
          localViewGroup1.removeView(localActionMenuView);
        }
        localActionMenuView.setVisibility(getAnimatedVisibility());
        this.mSplitView.addView(localActionMenuView, localLayoutParams);
      }
      else
      {
        localActionMenuView.setLayoutParams(localLayoutParams);
      }
    }
  }
  
  public void setNavigationMode(int paramInt)
  {
    int i = this.mNavigationMode;
    if (paramInt != i) {
      switch (i)
      {
      default: 
        switch (paramInt)
        {
        }
        break;
      }
    }
    for (;;)
    {
      this.mNavigationMode = paramInt;
      requestLayout();
      return;
      if (this.mListNavLayout == null) {
        break;
      }
      removeView(this.mListNavLayout);
      break;
      if ((this.mTabScrollView == null) || (!this.mIncludeTabs)) {
        break;
      }
      removeView(this.mTabScrollView);
      break;
      if (this.mSpinner == null)
      {
        this.mSpinner = new SpinnerICS(this.mContext, null, R.attr.actionDropDownStyle);
        this.mListNavLayout = ((LinearLayout)LayoutInflater.from(this.mContext).inflate(R.layout.abc_action_bar_view_list_nav_layout, null));
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -1);
        localLayoutParams.gravity = 17;
        this.mListNavLayout.addView(this.mSpinner, localLayoutParams);
      }
      if (this.mSpinner.getAdapter() != this.mSpinnerAdapter) {
        this.mSpinner.setAdapter(this.mSpinnerAdapter);
      }
      this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
      addView(this.mListNavLayout);
      continue;
      if ((this.mTabScrollView != null) && (this.mIncludeTabs)) {
        addView(this.mTabScrollView);
      }
    }
  }
  
  public void setSplitActionBar(boolean paramBoolean)
  {
    int i;
    if (this.mSplitActionBar != paramBoolean)
    {
      if (this.mMenuView != null)
      {
        ViewGroup localViewGroup = (ViewGroup)this.mMenuView.getParent();
        if (localViewGroup != null) {
          localViewGroup.removeView(this.mMenuView);
        }
        if (!paramBoolean) {
          break label138;
        }
        if (this.mSplitView != null) {
          this.mSplitView.addView(this.mMenuView);
        }
        this.mMenuView.getLayoutParams().width = -1;
        this.mMenuView.requestLayout();
      }
      if (this.mSplitView != null)
      {
        ActionBarContainer localActionBarContainer = this.mSplitView;
        if (!paramBoolean) {
          break label161;
        }
        i = 0;
        label99:
        localActionBarContainer.setVisibility(i);
      }
      if (this.mActionMenuPresenter != null)
      {
        if (paramBoolean) {
          break label167;
        }
        this.mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.abc_action_bar_expanded_action_views_exclusive));
      }
    }
    for (;;)
    {
      super.setSplitActionBar(paramBoolean);
      return;
      label138:
      addView(this.mMenuView);
      this.mMenuView.getLayoutParams().width = -2;
      break;
      label161:
      i = 8;
      break label99;
      label167:
      this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
      this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
    }
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    this.mSubtitle = paramCharSequence;
    int i;
    int j;
    label76:
    LinearLayout localLinearLayout;
    int k;
    if (this.mSubtitleView != null)
    {
      this.mSubtitleView.setText(paramCharSequence);
      TextView localTextView = this.mSubtitleView;
      if (paramCharSequence == null) {
        break label98;
      }
      i = 0;
      localTextView.setVisibility(i);
      if ((this.mExpandedActionView != null) || ((0x8 & this.mDisplayOptions) == 0) || ((TextUtils.isEmpty(this.mTitle)) && (TextUtils.isEmpty(this.mSubtitle)))) {
        break label104;
      }
      j = 1;
      localLinearLayout = this.mTitleLayout;
      k = 0;
      if (j == 0) {
        break label110;
      }
    }
    for (;;)
    {
      localLinearLayout.setVisibility(k);
      return;
      label98:
      i = 8;
      break;
      label104:
      j = 0;
      break label76;
      label110:
      k = 8;
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mUserTitle = true;
    setTitleImpl(paramCharSequence);
  }
  
  public void setWindowCallback(Window.Callback paramCallback)
  {
    this.mWindowCallback = paramCallback;
  }
  
  public void setWindowTitle(CharSequence paramCharSequence)
  {
    if (!this.mUserTitle) {
      setTitleImpl(paramCharSequence);
    }
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  private class ExpandedActionViewMenuPresenter
    implements MenuPresenter
  {
    MenuItemImpl mCurrentExpandedItem;
    MenuBuilder mMenu;
    
    private ExpandedActionViewMenuPresenter() {}
    
    public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      if ((ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
      }
      ActionBarView.this.removeView(ActionBarView.this.mExpandedActionView);
      ActionBarView.this.removeView(ActionBarView.this.mExpandedHomeLayout);
      ActionBarView.this.mExpandedActionView = null;
      if ((0x2 & ActionBarView.this.mDisplayOptions) != 0) {
        ActionBarView.this.mHomeLayout.setVisibility(0);
      }
      if ((0x8 & ActionBarView.this.mDisplayOptions) != 0)
      {
        if (ActionBarView.this.mTitleLayout != null) {
          break label245;
        }
        ActionBarView.this.initTitle();
      }
      for (;;)
      {
        if ((ActionBarView.this.mTabScrollView != null) && (ActionBarView.this.mNavigationMode == 2)) {
          ActionBarView.this.mTabScrollView.setVisibility(0);
        }
        if ((ActionBarView.this.mSpinner != null) && (ActionBarView.this.mNavigationMode == 1)) {
          ActionBarView.this.mSpinner.setVisibility(0);
        }
        if ((ActionBarView.this.mCustomNavView != null) && ((0x10 & ActionBarView.this.mDisplayOptions) != 0)) {
          ActionBarView.this.mCustomNavView.setVisibility(0);
        }
        ActionBarView.this.mExpandedHomeLayout.setIcon(null);
        this.mCurrentExpandedItem = null;
        ActionBarView.this.requestLayout();
        paramMenuItemImpl.setActionViewExpanded(false);
        return true;
        label245:
        ActionBarView.this.mTitleLayout.setVisibility(0);
      }
    }
    
    public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      ActionBarView.this.mExpandedActionView = paramMenuItemImpl.getActionView();
      ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.mIcon.getConstantState().newDrawable(ActionBarView.this.getResources()));
      this.mCurrentExpandedItem = paramMenuItemImpl;
      if (ActionBarView.this.mExpandedActionView.getParent() != ActionBarView.this) {
        ActionBarView.this.addView(ActionBarView.this.mExpandedActionView);
      }
      if (ActionBarView.this.mExpandedHomeLayout.getParent() != ActionBarView.this) {
        ActionBarView.this.addView(ActionBarView.this.mExpandedHomeLayout);
      }
      ActionBarView.this.mHomeLayout.setVisibility(8);
      if (ActionBarView.this.mTitleLayout != null) {
        ActionBarView.this.mTitleLayout.setVisibility(8);
      }
      if (ActionBarView.this.mTabScrollView != null) {
        ActionBarView.this.mTabScrollView.setVisibility(8);
      }
      if (ActionBarView.this.mSpinner != null) {
        ActionBarView.this.mSpinner.setVisibility(8);
      }
      if (ActionBarView.this.mCustomNavView != null) {
        ActionBarView.this.mCustomNavView.setVisibility(8);
      }
      ActionBarView.this.requestLayout();
      paramMenuItemImpl.setActionViewExpanded(true);
      if ((ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewExpanded();
      }
      return true;
    }
    
    public boolean flagActionItems()
    {
      return false;
    }
    
    public int getId()
    {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup paramViewGroup)
    {
      return null;
    }
    
    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
    {
      if ((this.mMenu != null) && (this.mCurrentExpandedItem != null)) {
        this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
      }
      this.mMenu = paramMenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public void onRestoreInstanceState(Parcelable paramParcelable) {}
    
    public Parcelable onSaveInstanceState()
    {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback paramCallback) {}
    
    public void updateMenuView(boolean paramBoolean)
    {
      int i;
      int j;
      if (this.mCurrentExpandedItem != null)
      {
        MenuBuilder localMenuBuilder = this.mMenu;
        i = 0;
        if (localMenuBuilder != null) {
          j = this.mMenu.size();
        }
      }
      for (int k = 0;; k++)
      {
        i = 0;
        if (k < j)
        {
          if ((SupportMenuItem)this.mMenu.getItem(k) == this.mCurrentExpandedItem) {
            i = 1;
          }
        }
        else
        {
          if (i == 0) {
            collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
          }
          return;
        }
      }
    }
  }
  
  private static class HomeView
    extends FrameLayout
  {
    private Drawable mDefaultUpIndicator;
    private ImageView mIconView;
    private int mUpIndicatorRes;
    private ImageView mUpView;
    private int mUpWidth;
    
    public HomeView(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public HomeView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      CharSequence localCharSequence = getContentDescription();
      if (!TextUtils.isEmpty(localCharSequence)) {
        paramAccessibilityEvent.getText().add(localCharSequence);
      }
      return true;
    }
    
    public int getLeftOffset()
    {
      if (this.mUpView.getVisibility() == 8) {
        return this.mUpWidth;
      }
      return 0;
    }
    
    protected void onConfigurationChanged(Configuration paramConfiguration)
    {
      super.onConfigurationChanged(paramConfiguration);
      if (this.mUpIndicatorRes != 0) {
        setUpIndicator(this.mUpIndicatorRes);
      }
    }
    
    protected void onFinishInflate()
    {
      this.mUpView = ((ImageView)findViewById(R.id.up));
      this.mIconView = ((ImageView)findViewById(R.id.home));
      this.mDefaultUpIndicator = this.mUpView.getDrawable();
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = (paramInt4 - paramInt2) / 2;
      int j = paramInt3 - paramInt1;
      int k = this.mUpView.getVisibility();
      int m = 0;
      if (k != 8)
      {
        FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
        int i5 = this.mUpView.getMeasuredHeight();
        int i6 = this.mUpView.getMeasuredWidth();
        int i7 = i - i5 / 2;
        this.mUpView.layout(0, i7, i6, i7 + i5);
        m = i6 + localLayoutParams2.leftMargin + localLayoutParams2.rightMargin;
        (j - m);
        paramInt1 += m;
      }
      FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
      int n = this.mIconView.getMeasuredHeight();
      int i1 = this.mIconView.getMeasuredWidth();
      int i2 = (paramInt3 - paramInt1) / 2;
      int i3 = m + Math.max(localLayoutParams1.leftMargin, i2 - i1 / 2);
      int i4 = Math.max(localLayoutParams1.topMargin, i - n / 2);
      this.mIconView.layout(i3, i4, i3 + i1, i4 + n);
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      measureChildWithMargins(this.mUpView, paramInt1, 0, paramInt2, 0);
      FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
      this.mUpWidth = (localLayoutParams1.leftMargin + this.mUpView.getMeasuredWidth() + localLayoutParams1.rightMargin);
      int i;
      int k;
      int m;
      int i2;
      int i3;
      if (this.mUpView.getVisibility() == 8)
      {
        i = 0;
        int j = localLayoutParams1.topMargin + this.mUpView.getMeasuredHeight() + localLayoutParams1.bottomMargin;
        measureChildWithMargins(this.mIconView, paramInt1, i, paramInt2, 0);
        FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
        k = i + (localLayoutParams2.leftMargin + this.mIconView.getMeasuredWidth() + localLayoutParams2.rightMargin);
        m = Math.max(j, localLayoutParams2.topMargin + this.mIconView.getMeasuredHeight() + localLayoutParams2.bottomMargin);
        int n = View.MeasureSpec.getMode(paramInt1);
        int i1 = View.MeasureSpec.getMode(paramInt2);
        i2 = View.MeasureSpec.getSize(paramInt1);
        i3 = View.MeasureSpec.getSize(paramInt2);
        switch (n)
        {
        default: 
          label204:
          switch (i1)
          {
          }
          break;
        }
      }
      for (;;)
      {
        setMeasuredDimension(k, m);
        return;
        i = this.mUpWidth;
        break;
        k = Math.min(k, i2);
        break label204;
        k = i2;
        break label204;
        m = Math.min(m, i3);
        continue;
        m = i3;
      }
    }
    
    public void setIcon(Drawable paramDrawable)
    {
      this.mIconView.setImageDrawable(paramDrawable);
    }
    
    public void setUp(boolean paramBoolean)
    {
      ImageView localImageView = this.mUpView;
      if (paramBoolean) {}
      for (int i = 0;; i = 8)
      {
        localImageView.setVisibility(i);
        return;
      }
    }
    
    public void setUpIndicator(int paramInt)
    {
      this.mUpIndicatorRes = paramInt;
      ImageView localImageView = this.mUpView;
      if (paramInt != 0) {}
      for (Drawable localDrawable = getResources().getDrawable(paramInt);; localDrawable = this.mDefaultUpIndicator)
      {
        localImageView.setImageDrawable(localDrawable);
        return;
      }
    }
    
    public void setUpIndicator(Drawable paramDrawable)
    {
      ImageView localImageView = this.mUpView;
      if (paramDrawable != null) {}
      for (;;)
      {
        localImageView.setImageDrawable(paramDrawable);
        this.mUpIndicatorRes = 0;
        return;
        paramDrawable = this.mDefaultUpIndicator;
      }
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ActionBarView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActionBarView.SavedState(paramAnonymousParcel, null);
      }
      
      public ActionBarView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ActionBarView.SavedState[paramAnonymousInt];
      }
    };
    int expandedMenuItemId;
    boolean isOverflowOpen;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      this.expandedMenuItemId = paramParcel.readInt();
      if (paramParcel.readInt() != 0) {}
      for (boolean bool = true;; bool = false)
      {
        this.isOverflowOpen = bool;
        return;
      }
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.expandedMenuItemId);
      if (this.isOverflowOpen) {}
      for (int i = 1;; i = 0)
      {
        paramParcel.writeInt(i);
        return;
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarView
 * JD-Core Version:    0.7.0.1
 */