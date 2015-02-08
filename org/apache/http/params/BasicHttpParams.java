package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class BasicHttpParams
  extends AbstractHttpParams
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = -7086398485908701455L;
  private final Map<String, Object> parameters = new ConcurrentHashMap();
  
  public void clear()
  {
    this.parameters.clear();
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    BasicHttpParams localBasicHttpParams = (BasicHttpParams)super.clone();
    copyParams(localBasicHttpParams);
    return localBasicHttpParams;
  }
  
  public HttpParams copy()
  {
    try
    {
      HttpParams localHttpParams = (HttpParams)clone();
      return localHttpParams;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new UnsupportedOperationException("Cloning not supported");
    }
  }
  
  public void copyParams(HttpParams paramHttpParams)
  {
    Iterator localIterator = this.parameters.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramHttpParams.setParameter((String)localEntry.getKey(), localEntry.getValue());
    }
  }
  
  public Set<String> getNames()
  {
    return new HashSet(this.parameters.keySet());
  }
  
  public Object getParameter(String paramString)
  {
    return this.parameters.get(paramString);
  }
  
  public boolean isParameterSet(String paramString)
  {
    return getParameter(paramString) != null;
  }
  
  public boolean isParameterSetLocally(String paramString)
  {
    return this.parameters.get(paramString) != null;
  }
  
  public boolean removeParameter(String paramString)
  {
    if (this.parameters.containsKey(paramString))
    {
      this.parameters.remove(paramString);
      return true;
    }
    return false;
  }
  
  public HttpParams setParameter(String paramString, Object paramObject)
  {
    if (paramString == null) {
      return this;
    }
    if (paramObject != null)
    {
      this.parameters.put(paramString, paramObject);
      return this;
    }
    this.parameters.remove(paramString);
    return this;
  }
  
  public void setParameters(String[] paramArrayOfString, Object paramObject)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      setParameter(paramArrayOfString[j], paramObject);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.params.BasicHttpParams
 * JD-Core Version:    0.7.0.1
 */