package org.apache.http.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VersionInfo
{
  public static final String PROPERTY_MODULE = "info.module";
  public static final String PROPERTY_RELEASE = "info.release";
  public static final String PROPERTY_TIMESTAMP = "info.timestamp";
  public static final String UNAVAILABLE = "UNAVAILABLE";
  public static final String VERSION_PROPERTY_FILE = "version.properties";
  private final String infoClassloader;
  private final String infoModule;
  private final String infoPackage;
  private final String infoRelease;
  private final String infoTimestamp;
  
  protected VersionInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    Args.notNull(paramString1, "Package identifier");
    this.infoPackage = paramString1;
    if (paramString2 != null)
    {
      this.infoModule = paramString2;
      if (paramString3 == null) {
        break label63;
      }
      label29:
      this.infoRelease = paramString3;
      if (paramString4 == null) {
        break label69;
      }
      label39:
      this.infoTimestamp = paramString4;
      if (paramString5 == null) {
        break label76;
      }
    }
    for (;;)
    {
      this.infoClassloader = paramString5;
      return;
      paramString2 = "UNAVAILABLE";
      break;
      label63:
      paramString3 = "UNAVAILABLE";
      break label29;
      label69:
      paramString4 = "UNAVAILABLE";
      break label39;
      label76:
      paramString5 = "UNAVAILABLE";
    }
  }
  
  protected static VersionInfo fromMap(String paramString, Map<?, ?> paramMap, ClassLoader paramClassLoader)
  {
    Args.notNull(paramString, "Package identifier");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    if (paramMap != null)
    {
      str1 = (String)paramMap.get("info.module");
      if ((str1 != null) && (str1.length() < 1)) {
        str1 = null;
      }
      str2 = (String)paramMap.get("info.release");
      if ((str2 != null) && ((str2.length() < 1) || (str2.equals("${pom.version}")))) {
        str2 = null;
      }
      str3 = (String)paramMap.get("info.timestamp");
      if ((str3 != null) && ((str3.length() < 1) || (str3.equals("${mvn.timestamp}")))) {
        str3 = null;
      }
    }
    String str4 = null;
    if (paramClassLoader != null) {
      str4 = paramClassLoader.toString();
    }
    return new VersionInfo(paramString, str1, str2, str3, str4);
  }
  
  public static String getUserAgent(String paramString1, String paramString2, Class<?> paramClass)
  {
    VersionInfo localVersionInfo = loadVersionInfo(paramString2, paramClass.getClassLoader());
    if (localVersionInfo != null) {}
    for (String str1 = localVersionInfo.getRelease();; str1 = "UNAVAILABLE")
    {
      String str2 = System.getProperty("java.version");
      return paramString1 + "/" + str1 + " (Java 1.5 minimum; Java/" + str2 + ")";
    }
  }
  
  /* Error */
  public static VersionInfo loadVersionInfo(String paramString, ClassLoader paramClassLoader)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc 31
    //   3: invokestatic 37	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_1
    //   8: ifnull +98 -> 106
    //   11: aload_1
    //   12: astore_3
    //   13: aconst_null
    //   14: astore 4
    //   16: aload_3
    //   17: new 100	java/lang/StringBuilder
    //   20: dup
    //   21: invokespecial 101	java/lang/StringBuilder:<init>	()V
    //   24: aload_0
    //   25: bipush 46
    //   27: bipush 47
    //   29: invokevirtual 118	java/lang/String:replace	(CC)Ljava/lang/String;
    //   32: invokevirtual 105	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: ldc 107
    //   37: invokevirtual 105	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: ldc 19
    //   42: invokevirtual 105	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   48: invokevirtual 124	java/lang/ClassLoader:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
    //   51: astore 7
    //   53: aconst_null
    //   54: astore 4
    //   56: aload 7
    //   58: ifnull +28 -> 86
    //   61: new 126	java/util/Properties
    //   64: dup
    //   65: invokespecial 127	java/util/Properties:<init>	()V
    //   68: astore 8
    //   70: aload 8
    //   72: aload 7
    //   74: invokevirtual 131	java/util/Properties:load	(Ljava/io/InputStream;)V
    //   77: aload 8
    //   79: astore 4
    //   81: aload 7
    //   83: invokevirtual 136	java/io/InputStream:close	()V
    //   86: aconst_null
    //   87: astore 6
    //   89: aload 4
    //   91: ifnull +12 -> 103
    //   94: aload_0
    //   95: aload 4
    //   97: aload_3
    //   98: invokestatic 138	org/apache/http/util/VersionInfo:fromMap	(Ljava/lang/String;Ljava/util/Map;Ljava/lang/ClassLoader;)Lorg/apache/http/util/VersionInfo;
    //   101: astore 6
    //   103: aload 6
    //   105: areturn
    //   106: invokestatic 144	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   109: invokevirtual 147	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
    //   112: astore_3
    //   113: goto -100 -> 13
    //   116: astore 9
    //   118: aload 7
    //   120: invokevirtual 136	java/io/InputStream:close	()V
    //   123: aload 9
    //   125: athrow
    //   126: astore 5
    //   128: goto -42 -> 86
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	131	0	paramString	String
    //   0	131	1	paramClassLoader	ClassLoader
    //   12	101	3	localClassLoader	ClassLoader
    //   14	82	4	localObject1	Object
    //   126	1	5	localIOException	java.io.IOException
    //   87	17	6	localVersionInfo	VersionInfo
    //   51	68	7	localInputStream	java.io.InputStream
    //   68	10	8	localProperties	java.util.Properties
    //   116	8	9	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   61	77	116	finally
    //   16	53	126	java/io/IOException
    //   81	86	126	java/io/IOException
    //   118	126	126	java/io/IOException
  }
  
  public static VersionInfo[] loadVersionInfo(String[] paramArrayOfString, ClassLoader paramClassLoader)
  {
    Args.notNull(paramArrayOfString, "Package identifier array");
    ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      VersionInfo localVersionInfo = loadVersionInfo(paramArrayOfString[j], paramClassLoader);
      if (localVersionInfo != null) {
        localArrayList.add(localVersionInfo);
      }
    }
    return (VersionInfo[])localArrayList.toArray(new VersionInfo[localArrayList.size()]);
  }
  
  public final String getClassloader()
  {
    return this.infoClassloader;
  }
  
  public final String getModule()
  {
    return this.infoModule;
  }
  
  public final String getPackage()
  {
    return this.infoPackage;
  }
  
  public final String getRelease()
  {
    return this.infoRelease;
  }
  
  public final String getTimestamp()
  {
    return this.infoTimestamp;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(20 + this.infoPackage.length() + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
    localStringBuilder.append("VersionInfo(").append(this.infoPackage).append(':').append(this.infoModule);
    if (!"UNAVAILABLE".equals(this.infoRelease)) {
      localStringBuilder.append(':').append(this.infoRelease);
    }
    if (!"UNAVAILABLE".equals(this.infoTimestamp)) {
      localStringBuilder.append(':').append(this.infoTimestamp);
    }
    localStringBuilder.append(')');
    if (!"UNAVAILABLE".equals(this.infoClassloader)) {
      localStringBuilder.append('@').append(this.infoClassloader);
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.VersionInfo
 * JD-Core Version:    0.7.0.1
 */