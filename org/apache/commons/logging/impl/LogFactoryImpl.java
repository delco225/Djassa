package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl
  extends LogFactory
{
  public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
  public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
  public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
  private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
  private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
  private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
  private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
  public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
  protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
  private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
  private static final int PKG_LEN = "org.apache.commons.logging.impl.".length();
  static Class class$java$lang$String;
  static Class class$org$apache$commons$logging$Log;
  static Class class$org$apache$commons$logging$LogFactory;
  static Class class$org$apache$commons$logging$impl$LogFactoryImpl;
  private static final String[] classesToDiscover = { "org.apache.commons.logging.impl.Log4JLogger", "org.apache.commons.logging.impl.Jdk14Logger", "org.apache.commons.logging.impl.Jdk13LumberjackLogger", "org.apache.commons.logging.impl.SimpleLog" };
  private boolean allowFlawedContext;
  private boolean allowFlawedDiscovery;
  private boolean allowFlawedHierarchy;
  protected Hashtable attributes = new Hashtable();
  private String diagnosticPrefix;
  protected Hashtable instances = new Hashtable();
  private String logClassName;
  protected Constructor logConstructor = null;
  protected Class[] logConstructorSignature;
  protected Method logMethod;
  protected Class[] logMethodSignature;
  private boolean useTCCL = true;
  
  public LogFactoryImpl()
  {
    Class[] arrayOfClass1 = new Class[1];
    Class localClass1;
    Class[] arrayOfClass2;
    Class localClass2;
    if (class$java$lang$String == null)
    {
      localClass1 = class$("java.lang.String");
      class$java$lang$String = localClass1;
      arrayOfClass1[0] = localClass1;
      this.logConstructorSignature = arrayOfClass1;
      this.logMethod = null;
      arrayOfClass2 = new Class[1];
      if (class$org$apache$commons$logging$LogFactory != null) {
        break label128;
      }
      localClass2 = class$("org.apache.commons.logging.LogFactory");
      class$org$apache$commons$logging$LogFactory = localClass2;
    }
    for (;;)
    {
      arrayOfClass2[0] = localClass2;
      this.logMethodSignature = arrayOfClass2;
      initDiagnostics();
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Instance created.");
      }
      return;
      localClass1 = class$java$lang$String;
      break;
      label128:
      localClass2 = class$org$apache$commons$logging$LogFactory;
    }
  }
  
  static Class class$(String paramString)
  {
    try
    {
      Class localClass = Class.forName(paramString);
      return localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
    }
  }
  
  /* Error */
  private Log createLogFromClass(String paramString1, String paramString2, boolean paramBoolean)
    throws LogConfigurationException
  {
    // Byte code:
    //   0: invokestatic 114	org/apache/commons/logging/impl/LogFactoryImpl:isDiagnosticsEnabled	()Z
    //   3: ifeq +31 -> 34
    //   6: aload_0
    //   7: new 148	java/lang/StringBuffer
    //   10: dup
    //   11: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   14: ldc 151
    //   16: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   19: aload_1
    //   20: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   23: ldc 157
    //   25: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   28: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   31: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   34: iconst_1
    //   35: anewarray 162	java/lang/Object
    //   38: dup
    //   39: iconst_0
    //   40: aload_2
    //   41: aastore
    //   42: astore 4
    //   44: aconst_null
    //   45: astore 5
    //   47: aconst_null
    //   48: astore 6
    //   50: aload_0
    //   51: invokespecial 165	org/apache/commons/logging/impl/LogFactoryImpl:getBaseClassLoader	()Ljava/lang/ClassLoader;
    //   54: astore 7
    //   56: aload_0
    //   57: new 148	java/lang/StringBuffer
    //   60: dup
    //   61: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   64: ldc 167
    //   66: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   69: aload_1
    //   70: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   73: ldc 169
    //   75: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   78: aload 7
    //   80: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   83: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   86: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   89: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   92: invokestatic 114	org/apache/commons/logging/impl/LogFactoryImpl:isDiagnosticsEnabled	()Z
    //   95: ifeq +88 -> 183
    //   98: new 148	java/lang/StringBuffer
    //   101: dup
    //   102: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   105: aload_1
    //   106: bipush 46
    //   108: bipush 47
    //   110: invokevirtual 177	java/lang/String:replace	(CC)Ljava/lang/String;
    //   113: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   116: ldc 179
    //   118: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   121: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   124: astore 24
    //   126: aload 7
    //   128: ifnull +217 -> 345
    //   131: aload 7
    //   133: aload 24
    //   135: invokevirtual 185	java/lang/ClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
    //   138: astore 25
    //   140: aload 25
    //   142: ifnonnull +231 -> 373
    //   145: aload_0
    //   146: new 148	java/lang/StringBuffer
    //   149: dup
    //   150: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   153: ldc 187
    //   155: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   158: aload_1
    //   159: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   162: ldc 189
    //   164: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   167: aload 24
    //   169: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   172: ldc 191
    //   174: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   177: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   180: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   183: aload_1
    //   184: iconst_1
    //   185: aload 7
    //   187: invokestatic 194	java/lang/Class:forName	(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
    //   190: astore 23
    //   192: aload 23
    //   194: astore 21
    //   196: aload 21
    //   198: aload_0
    //   199: getfield 99	org/apache/commons/logging/impl/LogFactoryImpl:logConstructorSignature	[Ljava/lang/Class;
    //   202: invokevirtual 198	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   205: astore 5
    //   207: aload 5
    //   209: aload 4
    //   211: invokevirtual 204	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   214: astore 22
    //   216: aload 22
    //   218: instanceof 206
    //   221: ifeq +443 -> 664
    //   224: aload 21
    //   226: astore 6
    //   228: aload 22
    //   230: checkcast 206	org/apache/commons/logging/Log
    //   233: astore 9
    //   235: aload 6
    //   237: ifnull +105 -> 342
    //   240: iload_3
    //   241: ifeq +101 -> 342
    //   244: aload_0
    //   245: aload_1
    //   246: putfield 208	org/apache/commons/logging/impl/LogFactoryImpl:logClassName	Ljava/lang/String;
    //   249: aload_0
    //   250: aload 5
    //   252: putfield 87	org/apache/commons/logging/impl/LogFactoryImpl:logConstructor	Ljava/lang/reflect/Constructor;
    //   255: aload_0
    //   256: aload 6
    //   258: ldc 210
    //   260: aload_0
    //   261: getfield 107	org/apache/commons/logging/impl/LogFactoryImpl:logMethodSignature	[Ljava/lang/Class;
    //   264: invokevirtual 214	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   267: putfield 101	org/apache/commons/logging/impl/LogFactoryImpl:logMethod	Ljava/lang/reflect/Method;
    //   270: aload_0
    //   271: new 148	java/lang/StringBuffer
    //   274: dup
    //   275: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   278: ldc 216
    //   280: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   283: aload_1
    //   284: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   287: ldc 157
    //   289: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   292: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   295: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   298: aload_0
    //   299: new 148	java/lang/StringBuffer
    //   302: dup
    //   303: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   306: ldc 218
    //   308: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   311: aload_1
    //   312: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   315: ldc 169
    //   317: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   320: aload 6
    //   322: invokevirtual 221	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
    //   325: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   328: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   331: ldc 223
    //   333: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   336: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   339: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   342: aload 9
    //   344: areturn
    //   345: new 148	java/lang/StringBuffer
    //   348: dup
    //   349: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   352: aload 24
    //   354: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   357: ldc 179
    //   359: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   362: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   365: invokestatic 226	java/lang/ClassLoader:getSystemResource	(Ljava/lang/String;)Ljava/net/URL;
    //   368: astore 25
    //   370: goto -230 -> 140
    //   373: aload_0
    //   374: new 148	java/lang/StringBuffer
    //   377: dup
    //   378: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   381: ldc 187
    //   383: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   386: aload_1
    //   387: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   390: ldc 228
    //   392: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   395: aload 25
    //   397: invokevirtual 231	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
    //   400: ldc 157
    //   402: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   405: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   408: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   411: goto -228 -> 183
    //   414: astore 14
    //   416: aload 14
    //   418: invokevirtual 140	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   421: astore 15
    //   423: aload_0
    //   424: new 148	java/lang/StringBuffer
    //   427: dup
    //   428: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   431: ldc 233
    //   433: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   436: aload_1
    //   437: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   440: ldc 235
    //   442: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   445: aload 7
    //   447: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   450: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   453: ldc 237
    //   455: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   458: aload 15
    //   460: invokevirtual 240	java/lang/String:trim	()Ljava/lang/String;
    //   463: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   466: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   469: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   472: aconst_null
    //   473: astore 9
    //   475: goto -240 -> 235
    //   478: astore 16
    //   480: aload 16
    //   482: invokevirtual 140	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   485: astore 17
    //   487: aload_0
    //   488: new 148	java/lang/StringBuffer
    //   491: dup
    //   492: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   495: ldc 233
    //   497: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   500: aload_1
    //   501: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   504: ldc 242
    //   506: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   509: aload 7
    //   511: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   514: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   517: ldc 237
    //   519: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   522: aload 17
    //   524: invokevirtual 240	java/lang/String:trim	()Ljava/lang/String;
    //   527: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   530: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   533: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   536: aload_1
    //   537: invokestatic 132	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   540: astore 20
    //   542: aload 20
    //   544: astore 21
    //   546: goto -350 -> 196
    //   549: astore 18
    //   551: aload 18
    //   553: invokevirtual 140	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   556: astore 19
    //   558: aload_0
    //   559: new 148	java/lang/StringBuffer
    //   562: dup
    //   563: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   566: ldc 233
    //   568: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   571: aload_1
    //   572: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   575: ldc 244
    //   577: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   580: aload 19
    //   582: invokevirtual 240	java/lang/String:trim	()Ljava/lang/String;
    //   585: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   588: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   591: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   594: aconst_null
    //   595: astore 9
    //   597: goto -362 -> 235
    //   600: astore 12
    //   602: aload 12
    //   604: invokevirtual 140	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   607: astore 13
    //   609: aload_0
    //   610: new 148	java/lang/StringBuffer
    //   613: dup
    //   614: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   617: ldc 233
    //   619: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   622: aload_1
    //   623: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   626: ldc 246
    //   628: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   631: aload 7
    //   633: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   636: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   639: ldc 237
    //   641: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   644: aload 13
    //   646: invokevirtual 240	java/lang/String:trim	()Ljava/lang/String;
    //   649: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   652: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   655: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   658: aconst_null
    //   659: astore 9
    //   661: goto -426 -> 235
    //   664: aload_0
    //   665: aload 7
    //   667: aload 21
    //   669: invokespecial 250	org/apache/commons/logging/impl/LogFactoryImpl:handleFlawedHierarchy	(Ljava/lang/ClassLoader;Ljava/lang/Class;)V
    //   672: aconst_null
    //   673: astore 9
    //   675: aload 7
    //   677: ifnull -442 -> 235
    //   680: aload_0
    //   681: aload 7
    //   683: invokespecial 254	org/apache/commons/logging/impl/LogFactoryImpl:getParentClassLoader	(Ljava/lang/ClassLoader;)Ljava/lang/ClassLoader;
    //   686: astore 7
    //   688: goto -632 -> 56
    //   691: astore 11
    //   693: aload 11
    //   695: athrow
    //   696: astore 8
    //   698: aload 8
    //   700: invokestatic 258	org/apache/commons/logging/LogFactory:handleThrowable	(Ljava/lang/Throwable;)V
    //   703: aload_0
    //   704: aload_1
    //   705: aload 7
    //   707: aload 8
    //   709: invokespecial 262	org/apache/commons/logging/impl/LogFactoryImpl:handleFlawedDiscovery	(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/Throwable;)V
    //   712: goto -40 -> 672
    //   715: astore 10
    //   717: aload 10
    //   719: invokestatic 258	org/apache/commons/logging/LogFactory:handleThrowable	(Ljava/lang/Throwable;)V
    //   722: aload_0
    //   723: aconst_null
    //   724: putfield 101	org/apache/commons/logging/impl/LogFactoryImpl:logMethod	Ljava/lang/reflect/Method;
    //   727: aload_0
    //   728: new 148	java/lang/StringBuffer
    //   731: dup
    //   732: invokespecial 149	java/lang/StringBuffer:<init>	()V
    //   735: ldc_w 264
    //   738: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   741: aload_1
    //   742: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   745: ldc 169
    //   747: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   750: aload 7
    //   752: invokestatic 173	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   755: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   758: ldc_w 266
    //   761: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   764: ldc_w 268
    //   767: invokevirtual 155	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   770: invokevirtual 160	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   773: invokevirtual 120	org/apache/commons/logging/impl/LogFactoryImpl:logDiagnostic	(Ljava/lang/String;)V
    //   776: goto -478 -> 298
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	779	0	this	LogFactoryImpl
    //   0	779	1	paramString1	String
    //   0	779	2	paramString2	String
    //   0	779	3	paramBoolean	boolean
    //   42	168	4	arrayOfObject	Object[]
    //   45	206	5	localConstructor	Constructor
    //   48	273	6	localObject1	Object
    //   54	697	7	localClassLoader	ClassLoader
    //   696	12	8	localThrowable1	Throwable
    //   233	441	9	localLog	Log
    //   715	3	10	localThrowable2	Throwable
    //   691	3	11	localLogConfigurationException	LogConfigurationException
    //   600	3	12	localExceptionInInitializerError	ExceptionInInitializerError
    //   607	38	13	str1	String
    //   414	3	14	localNoClassDefFoundError	NoClassDefFoundError
    //   421	38	15	str2	String
    //   478	3	16	localClassNotFoundException1	ClassNotFoundException
    //   485	38	17	str3	String
    //   549	3	18	localClassNotFoundException2	ClassNotFoundException
    //   556	25	19	str4	String
    //   540	3	20	localClass1	Class
    //   194	474	21	localObject2	Object
    //   214	15	22	localObject3	Object
    //   190	3	23	localClass2	Class
    //   124	229	24	str5	String
    //   138	258	25	localURL	java.net.URL
    // Exception table:
    //   from	to	target	type
    //   92	126	414	java/lang/NoClassDefFoundError
    //   131	140	414	java/lang/NoClassDefFoundError
    //   145	183	414	java/lang/NoClassDefFoundError
    //   183	192	414	java/lang/NoClassDefFoundError
    //   196	224	414	java/lang/NoClassDefFoundError
    //   228	235	414	java/lang/NoClassDefFoundError
    //   345	370	414	java/lang/NoClassDefFoundError
    //   373	411	414	java/lang/NoClassDefFoundError
    //   480	536	414	java/lang/NoClassDefFoundError
    //   536	542	414	java/lang/NoClassDefFoundError
    //   551	594	414	java/lang/NoClassDefFoundError
    //   664	672	414	java/lang/NoClassDefFoundError
    //   183	192	478	java/lang/ClassNotFoundException
    //   536	542	549	java/lang/ClassNotFoundException
    //   92	126	600	java/lang/ExceptionInInitializerError
    //   131	140	600	java/lang/ExceptionInInitializerError
    //   145	183	600	java/lang/ExceptionInInitializerError
    //   183	192	600	java/lang/ExceptionInInitializerError
    //   196	224	600	java/lang/ExceptionInInitializerError
    //   228	235	600	java/lang/ExceptionInInitializerError
    //   345	370	600	java/lang/ExceptionInInitializerError
    //   373	411	600	java/lang/ExceptionInInitializerError
    //   480	536	600	java/lang/ExceptionInInitializerError
    //   536	542	600	java/lang/ExceptionInInitializerError
    //   551	594	600	java/lang/ExceptionInInitializerError
    //   664	672	600	java/lang/ExceptionInInitializerError
    //   92	126	691	org/apache/commons/logging/LogConfigurationException
    //   131	140	691	org/apache/commons/logging/LogConfigurationException
    //   145	183	691	org/apache/commons/logging/LogConfigurationException
    //   183	192	691	org/apache/commons/logging/LogConfigurationException
    //   196	224	691	org/apache/commons/logging/LogConfigurationException
    //   228	235	691	org/apache/commons/logging/LogConfigurationException
    //   345	370	691	org/apache/commons/logging/LogConfigurationException
    //   373	411	691	org/apache/commons/logging/LogConfigurationException
    //   480	536	691	org/apache/commons/logging/LogConfigurationException
    //   536	542	691	org/apache/commons/logging/LogConfigurationException
    //   551	594	691	org/apache/commons/logging/LogConfigurationException
    //   664	672	691	org/apache/commons/logging/LogConfigurationException
    //   92	126	696	java/lang/Throwable
    //   131	140	696	java/lang/Throwable
    //   145	183	696	java/lang/Throwable
    //   183	192	696	java/lang/Throwable
    //   196	224	696	java/lang/Throwable
    //   228	235	696	java/lang/Throwable
    //   345	370	696	java/lang/Throwable
    //   373	411	696	java/lang/Throwable
    //   480	536	696	java/lang/Throwable
    //   536	542	696	java/lang/Throwable
    //   551	594	696	java/lang/Throwable
    //   664	672	696	java/lang/Throwable
    //   255	298	715	java/lang/Throwable
  }
  
  private Log discoverLogImplementation(String paramString)
    throws LogConfigurationException
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Discovering a Log implementation...");
    }
    initConfiguration();
    Log localLog1 = null;
    String str = findUserSpecifiedLogClassName();
    if (str != null)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Attempting to load user-specified log class '" + str + "'...");
      }
      Log localLog2 = createLogFromClass(str, paramString, true);
      if (localLog2 == null)
      {
        StringBuffer localStringBuffer = new StringBuffer("User-specified log class '");
        localStringBuffer.append(str);
        localStringBuffer.append("' cannot be found or is not useable.");
        informUponSimilarName(localStringBuffer, str, "org.apache.commons.logging.impl.Log4JLogger");
        informUponSimilarName(localStringBuffer, str, "org.apache.commons.logging.impl.Jdk14Logger");
        informUponSimilarName(localStringBuffer, str, "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
        informUponSimilarName(localStringBuffer, str, "org.apache.commons.logging.impl.SimpleLog");
        throw new LogConfigurationException(localStringBuffer.toString());
      }
      return localLog2;
    }
    if (isDiagnosticsEnabled()) {
      logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
    }
    for (int i = 0; (i < classesToDiscover.length) && (localLog1 == null); i++) {
      localLog1 = createLogFromClass(classesToDiscover[i], paramString, true);
    }
    if (localLog1 == null) {
      throw new LogConfigurationException("No suitable Log implementation");
    }
    return localLog1;
  }
  
  private String findUserSpecifiedLogClassName()
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
    }
    Object localObject = (String)getAttribute("org.apache.commons.logging.Log");
    if (localObject == null)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
      }
      localObject = (String)getAttribute("org.apache.commons.logging.log");
    }
    if (localObject == null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
      }
    }
    try
    {
      String str2 = getSystemProperty("org.apache.commons.logging.Log", null);
      localObject = str2;
    }
    catch (SecurityException localSecurityException2)
    {
      for (;;)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("No access allowed to system property 'org.apache.commons.logging.Log' - " + localSecurityException2.getMessage());
        }
      }
    }
    if (localObject == null) {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
      }
    }
    try
    {
      String str1 = getSystemProperty("org.apache.commons.logging.log", null);
      localObject = str1;
    }
    catch (SecurityException localSecurityException1)
    {
      for (;;)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("No access allowed to system property 'org.apache.commons.logging.log' - " + localSecurityException1.getMessage());
        }
      }
    }
    if (localObject != null) {
      localObject = ((String)localObject).trim();
    }
    return localObject;
  }
  
  private ClassLoader getBaseClassLoader()
    throws LogConfigurationException
  {
    Class localClass;
    if (class$org$apache$commons$logging$impl$LogFactoryImpl == null)
    {
      localClass = class$("org.apache.commons.logging.impl.LogFactoryImpl");
      class$org$apache$commons$logging$impl$LogFactoryImpl = localClass;
    }
    ClassLoader localClassLoader1;
    for (;;)
    {
      localClassLoader1 = getClassLoader(localClass);
      if (this.useTCCL) {
        break;
      }
      return localClassLoader1;
      localClass = class$org$apache$commons$logging$impl$LogFactoryImpl;
    }
    ClassLoader localClassLoader2 = getContextClassLoaderInternal();
    ClassLoader localClassLoader3 = getLowestClassLoader(localClassLoader2, localClassLoader1);
    if (localClassLoader3 == null)
    {
      if (this.allowFlawedContext)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
        }
        return localClassLoader2;
      }
      throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
    }
    if (localClassLoader3 != localClassLoader2)
    {
      if (!this.allowFlawedContext) {
        break label117;
      }
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
      }
    }
    return localClassLoader3;
    label117:
    throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
  }
  
  private boolean getBooleanConfiguration(String paramString, boolean paramBoolean)
  {
    String str = getConfigurationValue(paramString);
    if (str == null) {
      return paramBoolean;
    }
    return Boolean.valueOf(str).booleanValue();
  }
  
  protected static ClassLoader getClassLoader(Class paramClass)
  {
    return LogFactory.getClassLoader(paramClass);
  }
  
  private String getConfigurationValue(String paramString)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("[ENV] Trying to get configuration for item " + paramString);
    }
    Object localObject = getAttribute(paramString);
    String str;
    if (localObject != null)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] Found LogFactory attribute [" + localObject + "] for " + paramString);
      }
      str = localObject.toString();
    }
    for (;;)
    {
      return str;
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] No LogFactory attribute found for " + paramString);
      }
      try
      {
        str = getSystemProperty(paramString, null);
        if (str != null)
        {
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          logDiagnostic("[ENV] Found system property [" + str + "] for " + paramString);
          return str;
        }
      }
      catch (SecurityException localSecurityException)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[ENV] Security prevented reading system property " + paramString);
        }
      }
    }
    for (;;)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] No configuration defined for item " + paramString);
      }
      return null;
      if (isDiagnosticsEnabled()) {
        logDiagnostic("[ENV] No system property found for property " + paramString);
      }
    }
  }
  
  protected static ClassLoader getContextClassLoader()
    throws LogConfigurationException
  {
    return LogFactory.getContextClassLoader();
  }
  
  private static ClassLoader getContextClassLoaderInternal()
    throws LogConfigurationException
  {
    return (ClassLoader)AccessController.doPrivileged(new LogFactoryImpl.1());
  }
  
  private ClassLoader getLowestClassLoader(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
  {
    if (paramClassLoader1 == null) {
      return paramClassLoader2;
    }
    if (paramClassLoader2 == null) {
      return paramClassLoader1;
    }
    for (ClassLoader localClassLoader1 = paramClassLoader1; localClassLoader1 != null; localClassLoader1 = getParentClassLoader(localClassLoader1)) {
      if (localClassLoader1 == paramClassLoader2) {
        return paramClassLoader1;
      }
    }
    for (ClassLoader localClassLoader2 = paramClassLoader2;; localClassLoader2 = getParentClassLoader(localClassLoader2))
    {
      if (localClassLoader2 == null) {
        break label59;
      }
      if (localClassLoader2 == paramClassLoader1) {
        break;
      }
    }
    label59:
    return null;
  }
  
  private ClassLoader getParentClassLoader(ClassLoader paramClassLoader)
  {
    try
    {
      ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new LogFactoryImpl.3(this, paramClassLoader));
      return localClassLoader;
    }
    catch (SecurityException localSecurityException)
    {
      logDiagnostic("[SECURITY] Unable to obtain parent classloader");
    }
    return null;
  }
  
  private static String getSystemProperty(String paramString1, String paramString2)
    throws SecurityException
  {
    return (String)AccessController.doPrivileged(new LogFactoryImpl.2(paramString1, paramString2));
  }
  
  private void handleFlawedDiscovery(String paramString, ClassLoader paramClassLoader, Throwable paramThrowable)
  {
    if (isDiagnosticsEnabled())
    {
      logDiagnostic("Could not instantiate Log '" + paramString + "' -- " + paramThrowable.getClass().getName() + ": " + paramThrowable.getLocalizedMessage());
      if ((paramThrowable instanceof InvocationTargetException))
      {
        Throwable localThrowable1 = ((InvocationTargetException)paramThrowable).getTargetException();
        if (localThrowable1 != null)
        {
          logDiagnostic("... InvocationTargetException: " + localThrowable1.getClass().getName() + ": " + localThrowable1.getLocalizedMessage());
          if ((localThrowable1 instanceof ExceptionInInitializerError))
          {
            Throwable localThrowable2 = ((ExceptionInInitializerError)localThrowable1).getException();
            if (localThrowable2 != null)
            {
              StringWriter localStringWriter = new StringWriter();
              localThrowable2.printStackTrace(new PrintWriter(localStringWriter, true));
              logDiagnostic("... ExceptionInInitializerError: " + localStringWriter.toString());
            }
          }
        }
      }
    }
    if (!this.allowFlawedDiscovery) {
      throw new LogConfigurationException(paramThrowable);
    }
  }
  
  private void handleFlawedHierarchy(ClassLoader paramClassLoader, Class paramClass)
    throws LogConfigurationException
  {
    Class localClass1;
    int i;
    if (class$org$apache$commons$logging$Log == null)
    {
      localClass1 = class$("org.apache.commons.logging.Log");
      class$org$apache$commons$logging$Log = localClass1;
      String str = localClass1.getName();
      Class[] arrayOfClass = paramClass.getInterfaces();
      i = 0;
      label31:
      int j = arrayOfClass.length;
      int k = 0;
      if (i < j)
      {
        if (!str.equals(arrayOfClass[i].getName())) {
          break label276;
        }
        k = 1;
      }
      if (k == 0) {
        break label426;
      }
      if (!isDiagnosticsEnabled()) {}
    }
    for (;;)
    {
      try
      {
        if (class$org$apache$commons$logging$Log != null) {
          continue;
        }
        localClass4 = class$("org.apache.commons.logging.Log");
        class$org$apache$commons$logging$Log = localClass4;
        ClassLoader localClassLoader = getClassLoader(localClass4);
        logDiagnostic("Class '" + paramClass.getName() + "' was found in classloader " + LogFactory.objectId(paramClassLoader) + ". It is bound to a Log interface which is not" + " the one loaded from classloader " + LogFactory.objectId(localClassLoader));
      }
      catch (Throwable localThrowable)
      {
        Class localClass4;
        StringBuffer localStringBuffer3;
        label276:
        LogFactory.handleThrowable(localThrowable);
        logDiagnostic("Error while trying to output diagnostics about bad class '" + paramClass + "'");
        continue;
        Class localClass2 = class$org$apache$commons$logging$Log;
        continue;
      }
      if (this.allowFlawedHierarchy) {
        break label337;
      }
      localStringBuffer3 = new StringBuffer();
      localStringBuffer3.append("Terminating logging for this context ");
      localStringBuffer3.append("due to bad log hierarchy. ");
      localStringBuffer3.append("You have more than one version of '");
      if (class$org$apache$commons$logging$Log != null) {
        continue;
      }
      localClass2 = class$("org.apache.commons.logging.Log");
      class$org$apache$commons$logging$Log = localClass2;
      localStringBuffer3.append(localClass2.getName());
      localStringBuffer3.append("' visible.");
      if (isDiagnosticsEnabled()) {
        logDiagnostic(localStringBuffer3.toString());
      }
      throw new LogConfigurationException(localStringBuffer3.toString());
      localClass1 = class$org$apache$commons$logging$Log;
      break;
      i++;
      break label31;
      localClass4 = class$org$apache$commons$logging$Log;
    }
    label337:
    Class localClass3;
    if (isDiagnosticsEnabled())
    {
      StringBuffer localStringBuffer4 = new StringBuffer();
      localStringBuffer4.append("Warning: bad log hierarchy. ");
      localStringBuffer4.append("You have more than one version of '");
      if (class$org$apache$commons$logging$Log != null) {
        break label418;
      }
      localClass3 = class$("org.apache.commons.logging.Log");
      class$org$apache$commons$logging$Log = localClass3;
      localStringBuffer4.append(localClass3.getName());
      localStringBuffer4.append("' visible.");
      logDiagnostic(localStringBuffer4.toString());
    }
    label418:
    label426:
    do
    {
      return;
      localClass3 = class$org$apache$commons$logging$Log;
      break;
      if (!this.allowFlawedDiscovery)
      {
        StringBuffer localStringBuffer1 = new StringBuffer();
        localStringBuffer1.append("Terminating logging for this context. ");
        localStringBuffer1.append("Log class '");
        localStringBuffer1.append(paramClass.getName());
        localStringBuffer1.append("' does not implement the Log interface.");
        if (isDiagnosticsEnabled()) {
          logDiagnostic(localStringBuffer1.toString());
        }
        throw new LogConfigurationException(localStringBuffer1.toString());
      }
    } while (!isDiagnosticsEnabled());
    StringBuffer localStringBuffer2 = new StringBuffer();
    localStringBuffer2.append("[WARNING] Log class '");
    localStringBuffer2.append(paramClass.getName());
    localStringBuffer2.append("' does not implement the Log interface.");
    logDiagnostic(localStringBuffer2.toString());
  }
  
  private void informUponSimilarName(StringBuffer paramStringBuffer, String paramString1, String paramString2)
  {
    if (paramString1.equals(paramString2)) {}
    while (!paramString1.regionMatches(true, 0, paramString2, 0, 5 + PKG_LEN)) {
      return;
    }
    paramStringBuffer.append(" Did you mean '");
    paramStringBuffer.append(paramString2);
    paramStringBuffer.append("'?");
  }
  
  private void initConfiguration()
  {
    this.allowFlawedContext = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedContext", true);
    this.allowFlawedDiscovery = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedDiscovery", true);
    this.allowFlawedHierarchy = getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedHierarchy", true);
  }
  
  private void initDiagnostics()
  {
    ClassLoader localClassLoader = getClassLoader(getClass());
    Object localObject;
    if (localClassLoader == null) {
      localObject = "BOOTLOADER";
    }
    for (;;)
    {
      this.diagnosticPrefix = ("[LogFactoryImpl@" + System.identityHashCode(this) + " from " + (String)localObject + "] ");
      return;
      try
      {
        String str = LogFactory.objectId(localClassLoader);
        localObject = str;
      }
      catch (SecurityException localSecurityException)
      {
        localObject = "UNKNOWN";
      }
    }
  }
  
  protected static boolean isDiagnosticsEnabled()
  {
    return LogFactory.isDiagnosticsEnabled();
  }
  
  private boolean isLogLibraryAvailable(String paramString1, String paramString2)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Checking for '" + paramString1 + "'.");
    }
    try
    {
      if (createLogFromClass(paramString2, getClass().getName(), false) == null)
      {
        if (isDiagnosticsEnabled())
        {
          logDiagnostic("Did not find '" + paramString1 + "'.");
          return false;
        }
      }
      else
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("Found '" + paramString1 + "'.");
        }
        return true;
      }
    }
    catch (LogConfigurationException localLogConfigurationException)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Logging system '" + paramString1 + "' is available but not useable.");
      }
    }
    return false;
  }
  
  public Object getAttribute(String paramString)
  {
    return this.attributes.get(paramString);
  }
  
  public String[] getAttributeNames()
  {
    return (String[])this.attributes.keySet().toArray(new String[this.attributes.size()]);
  }
  
  public Log getInstance(Class paramClass)
    throws LogConfigurationException
  {
    return getInstance(paramClass.getName());
  }
  
  public Log getInstance(String paramString)
    throws LogConfigurationException
  {
    Log localLog = (Log)this.instances.get(paramString);
    if (localLog == null)
    {
      localLog = newInstance(paramString);
      this.instances.put(paramString, localLog);
    }
    return localLog;
  }
  
  protected String getLogClassName()
  {
    if (this.logClassName == null) {
      discoverLogImplementation(getClass().getName());
    }
    return this.logClassName;
  }
  
  protected Constructor getLogConstructor()
    throws LogConfigurationException
  {
    if (this.logConstructor == null) {
      discoverLogImplementation(getClass().getName());
    }
    return this.logConstructor;
  }
  
  protected boolean isJdk13LumberjackAvailable()
  {
    return isLogLibraryAvailable("Jdk13Lumberjack", "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
  }
  
  protected boolean isJdk14Available()
  {
    return isLogLibraryAvailable("Jdk14", "org.apache.commons.logging.impl.Jdk14Logger");
  }
  
  protected boolean isLog4JAvailable()
  {
    return isLogLibraryAvailable("Log4J", "org.apache.commons.logging.impl.Log4JLogger");
  }
  
  protected void logDiagnostic(String paramString)
  {
    if (isDiagnosticsEnabled()) {
      LogFactory.logRawDiagnostic(this.diagnosticPrefix + paramString);
    }
  }
  
  protected Log newInstance(String paramString)
    throws LogConfigurationException
  {
    try
    {
      if (this.logConstructor == null) {}
      Object[] arrayOfObject1;
      for (Log localLog = discoverLogImplementation(paramString); this.logMethod != null; localLog = (Log)this.logConstructor.newInstance(arrayOfObject1))
      {
        Object[] arrayOfObject2 = { this };
        this.logMethod.invoke(localLog, arrayOfObject2);
        return localLog;
        arrayOfObject1 = new Object[] { paramString };
      }
      Throwable localThrowable2;
      Object localObject;
      return localLog;
    }
    catch (LogConfigurationException localLogConfigurationException)
    {
      throw localLogConfigurationException;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localThrowable2 = localInvocationTargetException.getTargetException();
      if (localThrowable2 == null) {}
      for (;;)
      {
        throw new LogConfigurationException(localInvocationTargetException);
        localObject = localThrowable2;
      }
    }
    catch (Throwable localThrowable1)
    {
      LogFactory.handleThrowable(localThrowable1);
      throw new LogConfigurationException(localThrowable1);
    }
  }
  
  public void release()
  {
    logDiagnostic("Releasing all known loggers");
    this.instances.clear();
  }
  
  public void removeAttribute(String paramString)
  {
    this.attributes.remove(paramString);
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    if (this.logConstructor != null) {
      logDiagnostic("setAttribute: call too late; configuration already performed.");
    }
    if (paramObject == null)
    {
      this.attributes.remove(paramString);
      if (paramString.equals("use_tccl")) {
        if ((paramObject == null) || (!Boolean.valueOf(paramObject.toString()).booleanValue())) {
          break label77;
        }
      }
    }
    label77:
    for (boolean bool = true;; bool = false)
    {
      this.useTCCL = bool;
      return;
      this.attributes.put(paramString, paramObject);
      break;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.LogFactoryImpl
 * JD-Core Version:    0.7.0.1
 */