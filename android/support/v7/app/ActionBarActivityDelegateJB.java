package android.support.v7.app;

import android.content.Context;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.internal.view.ActionModeWrapper.CallbackWrapper;
import android.support.v7.internal.view.ActionModeWrapperJB;
import android.support.v7.internal.view.ActionModeWrapperJB.CallbackWrapper;
import android.support.v7.view.ActionMode.Callback;
import android.view.ActionMode;

class ActionBarActivityDelegateJB
  extends ActionBarActivityDelegateICS
{
  ActionBarActivityDelegateJB(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
  }
  
  ActionModeWrapper.CallbackWrapper createActionModeCallbackWrapper(Context paramContext, ActionMode.Callback paramCallback)
  {
    return new ActionModeWrapperJB.CallbackWrapper(paramContext, paramCallback);
  }
  
  ActionModeWrapper createActionModeWrapper(Context paramContext, ActionMode paramActionMode)
  {
    return new ActionModeWrapperJB(paramContext, paramActionMode);
  }
  
  public ActionBar createSupportActionBar()
  {
    return new ActionBarImplJB(this.mActivity, this.mActivity);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateJB
 * JD-Core Version:    0.7.0.1
 */