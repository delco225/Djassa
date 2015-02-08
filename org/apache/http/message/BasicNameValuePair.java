package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public class BasicNameValuePair
  implements NameValuePair, Cloneable, Serializable
{
  private static final long serialVersionUID = -6437800749411518984L;
  private final String name;
  private final String value;
  
  public BasicNameValuePair(String paramString1, String paramString2)
  {
    this.name = ((String)Args.notNull(paramString1, "Name"));
    this.value = paramString2;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    BasicNameValuePair localBasicNameValuePair;
    do
    {
      return true;
      if (!(paramObject instanceof NameValuePair)) {
        break;
      }
      localBasicNameValuePair = (BasicNameValuePair)paramObject;
    } while ((this.name.equals(localBasicNameValuePair.name)) && (LangUtils.equals(this.value, localBasicNameValuePair.value)));
    return false;
    return false;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public int hashCode()
  {
    return LangUtils.hashCode(LangUtils.hashCode(17, this.name), this.value);
  }
  
  public String toString()
  {
    if (this.value == null) {
      return this.name;
    }
    StringBuilder localStringBuilder = new StringBuilder(1 + this.name.length() + this.value.length());
    localStringBuilder.append(this.name);
    localStringBuilder.append("=");
    localStringBuilder.append(this.value);
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicNameValuePair
 * JD-Core Version:    0.7.0.1
 */