package delco.devbits.djassa.util;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;

public abstract class SystemUiHider
{
  public static final int FLAG_FULLSCREEN = 2;
  public static final int FLAG_HIDE_NAVIGATION = 6;
  public static final int FLAG_LAYOUT_IN_SCREEN_OLDER_DEVICES = 1;
  private static OnVisibilityChangeListener sDummyListener = new OnVisibilityChangeListener()
  {
    public void onVisibilityChange(boolean paramAnonymousBoolean) {}
  };
  protected Activity mActivity;
  protected View mAnchorView;
  protected int mFlags;
  protected OnVisibilityChangeListener mOnVisibilityChangeListener = sDummyListener;
  
  protected SystemUiHider(Activity paramActivity, View paramView, int paramInt)
  {
    this.mActivity = paramActivity;
    this.mAnchorView = paramView;
    this.mFlags = paramInt;
  }
  
  public static SystemUiHider getInstance(Activity paramActivity, View paramView, int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 11) {
      return new SystemUiHiderHoneycomb(paramActivity, paramView, paramInt);
    }
    return new SystemUiHiderBase(paramActivity, paramView, paramInt);
  }
  
  public abstract void hide();
  
  public abstract boolean isVisible();
  
  public void setOnVisibilityChangeListener(OnVisibilityChangeListener paramOnVisibilityChangeListener)
  {
    if (paramOnVisibilityChangeListener == null) {
      paramOnVisibilityChangeListener = sDummyListener;
    }
    this.mOnVisibilityChangeListener = paramOnVisibilityChangeListener;
  }
  
  public abstract void setup();
  
  public abstract void show();
  
  public void toggle()
  {
    if (isVisible())
    {
      hide();
      return;
    }
    show();
  }
  
  public static abstract interface OnVisibilityChangeListener
  {
    public abstract void onVisibilityChange(boolean paramBoolean);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.util.SystemUiHider
 * JD-Core Version:    0.7.0.1
 */