package org.apache.http.cookie;

import java.util.Date;

public abstract interface SetCookie
  extends Cookie
{
  public abstract void setComment(String paramString);
  
  public abstract void setDomain(String paramString);
  
  public abstract void setExpiryDate(Date paramDate);
  
  public abstract void setPath(String paramString);
  
  public abstract void setSecure(boolean paramBoolean);
  
  public abstract void setValue(String paramString);
  
  public abstract void setVersion(int paramInt);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.SetCookie
 * JD-Core Version:    0.7.0.1
 */