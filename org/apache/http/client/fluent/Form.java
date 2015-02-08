package org.apache.http.client.fluent;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Form
{
  private final List<NameValuePair> params = new ArrayList();
  
  public static Form form()
  {
    return new Form();
  }
  
  public Form add(String paramString1, String paramString2)
  {
    this.params.add(new BasicNameValuePair(paramString1, paramString2));
    return this;
  }
  
  public List<NameValuePair> build()
  {
    return new ArrayList(this.params);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Form
 * JD-Core Version:    0.7.0.1
 */