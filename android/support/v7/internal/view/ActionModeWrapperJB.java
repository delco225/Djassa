package android.support.v7.internal.view;

import android.content.Context;
import android.support.v7.view.ActionMode.Callback;
import android.view.ActionMode;

public class ActionModeWrapperJB
  extends ActionModeWrapper
{
  public ActionModeWrapperJB(Context paramContext, ActionMode paramActionMode)
  {
    super(paramContext, paramActionMode);
  }
  
  public boolean getTitleOptionalHint()
  {
    return this.mWrappedObject.getTitleOptionalHint();
  }
  
  public boolean isTitleOptional()
  {
    return this.mWrappedObject.isTitleOptional();
  }
  
  public void setTitleOptionalHint(boolean paramBoolean)
  {
    this.mWrappedObject.setTitleOptionalHint(paramBoolean);
  }
  
  public static class CallbackWrapper
    extends ActionModeWrapper.CallbackWrapper
  {
    public CallbackWrapper(Context paramContext, ActionMode.Callback paramCallback)
    {
      super(paramCallback);
    }
    
    protected ActionModeWrapper createActionModeWrapper(Context paramContext, ActionMode paramActionMode)
    {
      return new ActionModeWrapperJB(paramContext, paramActionMode);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.ActionModeWrapperJB
 * JD-Core Version:    0.7.0.1
 */