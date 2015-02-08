package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class LaxRedirectStrategy
  extends DefaultRedirectStrategy
{
  private static final String[] REDIRECT_METHODS = { "GET", "POST", "HEAD" };
  
  protected boolean isRedirectable(String paramString)
  {
    String[] arrayOfString = REDIRECT_METHODS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (arrayOfString[j].equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.LaxRedirectStrategy
 * JD-Core Version:    0.7.0.1
 */