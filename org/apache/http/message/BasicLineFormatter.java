package org.apache.http.message;

import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicLineFormatter
  implements LineFormatter
{
  @Deprecated
  public static final BasicLineFormatter DEFAULT = new BasicLineFormatter();
  public static final BasicLineFormatter INSTANCE = new BasicLineFormatter();
  
  public static String formatHeader(Header paramHeader, LineFormatter paramLineFormatter)
  {
    if (paramLineFormatter != null) {}
    for (;;)
    {
      return paramLineFormatter.formatHeader(null, paramHeader).toString();
      paramLineFormatter = INSTANCE;
    }
  }
  
  public static String formatProtocolVersion(ProtocolVersion paramProtocolVersion, LineFormatter paramLineFormatter)
  {
    if (paramLineFormatter != null) {}
    for (;;)
    {
      return paramLineFormatter.appendProtocolVersion(null, paramProtocolVersion).toString();
      paramLineFormatter = INSTANCE;
    }
  }
  
  public static String formatRequestLine(RequestLine paramRequestLine, LineFormatter paramLineFormatter)
  {
    if (paramLineFormatter != null) {}
    for (;;)
    {
      return paramLineFormatter.formatRequestLine(null, paramRequestLine).toString();
      paramLineFormatter = INSTANCE;
    }
  }
  
  public static String formatStatusLine(StatusLine paramStatusLine, LineFormatter paramLineFormatter)
  {
    if (paramLineFormatter != null) {}
    for (;;)
    {
      return paramLineFormatter.formatStatusLine(null, paramStatusLine).toString();
      paramLineFormatter = INSTANCE;
    }
  }
  
  public CharArrayBuffer appendProtocolVersion(CharArrayBuffer paramCharArrayBuffer, ProtocolVersion paramProtocolVersion)
  {
    Args.notNull(paramProtocolVersion, "Protocol version");
    CharArrayBuffer localCharArrayBuffer = paramCharArrayBuffer;
    int i = estimateProtocolVersionLen(paramProtocolVersion);
    if (localCharArrayBuffer == null) {
      localCharArrayBuffer = new CharArrayBuffer(i);
    }
    for (;;)
    {
      localCharArrayBuffer.append(paramProtocolVersion.getProtocol());
      localCharArrayBuffer.append('/');
      localCharArrayBuffer.append(Integer.toString(paramProtocolVersion.getMajor()));
      localCharArrayBuffer.append('.');
      localCharArrayBuffer.append(Integer.toString(paramProtocolVersion.getMinor()));
      return localCharArrayBuffer;
      localCharArrayBuffer.ensureCapacity(i);
    }
  }
  
  protected void doFormatHeader(CharArrayBuffer paramCharArrayBuffer, Header paramHeader)
  {
    String str1 = paramHeader.getName();
    String str2 = paramHeader.getValue();
    int i = 2 + str1.length();
    if (str2 != null) {
      i += str2.length();
    }
    paramCharArrayBuffer.ensureCapacity(i);
    paramCharArrayBuffer.append(str1);
    paramCharArrayBuffer.append(": ");
    if (str2 != null) {
      paramCharArrayBuffer.append(str2);
    }
  }
  
  protected void doFormatRequestLine(CharArrayBuffer paramCharArrayBuffer, RequestLine paramRequestLine)
  {
    String str1 = paramRequestLine.getMethod();
    String str2 = paramRequestLine.getUri();
    paramCharArrayBuffer.ensureCapacity(1 + (1 + str1.length() + str2.length()) + estimateProtocolVersionLen(paramRequestLine.getProtocolVersion()));
    paramCharArrayBuffer.append(str1);
    paramCharArrayBuffer.append(' ');
    paramCharArrayBuffer.append(str2);
    paramCharArrayBuffer.append(' ');
    appendProtocolVersion(paramCharArrayBuffer, paramRequestLine.getProtocolVersion());
  }
  
  protected void doFormatStatusLine(CharArrayBuffer paramCharArrayBuffer, StatusLine paramStatusLine)
  {
    int i = 1 + (3 + (1 + estimateProtocolVersionLen(paramStatusLine.getProtocolVersion())));
    String str = paramStatusLine.getReasonPhrase();
    if (str != null) {
      i += str.length();
    }
    paramCharArrayBuffer.ensureCapacity(i);
    appendProtocolVersion(paramCharArrayBuffer, paramStatusLine.getProtocolVersion());
    paramCharArrayBuffer.append(' ');
    paramCharArrayBuffer.append(Integer.toString(paramStatusLine.getStatusCode()));
    paramCharArrayBuffer.append(' ');
    if (str != null) {
      paramCharArrayBuffer.append(str);
    }
  }
  
  protected int estimateProtocolVersionLen(ProtocolVersion paramProtocolVersion)
  {
    return 4 + paramProtocolVersion.getProtocol().length();
  }
  
  public CharArrayBuffer formatHeader(CharArrayBuffer paramCharArrayBuffer, Header paramHeader)
  {
    Args.notNull(paramHeader, "Header");
    if ((paramHeader instanceof FormattedHeader)) {
      return ((FormattedHeader)paramHeader).getBuffer();
    }
    CharArrayBuffer localCharArrayBuffer = initBuffer(paramCharArrayBuffer);
    doFormatHeader(localCharArrayBuffer, paramHeader);
    return localCharArrayBuffer;
  }
  
  public CharArrayBuffer formatRequestLine(CharArrayBuffer paramCharArrayBuffer, RequestLine paramRequestLine)
  {
    Args.notNull(paramRequestLine, "Request line");
    CharArrayBuffer localCharArrayBuffer = initBuffer(paramCharArrayBuffer);
    doFormatRequestLine(localCharArrayBuffer, paramRequestLine);
    return localCharArrayBuffer;
  }
  
  public CharArrayBuffer formatStatusLine(CharArrayBuffer paramCharArrayBuffer, StatusLine paramStatusLine)
  {
    Args.notNull(paramStatusLine, "Status line");
    CharArrayBuffer localCharArrayBuffer = initBuffer(paramCharArrayBuffer);
    doFormatStatusLine(localCharArrayBuffer, paramStatusLine);
    return localCharArrayBuffer;
  }
  
  protected CharArrayBuffer initBuffer(CharArrayBuffer paramCharArrayBuffer)
  {
    if (paramCharArrayBuffer != null)
    {
      paramCharArrayBuffer.clear();
      return paramCharArrayBuffer;
    }
    return new CharArrayBuffer(64);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicLineFormatter
 * JD-Core Version:    0.7.0.1
 */