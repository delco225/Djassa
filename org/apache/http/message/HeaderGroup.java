package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class HeaderGroup
  implements Cloneable, Serializable
{
  private static final long serialVersionUID = 2608834160639271617L;
  private final List<Header> headers = new ArrayList(16);
  
  public void addHeader(Header paramHeader)
  {
    if (paramHeader == null) {
      return;
    }
    this.headers.add(paramHeader);
  }
  
  public void clear()
  {
    this.headers.clear();
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public boolean containsHeader(String paramString)
  {
    for (int i = 0; i < this.headers.size(); i++) {
      if (((Header)this.headers.get(i)).getName().equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public HeaderGroup copy()
  {
    HeaderGroup localHeaderGroup = new HeaderGroup();
    localHeaderGroup.headers.addAll(this.headers);
    return localHeaderGroup;
  }
  
  public Header[] getAllHeaders()
  {
    return (Header[])this.headers.toArray(new Header[this.headers.size()]);
  }
  
  public Header getCondensedHeader(String paramString)
  {
    Header[] arrayOfHeader = getHeaders(paramString);
    if (arrayOfHeader.length == 0) {
      return null;
    }
    if (arrayOfHeader.length == 1) {
      return arrayOfHeader[0];
    }
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(128);
    localCharArrayBuffer.append(arrayOfHeader[0].getValue());
    for (int i = 1; i < arrayOfHeader.length; i++)
    {
      localCharArrayBuffer.append(", ");
      localCharArrayBuffer.append(arrayOfHeader[i].getValue());
    }
    return new BasicHeader(paramString.toLowerCase(Locale.ENGLISH), localCharArrayBuffer.toString());
  }
  
  public Header getFirstHeader(String paramString)
  {
    for (int i = 0; i < this.headers.size(); i++)
    {
      Header localHeader = (Header)this.headers.get(i);
      if (localHeader.getName().equalsIgnoreCase(paramString)) {
        return localHeader;
      }
    }
    return null;
  }
  
  public Header[] getHeaders(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < this.headers.size(); i++)
    {
      Header localHeader = (Header)this.headers.get(i);
      if (localHeader.getName().equalsIgnoreCase(paramString)) {
        localArrayList.add(localHeader);
      }
    }
    return (Header[])localArrayList.toArray(new Header[localArrayList.size()]);
  }
  
  public Header getLastHeader(String paramString)
  {
    for (int i = -1 + this.headers.size(); i >= 0; i--)
    {
      Header localHeader = (Header)this.headers.get(i);
      if (localHeader.getName().equalsIgnoreCase(paramString)) {
        return localHeader;
      }
    }
    return null;
  }
  
  public HeaderIterator iterator()
  {
    return new BasicListHeaderIterator(this.headers, null);
  }
  
  public HeaderIterator iterator(String paramString)
  {
    return new BasicListHeaderIterator(this.headers, paramString);
  }
  
  public void removeHeader(Header paramHeader)
  {
    if (paramHeader == null) {
      return;
    }
    this.headers.remove(paramHeader);
  }
  
  public void setHeaders(Header[] paramArrayOfHeader)
  {
    clear();
    if (paramArrayOfHeader == null) {
      return;
    }
    Collections.addAll(this.headers, paramArrayOfHeader);
  }
  
  public String toString()
  {
    return this.headers.toString();
  }
  
  public void updateHeader(Header paramHeader)
  {
    if (paramHeader == null) {
      return;
    }
    for (int i = 0; i < this.headers.size(); i++) {
      if (((Header)this.headers.get(i)).getName().equalsIgnoreCase(paramHeader.getName()))
      {
        this.headers.set(i, paramHeader);
        return;
      }
    }
    this.headers.add(paramHeader);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.HeaderGroup
 * JD-Core Version:    0.7.0.1
 */