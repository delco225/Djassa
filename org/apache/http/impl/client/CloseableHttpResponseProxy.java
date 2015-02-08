package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
class CloseableHttpResponseProxy
  implements InvocationHandler
{
  private final HttpResponse original;
  
  CloseableHttpResponseProxy(HttpResponse paramHttpResponse)
  {
    this.original = paramHttpResponse;
  }
  
  public static CloseableHttpResponse newProxy(HttpResponse paramHttpResponse)
  {
    return (CloseableHttpResponse)Proxy.newProxyInstance(CloseableHttpResponseProxy.class.getClassLoader(), new Class[] { CloseableHttpResponse.class }, new CloseableHttpResponseProxy(paramHttpResponse));
  }
  
  public void close()
    throws IOException
  {
    EntityUtils.consume(this.original.getEntity());
  }
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    if (paramMethod.getName().equals("close"))
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
 * Qualified Name:     org.apache.http.impl.client.CloseableHttpResponseProxy
 * JD-Core Version:    0.7.0.1
 */