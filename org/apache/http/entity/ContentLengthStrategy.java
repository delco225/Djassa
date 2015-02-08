package org.apache.http.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public abstract interface ContentLengthStrategy
{
  public static final int CHUNKED = -2;
  public static final int IDENTITY = -1;
  
  public abstract long determineLength(HttpMessage paramHttpMessage)
    throws HttpException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.ContentLengthStrategy
 * JD-Core Version:    0.7.0.1
 */