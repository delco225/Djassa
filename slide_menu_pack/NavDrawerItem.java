package slide_menu_pack;

public class NavDrawerItem
{
  private String count = "0";
  private int icon;
  private boolean isCounterVisible = false;
  private String title;
  
  public NavDrawerItem() {}
  
  public NavDrawerItem(String paramString, int paramInt)
  {
    this.title = paramString;
    this.icon = paramInt;
  }
  
  public NavDrawerItem(String paramString1, int paramInt, boolean paramBoolean, String paramString2)
  {
    this.title = paramString1;
    this.icon = paramInt;
    this.isCounterVisible = paramBoolean;
    this.count = paramString2;
  }
  
  public String getCount()
  {
    return this.count;
  }
  
  public boolean getCounterVisibility()
  {
    return this.isCounterVisible;
  }
  
  public int getIcon()
  {
    return this.icon;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setCount(String paramString)
  {
    this.count = paramString;
  }
  
  public void setCounterVisibility(boolean paramBoolean)
  {
    this.isCounterVisible = paramBoolean;
  }
  
  public void setIcon(int paramInt)
  {
    this.icon = paramInt;
  }
  
  public void setTitle(String paramString)
  {
    this.title = paramString;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     slide_menu_pack.NavDrawerItem
 * JD-Core Version:    0.7.0.1
 */