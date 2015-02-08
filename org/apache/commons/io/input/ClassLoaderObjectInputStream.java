package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Proxy;

public class ClassLoaderObjectInputStream
  extends ObjectInputStream
{
  private final ClassLoader classLoader;
  
  public ClassLoaderObjectInputStream(ClassLoader paramClassLoader, InputStream paramInputStream)
    throws IOException, StreamCorruptedException
  {
    super(paramInputStream);
    this.classLoader = paramClassLoader;
  }
  
  protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
    throws IOException, ClassNotFoundException
  {
    Class localClass = Class.forName(paramObjectStreamClass.getName(), false, this.classLoader);
    if (localClass != null) {
      return localClass;
    }
    return super.resolveClass(paramObjectStreamClass);
  }
  
  protected Class<?> resolveProxyClass(String[] paramArrayOfString)
    throws IOException, ClassNotFoundException
  {
    Class[] arrayOfClass = new Class[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      arrayOfClass[i] = Class.forName(paramArrayOfString[i], false, this.classLoader);
    }
    try
    {
      Class localClass = Proxy.getProxyClass(this.classLoader, arrayOfClass);
      return localClass;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return super.resolveProxyClass(paramArrayOfString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ClassLoaderObjectInputStream
 * JD-Core Version:    0.7.0.1
 */