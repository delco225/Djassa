package org.apache.http.impl.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@ThreadSafe
public class BasicCredentialsProvider
  implements CredentialsProvider
{
  private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap();
  
  private static Credentials matchCredentials(Map<AuthScope, Credentials> paramMap, AuthScope paramAuthScope)
  {
    Credentials localCredentials = (Credentials)paramMap.get(paramAuthScope);
    if (localCredentials == null)
    {
      int i = -1;
      Object localObject = null;
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        AuthScope localAuthScope = (AuthScope)localIterator.next();
        int j = paramAuthScope.match(localAuthScope);
        if (j > i)
        {
          i = j;
          localObject = localAuthScope;
        }
      }
      if (localObject != null) {
        localCredentials = (Credentials)paramMap.get(localObject);
      }
    }
    return localCredentials;
  }
  
  public void clear()
  {
    this.credMap.clear();
  }
  
  public Credentials getCredentials(AuthScope paramAuthScope)
  {
    Args.notNull(paramAuthScope, "Authentication scope");
    return matchCredentials(this.credMap, paramAuthScope);
  }
  
  public void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials)
  {
    Args.notNull(paramAuthScope, "Authentication scope");
    this.credMap.put(paramAuthScope, paramCredentials);
  }
  
  public String toString()
  {
    return this.credMap.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.BasicCredentialsProvider
 * JD-Core Version:    0.7.0.1
 */