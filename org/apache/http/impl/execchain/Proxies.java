package org.apache.http.impl.execchain;

import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;

@NotThreadSafe
class Proxies
{
  static void enhanceEntity(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
  {
    HttpEntity localHttpEntity = paramHttpEntityEnclosingRequest.getEntity();
    if ((localHttpEntity != null) && (!localHttpEntity.isRepeatable()) && (!isEnhanced(localHttpEntity))) {
      paramHttpEntityEnclosingRequest.setEntity((HttpEntity)Proxy.newProxyInstance(HttpEntity.class.getClassLoader(), new Class[] { HttpEntity.class }, new RequestEntityExecHandler(localHttpEntity)));
    }
  }
  
  public static CloseableHttpResponse enhanceResponse(HttpResponse paramHttpResponse, ConnectionHolder paramConnectionHolder)
  {
    return (CloseableHttpResponse)Proxy.newProxyInstance(ResponseProxyHandler.class.getClassLoader(), new Class[] { CloseableHttpResponse.class }, new ResponseProxyHandler(paramHttpResponse, paramConnectionHolder));
  }
  
  static boolean isEnhanced(HttpEntity paramHttpEntity)
  {
    if ((paramHttpEntity != null) && (Proxy.isProxyClass(paramHttpEntity.getClass()))) {
      return Proxy.getInvocationHandler(paramHttpEntity) instanceof RequestEntityExecHandler;
    }
    return false;
  }
  
  static boolean isRepeatable(HttpRequest paramHttpRequest)
  {
    HttpEntity localHttpEntity;
    if ((paramHttpRequest instanceof HttpEntityEnclosingRequest))
    {
      localHttpEntity = ((HttpEntityEnclosingRequest)paramHttpRequest).getEntity();
      if ((localHttpEntity != null) && ((!isEnhanced(localHttpEntity)) || (((RequestEntityExecHandler)Proxy.getInvocationHandler(localHttpEntity)).isConsumed()))) {}
    }
    else
    {
      return true;
    }
    return localHttpEntity.isRepeatable();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.Proxies
 * JD-Core Version:    0.7.0.1
 */