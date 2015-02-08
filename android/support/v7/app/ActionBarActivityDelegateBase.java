package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.style;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ListMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuBuilder.Callback;
import android.support.v7.internal.view.menu.MenuPresenter.Callback;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.widget.ProgressBarICS;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;

class ActionBarActivityDelegateBase
  extends ActionBarActivityDelegate
  implements MenuPresenter.Callback, MenuBuilder.Callback
{
  private static final int[] ACTION_BAR_DRAWABLE_TOGGLE_ATTRS;
  private static final String TAG = "ActionBarActivityDelegateBase";
  private ActionBarView mActionBarView;
  private ActionMode mActionMode;
  private boolean mClosingActionMenu;
  private boolean mFeatureIndeterminateProgress;
  private boolean mFeatureProgress;
  private ListMenuPresenter mListMenuPresenter;
  private MenuBuilder mMenu;
  private Bundle mPanelFrozenActionViewState;
  private boolean mPanelIsPrepared;
  private boolean mPanelRefreshContent;
  private boolean mSubDecorInstalled;
  private CharSequence mTitleToSet;
  
  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = R.attr.homeAsUpIndicator;
    ACTION_BAR_DRAWABLE_TOGGLE_ATTRS = arrayOfInt;
  }
  
  ActionBarActivityDelegateBase(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
  }
  
  private void applyFixedSizeWindow()
  {
    TypedArray localTypedArray = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
    boolean bool1 = localTypedArray.hasValue(3);
    TypedValue localTypedValue1 = null;
    if (bool1)
    {
      localTypedValue1 = null;
      if (0 == 0) {
        localTypedValue1 = new TypedValue();
      }
      localTypedArray.getValue(3, localTypedValue1);
    }
    boolean bool2 = localTypedArray.hasValue(5);
    TypedValue localTypedValue2 = null;
    if (bool2)
    {
      localTypedValue2 = null;
      if (0 == 0) {
        localTypedValue2 = new TypedValue();
      }
      localTypedArray.getValue(5, localTypedValue2);
    }
    boolean bool3 = localTypedArray.hasValue(6);
    TypedValue localTypedValue3 = null;
    if (bool3)
    {
      localTypedValue3 = null;
      if (0 == 0) {
        localTypedValue3 = new TypedValue();
      }
      localTypedArray.getValue(6, localTypedValue3);
    }
    boolean bool4 = localTypedArray.hasValue(4);
    TypedValue localTypedValue4 = null;
    if (bool4)
    {
      localTypedValue4 = null;
      if (0 == 0) {
        localTypedValue4 = new TypedValue();
      }
      localTypedArray.getValue(4, localTypedValue4);
    }
    DisplayMetrics localDisplayMetrics = this.mActivity.getResources().getDisplayMetrics();
    int i;
    int j;
    int k;
    TypedValue localTypedValue5;
    label206:
    label238:
    TypedValue localTypedValue6;
    if (localDisplayMetrics.widthPixels < localDisplayMetrics.heightPixels)
    {
      i = 1;
      j = -1;
      k = -1;
      if (i == 0) {
        break label316;
      }
      localTypedValue5 = localTypedValue2;
      if ((localTypedValue5 != null) && (localTypedValue5.type != 0))
      {
        if (localTypedValue5.type != 5) {
          break label322;
        }
        j = (int)localTypedValue5.getDimension(localDisplayMetrics);
      }
      if (i == 0) {
        break label355;
      }
      localTypedValue6 = localTypedValue3;
      label247:
      if ((localTypedValue6 != null) && (localTypedValue6.type != 0))
      {
        if (localTypedValue6.type != 5) {
          break label362;
        }
        k = (int)localTypedValue6.getDimension(localDisplayMetrics);
      }
    }
    for (;;)
    {
      if ((j != -1) || (k != -1)) {
        this.mActivity.getWindow().setLayout(j, k);
      }
      localTypedArray.recycle();
      return;
      i = 0;
      break;
      label316:
      localTypedValue5 = localTypedValue1;
      break label206;
      label322:
      if (localTypedValue5.type != 6) {
        break label238;
      }
      j = (int)localTypedValue5.getFraction(localDisplayMetrics.widthPixels, localDisplayMetrics.widthPixels);
      break label238;
      label355:
      localTypedValue6 = localTypedValue4;
      break label247;
      label362:
      if (localTypedValue6.type == 6) {
        k = (int)localTypedValue6.getFraction(localDisplayMetrics.heightPixels, localDisplayMetrics.heightPixels);
      }
    }
  }
  
  private ProgressBarICS getCircularProgressBar()
  {
    ProgressBarICS localProgressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_circular);
    if (localProgressBarICS != null) {
      localProgressBarICS.setVisibility(4);
    }
    return localProgressBarICS;
  }
  
  private ProgressBarICS getHorizontalProgressBar()
  {
    ProgressBarICS localProgressBarICS = (ProgressBarICS)this.mActionBarView.findViewById(R.id.progress_horizontal);
    if (localProgressBarICS != null) {
      localProgressBarICS.setVisibility(4);
    }
    return localProgressBarICS;
  }
  
  private MenuView getListMenuView(Context paramContext, MenuPresenter.Callback paramCallback)
  {
    if (this.mMenu == null) {
      return null;
    }
    if (this.mListMenuPresenter == null)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(R.styleable.Theme);
      int i = localTypedArray.getResourceId(4, R.style.Theme_AppCompat_CompactMenu);
      localTypedArray.recycle();
      this.mListMenuPresenter = new ListMenuPresenter(R.layout.abc_list_menu_item_layout, i);
      this.mListMenuPresenter.setCallback(paramCallback);
      this.mMenu.addMenuPresenter(this.mListMenuPresenter);
    }
    for (;;)
    {
      return this.mListMenuPresenter.getMenuView(new FrameLayout(paramContext));
      this.mListMenuPresenter.updateMenuView(false);
    }
  }
  
  private void hideProgressBars(ProgressBarICS paramProgressBarICS1, ProgressBarICS paramProgressBarICS2)
  {
    if ((this.mFeatureIndeterminateProgress) && (paramProgressBarICS2.getVisibility() == 0)) {
      paramProgressBarICS2.setVisibility(4);
    }
    if ((this.mFeatureProgress) && (paramProgressBarICS1.getVisibility() == 0)) {
      paramProgressBarICS1.setVisibility(4);
    }
  }
  
  private boolean initializePanelMenu()
  {
    this.mMenu = new MenuBuilder(getActionBarThemedContext());
    this.mMenu.setCallback(this);
    return true;
  }
  
  private boolean preparePanel()
  {
    if (this.mPanelIsPrepared) {
      return true;
    }
    if ((this.mMenu == null) || (this.mPanelRefreshContent))
    {
      if ((this.mMenu == null) && ((!initializePanelMenu()) || (this.mMenu == null))) {
        return false;
      }
      if (this.mActionBarView != null) {
        this.mActionBarView.setMenu(this.mMenu, this);
      }
      this.mMenu.stopDispatchingItemsChanged();
      if (!this.mActivity.superOnCreatePanelMenu(0, this.mMenu))
      {
        this.mMenu = null;
        if (this.mActionBarView != null) {
          this.mActionBarView.setMenu(null, this);
        }
        return false;
      }
      this.mPanelRefreshContent = false;
    }
    this.mMenu.stopDispatchingItemsChanged();
    if (this.mPanelFrozenActionViewState != null)
    {
      this.mMenu.restoreActionViewStates(this.mPanelFrozenActionViewState);
      this.mPanelFrozenActionViewState = null;
    }
    if (!this.mActivity.superOnPreparePanel(0, null, this.mMenu))
    {
      if (this.mActionBarView != null) {
        this.mActionBarView.setMenu(null, this);
      }
      this.mMenu.startDispatchingItemsChanged();
      return false;
    }
    this.mMenu.startDispatchingItemsChanged();
    this.mPanelIsPrepared = true;
    return true;
  }
  
  private void reopenMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if ((this.mActionBarView != null) && (this.mActionBarView.isOverflowReserved()))
    {
      if ((!this.mActionBarView.isOverflowMenuShowing()) || (!paramBoolean))
      {
        if (this.mActionBarView.getVisibility() == 0) {
          this.mActionBarView.showOverflowMenu();
        }
        return;
      }
      this.mActionBarView.hideOverflowMenu();
      return;
    }
    paramMenuBuilder.close();
  }
  
  private void showProgressBars(ProgressBarICS paramProgressBarICS1, ProgressBarICS paramProgressBarICS2)
  {
    if ((this.mFeatureIndeterminateProgress) && (paramProgressBarICS2.getVisibility() == 4)) {
      paramProgressBarICS2.setVisibility(0);
    }
    if ((this.mFeatureProgress) && (paramProgressBarICS1.getProgress() < 10000)) {
      paramProgressBarICS1.setVisibility(0);
    }
  }
  
  private void updateProgressBars(int paramInt)
  {
    ProgressBarICS localProgressBarICS1 = getCircularProgressBar();
    ProgressBarICS localProgressBarICS2 = getHorizontalProgressBar();
    int j;
    if (paramInt == -1) {
      if (this.mFeatureProgress)
      {
        int i = localProgressBarICS2.getProgress();
        if ((localProgressBarICS2.isIndeterminate()) || (i < 10000))
        {
          j = 0;
          localProgressBarICS2.setVisibility(j);
        }
      }
      else if (this.mFeatureIndeterminateProgress)
      {
        localProgressBarICS1.setVisibility(0);
      }
    }
    label104:
    do
    {
      do
      {
        return;
        j = 4;
        break;
        if (paramInt != -2) {
          break label104;
        }
        if (this.mFeatureProgress) {
          localProgressBarICS2.setVisibility(8);
        }
      } while (!this.mFeatureIndeterminateProgress);
      localProgressBarICS1.setVisibility(8);
      return;
      if (paramInt == -3)
      {
        localProgressBarICS2.setIndeterminate(true);
        return;
      }
      if (paramInt == -4)
      {
        localProgressBarICS2.setIndeterminate(false);
        return;
      }
    } while ((paramInt < 0) || (paramInt > 10000));
    localProgressBarICS2.setProgress(paramInt + 0);
    if (paramInt < 10000)
    {
      showProgressBars(localProgressBarICS2, localProgressBarICS1);
      return;
    }
    hideProgressBars(localProgressBarICS2, localProgressBarICS1);
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    ensureSubDecor();
    ((ViewGroup)this.mActivity.findViewById(16908290)).addView(paramView, paramLayoutParams);
    this.mActivity.onSupportContentChanged();
  }
  
  public ActionBar createSupportActionBar()
  {
    ensureSubDecor();
    return new ActionBarImplBase(this.mActivity, this.mActivity);
  }
  
  final void ensureSubDecor()
  {
    boolean bool2;
    if (!this.mSubDecorInstalled)
    {
      if (!this.mHasActionBar) {
        break label322;
      }
      if (!this.mOverlayActionBar) {
        break label283;
      }
      this.mActivity.superSetContentView(R.layout.abc_action_bar_decor_overlay);
      this.mActionBarView = ((ActionBarView)this.mActivity.findViewById(R.id.action_bar));
      this.mActionBarView.setWindowCallback(this.mActivity);
      if (this.mFeatureProgress) {
        this.mActionBarView.initProgress();
      }
      if (this.mFeatureIndeterminateProgress) {
        this.mActionBarView.initIndeterminateProgress();
      }
      boolean bool1 = "splitActionBarWhenNarrow".equals(getUiOptionsFromMetadata());
      if (!bool1) {
        break label296;
      }
      bool2 = this.mActivity.getResources().getBoolean(R.bool.abc_split_action_bar_is_narrow);
      label117:
      ActionBarContainer localActionBarContainer = (ActionBarContainer)this.mActivity.findViewById(R.id.split_action_bar);
      if (localActionBarContainer != null)
      {
        this.mActionBarView.setSplitView(localActionBarContainer);
        this.mActionBarView.setSplitActionBar(bool2);
        this.mActionBarView.setSplitWhenNarrow(bool1);
        ActionBarContextView localActionBarContextView = (ActionBarContextView)this.mActivity.findViewById(R.id.action_context_bar);
        localActionBarContextView.setSplitView(localActionBarContainer);
        localActionBarContextView.setSplitActionBar(bool2);
        localActionBarContextView.setSplitWhenNarrow(bool1);
      }
    }
    for (;;)
    {
      this.mActivity.findViewById(16908290).setId(-1);
      this.mActivity.findViewById(R.id.action_bar_activity_content).setId(16908290);
      if (this.mTitleToSet != null)
      {
        this.mActionBarView.setWindowTitle(this.mTitleToSet);
        this.mTitleToSet = null;
      }
      applyFixedSizeWindow();
      this.mSubDecorInstalled = true;
      this.mActivity.getWindow().getDecorView().post(new Runnable()
      {
        public void run()
        {
          ActionBarActivityDelegateBase.this.supportInvalidateOptionsMenu();
        }
      });
      return;
      label283:
      this.mActivity.superSetContentView(R.layout.abc_action_bar_decor);
      break;
      label296:
      TypedArray localTypedArray = this.mActivity.obtainStyledAttributes(R.styleable.ActionBarWindow);
      bool2 = localTypedArray.getBoolean(2, false);
      localTypedArray.recycle();
      break label117;
      label322:
      this.mActivity.superSetContentView(R.layout.abc_simple_decor);
    }
  }
  
  int getHomeAsUpIndicatorAttrId()
  {
    return R.attr.homeAsUpIndicator;
  }
  
  public boolean onBackPressed()
  {
    if (this.mActionMode != null)
    {
      this.mActionMode.finish();
      return true;
    }
    if ((this.mActionBarView != null) && (this.mActionBarView.hasExpandedActionView()))
    {
      this.mActionBarView.collapseActionView();
      return true;
    }
    return false;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (this.mClosingActionMenu) {
      return;
    }
    this.mClosingActionMenu = true;
    this.mActivity.closeOptionsMenu();
    this.mActionBarView.dismissPopupMenus();
    this.mClosingActionMenu = false;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if ((this.mHasActionBar) && (this.mSubDecorInstalled)) {
      ((ActionBarImplBase)getSupportActionBar()).onConfigurationChanged(paramConfiguration);
    }
  }
  
  public void onContentChanged() {}
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    if (paramInt != 0) {
      return this.mActivity.superOnCreatePanelMenu(paramInt, paramMenu);
    }
    return false;
  }
  
  public View onCreatePanelView(int paramInt)
  {
    View localView = null;
    if (paramInt == 0)
    {
      boolean bool = preparePanel();
      localView = null;
      if (bool) {
        localView = (View)getListMenuView(this.mActivity, this);
      }
    }
    return localView;
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    if (paramInt == 0) {
      paramMenuItem = MenuWrapperFactory.createMenuItemWrapper(paramMenuItem);
    }
    return this.mActivity.superOnMenuItemSelected(paramInt, paramMenuItem);
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    return this.mActivity.onMenuItemSelected(0, paramMenuItem);
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder)
  {
    reopenMenu(paramMenuBuilder, true);
  }
  
  public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
  {
    return false;
  }
  
  public void onPostResume()
  {
    ActionBarImplBase localActionBarImplBase = (ActionBarImplBase)getSupportActionBar();
    if (localActionBarImplBase != null) {
      localActionBarImplBase.setShowHideAnimationEnabled(true);
    }
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    if (paramInt != 0) {
      return this.mActivity.superOnPreparePanel(paramInt, paramView, paramMenu);
    }
    return false;
  }
  
  public void onStop()
  {
    ActionBarImplBase localActionBarImplBase = (ActionBarImplBase)getSupportActionBar();
    if (localActionBarImplBase != null) {
      localActionBarImplBase.setShowHideAnimationEnabled(false);
    }
  }
  
  public void onTitleChanged(CharSequence paramCharSequence)
  {
    if (this.mActionBarView != null)
    {
      this.mActionBarView.setWindowTitle(paramCharSequence);
      return;
    }
    this.mTitleToSet = paramCharSequence;
  }
  
  public void setContentView(int paramInt)
  {
    ensureSubDecor();
    ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
    localViewGroup.removeAllViews();
    this.mActivity.getLayoutInflater().inflate(paramInt, localViewGroup);
    this.mActivity.onSupportContentChanged();
  }
  
  public void setContentView(View paramView)
  {
    ensureSubDecor();
    ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
    localViewGroup.removeAllViews();
    localViewGroup.addView(paramView);
    this.mActivity.onSupportContentChanged();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    ensureSubDecor();
    ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
    localViewGroup.removeAllViews();
    localViewGroup.addView(paramView, paramLayoutParams);
    this.mActivity.onSupportContentChanged();
  }
  
  void setSupportProgress(int paramInt)
  {
    updateProgressBars(paramInt + 0);
  }
  
  void setSupportProgressBarIndeterminate(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = -3;; i = -4)
    {
      updateProgressBars(i);
      return;
    }
  }
  
  void setSupportProgressBarIndeterminateVisibility(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = -1;; i = -2)
    {
      updateProgressBars(i);
      return;
    }
  }
  
  void setSupportProgressBarVisibility(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = -1;; i = -2)
    {
      updateProgressBars(i);
      return;
    }
  }
  
  public ActionMode startSupportActionMode(ActionMode.Callback paramCallback)
  {
    if (paramCallback == null) {
      throw new IllegalArgumentException("ActionMode callback can not be null.");
    }
    if (this.mActionMode != null) {
      this.mActionMode.finish();
    }
    ActionModeCallbackWrapper localActionModeCallbackWrapper = new ActionModeCallbackWrapper(paramCallback);
    ActionBarImplBase localActionBarImplBase = (ActionBarImplBase)getSupportActionBar();
    if (localActionBarImplBase != null) {
      this.mActionMode = localActionBarImplBase.startActionMode(localActionModeCallbackWrapper);
    }
    if (this.mActionMode != null) {
      this.mActivity.onSupportActionModeStarted(this.mActionMode);
    }
    return this.mActionMode;
  }
  
  public void supportInvalidateOptionsMenu()
  {
    if (this.mMenu != null)
    {
      Bundle localBundle = new Bundle();
      this.mMenu.saveActionViewStates(localBundle);
      if (localBundle.size() > 0) {
        this.mPanelFrozenActionViewState = localBundle;
      }
      this.mMenu.stopDispatchingItemsChanged();
      this.mMenu.clear();
    }
    this.mPanelRefreshContent = true;
    if (this.mActionBarView != null)
    {
      this.mPanelIsPrepared = false;
      preparePanel();
    }
  }
  
  public boolean supportRequestWindowFeature(int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
    case 4: 
    case 6: 
    case 7: 
    default: 
      return this.mActivity.requestWindowFeature(paramInt);
    case 8: 
      this.mHasActionBar = true;
      return true;
    case 9: 
      this.mOverlayActionBar = true;
      return true;
    case 2: 
      this.mFeatureProgress = true;
      return true;
    }
    this.mFeatureIndeterminateProgress = true;
    return true;
  }
  
  private class ActionModeCallbackWrapper
    implements ActionMode.Callback
  {
    private ActionMode.Callback mWrapped;
    
    public ActionModeCallbackWrapper(ActionMode.Callback paramCallback)
    {
      this.mWrapped = paramCallback;
    }
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      return this.mWrapped.onActionItemClicked(paramActionMode, paramMenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return this.mWrapped.onCreateActionMode(paramActionMode, paramMenu);
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode)
    {
      this.mWrapped.onDestroyActionMode(paramActionMode);
      ActionBarActivityDelegateBase.this.mActivity.onSupportActionModeFinished(paramActionMode);
      ActionBarActivityDelegateBase.access$002(ActionBarActivityDelegateBase.this, null);
    }
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return this.mWrapped.onPrepareActionMode(paramActionMode, paramMenu);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateBase
 * JD-Core Version:    0.7.0.1
 */