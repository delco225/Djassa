package org.apache.http.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class BasicHttpContext
  implements HttpContext
{
  private final Map<String, Object> map = new ConcurrentHashMap();
  private final HttpContext parentContext;
  
  public BasicHttpContext()
  {
    this(null);
  }
  
  public BasicHttpContext(HttpContext paramHttpContext)
  {
    this.parentContext = paramHttpContext;
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public Object getAttribute(String paramString)
  {
    Args.notNull(paramString, "Id");
    Object localObject = this.map.get(paramString);
    if ((localObject == null) && (this.parentContext != null)) {
      localObject = this.parentContext.getAttribute(paramString);
    }
    return localObject;
  }
  
  public Object removeAttribute(String paramString)
  {
    Args.notNull(paramString, "Id");
    return this.map.remove(paramString);
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    Args.notNull(paramString, "Id");
    if (paramObject != null)
    {
      this.map.put(paramString, paramObject);
      return;
    }
    this.map.remove(paramString);
  }
  
  public String toString()
  {
    return this.map.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.BasicHttpContext
 * JD-Core Version:    0.7.0.1
 */