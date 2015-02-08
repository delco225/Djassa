package org.apache.commons.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory
{
  public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
  public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
  public static final String FACTORY_PROPERTIES = "commons-logging.properties";
  public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
  public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
  public static final String PRIORITY_KEY = "priority";
  protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
  public static final String TCCL_KEY = "use_tccl";
  private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
  static Class class$java$lang$Thread;
  static Class class$org$apache$commons$logging$LogFactory;
  private static final String diagnosticPrefix;
  private static PrintStream diagnosticsStream = null;
  protected static Hashtable factories = null;
  protected static volatile LogFactory nullClassLoaderFactory = null;
  private static final ClassLoader thisClassLoader;
  
  static
  {
    Class localClass1;
    if (class$org$apache$commons$logging$LogFactory == null)
    {
      localClass1 = class$("org.apache.commons.logging.LogFactory");
      class$org$apache$commons$logging$LogFactory = localClass1;
      thisClassLoader = getClassLoader(localClass1);
    }
    for (;;)
    {
      try
      {
        localClassLoader = thisClassLoader;
        if (thisClassLoader != null) {
          continue;
        }
        localObject = "BOOTLOADER";
      }
      catch (SecurityException localSecurityException)
      {
        ClassLoader localClassLoader;
        String str;
        Object localObject = "UNKNOWN";
        continue;
        Class localClass2 = class$org$apache$commons$logging$LogFactory;
        continue;
      }
      diagnosticPrefix = "[LogFactory from " + (String)localObject + "] ";
      diagnosticsStream = initDiagnostics();
      if (class$org$apache$commons$logging$LogFactory != null) {
        continue;
      }
      localClass2 = class$("org.apache.commons.logging.LogFactory");
      class$org$apache$commons$logging$LogFactory = localClass2;
      logClassLoaderEnvironment(localClass2);
      factories = createFactoryStore();
      if (isDiagnosticsEnabled()) {
        logDiagnostic("BOOTSTRAP COMPLETED");
      }
      return;
      localClass1 = class$org$apache$commons$logging$LogFactory;
      break;
      str = objectId(localClassLoader);
      localObject = str;
    }
  }
  
  private static void cacheFactory(ClassLoader paramClassLoader, LogFactory paramLogFactory)
  {
    if (paramLogFactory != null)
    {
      if (paramClassLoader == null) {
        nullClassLoaderFactory = paramLogFactory;
      }
    }
    else {
      return;
    }
    factories.put(paramClassLoader, paramLogFactory);
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
  
  protected static Object createFactory(String paramString, ClassLoader paramClassLoader)
  {
    Class localClass1 = null;
    if (paramClassLoader != null) {
      for (;;)
      {
        try
        {
          localClass1 = paramClassLoader.loadClass(paramString);
          Class localClass4;
          if (class$org$apache$commons$logging$LogFactory == null)
          {
            localClass4 = class$("org.apache.commons.logging.LogFactory");
            class$org$apache$commons$logging$LogFactory = localClass4;
            if (localClass4.isAssignableFrom(localClass1))
            {
              if (isDiagnosticsEnabled()) {
                logDiagnostic("Loaded class " + localClass1.getName() + " from classloader " + objectId(paramClassLoader));
              }
              return (LogFactory)localClass1.newInstance();
            }
          }
          else
          {
            localClass4 = class$org$apache$commons$logging$LogFactory;
            continue;
          }
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          localStringBuffer2 = new StringBuffer().append("Factory class ").append(localClass1.getName()).append(" loaded from classloader ").append(objectId(localClass1.getClassLoader())).append(" does not extend '");
          if (class$org$apache$commons$logging$LogFactory != null) {
            continue;
          }
          localClass5 = class$("org.apache.commons.logging.LogFactory");
          class$org$apache$commons$logging$LogFactory = localClass5;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          StringBuffer localStringBuffer2;
          if (paramClassLoader != thisClassLoader) {
            break label578;
          }
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          logDiagnostic("Unable to locate any class called '" + paramString + "' via classloader " + objectId(paramClassLoader));
          throw localClassNotFoundException;
        }
        catch (Exception localException)
        {
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          logDiagnostic("Unable to create LogFactory instance.");
          if (localClass1 == null) {
            break label637;
          }
          if (class$org$apache$commons$logging$LogFactory != null) {
            break label629;
          }
          localClass2 = class$("org.apache.commons.logging.LogFactory");
          class$org$apache$commons$logging$LogFactory = localClass2;
          if (localClass2.isAssignableFrom(localClass1)) {
            break label637;
          }
          return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", localException);
          Class localClass5 = class$org$apache$commons$logging$LogFactory;
          continue;
        }
        catch (NoClassDefFoundError localNoClassDefFoundError)
        {
          if (paramClassLoader != thisClassLoader) {
            break label578;
          }
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          logDiagnostic("Class '" + paramString + "' cannot be loaded" + " via classloader " + objectId(paramClassLoader) + " - it depends on some other class that cannot be found.");
          throw localNoClassDefFoundError;
        }
        catch (ClassCastException localClassCastException)
        {
          if (paramClassLoader != thisClassLoader) {
            break label578;
          }
        }
        logDiagnostic(localClass5.getName() + "' as loaded by this classloader.");
        logHierarchy("[BAD CL TREE] ", paramClassLoader);
      }
    }
    for (;;)
    {
      boolean bool = implementsLogFactory(localClass1);
      StringBuffer localStringBuffer1 = new StringBuffer();
      localStringBuffer1.append("The application has specified that a custom LogFactory implementation ");
      localStringBuffer1.append("should be used but Class '");
      localStringBuffer1.append(paramString);
      localStringBuffer1.append("' cannot be converted to '");
      Class localClass3;
      if (class$org$apache$commons$logging$LogFactory == null)
      {
        localClass3 = class$("org.apache.commons.logging.LogFactory");
        class$org$apache$commons$logging$LogFactory = localClass3;
        localStringBuffer1.append(localClass3.getName());
        localStringBuffer1.append("'. ");
        if (!bool) {
          break label567;
        }
        localStringBuffer1.append("The conflict is caused by the presence of multiple LogFactory classes ");
        localStringBuffer1.append("in incompatible classloaders. ");
        localStringBuffer1.append("Background can be found in http://commons.apache.org/logging/tech.html. ");
        localStringBuffer1.append("If you have not explicitly specified a custom LogFactory then it is likely ");
        localStringBuffer1.append("that the container has set one without your knowledge. ");
        localStringBuffer1.append("In this case, consider using the commons-logging-adapters.jar file or ");
        localStringBuffer1.append("specifying the standard LogFactory from the command line. ");
      }
      for (;;)
      {
        localStringBuffer1.append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.");
        if (isDiagnosticsEnabled()) {
          logDiagnostic(localStringBuffer1.toString());
        }
        throw new ClassCastException(localStringBuffer1.toString());
        localClass3 = class$org$apache$commons$logging$LogFactory;
        break;
        label567:
        localStringBuffer1.append("Please check the custom implementation. ");
      }
      label578:
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Unable to load factory class via classloader " + objectId(paramClassLoader) + " - trying the classloader associated with this LogFactory.");
      }
      localClass1 = Class.forName(paramString);
      LogFactory localLogFactory = (LogFactory)localClass1.newInstance();
      return localLogFactory;
      label629:
      Class localClass2 = class$org$apache$commons$logging$LogFactory;
    }
    label637:
    return new LogConfigurationException(localException);
  }
  
  private static final Hashtable createFactoryStore()
  {
    try
    {
      String str2 = getSystemProperty("org.apache.commons.logging.LogFactory.HashtableImpl", null);
      str1 = str2;
    }
    catch (SecurityException localSecurityException)
    {
      try
      {
        localHashtable = (Hashtable)Class.forName(str1).newInstance();
        if (localHashtable != null) {
          break label44;
        }
        localHashtable = new Hashtable();
        label44:
        return localHashtable;
        localSecurityException = localSecurityException;
        str1 = null;
      }
      catch (Throwable localThrowable)
      {
        for (;;)
        {
          String str1;
          handleThrowable(localThrowable);
          boolean bool = "org.apache.commons.logging.impl.WeakHashtable".equals(str1);
          Hashtable localHashtable = null;
          if (!bool) {
            if (isDiagnosticsEnabled())
            {
              logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
              localHashtable = null;
            }
            else
            {
              System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
              localHashtable = null;
            }
          }
        }
      }
    }
    if (str1 == null) {
      str1 = "org.apache.commons.logging.impl.WeakHashtable";
    }
  }
  
  /* Error */
  protected static ClassLoader directGetContextClassLoader()
    throws LogConfigurationException
  {
    // Byte code:
    //   0: getstatic 277	org/apache/commons/logging/LogFactory:class$java$lang$Thread	Ljava/lang/Class;
    //   3: ifnonnull +50 -> 53
    //   6: ldc_w 279
    //   9: invokestatic 60	org/apache/commons/logging/LogFactory:class$	(Ljava/lang/String;)Ljava/lang/Class;
    //   12: astore 8
    //   14: aload 8
    //   16: putstatic 277	org/apache/commons/logging/LogFactory:class$java$lang$Thread	Ljava/lang/Class;
    //   19: aload 8
    //   21: astore_2
    //   22: aload_2
    //   23: ldc_w 281
    //   26: aconst_null
    //   27: checkcast 283	[Ljava/lang/Class;
    //   30: invokevirtual 287	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   33: astore_3
    //   34: aload_3
    //   35: invokestatic 293	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   38: aconst_null
    //   39: checkcast 295	[Ljava/lang/Object;
    //   42: invokevirtual 301	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   45: checkcast 149	java/lang/ClassLoader
    //   48: astore 6
    //   50: aload 6
    //   52: areturn
    //   53: getstatic 277	org/apache/commons/logging/LogFactory:class$java$lang$Thread	Ljava/lang/Class;
    //   56: astore_2
    //   57: goto -35 -> 22
    //   60: astore 7
    //   62: new 192	org/apache/commons/logging/LogConfigurationException
    //   65: dup
    //   66: ldc_w 303
    //   69: aload 7
    //   71: invokespecial 197	org/apache/commons/logging/LogConfigurationException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   74: athrow
    //   75: astore_0
    //   76: getstatic 56	org/apache/commons/logging/LogFactory:class$org$apache$commons$logging$LogFactory	Ljava/lang/Class;
    //   79: ifnonnull +54 -> 133
    //   82: ldc 17
    //   84: invokestatic 60	org/apache/commons/logging/LogFactory:class$	(Ljava/lang/String;)Ljava/lang/Class;
    //   87: astore_1
    //   88: aload_1
    //   89: putstatic 56	org/apache/commons/logging/LogFactory:class$org$apache$commons$logging$LogFactory	Ljava/lang/Class;
    //   92: aload_1
    //   93: invokestatic 64	org/apache/commons/logging/LogFactory:getClassLoader	(Ljava/lang/Class;)Ljava/lang/ClassLoader;
    //   96: areturn
    //   97: astore 4
    //   99: aload 4
    //   101: invokevirtual 307	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
    //   104: instanceof 48
    //   107: istore 5
    //   109: aconst_null
    //   110: astore 6
    //   112: iload 5
    //   114: ifne -64 -> 50
    //   117: new 192	org/apache/commons/logging/LogConfigurationException
    //   120: dup
    //   121: ldc_w 309
    //   124: aload 4
    //   126: invokevirtual 307	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
    //   129: invokespecial 197	org/apache/commons/logging/LogConfigurationException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   132: athrow
    //   133: getstatic 56	org/apache/commons/logging/LogFactory:class$org$apache$commons$logging$LogFactory	Ljava/lang/Class;
    //   136: astore_1
    //   137: goto -45 -> 92
    // Local variable table:
    //   start	length	slot	name	signature
    //   75	1	0	localNoSuchMethodException	java.lang.NoSuchMethodException
    //   87	50	1	localClass1	Class
    //   21	36	2	localClass2	Class
    //   33	2	3	localMethod	java.lang.reflect.Method
    //   97	28	4	localInvocationTargetException	java.lang.reflect.InvocationTargetException
    //   107	6	5	bool	boolean
    //   48	63	6	localClassLoader	ClassLoader
    //   60	10	7	localIllegalAccessException	java.lang.IllegalAccessException
    //   12	8	8	localClass3	Class
    // Exception table:
    //   from	to	target	type
    //   34	50	60	java/lang/IllegalAccessException
    //   0	19	75	java/lang/NoSuchMethodException
    //   22	34	75	java/lang/NoSuchMethodException
    //   34	50	75	java/lang/NoSuchMethodException
    //   53	57	75	java/lang/NoSuchMethodException
    //   62	75	75	java/lang/NoSuchMethodException
    //   99	109	75	java/lang/NoSuchMethodException
    //   117	133	75	java/lang/NoSuchMethodException
    //   34	50	97	java/lang/reflect/InvocationTargetException
  }
  
  private static LogFactory getCachedFactory(ClassLoader paramClassLoader)
  {
    if (paramClassLoader == null) {
      return nullClassLoaderFactory;
    }
    return (LogFactory)factories.get(paramClassLoader);
  }
  
  protected static ClassLoader getClassLoader(Class paramClass)
  {
    try
    {
      ClassLoader localClassLoader = paramClass.getClassLoader();
      return localClassLoader;
    }
    catch (SecurityException localSecurityException)
    {
      if (isDiagnosticsEnabled()) {
        logDiagnostic("Unable to get classloader for class '" + paramClass + "' due to security restrictions - " + localSecurityException.getMessage());
      }
      throw localSecurityException;
    }
  }
  
  private static final Properties getConfigurationFile(ClassLoader paramClassLoader, String paramString)
  {
    Object localObject1 = null;
    double d1 = 0.0D;
    Object localObject2 = null;
    for (;;)
    {
      URL localURL;
      Properties localProperties;
      try
      {
        Enumeration localEnumeration = getResources(paramClassLoader, paramString);
        localObject1 = null;
        localObject2 = null;
        if (localEnumeration == null) {
          return null;
        }
        if (localEnumeration.hasMoreElements())
        {
          localURL = (URL)localEnumeration.nextElement();
          localProperties = getProperties(localURL);
          if (localProperties == null) {
            continue;
          }
          if (localObject1 != null) {
            break label196;
          }
          localObject2 = localURL;
          localObject1 = localProperties;
          String str2 = localObject1.getProperty("priority");
          d1 = 0.0D;
          if (str2 != null) {
            d1 = Double.parseDouble(str2);
          }
          if (!isDiagnosticsEnabled()) {
            continue;
          }
          logDiagnostic("[LOOKUP] Properties file found at '" + localURL + "'" + " with priority " + d1);
        }
        str1 = localProperties.getProperty("priority");
      }
      catch (SecurityException localSecurityException)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("SecurityException thrown while trying to find/read config files.");
        }
        if (isDiagnosticsEnabled())
        {
          if (localObject1 != null) {
            break label381;
          }
          logDiagnostic("[LOOKUP] No properties file of name '" + paramString + "' found.");
        }
        return localObject1;
      }
      label196:
      String str1;
      double d2 = 0.0D;
      if (str1 != null) {
        d2 = Double.parseDouble(str1);
      }
      if (d2 > d1)
      {
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[LOOKUP] Properties file at '" + localURL + "'" + " with priority " + d2 + " overrides file at '" + localObject2 + "'" + " with priority " + d1);
        }
      }
      else
      {
        if (!isDiagnosticsEnabled()) {
          continue;
        }
        logDiagnostic("[LOOKUP] Properties file at '" + localURL + "'" + " with priority " + d2 + " does not override file at '" + localObject2 + "'" + " with priority " + d1);
        continue;
        label381:
        logDiagnostic("[LOOKUP] Properties file of name '" + paramString + "' found at '" + localObject2 + '"');
        continue;
      }
      localObject2 = localURL;
      localObject1 = localProperties;
      d1 = d2;
    }
  }
  
  protected static ClassLoader getContextClassLoader()
    throws LogConfigurationException
  {
    return directGetContextClassLoader();
  }
  
  private static ClassLoader getContextClassLoaderInternal()
    throws LogConfigurationException
  {
    return (ClassLoader)AccessController.doPrivileged(new LogFactory.1());
  }
  
  /* Error */
  public static LogFactory getFactory()
    throws LogConfigurationException
  {
    // Byte code:
    //   0: invokestatic 402	org/apache/commons/logging/LogFactory:getContextClassLoaderInternal	()Ljava/lang/ClassLoader;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnonnull +15 -> 20
    //   8: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   11: ifeq +9 -> 20
    //   14: ldc_w 404
    //   17: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   20: aload_0
    //   21: invokestatic 406	org/apache/commons/logging/LogFactory:getCachedFactory	(Ljava/lang/ClassLoader;)Lorg/apache/commons/logging/LogFactory;
    //   24: astore_1
    //   25: aload_1
    //   26: ifnull +5 -> 31
    //   29: aload_1
    //   30: areturn
    //   31: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   34: ifeq +36 -> 70
    //   37: new 70	java/lang/StringBuffer
    //   40: dup
    //   41: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   44: ldc_w 408
    //   47: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   50: aload_0
    //   51: invokestatic 113	org/apache/commons/logging/LogFactory:objectId	(Ljava/lang/Object;)Ljava/lang/String;
    //   54: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   57: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   60: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   63: ldc_w 410
    //   66: aload_0
    //   67: invokestatic 184	org/apache/commons/logging/LogFactory:logHierarchy	(Ljava/lang/String;Ljava/lang/ClassLoader;)V
    //   70: aload_0
    //   71: ldc 14
    //   73: invokestatic 412	org/apache/commons/logging/LogFactory:getConfigurationFile	(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/util/Properties;
    //   76: astore_2
    //   77: aload_0
    //   78: astore_3
    //   79: aload_2
    //   80: ifnull +31 -> 111
    //   83: aload_2
    //   84: ldc 29
    //   86: invokevirtual 348	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   89: astore 19
    //   91: aload 19
    //   93: ifnull +18 -> 111
    //   96: aload 19
    //   98: invokestatic 418	java/lang/Boolean:valueOf	(Ljava/lang/String;)Ljava/lang/Boolean;
    //   101: invokevirtual 421	java/lang/Boolean:booleanValue	()Z
    //   104: ifne +7 -> 111
    //   107: getstatic 66	org/apache/commons/logging/LogFactory:thisClassLoader	Ljava/lang/ClassLoader;
    //   110: astore_3
    //   111: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   114: ifeq +9 -> 123
    //   117: ldc_w 423
    //   120: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   123: ldc 17
    //   125: aconst_null
    //   126: invokestatic 246	org/apache/commons/logging/LogFactory:getSystemProperty	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   129: astore 17
    //   131: aload 17
    //   133: ifnull +356 -> 489
    //   136: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   139: ifeq +38 -> 177
    //   142: new 70	java/lang/StringBuffer
    //   145: dup
    //   146: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   149: ldc_w 425
    //   152: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   155: aload 17
    //   157: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   160: ldc_w 427
    //   163: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   166: ldc 17
    //   168: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   171: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   174: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   177: aload 17
    //   179: aload_3
    //   180: aload_0
    //   181: invokestatic 431	org/apache/commons/logging/LogFactory:newFactory	(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/ClassLoader;)Lorg/apache/commons/logging/LogFactory;
    //   184: astore 18
    //   186: aload 18
    //   188: astore_1
    //   189: aload_1
    //   190: ifnonnull +140 -> 330
    //   193: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   196: ifeq +9 -> 205
    //   199: ldc_w 433
    //   202: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   205: aload_0
    //   206: ldc 26
    //   208: invokestatic 437	org/apache/commons/logging/LogFactory:getResourceAsStream	(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/io/InputStream;
    //   211: astore 10
    //   213: aload 10
    //   215: ifnull +410 -> 625
    //   218: new 439	java/io/InputStreamReader
    //   221: dup
    //   222: aload 10
    //   224: ldc_w 441
    //   227: invokespecial 444	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   230: astore 11
    //   232: new 446	java/io/BufferedReader
    //   235: dup
    //   236: aload 11
    //   238: invokespecial 449	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   241: astore 12
    //   243: aload 12
    //   245: invokevirtual 452	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   248: astore 13
    //   250: aload 12
    //   252: invokevirtual 455	java/io/BufferedReader:close	()V
    //   255: aload 13
    //   257: ifnull +73 -> 330
    //   260: ldc_w 457
    //   263: aload 13
    //   265: invokevirtual 256	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   268: ifne +62 -> 330
    //   271: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   274: ifeq +44 -> 318
    //   277: new 70	java/lang/StringBuffer
    //   280: dup
    //   281: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   284: ldc_w 459
    //   287: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   290: aload 13
    //   292: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   295: ldc_w 461
    //   298: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   301: ldc 26
    //   303: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   306: ldc_w 463
    //   309: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   312: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   315: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   318: aload 13
    //   320: aload_3
    //   321: aload_0
    //   322: invokestatic 431	org/apache/commons/logging/LogFactory:newFactory	(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/ClassLoader;)Lorg/apache/commons/logging/LogFactory;
    //   325: astore 14
    //   327: aload 14
    //   329: astore_1
    //   330: aload_1
    //   331: ifnonnull +76 -> 407
    //   334: aload_2
    //   335: ifnull +367 -> 702
    //   338: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   341: ifeq +9 -> 350
    //   344: ldc_w 465
    //   347: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   350: aload_2
    //   351: ldc 17
    //   353: invokevirtual 348	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   356: astore 8
    //   358: aload 8
    //   360: ifnull +327 -> 687
    //   363: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   366: ifeq +33 -> 399
    //   369: new 70	java/lang/StringBuffer
    //   372: dup
    //   373: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   376: ldc_w 467
    //   379: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   382: aload 8
    //   384: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   387: ldc_w 358
    //   390: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   393: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   396: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   399: aload 8
    //   401: aload_3
    //   402: aload_0
    //   403: invokestatic 431	org/apache/commons/logging/LogFactory:newFactory	(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/ClassLoader;)Lorg/apache/commons/logging/LogFactory;
    //   406: astore_1
    //   407: aload_1
    //   408: ifnonnull +25 -> 433
    //   411: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   414: ifeq +9 -> 423
    //   417: ldc_w 469
    //   420: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   423: ldc 11
    //   425: getstatic 66	org/apache/commons/logging/LogFactory:thisClassLoader	Ljava/lang/ClassLoader;
    //   428: aload_0
    //   429: invokestatic 431	org/apache/commons/logging/LogFactory:newFactory	(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/ClassLoader;)Lorg/apache/commons/logging/LogFactory;
    //   432: astore_1
    //   433: aload_1
    //   434: ifnull +283 -> 717
    //   437: aload_0
    //   438: aload_1
    //   439: invokestatic 471	org/apache/commons/logging/LogFactory:cacheFactory	(Ljava/lang/ClassLoader;Lorg/apache/commons/logging/LogFactory;)V
    //   442: aload_2
    //   443: ifnull +274 -> 717
    //   446: aload_2
    //   447: invokevirtual 475	java/util/Properties:propertyNames	()Ljava/util/Enumeration;
    //   450: astore 6
    //   452: aload 6
    //   454: invokeinterface 333 1 0
    //   459: ifeq +258 -> 717
    //   462: aload 6
    //   464: invokeinterface 336 1 0
    //   469: checkcast 252	java/lang/String
    //   472: astore 7
    //   474: aload_1
    //   475: aload 7
    //   477: aload_2
    //   478: aload 7
    //   480: invokevirtual 348	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   483: invokevirtual 479	org/apache/commons/logging/LogFactory:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   486: goto -34 -> 452
    //   489: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   492: ifeq -303 -> 189
    //   495: ldc_w 481
    //   498: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   501: goto -312 -> 189
    //   504: astore 5
    //   506: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   509: ifeq -320 -> 189
    //   512: new 70	java/lang/StringBuffer
    //   515: dup
    //   516: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   519: ldc_w 483
    //   522: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   525: aload 5
    //   527: invokevirtual 139	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   530: invokestatic 486	org/apache/commons/logging/LogFactory:trim	(Ljava/lang/String;)Ljava/lang/String;
    //   533: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   536: ldc_w 488
    //   539: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   542: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   545: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   548: goto -359 -> 189
    //   551: astore 4
    //   553: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   556: ifeq +39 -> 595
    //   559: new 70	java/lang/StringBuffer
    //   562: dup
    //   563: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   566: ldc_w 490
    //   569: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   572: aload 4
    //   574: invokevirtual 139	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   577: invokestatic 486	org/apache/commons/logging/LogFactory:trim	(Ljava/lang/String;)Ljava/lang/String;
    //   580: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   583: ldc_w 492
    //   586: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   589: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   592: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   595: aload 4
    //   597: athrow
    //   598: astore 15
    //   600: new 439	java/io/InputStreamReader
    //   603: dup
    //   604: aload 10
    //   606: invokespecial 495	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   609: astore 16
    //   611: new 446	java/io/BufferedReader
    //   614: dup
    //   615: aload 16
    //   617: invokespecial 449	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   620: astore 12
    //   622: goto -379 -> 243
    //   625: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   628: ifeq -298 -> 330
    //   631: ldc_w 497
    //   634: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   637: goto -307 -> 330
    //   640: astore 9
    //   642: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   645: ifeq -315 -> 330
    //   648: new 70	java/lang/StringBuffer
    //   651: dup
    //   652: invokespecial 73	java/lang/StringBuffer:<init>	()V
    //   655: ldc_w 483
    //   658: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   661: aload 9
    //   663: invokevirtual 139	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   666: invokestatic 486	org/apache/commons/logging/LogFactory:trim	(Ljava/lang/String;)Ljava/lang/String;
    //   669: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   672: ldc_w 488
    //   675: invokevirtual 79	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   678: invokevirtual 85	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   681: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   684: goto -354 -> 330
    //   687: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   690: ifeq -283 -> 407
    //   693: ldc_w 499
    //   696: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   699: goto -292 -> 407
    //   702: invokestatic 103	org/apache/commons/logging/LogFactory:isDiagnosticsEnabled	()Z
    //   705: ifeq -298 -> 407
    //   708: ldc_w 501
    //   711: invokestatic 109	org/apache/commons/logging/LogFactory:logDiagnostic	(Ljava/lang/String;)V
    //   714: goto -307 -> 407
    //   717: aload_1
    //   718: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	435	0	localClassLoader1	ClassLoader
    //   24	694	1	localObject	Object
    //   76	402	2	localProperties	Properties
    //   78	324	3	localClassLoader2	ClassLoader
    //   551	45	4	localRuntimeException	java.lang.RuntimeException
    //   504	22	5	localSecurityException	SecurityException
    //   450	13	6	localEnumeration	Enumeration
    //   472	7	7	str1	String
    //   356	44	8	str2	String
    //   640	22	9	localException	Exception
    //   211	394	10	localInputStream	InputStream
    //   230	7	11	localInputStreamReader1	java.io.InputStreamReader
    //   241	380	12	localBufferedReader	java.io.BufferedReader
    //   248	71	13	str3	String
    //   325	3	14	localLogFactory1	LogFactory
    //   598	1	15	localUnsupportedEncodingException	java.io.UnsupportedEncodingException
    //   609	7	16	localInputStreamReader2	java.io.InputStreamReader
    //   129	49	17	str4	String
    //   184	3	18	localLogFactory2	LogFactory
    //   89	8	19	str5	String
    // Exception table:
    //   from	to	target	type
    //   123	131	504	java/lang/SecurityException
    //   136	177	504	java/lang/SecurityException
    //   177	186	504	java/lang/SecurityException
    //   489	501	504	java/lang/SecurityException
    //   123	131	551	java/lang/RuntimeException
    //   136	177	551	java/lang/RuntimeException
    //   177	186	551	java/lang/RuntimeException
    //   489	501	551	java/lang/RuntimeException
    //   218	243	598	java/io/UnsupportedEncodingException
    //   205	213	640	java/lang/Exception
    //   218	243	640	java/lang/Exception
    //   243	255	640	java/lang/Exception
    //   260	318	640	java/lang/Exception
    //   318	327	640	java/lang/Exception
    //   600	622	640	java/lang/Exception
    //   625	637	640	java/lang/Exception
  }
  
  public static Log getLog(Class paramClass)
    throws LogConfigurationException
  {
    return getFactory().getInstance(paramClass);
  }
  
  public static Log getLog(String paramString)
    throws LogConfigurationException
  {
    return getFactory().getInstance(paramString);
  }
  
  private static Properties getProperties(URL paramURL)
  {
    return (Properties)AccessController.doPrivileged(new LogFactory.5(paramURL));
  }
  
  private static InputStream getResourceAsStream(ClassLoader paramClassLoader, String paramString)
  {
    return (InputStream)AccessController.doPrivileged(new LogFactory.3(paramClassLoader, paramString));
  }
  
  private static Enumeration getResources(ClassLoader paramClassLoader, String paramString)
  {
    return (Enumeration)AccessController.doPrivileged(new LogFactory.4(paramClassLoader, paramString));
  }
  
  private static String getSystemProperty(String paramString1, String paramString2)
    throws SecurityException
  {
    return (String)AccessController.doPrivileged(new LogFactory.6(paramString1, paramString2));
  }
  
  protected static void handleThrowable(Throwable paramThrowable)
  {
    if ((paramThrowable instanceof ThreadDeath)) {
      throw ((ThreadDeath)paramThrowable);
    }
    if ((paramThrowable instanceof VirtualMachineError)) {
      throw ((VirtualMachineError)paramThrowable);
    }
  }
  
  private static boolean implementsLogFactory(Class paramClass)
  {
    boolean bool = false;
    if (paramClass != null) {
      try
      {
        ClassLoader localClassLoader = paramClass.getClassLoader();
        if (localClassLoader == null)
        {
          logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
          return false;
        }
        logHierarchy("[CUSTOM LOG FACTORY] ", localClassLoader);
        bool = Class.forName("org.apache.commons.logging.LogFactory", false, localClassLoader).isAssignableFrom(paramClass);
        if (bool)
        {
          logDiagnostic("[CUSTOM LOG FACTORY] " + paramClass.getName() + " implements LogFactory but was loaded by an incompatible classloader.");
          return bool;
        }
      }
      catch (SecurityException localSecurityException)
      {
        logDiagnostic("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + localSecurityException.getMessage());
        return bool;
        logDiagnostic("[CUSTOM LOG FACTORY] " + paramClass.getName() + " does not implement LogFactory.");
        return bool;
      }
      catch (LinkageError localLinkageError)
      {
        logDiagnostic("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + localLinkageError.getMessage());
        return bool;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
      }
    }
    return bool;
  }
  
  private static PrintStream initDiagnostics()
  {
    String str;
    try
    {
      str = getSystemProperty("org.apache.commons.logging.diagnostics.dest", null);
      if (str == null) {
        return null;
      }
    }
    catch (SecurityException localSecurityException)
    {
      return null;
    }
    if (str.equals("STDOUT")) {
      return System.out;
    }
    if (str.equals("STDERR")) {
      return System.err;
    }
    try
    {
      PrintStream localPrintStream = new PrintStream(new FileOutputStream(str, true));
      return localPrintStream;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  protected static boolean isDiagnosticsEnabled()
  {
    return diagnosticsStream != null;
  }
  
  private static void logClassLoaderEnvironment(Class paramClass)
  {
    if (!isDiagnosticsEnabled()) {
      return;
    }
    try
    {
      logDiagnostic("[ENV] Extension directories (java.ext.dir): " + System.getProperty("java.ext.dir"));
      logDiagnostic("[ENV] Application classpath (java.class.path): " + System.getProperty("java.class.path"));
      str = paramClass.getName();
    }
    catch (SecurityException localSecurityException1)
    {
      for (;;)
      {
        try
        {
          ClassLoader localClassLoader = getClassLoader(paramClass);
          logDiagnostic("[ENV] Class " + str + " was loaded via classloader " + objectId(localClassLoader));
          logHierarchy("[ENV] Ancestry of classloader which loaded " + str + " is ", localClassLoader);
          return;
        }
        catch (SecurityException localSecurityException2)
        {
          String str;
          logDiagnostic("[ENV] Security forbids determining the classloader for " + str);
        }
        localSecurityException1 = localSecurityException1;
        logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
      }
    }
  }
  
  private static final void logDiagnostic(String paramString)
  {
    if (diagnosticsStream != null)
    {
      diagnosticsStream.print(diagnosticPrefix);
      diagnosticsStream.println(paramString);
      diagnosticsStream.flush();
    }
  }
  
  private static void logHierarchy(String paramString, ClassLoader paramClassLoader)
  {
    if (!isDiagnosticsEnabled()) {
      return;
    }
    if (paramClassLoader != null)
    {
      String str = paramClassLoader.toString();
      logDiagnostic(paramString + objectId(paramClassLoader) + " == '" + str + "'");
    }
    for (;;)
    {
      StringBuffer localStringBuffer;
      try
      {
        ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
        if (paramClassLoader == null) {
          break;
        }
        localStringBuffer = new StringBuffer(paramString + "ClassLoader tree:");
        localStringBuffer.append(objectId(paramClassLoader));
        if (paramClassLoader == localClassLoader1) {
          localStringBuffer.append(" (SYSTEM) ");
        }
      }
      catch (SecurityException localSecurityException1)
      {
        ClassLoader localClassLoader2;
        logDiagnostic(paramString + "Security forbids determining the system classloader.");
        return;
      }
      try
      {
        localClassLoader2 = paramClassLoader.getParent();
        paramClassLoader = localClassLoader2;
        localStringBuffer.append(" --> ");
        if (paramClassLoader == null) {
          localStringBuffer.append("BOOT");
        }
      }
      catch (SecurityException localSecurityException2)
      {
        localStringBuffer.append(" --> SECRET");
      }
    }
    logDiagnostic(localStringBuffer.toString());
  }
  
  protected static final void logRawDiagnostic(String paramString)
  {
    if (diagnosticsStream != null)
    {
      diagnosticsStream.println(paramString);
      diagnosticsStream.flush();
    }
  }
  
  protected static LogFactory newFactory(String paramString, ClassLoader paramClassLoader)
  {
    return newFactory(paramString, paramClassLoader, null);
  }
  
  protected static LogFactory newFactory(String paramString, ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
    throws LogConfigurationException
  {
    Object localObject = AccessController.doPrivileged(new LogFactory.2(paramString, paramClassLoader1));
    if ((localObject instanceof LogConfigurationException))
    {
      LogConfigurationException localLogConfigurationException = (LogConfigurationException)localObject;
      if (isDiagnosticsEnabled()) {
        logDiagnostic("An error occurred while loading the factory class:" + localLogConfigurationException.getMessage());
      }
      throw localLogConfigurationException;
    }
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Created object " + objectId(localObject) + " to manage classloader " + objectId(paramClassLoader2));
    }
    return (LogFactory)localObject;
  }
  
  public static String objectId(Object paramObject)
  {
    if (paramObject == null) {
      return "null";
    }
    return paramObject.getClass().getName() + "@" + System.identityHashCode(paramObject);
  }
  
  public static void release(ClassLoader paramClassLoader)
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Releasing factory for classloader " + objectId(paramClassLoader));
    }
    localHashtable = factories;
    if (paramClassLoader == null) {}
    for (;;)
    {
      try
      {
        if (nullClassLoaderFactory != null)
        {
          nullClassLoaderFactory.release();
          nullClassLoaderFactory = null;
        }
        return;
      }
      finally {}
      LogFactory localLogFactory = (LogFactory)localHashtable.get(paramClassLoader);
      if (localLogFactory != null)
      {
        localLogFactory.release();
        localHashtable.remove(paramClassLoader);
      }
    }
  }
  
  public static void releaseAll()
  {
    if (isDiagnosticsEnabled()) {
      logDiagnostic("Releasing factory for all classloaders.");
    }
    synchronized (factories)
    {
      Enumeration localEnumeration = ???.elements();
      if (localEnumeration.hasMoreElements()) {
        ((LogFactory)localEnumeration.nextElement()).release();
      }
    }
    ???.clear();
    if (nullClassLoaderFactory != null)
    {
      nullClassLoaderFactory.release();
      nullClassLoaderFactory = null;
    }
  }
  
  private static String trim(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return paramString.trim();
  }
  
  public abstract Object getAttribute(String paramString);
  
  public abstract String[] getAttributeNames();
  
  public abstract Log getInstance(Class paramClass)
    throws LogConfigurationException;
  
  public abstract Log getInstance(String paramString)
    throws LogConfigurationException;
  
  public abstract void release();
  
  public abstract void removeAttribute(String paramString);
  
  public abstract void setAttribute(String paramString, Object paramObject);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory
 * JD-Core Version:    0.7.0.1
 */