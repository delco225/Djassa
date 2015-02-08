package org.apache.http.protocol;

@Deprecated
public class SyncBasicHttpContext
  extends BasicHttpContext
{
  public SyncBasicHttpContext() {}
  
  public SyncBasicHttpContext(HttpContext paramHttpContext)
  {
    super(paramHttpContext);
  }
  
  public void clear()
  {
    try
    {
      super.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Object getAttribute(String paramString)
  {
    try
    {
      Object localObject2 = super.getAttribute(paramString);
      return localObject2;
    }
    finally
    {
      localObject1 = finally;
      throw localObject1;
    }
  }
  
  public Object removeAttribute(String paramString)
  {
    try
    {
      Object localObject2 = super.removeAttribute(paramString);
      return localObject2;
    }
    finally
    {
      localObject1 = finally;
      throw localObject1;
    }
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    try
    {
      super.setAttribute(paramString, paramObject);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.SyncBasicHttpContext
 * JD-Core Version:    0.7.0.1
 */