package delco.devbits.djassa;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import delco.devbits.Media.Publication;
import java.util.ArrayList;
import slide_menu_pack.NavDrawerItem;
import slide_menu_pack.NavDrawerListAdapter;

public class Djassa_Main
  extends FragmentActivity
{
  private ActionBar Action_Bar = null;
  private ListView Gestion_List_Vue;
  private String[] NAVList;
  private TextView NavD = null;
  private NavDrawerListAdapter adapter;
  private ColectorPageAdaptor mColectorPageAdaptor = null;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private CharSequence mDrawerTitle;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mTitle;
  private DrawerLayout monLister;
  private ArrayList<NavDrawerItem> navDrawerItems;
  private TypedArray navMenuIcons;
  private String[] navMenuTitles;
  public ProgressDialog ptDialog;
  private ViewPager viewPager = null;
  
  private void displayView(int paramInt, Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, Default_construction.class);
    switch (paramInt)
    {
    default: 
      return;
    case 0: 
      startActivity(new Intent(paramContext, Publication.class));
      return;
    case 1: 
      startActivity(localIntent);
      return;
    case 2: 
      startActivity(localIntent);
      return;
    case 3: 
      startActivity(localIntent);
      return;
    case 4: 
      startActivity(localIntent);
      return;
    }
    startActivity(localIntent);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mDrawerToggle.onConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903065);
    CharSequence localCharSequence = getTitle();
    this.mDrawerTitle = localCharSequence;
    this.mTitle = localCharSequence;
    this.navMenuTitles = getResources().getStringArray(2131492865);
    this.navMenuIcons = getResources().obtainTypedArray(2131492864);
    this.mDrawerLayout = ((DrawerLayout)findViewById(2131034173));
    this.mDrawerList = ((ListView)findViewById(2131034175));
    this.navDrawerItems = new ArrayList();
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[0], this.navMenuIcons.getResourceId(0, -1)));
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[1], this.navMenuIcons.getResourceId(1, -1), true, "+140"));
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[2], this.navMenuIcons.getResourceId(2, -1)));
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[3], this.navMenuIcons.getResourceId(3, -1), true, "22"));
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[4], this.navMenuIcons.getResourceId(4, -1)));
    this.navDrawerItems.add(new NavDrawerItem(this.navMenuTitles[5], this.navMenuIcons.getResourceId(5, -1), true, "50+"));
    this.navMenuIcons.recycle();
    this.adapter = new NavDrawerListAdapter(getApplicationContext(), this.navDrawerItems);
    this.mDrawerList.setAdapter(this.adapter);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, 2130837609, 2131361805, 2131361805)
    {
      public void onDrawerClosed(View paramAnonymousView)
      {
        Djassa_Main.this.getActionBar().setTitle(Djassa_Main.this.mTitle);
        Djassa_Main.this.invalidateOptionsMenu();
      }
      
      public void onDrawerOpened(View paramAnonymousView)
      {
        Djassa_Main.this.getActionBar().setTitle(Djassa_Main.this.mDrawerTitle);
        Djassa_Main.this.invalidateOptionsMenu();
      }
    };
    this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
    SlideMenuClickListener localSlideMenuClickListener = new SlideMenuClickListener(null);
    localSlideMenuClickListener.SetContext(getApplicationContext());
    this.mDrawerList.setOnItemClickListener(localSlideMenuClickListener);
    this.mColectorPageAdaptor = new ColectorPageAdaptor(getSupportFragmentManager(), this);
    this.viewPager = ((ViewPager)findViewById(2131034174));
    this.viewPager.setAdapter(this.mColectorPageAdaptor);
    this.Action_Bar = getActionBar();
    this.Action_Bar.show();
    this.Action_Bar.setNavigationMode(2);
    this.viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
    {
      public void onPageSelected(int paramAnonymousInt)
      {
        Djassa_Main.this.getActionBar().setSelectedNavigationItem(paramAnonymousInt);
      }
    });
    ActionBar.TabListener local3 = new ActionBar.TabListener()
    {
      public void onTabReselected(ActionBar.Tab paramAnonymousTab, FragmentTransaction paramAnonymousFragmentTransaction) {}
      
      public void onTabSelected(ActionBar.Tab paramAnonymousTab, FragmentTransaction paramAnonymousFragmentTransaction)
      {
        Djassa_Main.this.viewPager.setCurrentItem(paramAnonymousTab.getPosition());
      }
      
      public void onTabUnselected(ActionBar.Tab paramAnonymousTab, FragmentTransaction paramAnonymousFragmentTransaction) {}
    };
    ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903044, null);
    this.Action_Bar.addTab(this.Action_Bar.newTab().setText("Offres ").setTabListener(local3));
    this.Action_Bar.addTab(this.Action_Bar.newTab().setText("Markets").setTabListener(local3));
    this.Action_Bar.addTab(this.Action_Bar.newTab().setText("Vers le Forums ").setTabListener(local3));
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    }
    while (this.mDrawerToggle.onOptionsItemSelected(paramMenuItem))
    {
      return true;
      this.mColectorPageAdaptor = new ColectorPageAdaptor(getSupportFragmentManager(), this);
      this.viewPager = ((ViewPager)findViewById(2131034174));
      this.viewPager.setAdapter(this.mColectorPageAdaptor);
      return true;
      Toast.makeText(getApplicationContext(), " Disponible en version pro ", 1).show();
      return true;
      Toast.makeText(getApplicationContext(), " Disponible en version pro ", 1).show();
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  protected void onPause()
  {
    super.onPause();
  }
  
  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mDrawerToggle.syncState();
  }
  
  protected void onRestart()
  {
    super.onRestart();
  }
  
  protected void onResume()
  {
    super.onResume();
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    getActionBar().setTitle(this.mTitle);
  }
  
  private class SlideMenuClickListener
    implements AdapterView.OnItemClickListener
  {
    public Context context;
    
    private SlideMenuClickListener() {}
    
    public void SetContext(Context paramContext)
    {
      this.context = paramContext;
    }
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      Djassa_Main.this.displayView(paramInt, this.context);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.Djassa_Main
 * JD-Core Version:    0.7.0.1
 */