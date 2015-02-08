package org.apache.http.impl.conn;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
class CPoolProxy
  implements InvocationHandler
{
  private static final Method CLOSE_METHOD;
  private static final Method IS_OPEN_METHOD;
  private static final Method IS_STALE_METHOD;
  private static final Method SHUTDOWN_METHOD;
  private volatile CPoolEntry poolEntry;
  
  static
  {
    try
    {
      CLOSE_METHOD = HttpConnection.class.getMethod("close", new Class[0]);
      SHUTDOWN_METHOD = HttpConnection.class.getMethod("shutdown", new Class[0]);
      IS_OPEN_METHOD = HttpConnection.class.getMethod("isOpen", new Class[0]);
      IS_STALE_METHOD = HttpConnection.class.getMethod("isStale", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new Error(localNoSuchMethodException);
    }
  }
  
  CPoolProxy(CPoolEntry paramCPoolEntry)
  {
    this.poolEntry = paramCPoolEntry;
  }
  
  public static CPoolEntry detach(HttpClientConnection paramHttpClientConnection)
  {
    return getHandler(paramHttpClientConnection).detach();
  }
  
  private static CPoolProxy getHandler(HttpClientConnection paramHttpClientConnection)
  {
    InvocationHandler localInvocationHandler = Proxy.getInvocationHandler(paramHttpClientConnection);
    if (!CPoolProxy.class.isInstance(localInvocationHandler)) {
      throw new IllegalStateException("Unexpected proxy handler class: " + localInvocationHandler);
    }
    return (CPoolProxy)CPoolProxy.class.cast(localInvocationHandler);
  }
  
  public static CPoolEntry getPoolEntry(HttpClientConnection paramHttpClientConnection)
  {
    CPoolEntry localCPoolEntry = getHandler(paramHttpClientConnection).getPoolEntry();
    if (localCPoolEntry == null) {
      throw new ConnectionShutdownException();
    }
    return localCPoolEntry;
  }
  
  public static HttpClientConnection newProxy(CPoolEntry paramCPoolEntry)
  {
    return (HttpClientConnection)Proxy.newProxyInstance(CPoolProxy.class.getClassLoader(), new Class[] { ManagedHttpClientConnection.class, HttpContext.class }, new CPoolProxy(paramCPoolEntry));
  }
  
  public void close()
    throws IOException
  {
    CPoolEntry localCPoolEntry = this.poolEntry;
    if (localCPoolEntry != null) {
      localCPoolEntry.closeConnection();
    }
  }
  
  CPoolEntry detach()
  {
    CPoolEntry localCPoolEntry = this.poolEntry;
    this.poolEntry = null;
    return localCPoolEntry;
  }
  
  HttpClientConnection getConnection()
  {
    CPoolEntry localCPoolEntry = this.poolEntry;
    if (localCPoolEntry == null) {
      return null;
    }
    return (HttpClientConnection)localCPoolEntry.getConnection();
  }
  
  CPoolEntry getPoolEntry()
  {
    return this.poolEntry;
  }
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    if (paramMethod.equals(CLOSE_METHOD))
    {
      close();
      return null;
    }
    if (paramMethod.equals(SHUTDOWN_METHOD))
    {
      shutdown();
      return null;
    }
    if (paramMethod.equals(IS_OPEN_METHOD)) {
      return Boolean.valueOf(isOpen());
    }
    if (paramMethod.equals(IS_STALE_METHOD)) {
      return Boolean.valueOf(isStale());
    }
    HttpClientConnection localHttpClientConnection = getConnection();
    if (localHttpClientConnection == null) {
      throw new ConnectionShutdownException();
    }
    try
    {
      Object localObject = paramMethod.invoke(localHttpClientConnection, paramArrayOfObject);
      return localObject;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      Throwable localThrowable = localInvocationTargetException.getCause();
      if (localThrowable != null) {
        throw localThrowable;
      }
      throw localInvocationTargetException;
    }
  }
  
  public boolean isOpen()
  {
    CPoolEntry localCPoolEntry = this.poolEntry;
    boolean bool1 = false;
    if (localCPoolEntry != null)
    {
      boolean bool2 = localCPoolEntry.isClosed();
      bool1 = false;
      if (!bool2) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public boolean isStale()
  {
    HttpClientConnection localHttpClientConnection = getConnection();
    if (localHttpClientConnection != null) {
      return localHttpClientConnection.isStale();
    }
    return true;
  }
  
  public void shutdown()
    throws IOException
  {
    CPoolEntry localCPoolEntry = this.poolEntry;
    if (localCPoolEntry != null) {
      localCPoolEntry.shutdownConnection();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.CPoolProxy
 * JD-Core Version:    0.7.0.1
 */