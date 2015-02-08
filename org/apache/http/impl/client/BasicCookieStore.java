package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;

@ThreadSafe
public class BasicCookieStore
  implements CookieStore, Serializable
{
  private static final long serialVersionUID = -7581093305228232025L;
  @GuardedBy("this")
  private final TreeSet<Cookie> cookies = new TreeSet(new CookieIdentityComparator());
  
  public void addCookie(Cookie paramCookie)
  {
    if (paramCookie != null) {}
    try
    {
      this.cookies.remove(paramCookie);
      if (!paramCookie.isExpired(new Date())) {
        this.cookies.add(paramCookie);
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void addCookies(Cookie[] paramArrayOfCookie)
  {
    if (paramArrayOfCookie != null) {}
    try
    {
      int i = paramArrayOfCookie.length;
      for (int j = 0; j < i; j++) {
        addCookie(paramArrayOfCookie[j]);
      }
      return;
    }
    finally {}
  }
  
  public void clear()
  {
    try
    {
      this.cookies.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  public boolean clearExpired(Date paramDate)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnonnull +9 -> 12
    //   6: iconst_0
    //   7: istore_2
    //   8: aload_0
    //   9: monitorexit
    //   10: iload_2
    //   11: ireturn
    //   12: iconst_0
    //   13: istore_2
    //   14: aload_0
    //   15: getfield 33	org/apache/http/impl/client/BasicCookieStore:cookies	Ljava/util/TreeSet;
    //   18: invokevirtual 63	java/util/TreeSet:iterator	()Ljava/util/Iterator;
    //   21: astore 4
    //   23: aload 4
    //   25: invokeinterface 69 1 0
    //   30: ifeq -22 -> 8
    //   33: aload 4
    //   35: invokeinterface 73 1 0
    //   40: checkcast 44	org/apache/http/cookie/Cookie
    //   43: aload_1
    //   44: invokeinterface 48 2 0
    //   49: ifeq -26 -> 23
    //   52: aload 4
    //   54: invokeinterface 75 1 0
    //   59: iconst_1
    //   60: istore_2
    //   61: goto -38 -> 23
    //   64: astore_3
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	69	0	this	BasicCookieStore
    //   0	69	1	paramDate	Date
    //   7	54	2	bool	boolean
    //   64	4	3	localObject	Object
    //   21	32	4	localIterator	java.util.Iterator
    // Exception table:
    //   from	to	target	type
    //   14	23	64	finally
    //   23	59	64	finally
  }
  
  public List<Cookie> getCookies()
  {
    try
    {
      ArrayList localArrayList = new ArrayList(this.cookies);
      return localArrayList;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public String toString()
  {
    try
    {
      String str = this.cookies.toString();
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.BasicCookieStore
 * JD-Core Version:    0.7.0.1
 */