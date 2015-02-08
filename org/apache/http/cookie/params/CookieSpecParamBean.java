package org.apache.http.cookie.params;

import java.util.Collection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
@NotThreadSafe
public class CookieSpecParamBean
  extends HttpAbstractParamBean
{
  public CookieSpecParamBean(HttpParams paramHttpParams)
  {
    super(paramHttpParams);
  }
  
  public void setDatePatterns(Collection<String> paramCollection)
  {
    this.params.setParameter("http.protocol.cookie-datepatterns", paramCollection);
  }
  
  public void setSingleHeader(boolean paramBoolean)
  {
    this.params.setBooleanParameter("http.protocol.single-cookie-header", paramBoolean);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.params.CookieSpecParamBean
 * JD-Core Version:    0.7.0.1
 */