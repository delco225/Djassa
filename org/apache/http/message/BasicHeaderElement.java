package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public class BasicHeaderElement
  implements HeaderElement, Cloneable
{
  private final String name;
  private final NameValuePair[] parameters;
  private final String value;
  
  public BasicHeaderElement(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, null);
  }
  
  public BasicHeaderElement(String paramString1, String paramString2, NameValuePair[] paramArrayOfNameValuePair)
  {
    this.name = ((String)Args.notNull(paramString1, "Name"));
    this.value = paramString2;
    if (paramArrayOfNameValuePair != null)
    {
      this.parameters = paramArrayOfNameValuePair;
      return;
    }
    this.parameters = new NameValuePair[0];
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    BasicHeaderElement localBasicHeaderElement;
    do
    {
      return true;
      if (!(paramObject instanceof HeaderElement)) {
        break;
      }
      localBasicHeaderElement = (BasicHeaderElement)paramObject;
    } while ((this.name.equals(localBasicHeaderElement.name)) && (LangUtils.equals(this.value, localBasicHeaderElement.value)) && (LangUtils.equals(this.parameters, localBasicHeaderElement.parameters)));
    return false;
    return false;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public NameValuePair getParameter(int paramInt)
  {
    return this.parameters[paramInt];
  }
  
  public NameValuePair getParameterByName(String paramString)
  {
    Args.notNull(paramString, "Name");
    NameValuePair[] arrayOfNameValuePair = this.parameters;
    int i = arrayOfNameValuePair.length;
    for (int j = 0;; j++)
    {
      Object localObject = null;
      if (j < i)
      {
        NameValuePair localNameValuePair = arrayOfNameValuePair[j];
        if (localNameValuePair.getName().equalsIgnoreCase(paramString)) {
          localObject = localNameValuePair;
        }
      }
      else
      {
        return localObject;
      }
    }
  }
  
  public int getParameterCount()
  {
    return this.parameters.length;
  }
  
  public NameValuePair[] getParameters()
  {
    return (NameValuePair[])this.parameters.clone();
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public int hashCode()
  {
    int i = LangUtils.hashCode(LangUtils.hashCode(17, this.name), this.value);
    NameValuePair[] arrayOfNameValuePair = this.parameters;
    int j = arrayOfNameValuePair.length;
    for (int k = 0; k < j; k++) {
      i = LangUtils.hashCode(i, arrayOfNameValuePair[k]);
    }
    return i;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.name);
    if (this.value != null)
    {
      localStringBuilder.append("=");
      localStringBuilder.append(this.value);
    }
    for (NameValuePair localNameValuePair : this.parameters)
    {
      localStringBuilder.append("; ");
      localStringBuilder.append(localNameValuePair);
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderElement
 * JD-Core Version:    0.7.0.1
 */