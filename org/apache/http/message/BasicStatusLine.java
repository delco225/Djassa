package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicStatusLine
  implements StatusLine, Cloneable, Serializable
{
  private static final long serialVersionUID = -2443303766890459269L;
  private final ProtocolVersion protoVersion;
  private final String reasonPhrase;
  private final int statusCode;
  
  public BasicStatusLine(ProtocolVersion paramProtocolVersion, int paramInt, String paramString)
  {
    this.protoVersion = ((ProtocolVersion)Args.notNull(paramProtocolVersion, "Version"));
    this.statusCode = Args.notNegative(paramInt, "Status code");
    this.reasonPhrase = paramString;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    return this.protoVersion;
  }
  
  public String getReasonPhrase()
  {
    return this.reasonPhrase;
  }
  
  public int getStatusCode()
  {
    return this.statusCode;
  }
  
  public String toString()
  {
    return BasicLineFormatter.INSTANCE.formatStatusLine(null, this).toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicStatusLine
 * JD-Core Version:    0.7.0.1
 */