package org.apache.http.pool;

import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;

public abstract interface ConnPool<T, E>
{
  public abstract Future<E> lease(T paramT, Object paramObject, FutureCallback<E> paramFutureCallback);
  
  public abstract void release(E paramE, boolean paramBoolean);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.pool.ConnPool
 * JD-Core Version:    0.7.0.1
 */