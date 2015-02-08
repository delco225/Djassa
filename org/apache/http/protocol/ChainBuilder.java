package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
final class ChainBuilder<E>
{
  private final LinkedList<E> list = new LinkedList();
  private final Map<Class<?>, E> uniqueClasses = new HashMap();
  
  private void ensureUnique(E paramE)
  {
    Object localObject = this.uniqueClasses.remove(paramE.getClass());
    if (localObject != null) {
      this.list.remove(localObject);
    }
    this.uniqueClasses.put(paramE.getClass(), paramE);
  }
  
  public ChainBuilder<E> addAllFirst(Collection<E> paramCollection)
  {
    if (paramCollection == null) {}
    for (;;)
    {
      return this;
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        addFirst(localIterator.next());
      }
    }
  }
  
  public ChainBuilder<E> addAllFirst(E... paramVarArgs)
  {
    if (paramVarArgs == null) {}
    for (;;)
    {
      return this;
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++) {
        addFirst(paramVarArgs[j]);
      }
    }
  }
  
  public ChainBuilder<E> addAllLast(Collection<E> paramCollection)
  {
    if (paramCollection == null) {}
    for (;;)
    {
      return this;
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        addLast(localIterator.next());
      }
    }
  }
  
  public ChainBuilder<E> addAllLast(E... paramVarArgs)
  {
    if (paramVarArgs == null) {}
    for (;;)
    {
      return this;
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++) {
        addLast(paramVarArgs[j]);
      }
    }
  }
  
  public ChainBuilder<E> addFirst(E paramE)
  {
    if (paramE == null) {
      return this;
    }
    ensureUnique(paramE);
    this.list.addFirst(paramE);
    return this;
  }
  
  public ChainBuilder<E> addLast(E paramE)
  {
    if (paramE == null) {
      return this;
    }
    ensureUnique(paramE);
    this.list.addLast(paramE);
    return this;
  }
  
  public LinkedList<E> build()
  {
    return new LinkedList(this.list);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.ChainBuilder
 * JD-Core Version:    0.7.0.1
 */