package org.apache.http.params;

import org.apache.http.util.Args;

@Deprecated
public abstract class HttpAbstractParamBean
{
  protected final HttpParams params;
  
  public HttpAbstractParamBean(HttpParams paramHttpParams)
  {
    this.params = ((HttpParams)Args.notNull(paramHttpParams, "HTTP parameters"));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.params.HttpAbstractParamBean
 * JD-Core Version:    0.7.0.1
 */