package delco.devbits.djassa;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import delco.devbits.network.Websupport;

public class ColectorPageAdaptor
  extends FragmentStatePagerAdapter
{
  public Context ApkContext;
  
  public ColectorPageAdaptor(FragmentManager paramFragmentManager, Context paramContext)
  {
    super(paramFragmentManager);
    this.ApkContext = paramContext;
  }
  
  public void SetApkContext(Context paramContext)
  {
    this.ApkContext = paramContext;
  }
  
  public int getCount()
  {
    return 3;
  }
  
  public Fragment getItem(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      DemoObjectFragment localDemoObjectFragment = new DemoObjectFragment();
      Bundle localBundle5 = new Bundle();
      localBundle5.putInt("object", paramInt + 1);
      localDemoObjectFragment.setArguments(localBundle5);
      return localDemoObjectFragment;
    case 0: 
      Websupport localWebsupport2 = new Websupport();
      localWebsupport2.setContext(this.ApkContext);
      if (localWebsupport2.Yaconnexion_internet())
      {
        OffreFragment localOffreFragment = new OffreFragment();
        localOffreFragment.SetContext(this.ApkContext);
        Bundle localBundle3 = new Bundle();
        localBundle3.putInt("object", paramInt + 1);
        localOffreFragment.setArguments(localBundle3);
        return localOffreFragment;
      }
      DefaultFragment localDefaultFragment2 = new DefaultFragment();
      Bundle localBundle4 = new Bundle();
      localBundle4.putInt("object", paramInt + 1);
      localDefaultFragment2.setArguments(localBundle4);
      return localDefaultFragment2;
    }
    Websupport localWebsupport1 = new Websupport();
    localWebsupport1.setContext(this.ApkContext);
    if (localWebsupport1.Yaconnexion_internet())
    {
      MarketFragment localMarketFragment = new MarketFragment();
      localMarketFragment.setContext(this.ApkContext);
      Bundle localBundle1 = new Bundle();
      localBundle1.putInt("object", paramInt + 1);
      localMarketFragment.setArguments(localBundle1);
      return localMarketFragment;
    }
    DefaultFragment localDefaultFragment1 = new DefaultFragment();
    Bundle localBundle2 = new Bundle();
    localBundle2.putInt("object", paramInt + 1);
    localDefaultFragment1.setArguments(localBundle2);
    return localDefaultFragment1;
  }
  
  public CharSequence getPageTitle(int paramInt)
  {
    return "OBJECT " + (paramInt + 1);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.ColectorPageAdaptor
 * JD-Core Version:    0.7.0.1
 */