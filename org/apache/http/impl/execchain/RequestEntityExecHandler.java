package org.apache.http.impl.execchain;

import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class RequestEntityExecHandler
  implements InvocationHandler
{
  private static final Method WRITE_TO_METHOD;
  private boolean consumed = false;
  private final HttpEntity original;
  
  static
  {
    try
    {
      WRITE_TO_METHOD = HttpEntity.class.getMethod("writeTo", new Class[] { OutputStream.class });
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new Error(localNoSuchMethodException);
    }
  }
  
  RequestEntityExecHandler(HttpEntity paramHttpEntity)
  {
    this.original = paramHttpEntity;
  }
  
  public HttpEntity getOriginal()
  {
    return this.original;
  }
  
  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    try
    {
      if (paramMethod.equals(WRITE_TO_METHOD)) {
        this.consumed = true;
      }
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
  
  public boolean isConsumed()
  {
    return this.consumed;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.RequestEntityExecHandler
 * JD-Core Version:    0.7.0.1
 */