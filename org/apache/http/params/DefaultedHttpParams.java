package org.apache.http.params;

import java.util.HashSet;
import java.util.Set;
import org.apache.http.util.Args;

@Deprecated
public final class DefaultedHttpParams
  extends AbstractHttpParams
{
  private final HttpParams defaults;
  private final HttpParams local;
  
  public DefaultedHttpParams(HttpParams paramHttpParams1, HttpParams paramHttpParams2)
  {
    this.local = ((HttpParams)Args.notNull(paramHttpParams1, "Local HTTP parameters"));
    this.defaults = paramHttpParams2;
  }
  
  private Set<String> getNames(HttpParams paramHttpParams)
  {
    if ((paramHttpParams instanceof HttpParamsNames)) {
      return ((HttpParamsNames)paramHttpParams).getNames();
    }
    throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
  }
  
  public HttpParams copy()
  {
    return new DefaultedHttpParams(this.local.copy(), this.defaults);
  }
  
  public Set<String> getDefaultNames()
  {
    return new HashSet(getNames(this.defaults));
  }
  
  public HttpParams getDefaults()
  {
    return this.defaults;
  }
  
  public Set<String> getLocalNames()
  {
    return new HashSet(getNames(this.local));
  }
  
  public Set<String> getNames()
  {
    HashSet localHashSet = new HashSet(getNames(this.defaults));
    localHashSet.addAll(getNames(this.local));
    return localHashSet;
  }
  
  public Object getParameter(String paramString)
  {
    Object localObject = this.local.getParameter(paramString);
    if ((localObject == null) && (this.defaults != null)) {
      localObject = this.defaults.getParameter(paramString);
    }
    return localObject;
  }
  
  public boolean removeParameter(String paramString)
  {
    return this.local.removeParameter(paramString);
  }
  
  public HttpParams setParameter(String paramString, Object paramObject)
  {
    return this.local.setParameter(paramString, paramObject);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.params.DefaultedHttpParams
 * JD-Core Version:    0.7.0.1
 */