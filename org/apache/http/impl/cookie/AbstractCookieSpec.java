package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.util.Args;

@NotThreadSafe
public abstract class AbstractCookieSpec
  implements CookieSpec
{
  private final Map<String, CookieAttributeHandler> attribHandlerMap = new HashMap(10);
  
  protected CookieAttributeHandler findAttribHandler(String paramString)
  {
    return (CookieAttributeHandler)this.attribHandlerMap.get(paramString);
  }
  
  protected CookieAttributeHandler getAttribHandler(String paramString)
  {
    CookieAttributeHandler localCookieAttributeHandler = findAttribHandler(paramString);
    if (localCookieAttributeHandler == null) {
      throw new IllegalStateException("Handler not registered for " + paramString + " attribute.");
    }
    return localCookieAttributeHandler;
  }
  
  protected Collection<CookieAttributeHandler> getAttribHandlers()
  {
    return this.attribHandlerMap.values();
  }
  
  public void registerAttribHandler(String paramString, CookieAttributeHandler paramCookieAttributeHandler)
  {
    Args.notNull(paramString, "Attribute name");
    Args.notNull(paramCookieAttributeHandler, "Attribute handler");
    this.attribHandlerMap.put(paramString, paramCookieAttributeHandler);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.AbstractCookieSpec
 * JD-Core Version:    0.7.0.1
 */