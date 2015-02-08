package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout.LayoutParams;

public class ActionMenuView
  extends LinearLayoutICS
  implements MenuBuilder.ItemInvoker, MenuView
{
  static final int GENERATED_ITEM_PADDING = 4;
  static final int MIN_CELL_SIZE = 56;
  private static final String TAG = "ActionMenuView";
  private boolean mFormatItems;
  private int mFormatItemsWidth;
  private int mGeneratedItemPadding;
  private int mMaxItemHeight;
  private int mMeasuredExtraWidth;
  private MenuBuilder mMenu;
  private int mMinCellSize;
  private ActionMenuPresenter mPresenter;
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = paramContext.getResources().getDisplayMetrics().density;
    this.mMinCellSize = ((int)(56.0F * f));
    this.mGeneratedItemPadding = ((int)(4.0F * f));
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
    this.mMaxItemHeight = localTypedArray.getDimensionPixelSize(1, 0);
    localTypedArray.recycle();
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt3) - paramInt4, View.MeasureSpec.getMode(paramInt3));
    ActionMenuItemView localActionMenuItemView;
    int j;
    label54:
    int k;
    if ((paramView instanceof ActionMenuItemView))
    {
      localActionMenuItemView = (ActionMenuItemView)paramView;
      if ((localActionMenuItemView == null) || (!localActionMenuItemView.hasText())) {
        break label178;
      }
      j = 1;
      k = 0;
      if (paramInt2 > 0) {
        if (j != 0)
        {
          k = 0;
          if (paramInt2 < 2) {}
        }
        else
        {
          paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1 * paramInt2, -2147483648), i);
          int m = paramView.getMeasuredWidth();
          k = m / paramInt1;
          if (m % paramInt1 != 0) {
            k++;
          }
          if ((j != 0) && (k < 2)) {
            k = 2;
          }
        }
      }
      if ((localLayoutParams.isOverflowButton) || (j == 0)) {
        break label184;
      }
    }
    label178:
    label184:
    for (boolean bool = true;; bool = false)
    {
      localLayoutParams.expandable = bool;
      localLayoutParams.cellsUsed = k;
      paramView.measure(View.MeasureSpec.makeMeasureSpec(k * paramInt1, 1073741824), i);
      return k;
      localActionMenuItemView = null;
      break;
      j = 0;
      break label54;
    }
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt2);
    int m = getPaddingLeft() + getPaddingRight();
    int n = getPaddingTop() + getPaddingBottom();
    if (i == 1073741824) {}
    int i2;
    int i3;
    int i4;
    for (int i1 = View.MeasureSpec.makeMeasureSpec(k - n, 1073741824);; i1 = View.MeasureSpec.makeMeasureSpec(Math.min(this.mMaxItemHeight, k - n), -2147483648))
    {
      i2 = j - m;
      i3 = i2 / this.mMinCellSize;
      i4 = i2 % this.mMinCellSize;
      if (i3 != 0) {
        break;
      }
      setMeasuredDimension(i2, 0);
      return;
    }
    int i5 = this.mMinCellSize + i4 / i3;
    int i6 = i3;
    int i7 = 0;
    int i8 = 0;
    int i9 = 0;
    int i10 = 0;
    int i11 = 0;
    long l1 = 0L;
    int i12 = getChildCount();
    int i13 = 0;
    while (i13 < i12)
    {
      View localView4 = getChildAt(i13);
      if (localView4.getVisibility() == 8)
      {
        i13++;
      }
      else
      {
        boolean bool1 = localView4 instanceof ActionMenuItemView;
        i10++;
        if (bool1) {
          localView4.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
        }
        LayoutParams localLayoutParams5 = (LayoutParams)localView4.getLayoutParams();
        localLayoutParams5.expanded = false;
        localLayoutParams5.extraPixels = 0;
        localLayoutParams5.cellsUsed = 0;
        localLayoutParams5.expandable = false;
        localLayoutParams5.leftMargin = 0;
        localLayoutParams5.rightMargin = 0;
        boolean bool2;
        if ((bool1) && (((ActionMenuItemView)localView4).hasText()))
        {
          bool2 = true;
          label286:
          localLayoutParams5.preventEdgeOffset = bool2;
          if (!localLayoutParams5.isOverflowButton) {
            break label398;
          }
        }
        label398:
        for (int i26 = 1;; i26 = i6)
        {
          int i27 = measureChildForCells(localView4, i5, i26, i1, n);
          i8 = Math.max(i8, i27);
          if (localLayoutParams5.expandable) {
            i9++;
          }
          if (localLayoutParams5.isOverflowButton) {
            i11 = 1;
          }
          i6 -= i27;
          int i28 = localView4.getMeasuredHeight();
          i7 = Math.max(i7, i28);
          if (i27 != 1) {
            break;
          }
          l1 |= 1 << i13;
          break;
          bool2 = false;
          break label286;
        }
      }
    }
    int i14;
    int i15;
    int i21;
    long l2;
    int i22;
    int i23;
    label445:
    LayoutParams localLayoutParams4;
    if ((i11 != 0) && (i10 == 2))
    {
      i14 = 1;
      i15 = 0;
      if ((i9 <= 0) || (i6 <= 0)) {
        break label556;
      }
      i21 = 2147483647;
      l2 = 0L;
      i22 = 0;
      i23 = 0;
      if (i23 >= i12) {
        break label542;
      }
      localLayoutParams4 = (LayoutParams)getChildAt(i23).getLayoutParams();
      if (localLayoutParams4.expandable) {
        break label486;
      }
    }
    for (;;)
    {
      i23++;
      break label445;
      i14 = 0;
      break;
      label486:
      if (localLayoutParams4.cellsUsed < i21)
      {
        i21 = localLayoutParams4.cellsUsed;
        l2 = 1 << i23;
        i22 = 1;
      }
      else if (localLayoutParams4.cellsUsed == i21)
      {
        l2 |= 1 << i23;
        i22++;
      }
    }
    label542:
    l1 |= l2;
    label556:
    int i16;
    label570:
    int i18;
    label709:
    int i19;
    if (i22 > i6)
    {
      if ((i11 != 0) || (i10 != 1)) {
        break label878;
      }
      i16 = 1;
      if ((i6 <= 0) || (l1 == 0L) || ((i6 >= i10 - 1) && (i16 == 0) && (i8 <= 1))) {
        break label1037;
      }
      float f = Long.bitCount(l1);
      if (i16 == 0)
      {
        if (((1L & l1) != 0L) && (!((LayoutParams)getChildAt(0).getLayoutParams()).preventEdgeOffset)) {
          f -= 0.5F;
        }
        if (((l1 & 1 << i12 - 1) != 0L) && (!((LayoutParams)getChildAt(i12 - 1).getLayoutParams()).preventEdgeOffset)) {
          f -= 0.5F;
        }
      }
      if (f <= 0.0F) {
        break label884;
      }
      i18 = (int)(i6 * i5 / f);
      i19 = 0;
      label712:
      if (i19 >= i12) {
        break label1034;
      }
      if ((l1 & 1 << i19) != 0L) {
        break label890;
      }
    }
    for (;;)
    {
      i19++;
      break label712;
      int i24 = i21 + 1;
      int i25 = 0;
      if (i25 < i12)
      {
        View localView3 = getChildAt(i25);
        LayoutParams localLayoutParams3 = (LayoutParams)localView3.getLayoutParams();
        if ((l2 & 1 << i25) == 0L) {
          if (localLayoutParams3.cellsUsed == i24) {
            l1 |= 1 << i25;
          }
        }
        for (;;)
        {
          i25++;
          break;
          if ((i14 != 0) && (localLayoutParams3.preventEdgeOffset) && (i6 == 1)) {
            localView3.setPadding(i5 + this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
          }
          localLayoutParams3.cellsUsed = (1 + localLayoutParams3.cellsUsed);
          localLayoutParams3.expanded = true;
          i6--;
        }
      }
      i15 = 1;
      break;
      label878:
      i16 = 0;
      break label570;
      label884:
      i18 = 0;
      break label709;
      label890:
      View localView2 = getChildAt(i19);
      LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
      if ((localView2 instanceof ActionMenuItemView))
      {
        localLayoutParams2.extraPixels = i18;
        localLayoutParams2.expanded = true;
        if ((i19 == 0) && (!localLayoutParams2.preventEdgeOffset)) {
          localLayoutParams2.leftMargin = (-i18 / 2);
        }
        i15 = 1;
      }
      else if (localLayoutParams2.isOverflowButton)
      {
        localLayoutParams2.extraPixels = i18;
        localLayoutParams2.expanded = true;
        localLayoutParams2.rightMargin = (-i18 / 2);
        i15 = 1;
      }
      else
      {
        if (i19 != 0) {
          localLayoutParams2.leftMargin = (i18 / 2);
        }
        int i20 = i12 - 1;
        if (i19 != i20) {
          localLayoutParams2.rightMargin = (i18 / 2);
        }
      }
    }
    label1034:
    i6 = 0;
    label1037:
    if (i15 != 0)
    {
      int i17 = 0;
      if (i17 < i12)
      {
        View localView1 = getChildAt(i17);
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if (!localLayoutParams1.expanded) {}
        for (;;)
        {
          i17++;
          break;
          localView1.measure(View.MeasureSpec.makeMeasureSpec(i5 * localLayoutParams1.cellsUsed + localLayoutParams1.extraPixels, 1073741824), i1);
        }
      }
    }
    if (i != 1073741824) {
      k = i7;
    }
    setMeasuredDimension(i2, k);
    this.mMeasuredExtraWidth = (i6 * i5);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return (paramLayoutParams != null) && ((paramLayoutParams instanceof LayoutParams));
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    LayoutParams localLayoutParams = new LayoutParams(-2, -2);
    localLayoutParams.gravity = 16;
    return localLayoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams))
    {
      LayoutParams localLayoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      if (localLayoutParams.gravity <= 0) {
        localLayoutParams.gravity = 16;
      }
      return localLayoutParams;
    }
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams()
  {
    LayoutParams localLayoutParams = generateDefaultLayoutParams();
    localLayoutParams.isOverflowButton = true;
    return localLayoutParams;
  }
  
  public int getWindowAnimations()
  {
    return 0;
  }
  
  protected boolean hasSupportDividerBeforeChildAt(int paramInt)
  {
    View localView1 = getChildAt(paramInt - 1);
    View localView2 = getChildAt(paramInt);
    int i = getChildCount();
    boolean bool1 = false;
    if (paramInt < i)
    {
      boolean bool2 = localView1 instanceof ActionMenuChildView;
      bool1 = false;
      if (bool2) {
        bool1 = false | ((ActionMenuChildView)localView1).needsDividerAfter();
      }
    }
    if ((paramInt > 0) && ((localView2 instanceof ActionMenuChildView))) {
      bool1 |= ((ActionMenuChildView)localView2).needsDividerBefore();
    }
    return bool1;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder)
  {
    this.mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl)
  {
    return this.mMenu.performItemAction(paramMenuItemImpl, 0);
  }
  
  public boolean isExpandedFormat()
  {
    return this.mFormatItems;
  }
  
  public boolean isOverflowReserved()
  {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (Build.VERSION.SDK_INT >= 8) {
      super.onConfigurationChanged(paramConfiguration);
    }
    this.mPresenter.updateMenuView(false);
    if ((this.mPresenter != null) && (this.mPresenter.isOverflowMenuShowing()))
    {
      this.mPresenter.hideOverflowMenu();
      this.mPresenter.showOverflowMenu();
    }
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mPresenter.dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!this.mFormatItems)
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    int i = getChildCount();
    int j = (paramInt2 + paramInt4) / 2;
    int k = getSupportDividerWidth();
    int m = 0;
    int n = 0;
    int i1 = paramInt3 - paramInt1 - getPaddingRight() - getPaddingLeft();
    int i2 = 0;
    int i3 = 0;
    if (i3 < i)
    {
      View localView3 = getChildAt(i3);
      if (localView3.getVisibility() == 8) {}
      for (;;)
      {
        i3++;
        break;
        LayoutParams localLayoutParams2 = (LayoutParams)localView3.getLayoutParams();
        if (localLayoutParams2.isOverflowButton)
        {
          int i19 = localView3.getMeasuredWidth();
          if (hasSupportDividerBeforeChildAt(i3)) {
            i19 += k;
          }
          int i20 = localView3.getMeasuredHeight();
          int i21 = getWidth() - getPaddingRight() - localLayoutParams2.rightMargin;
          int i22 = i21 - i19;
          int i23 = j - i20 / 2;
          localView3.layout(i22, i23, i21, i23 + i20);
          i1 -= i19;
          i2 = 1;
        }
        else
        {
          int i18 = localView3.getMeasuredWidth() + localLayoutParams2.leftMargin + localLayoutParams2.rightMargin;
          m += i18;
          i1 -= i18;
          if (hasSupportDividerBeforeChildAt(i3)) {
            m += k;
          }
          n++;
        }
      }
    }
    if ((i == 1) && (i2 == 0))
    {
      View localView2 = getChildAt(0);
      int i14 = localView2.getMeasuredWidth();
      int i15 = localView2.getMeasuredHeight();
      int i16 = (paramInt3 - paramInt1) / 2 - i14 / 2;
      int i17 = j - i15 / 2;
      localView2.layout(i16, i17, i16 + i14, i17 + i15);
      return;
    }
    int i4;
    label345:
    int i6;
    label364:
    int i7;
    int i8;
    int i9;
    label381:
    View localView1;
    LayoutParams localLayoutParams1;
    if (i2 != 0)
    {
      i4 = 0;
      int i5 = n - i4;
      if (i5 <= 0) {
        break label436;
      }
      i6 = i1 / i5;
      i7 = Math.max(0, i6);
      i8 = getPaddingLeft();
      i9 = 0;
      if (i9 < i)
      {
        localView1 = getChildAt(i9);
        localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if ((localView1.getVisibility() != 8) && (!localLayoutParams1.isOverflowButton)) {
          break label442;
        }
      }
    }
    for (;;)
    {
      i9++;
      break label381;
      break;
      i4 = 1;
      break label345;
      label436:
      i6 = 0;
      break label364;
      label442:
      int i10 = i8 + localLayoutParams1.leftMargin;
      int i11 = localView1.getMeasuredWidth();
      int i12 = localView1.getMeasuredHeight();
      int i13 = j - i12 / 2;
      localView1.layout(i10, i13, i10 + i11, i13 + i12);
      i8 = i10 + (i7 + (i11 + localLayoutParams1.rightMargin));
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool1 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {}
    for (boolean bool2 = true;; bool2 = false)
    {
      this.mFormatItems = bool2;
      if (bool1 != this.mFormatItems) {
        this.mFormatItemsWidth = 0;
      }
      int i = View.MeasureSpec.getMode(paramInt1);
      if ((this.mFormatItems) && (this.mMenu != null) && (i != this.mFormatItemsWidth))
      {
        this.mFormatItemsWidth = i;
        this.mMenu.onItemsChanged(true);
      }
      if (!this.mFormatItems) {
        break;
      }
      onMeasureExactFormat(paramInt1, paramInt2);
      return;
    }
    int j = getChildCount();
    for (int k = 0; k < j; k++)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(k).getLayoutParams();
      localLayoutParams.rightMargin = 0;
      localLayoutParams.leftMargin = 0;
    }
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void setOverflowReserved(boolean paramBoolean)
  {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter)
  {
    this.mPresenter = paramActionMenuPresenter;
  }
  
  public static abstract interface ActionMenuChildView
  {
    public abstract boolean needsDividerAfter();
    
    public abstract boolean needsDividerBefore();
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    @ViewDebug.ExportedProperty
    public int cellsUsed;
    @ViewDebug.ExportedProperty
    public boolean expandable;
    public boolean expanded;
    @ViewDebug.ExportedProperty
    public int extraPixels;
    @ViewDebug.ExportedProperty
    public boolean isOverflowButton;
    @ViewDebug.ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      this.isOverflowButton = false;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      super(paramInt2);
      this.isOverflowButton = paramBoolean;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      this.isOverflowButton = paramLayoutParams.isOverflowButton;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ActionMenuView
 * JD-Core Version:    0.7.0.1
 */