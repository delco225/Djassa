package delco.devbits.network;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Call_Management
{
  Context context;
  
  public void Gsm_Call(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.CALL");
    localIntent.setData(Uri.parse("tel:" + paramString));
    try
    {
      this.context.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      localActivityNotFoundException.printStackTrace();
      Toast.makeText(this.context, "Reprenez l Appel SVP ...... ", 0).show();
    }
  }
  
  public void Internet_call() {}
  
  public Context getContext()
  {
    return this.context;
  }
  
  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.network.Call_Management
 * JD-Core Version:    0.7.0.1
 */