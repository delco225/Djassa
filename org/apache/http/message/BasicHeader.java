package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeader
  implements Header, Cloneable, Serializable
{
  private static final long serialVersionUID = -5427236326487562174L;
  private final String name;
  private final String value;
  
  public BasicHeader(String paramString1, String paramString2)
  {
    this.name = ((String)Args.notNull(paramString1, "Name"));
    this.value = paramString2;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public HeaderElement[] getElements()
    throws ParseException
  {
    if (this.value != null) {
      return BasicHeaderValueParser.parseElements(this.value, null);
    }
    return new HeaderElement[0];
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public String toString()
  {
    return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicHeader
 * JD-Core Version:    0.7.0.1
 */