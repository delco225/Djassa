package org.apache.http.pool;

public abstract interface PoolEntryCallback<T, C>
{
  public abstract void process(PoolEntry<T, C> paramPoolEntry);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.pool.PoolEntryCallback
 * JD-Core Version:    0.7.0.1
 */