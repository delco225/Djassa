package org.apache.http.impl;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;

public final class ConnSupport
{
  public static CharsetDecoder createDecoder(ConnectionConfig paramConnectionConfig)
  {
    if (paramConnectionConfig == null) {}
    Charset localCharset;
    CodingErrorAction localCodingErrorAction1;
    CodingErrorAction localCodingErrorAction2;
    do
    {
      return null;
      localCharset = paramConnectionConfig.getCharset();
      localCodingErrorAction1 = paramConnectionConfig.getMalformedInputAction();
      localCodingErrorAction2 = paramConnectionConfig.getUnmappableInputAction();
    } while (localCharset == null);
    CharsetDecoder localCharsetDecoder1 = localCharset.newDecoder();
    CharsetDecoder localCharsetDecoder2;
    if (localCodingErrorAction1 != null)
    {
      localCharsetDecoder2 = localCharsetDecoder1.onMalformedInput(localCodingErrorAction1);
      if (localCodingErrorAction2 == null) {
        break label61;
      }
    }
    for (;;)
    {
      return localCharsetDecoder2.onUnmappableCharacter(localCodingErrorAction2);
      localCodingErrorAction1 = CodingErrorAction.REPORT;
      break;
      label61:
      localCodingErrorAction2 = CodingErrorAction.REPORT;
    }
  }
  
  public static CharsetEncoder createEncoder(ConnectionConfig paramConnectionConfig)
  {
    if (paramConnectionConfig == null) {}
    Charset localCharset;
    do
    {
      return null;
      localCharset = paramConnectionConfig.getCharset();
    } while (localCharset == null);
    CodingErrorAction localCodingErrorAction1 = paramConnectionConfig.getMalformedInputAction();
    CodingErrorAction localCodingErrorAction2 = paramConnectionConfig.getUnmappableInputAction();
    CharsetEncoder localCharsetEncoder1 = localCharset.newEncoder();
    CharsetEncoder localCharsetEncoder2;
    if (localCodingErrorAction1 != null)
    {
      localCharsetEncoder2 = localCharsetEncoder1.onMalformedInput(localCodingErrorAction1);
      if (localCodingErrorAction2 == null) {
        break label61;
      }
    }
    for (;;)
    {
      return localCharsetEncoder2.onUnmappableCharacter(localCodingErrorAction2);
      localCodingErrorAction1 = CodingErrorAction.REPORT;
      break;
      label61:
      localCodingErrorAction2 = CodingErrorAction.REPORT;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.ConnSupport
 * JD-Core Version:    0.7.0.1
 */