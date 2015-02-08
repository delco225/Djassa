package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicLineParser
  implements LineParser
{
  @Deprecated
  public static final BasicLineParser DEFAULT = new BasicLineParser();
  public static final BasicLineParser INSTANCE = new BasicLineParser();
  protected final ProtocolVersion protocol;
  
  public BasicLineParser()
  {
    this(null);
  }
  
  public BasicLineParser(ProtocolVersion paramProtocolVersion)
  {
    if (paramProtocolVersion != null) {}
    for (;;)
    {
      this.protocol = paramProtocolVersion;
      return;
      paramProtocolVersion = HttpVersion.HTTP_1_1;
    }
  }
  
  public static Header parseHeader(String paramString, LineParser paramLineParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    if (paramLineParser != null) {}
    for (;;)
    {
      return paramLineParser.parseHeader(localCharArrayBuffer);
      paramLineParser = INSTANCE;
    }
  }
  
  public static ProtocolVersion parseProtocolVersion(String paramString, LineParser paramLineParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramLineParser != null) {}
    for (;;)
    {
      return paramLineParser.parseProtocolVersion(localCharArrayBuffer, localParserCursor);
      paramLineParser = INSTANCE;
    }
  }
  
  public static RequestLine parseRequestLine(String paramString, LineParser paramLineParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramLineParser != null) {}
    for (;;)
    {
      return paramLineParser.parseRequestLine(localCharArrayBuffer, localParserCursor);
      paramLineParser = INSTANCE;
    }
  }
  
  public static StatusLine parseStatusLine(String paramString, LineParser paramLineParser)
    throws ParseException
  {
    Args.notNull(paramString, "Value");
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(paramString.length());
    localCharArrayBuffer.append(paramString);
    ParserCursor localParserCursor = new ParserCursor(0, paramString.length());
    if (paramLineParser != null) {}
    for (;;)
    {
      return paramLineParser.parseStatusLine(localCharArrayBuffer, localParserCursor);
      paramLineParser = INSTANCE;
    }
  }
  
  protected ProtocolVersion createProtocolVersion(int paramInt1, int paramInt2)
  {
    return this.protocol.forVersion(paramInt1, paramInt2);
  }
  
  protected RequestLine createRequestLine(String paramString1, String paramString2, ProtocolVersion paramProtocolVersion)
  {
    return new BasicRequestLine(paramString1, paramString2, paramProtocolVersion);
  }
  
  protected StatusLine createStatusLine(ProtocolVersion paramProtocolVersion, int paramInt, String paramString)
  {
    return new BasicStatusLine(paramProtocolVersion, paramInt, paramString);
  }
  
  public boolean hasProtocolVersion(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    int i = paramParserCursor.getPos();
    String str = this.protocol.getProtocol();
    int j = str.length();
    if (paramCharArrayBuffer.length() < j + 4) {}
    label66:
    while (4 + (i + j) > paramCharArrayBuffer.length())
    {
      return false;
      if (i >= 0) {
        break;
      }
      i = -4 + paramCharArrayBuffer.length() - j;
    }
    boolean bool = true;
    int k = 0;
    label86:
    if ((bool) && (k < j))
    {
      if (paramCharArrayBuffer.charAt(i + k) == str.charAt(k)) {}
      for (bool = true;; bool = false)
      {
        k++;
        break label86;
        if (i != 0) {
          break;
        }
        while ((i < paramCharArrayBuffer.length()) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(i)))) {
          i++;
        }
        break label66;
      }
    }
    if (bool) {
      if (paramCharArrayBuffer.charAt(i + j) != '/') {
        break label189;
      }
    }
    label189:
    for (bool = true;; bool = false) {
      return bool;
    }
  }
  
  public Header parseHeader(CharArrayBuffer paramCharArrayBuffer)
    throws ParseException
  {
    return new BufferedHeader(paramCharArrayBuffer);
  }
  
  /* Error */
  public ProtocolVersion parseProtocolVersion(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
    throws ParseException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 109
    //   3: invokestatic 46	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc 111
    //   10: invokestatic 46	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: getfield 28	org/apache/http/message/BasicLineParser:protocol	Lorg/apache/http/ProtocolVersion;
    //   18: invokevirtual 118	org/apache/http/ProtocolVersion:getProtocol	()Ljava/lang/String;
    //   21: astore 5
    //   23: aload 5
    //   25: invokevirtual 54	java/lang/String:length	()I
    //   28: istore 6
    //   30: aload_2
    //   31: invokevirtual 114	org/apache/http/message/ParserCursor:getPos	()I
    //   34: istore 7
    //   36: aload_2
    //   37: invokevirtual 140	org/apache/http/message/ParserCursor:getUpperBound	()I
    //   40: istore 8
    //   42: aload_0
    //   43: aload_1
    //   44: aload_2
    //   45: invokevirtual 144	org/apache/http/message/BasicLineParser:skipWhitespace	(Lorg/apache/http/util/CharArrayBuffer;Lorg/apache/http/message/ParserCursor;)V
    //   48: aload_2
    //   49: invokevirtual 114	org/apache/http/message/ParserCursor:getPos	()I
    //   52: istore 9
    //   54: iconst_4
    //   55: iload 9
    //   57: iload 6
    //   59: iadd
    //   60: iadd
    //   61: iload 8
    //   63: if_icmple +37 -> 100
    //   66: new 38	org/apache/http/ParseException
    //   69: dup
    //   70: new 146	java/lang/StringBuilder
    //   73: dup
    //   74: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   77: ldc 149
    //   79: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   82: aload_1
    //   83: iload 7
    //   85: iload 8
    //   87: invokevirtual 156	org/apache/http/util/CharArrayBuffer:substring	(II)Ljava/lang/String;
    //   90: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   96: invokespecial 161	org/apache/http/ParseException:<init>	(Ljava/lang/String;)V
    //   99: athrow
    //   100: iconst_1
    //   101: istore 10
    //   103: iconst_0
    //   104: istore 11
    //   106: iload 10
    //   108: ifeq +44 -> 152
    //   111: iload 11
    //   113: iload 6
    //   115: if_icmpge +37 -> 152
    //   118: aload_1
    //   119: iload 9
    //   121: iload 11
    //   123: iadd
    //   124: invokevirtual 123	org/apache/http/util/CharArrayBuffer:charAt	(I)C
    //   127: aload 5
    //   129: iload 11
    //   131: invokevirtual 124	java/lang/String:charAt	(I)C
    //   134: if_icmpne +12 -> 146
    //   137: iconst_1
    //   138: istore 10
    //   140: iinc 11 1
    //   143: goto -37 -> 106
    //   146: iconst_0
    //   147: istore 10
    //   149: goto -9 -> 140
    //   152: iload 10
    //   154: ifeq +20 -> 174
    //   157: aload_1
    //   158: iload 9
    //   160: iload 6
    //   162: iadd
    //   163: invokevirtual 123	org/apache/http/util/CharArrayBuffer:charAt	(I)C
    //   166: bipush 47
    //   168: if_icmpne +45 -> 213
    //   171: iconst_1
    //   172: istore 10
    //   174: iload 10
    //   176: ifne +43 -> 219
    //   179: new 38	org/apache/http/ParseException
    //   182: dup
    //   183: new 146	java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   190: ldc 149
    //   192: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: aload_1
    //   196: iload 7
    //   198: iload 8
    //   200: invokevirtual 156	org/apache/http/util/CharArrayBuffer:substring	(II)Ljava/lang/String;
    //   203: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   206: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   209: invokespecial 161	org/apache/http/ParseException:<init>	(Ljava/lang/String;)V
    //   212: athrow
    //   213: iconst_0
    //   214: istore 10
    //   216: goto -42 -> 174
    //   219: iload 9
    //   221: iload 6
    //   223: iconst_1
    //   224: iadd
    //   225: iadd
    //   226: istore 12
    //   228: aload_1
    //   229: bipush 46
    //   231: iload 12
    //   233: iload 8
    //   235: invokevirtual 165	org/apache/http/util/CharArrayBuffer:indexOf	(III)I
    //   238: istore 13
    //   240: iload 13
    //   242: iconst_m1
    //   243: if_icmpne +37 -> 280
    //   246: new 38	org/apache/http/ParseException
    //   249: dup
    //   250: new 146	java/lang/StringBuilder
    //   253: dup
    //   254: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   257: ldc 167
    //   259: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: aload_1
    //   263: iload 7
    //   265: iload 8
    //   267: invokevirtual 156	org/apache/http/util/CharArrayBuffer:substring	(II)Ljava/lang/String;
    //   270: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   276: invokespecial 161	org/apache/http/ParseException:<init>	(Ljava/lang/String;)V
    //   279: athrow
    //   280: aload_1
    //   281: iload 12
    //   283: iload 13
    //   285: invokevirtual 170	org/apache/http/util/CharArrayBuffer:substringTrimmed	(II)Ljava/lang/String;
    //   288: invokestatic 176	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   291: istore 15
    //   293: iload 13
    //   295: iconst_1
    //   296: iadd
    //   297: istore 16
    //   299: aload_1
    //   300: bipush 32
    //   302: iload 16
    //   304: iload 8
    //   306: invokevirtual 165	org/apache/http/util/CharArrayBuffer:indexOf	(III)I
    //   309: istore 17
    //   311: iload 17
    //   313: iconst_m1
    //   314: if_icmpne +7 -> 321
    //   317: iload 8
    //   319: istore 17
    //   321: aload_1
    //   322: iload 16
    //   324: iload 17
    //   326: invokevirtual 170	org/apache/http/util/CharArrayBuffer:substringTrimmed	(II)Ljava/lang/String;
    //   329: invokestatic 176	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   332: istore 19
    //   334: aload_2
    //   335: iload 17
    //   337: invokevirtual 179	org/apache/http/message/ParserCursor:updatePos	(I)V
    //   340: aload_0
    //   341: iload 15
    //   343: iload 19
    //   345: invokevirtual 181	org/apache/http/message/BasicLineParser:createProtocolVersion	(II)Lorg/apache/http/ProtocolVersion;
    //   348: areturn
    //   349: astore 14
    //   351: new 38	org/apache/http/ParseException
    //   354: dup
    //   355: new 146	java/lang/StringBuilder
    //   358: dup
    //   359: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   362: ldc 183
    //   364: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   367: aload_1
    //   368: iload 7
    //   370: iload 8
    //   372: invokevirtual 156	org/apache/http/util/CharArrayBuffer:substring	(II)Ljava/lang/String;
    //   375: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   381: invokespecial 161	org/apache/http/ParseException:<init>	(Ljava/lang/String;)V
    //   384: athrow
    //   385: astore 18
    //   387: new 38	org/apache/http/ParseException
    //   390: dup
    //   391: new 146	java/lang/StringBuilder
    //   394: dup
    //   395: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   398: ldc 185
    //   400: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   403: aload_1
    //   404: iload 7
    //   406: iload 8
    //   408: invokevirtual 156	org/apache/http/util/CharArrayBuffer:substring	(II)Ljava/lang/String;
    //   411: invokevirtual 152	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   414: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   417: invokespecial 161	org/apache/http/ParseException:<init>	(Ljava/lang/String;)V
    //   420: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	421	0	this	BasicLineParser
    //   0	421	1	paramCharArrayBuffer	CharArrayBuffer
    //   0	421	2	paramParserCursor	ParserCursor
    //   21	107	5	str	String
    //   28	197	6	i	int
    //   34	371	7	j	int
    //   40	367	8	k	int
    //   52	174	9	m	int
    //   101	114	10	n	int
    //   104	37	11	i1	int
    //   226	56	12	i2	int
    //   238	59	13	i3	int
    //   349	1	14	localNumberFormatException1	NumberFormatException
    //   291	51	15	i4	int
    //   297	26	16	i5	int
    //   309	27	17	i6	int
    //   385	1	18	localNumberFormatException2	NumberFormatException
    //   332	12	19	i7	int
    // Exception table:
    //   from	to	target	type
    //   280	293	349	java/lang/NumberFormatException
    //   321	334	385	java/lang/NumberFormatException
  }
  
  public RequestLine parseRequestLine(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
    throws ParseException
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getUpperBound();
    int k;
    int m;
    try
    {
      skipWhitespace(paramCharArrayBuffer, paramParserCursor);
      k = paramParserCursor.getPos();
      m = paramCharArrayBuffer.indexOf(32, k, j);
      if (m < 0) {
        throw new ParseException("Invalid request line: " + paramCharArrayBuffer.substring(i, j));
      }
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
      throw new ParseException("Invalid request line: " + paramCharArrayBuffer.substring(i, j));
    }
    String str1 = paramCharArrayBuffer.substringTrimmed(k, m);
    paramParserCursor.updatePos(m);
    skipWhitespace(paramCharArrayBuffer, paramParserCursor);
    int n = paramParserCursor.getPos();
    int i1 = paramCharArrayBuffer.indexOf(32, n, j);
    if (i1 < 0) {
      throw new ParseException("Invalid request line: " + paramCharArrayBuffer.substring(i, j));
    }
    String str2 = paramCharArrayBuffer.substringTrimmed(n, i1);
    paramParserCursor.updatePos(i1);
    ProtocolVersion localProtocolVersion = parseProtocolVersion(paramCharArrayBuffer, paramParserCursor);
    skipWhitespace(paramCharArrayBuffer, paramParserCursor);
    if (!paramParserCursor.atEnd()) {
      throw new ParseException("Invalid request line: " + paramCharArrayBuffer.substring(i, j));
    }
    RequestLine localRequestLine = createRequestLine(str1, str2, localProtocolVersion);
    return localRequestLine;
  }
  
  public StatusLine parseStatusLine(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
    throws ParseException
  {
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    Args.notNull(paramParserCursor, "Parser cursor");
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getUpperBound();
    ProtocolVersion localProtocolVersion;
    int m;
    String str1;
    for (;;)
    {
      int n;
      try
      {
        localProtocolVersion = parseProtocolVersion(paramCharArrayBuffer, paramParserCursor);
        skipWhitespace(paramCharArrayBuffer, paramParserCursor);
        int k = paramParserCursor.getPos();
        m = paramCharArrayBuffer.indexOf(32, k, j);
        if (m < 0) {
          m = j;
        }
        str1 = paramCharArrayBuffer.substringTrimmed(k, m);
        n = 0;
        if (n >= str1.length()) {
          break;
        }
        if (!Character.isDigit(str1.charAt(n))) {
          throw new ParseException("Status line contains invalid status code: " + paramCharArrayBuffer.substring(i, j));
        }
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        throw new ParseException("Invalid status line: " + paramCharArrayBuffer.substring(i, j));
      }
      n++;
    }
    for (;;)
    {
      try
      {
        int i1 = Integer.parseInt(str1);
        int i2 = m;
        if (i2 < j)
        {
          str2 = paramCharArrayBuffer.substringTrimmed(i2, j);
          return createStatusLine(localProtocolVersion, i1, str2);
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new ParseException("Status line contains invalid status code: " + paramCharArrayBuffer.substring(i, j));
      }
      String str2 = "";
    }
  }
  
  protected void skipWhitespace(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor)
  {
    int i = paramParserCursor.getPos();
    int j = paramParserCursor.getUpperBound();
    while ((i < j) && (HTTP.isWhitespace(paramCharArrayBuffer.charAt(i)))) {
      i++;
    }
    paramParserCursor.updatePos(i);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicLineParser
 * JD-Core Version:    0.7.0.1
 */