package delco.devbits.djassa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DemoObjectFragment
  extends Fragment
{
  public static final String ARG_OBJECT = "object";
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903071, paramViewGroup, false);
    getArguments();
    ((TextView)localView.findViewById(16908308)).setText(" en construction ");
    return localView;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.DemoObjectFragment
 * JD-Core Version:    0.7.0.1
 */