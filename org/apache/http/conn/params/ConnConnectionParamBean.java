package org.apache.http.conn.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
public class ConnConnectionParamBean
  extends HttpAbstractParamBean
{
  public ConnConnectionParamBean(HttpParams paramHttpParams)
  {
    super(paramHttpParams);
  }
  
  @Deprecated
  public void setMaxStatusLineGarbage(int paramInt)
  {
    this.params.setIntParameter("http.connection.max-status-line-garbage", paramInt);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.params.ConnConnectionParamBean
 * JD-Core Version:    0.7.0.1
 */