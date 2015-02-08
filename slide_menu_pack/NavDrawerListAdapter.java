package slide_menu_pack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class NavDrawerListAdapter
  extends BaseAdapter
{
  private Context context;
  private ArrayList<NavDrawerItem> navDrawerItems;
  
  public NavDrawerListAdapter(Context paramContext, ArrayList<NavDrawerItem> paramArrayList)
  {
    this.context = paramContext;
    this.navDrawerItems = paramArrayList;
  }
  
  public int getCount()
  {
    return this.navDrawerItems.size();
  }
  
  public Object getItem(int paramInt)
  {
    return this.navDrawerItems.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903069, null);
    }
    ImageView localImageView = (ImageView)paramView.findViewById(2131034154);
    TextView localTextView1 = (TextView)paramView.findViewById(2131034155);
    TextView localTextView2 = (TextView)paramView.findViewById(2131034195);
    localImageView.setImageResource(((NavDrawerItem)this.navDrawerItems.get(paramInt)).getIcon());
    localTextView1.setText(((NavDrawerItem)this.navDrawerItems.get(paramInt)).getTitle());
    if (((NavDrawerItem)this.navDrawerItems.get(paramInt)).getCounterVisibility())
    {
      localTextView2.setText(((NavDrawerItem)this.navDrawerItems.get(paramInt)).getCount());
      return paramView;
    }
    localTextView2.setVisibility(8);
    return paramView;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     slide_menu_pack.NavDrawerListAdapter
 * JD-Core Version:    0.7.0.1
 */