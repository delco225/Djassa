package delco.devbits.djassa.util;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.Window;

@TargetApi(11)
public class SystemUiHiderHoneycomb
  extends SystemUiHiderBase
{
  private int mHideFlags = 1;
  private int mShowFlags = 0;
  private View.OnSystemUiVisibilityChangeListener mSystemUiVisibilityChangeListener = new View.OnSystemUiVisibilityChangeListener()
  {
    public void onSystemUiVisibilityChange(int paramAnonymousInt)
    {
      if ((paramAnonymousInt & SystemUiHiderHoneycomb.this.mTestFlags) != 0)
      {
        if (Build.VERSION.SDK_INT < 16)
        {
          SystemUiHiderHoneycomb.this.mActivity.getActionBar().hide();
          SystemUiHiderHoneycomb.this.mActivity.getWindow().setFlags(1024, 1024);
        }
        SystemUiHiderHoneycomb.this.mOnVisibilityChangeListener.onVisibilityChange(false);
        SystemUiHiderHoneycomb.this.mVisible = false;
        return;
      }
      SystemUiHiderHoneycomb.this.mAnchorView.setSystemUiVisibility(SystemUiHiderHoneycomb.this.mShowFlags);
      if (Build.VERSION.SDK_INT < 16)
      {
        SystemUiHiderHoneycomb.this.mActivity.getActionBar().show();
        SystemUiHiderHoneycomb.this.mActivity.getWindow().setFlags(0, 1024);
      }
      SystemUiHiderHoneycomb.this.mOnVisibilityChangeListener.onVisibilityChange(true);
      SystemUiHiderHoneycomb.this.mVisible = true;
    }
  };
  private int mTestFlags = 1;
  private boolean mVisible = true;
  
  protected SystemUiHiderHoneycomb(Activity paramActivity, View paramView, int paramInt)
  {
    super(paramActivity, paramView, paramInt);
    if ((0x2 & this.mFlags) != 0)
    {
      this.mShowFlags = (0x400 | this.mShowFlags);
      this.mHideFlags = (0x404 | this.mHideFlags);
    }
    if ((0x6 & this.mFlags) != 0)
    {
      this.mShowFlags = (0x200 | this.mShowFlags);
      this.mHideFlags = (0x202 | this.mHideFlags);
      this.mTestFlags = (0x2 | this.mTestFlags);
    }
  }
  
  public void hide()
  {
    this.mAnchorView.setSystemUiVisibility(this.mHideFlags);
  }
  
  public boolean isVisible()
  {
    return this.mVisible;
  }
  
  public void setup()
  {
    this.mAnchorView.setOnSystemUiVisibilityChangeListener(this.mSystemUiVisibilityChangeListener);
  }
  
  public void show()
  {
    this.mAnchorView.setSystemUiVisibility(this.mShowFlags);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.util.SystemUiHiderHoneycomb
 * JD-Core Version:    0.7.0.1
 */