package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class ResponseProxyHandler
  implements InvocationHandler
{
  private static final Method CLOSE_METHOD;
  private final ConnectionHolder connHolder;
  private final HttpResponse original;
  
  static
  {
    try
    {
      CLOSE_METHOD = Closeable.class.getMethod("close", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new Error(localNoSuchMethodException);
    }
  }
  
  ResponseProxyHandler(HttpResponse paramHttpResponse, ConnectionHolder paramConnectionHolder)
  {
    this.original = paramHttpResponse;
    this.connHolder = paramConnectionHolder;
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    if ((localHttpEntity != null) && (localHttpEntity.isStreaming()) && (paramConnectionHolder != null)) {
      this.original.setEntity(new ResponseEntityWrapper(localHttpEntity, paramConnectionHolder));
    }
  }
  
  public void close()
    throws IOException
  {
    if (this.connHolder != null) {
      this.connHolder.abortConnection();
    }
  }
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    if (paramMethod.equals(CLOSE_METHOD))
    {
      close();
      return null;
    }
    try
    {
      Object localObject = paramMethod.invoke(this.original, paramArrayOfObject);
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
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.ResponseProxyHandler
 * JD-Core Version:    0.7.0.1
 */