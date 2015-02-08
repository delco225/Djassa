package org.apache.http.impl.client;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class ClientParamsStack
  extends AbstractHttpParams
{
  protected final HttpParams applicationParams;
  protected final HttpParams clientParams;
  protected final HttpParams overrideParams;
  protected final HttpParams requestParams;
  
  public ClientParamsStack(ClientParamsStack paramClientParamsStack)
  {
    this(paramClientParamsStack.getApplicationParams(), paramClientParamsStack.getClientParams(), paramClientParamsStack.getRequestParams(), paramClientParamsStack.getOverrideParams());
  }
  
  public ClientParamsStack(ClientParamsStack paramClientParamsStack, HttpParams paramHttpParams1, HttpParams paramHttpParams2, HttpParams paramHttpParams3, HttpParams paramHttpParams4) {}
  
  public ClientParamsStack(HttpParams paramHttpParams1, HttpParams paramHttpParams2, HttpParams paramHttpParams3, HttpParams paramHttpParams4)
  {
    this.applicationParams = paramHttpParams1;
    this.clientParams = paramHttpParams2;
    this.requestParams = paramHttpParams3;
    this.overrideParams = paramHttpParams4;
  }
  
  public HttpParams copy()
  {
    return this;
  }
  
  public final HttpParams getApplicationParams()
  {
    return this.applicationParams;
  }
  
  public final HttpParams getClientParams()
  {
    return this.clientParams;
  }
  
  public final HttpParams getOverrideParams()
  {
    return this.overrideParams;
  }
  
  public Object getParameter(String paramString)
  {
    Args.notNull(paramString, "Parameter name");
    HttpParams localHttpParams = this.overrideParams;
    Object localObject = null;
    if (localHttpParams != null) {
      localObject = this.overrideParams.getParameter(paramString);
    }
    if ((localObject == null) && (this.requestParams != null)) {
      localObject = this.requestParams.getParameter(paramString);
    }
    if ((localObject == null) && (this.clientParams != null)) {
      localObject = this.clientParams.getParameter(paramString);
    }
    if ((localObject == null) && (this.applicationParams != null)) {
      localObject = this.applicationParams.getParameter(paramString);
    }
    return localObject;
  }
  
  public final HttpParams getRequestParams()
  {
    return this.requestParams;
  }
  
  public boolean removeParameter(String paramString)
  {
    throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
  }
  
  public HttpParams setParameter(String paramString, Object paramObject)
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.ClientParamsStack
 * JD-Core Version:    0.7.0.1
 */