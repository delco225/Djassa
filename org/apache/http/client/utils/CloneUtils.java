package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
public class CloneUtils
{
  public static Object clone(Object paramObject)
    throws CloneNotSupportedException
  {
    return cloneObject(paramObject);
  }
  
  public static <T> T cloneObject(T paramT)
    throws CloneNotSupportedException
  {
    if (paramT == null) {
      return null;
    }
    if ((paramT instanceof Cloneable))
    {
      Class localClass = paramT.getClass();
      try
      {
        Method localMethod = localClass.getMethod("clone", (Class[])null);
        Object localObject;
        Throwable localThrowable;
        throw new CloneNotSupportedException();
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        try
        {
          localObject = localMethod.invoke(paramT, (Object[])null);
          return localObject;
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          localThrowable = localInvocationTargetException.getCause();
          if (!(localThrowable instanceof CloneNotSupportedException)) {
            break label79;
          }
          throw ((CloneNotSupportedException)localThrowable);
          throw new Error("Unexpected exception", localThrowable);
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          throw new IllegalAccessError(localIllegalAccessException.getMessage());
        }
        localNoSuchMethodException = localNoSuchMethodException;
        throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.utils.CloneUtils
 * JD-Core Version:    0.7.0.1
 */