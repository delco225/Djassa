package delco.devbits.network;

import android.content.Context;
import android.content.Intent;

public class Sms_Mangment
{
  private Context context;
  
  public void SmSSendByNet(String paramString1, String paramString2) {}
  
  public void SmsSend(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setType("vnd.android-dir/mms-sms");
    localIntent.putExtra("address", paramString);
    this.context.startActivity(localIntent);
  }
  
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
 * Qualified Name:     delco.devbits.network.Sms_Mangment
 * JD-Core Version:    0.7.0.1
 */