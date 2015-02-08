package org.apache.http.pool;

import java.io.IOException;

public abstract interface ConnFactory<T, C>
{
  public abstract C create(T paramT)
    throws IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.pool.ConnFactory
 * JD-Core Version:    0.7.0.1
 */