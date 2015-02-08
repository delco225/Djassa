package org.apache.http.conn;

import org.apache.http.HttpHost;

public abstract interface SchemePortResolver
{
  public abstract int resolve(HttpHost paramHttpHost)
    throws UnsupportedSchemeException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.SchemePortResolver
 * JD-Core Version:    0.7.0.1
 */