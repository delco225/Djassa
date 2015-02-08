package org.apache.commons.logging.impl;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class WeakHashtable
  extends Hashtable
{
  private static final int MAX_CHANGES_BEFORE_PURGE = 100;
  private static final int PARTIAL_PURGE_COUNT = 10;
  private static final long serialVersionUID = -1546036869799732453L;
  private int changeCount = 0;
  private final ReferenceQueue queue = new ReferenceQueue();
  
  private void purge()
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (this.queue)
    {
      WeakKey localWeakKey = (WeakKey)this.queue.poll();
      if (localWeakKey != null) {
        localArrayList.add(localWeakKey.getReferenced());
      }
    }
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      super.remove(localArrayList.get(j));
    }
  }
  
  private void purgeOne()
  {
    synchronized (this.queue)
    {
      WeakKey localWeakKey = (WeakKey)this.queue.poll();
      if (localWeakKey != null) {
        super.remove(localWeakKey.getReferenced());
      }
      return;
    }
  }
  
  public boolean containsKey(Object paramObject)
  {
    return super.containsKey(new Referenced(paramObject, null));
  }
  
  public Enumeration elements()
  {
    purge();
    return super.elements();
  }
  
  public Set entrySet()
  {
    purge();
    Set localSet = super.entrySet();
    HashSet localHashSet = new HashSet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = ((Referenced)localEntry.getKey()).getValue();
      Object localObject2 = localEntry.getValue();
      if (localObject1 != null) {
        localHashSet.add(new Entry(localObject1, localObject2, null));
      }
    }
    return localHashSet;
  }
  
  public Object get(Object paramObject)
  {
    return super.get(new Referenced(paramObject, null));
  }
  
  public boolean isEmpty()
  {
    purge();
    return super.isEmpty();
  }
  
  public Set keySet()
  {
    purge();
    Set localSet = super.keySet();
    HashSet localHashSet = new HashSet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = ((Referenced)localIterator.next()).getValue();
      if (localObject != null) {
        localHashSet.add(localObject);
      }
    }
    return localHashSet;
  }
  
  public Enumeration keys()
  {
    purge();
    return new WeakHashtable.1(this, super.keys());
  }
  
  public Object put(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == null) {
      try
      {
        throw new NullPointerException("Null keys are not allowed");
      }
      finally {}
    }
    if (paramObject2 == null) {
      throw new NullPointerException("Null values are not allowed");
    }
    int i = this.changeCount;
    this.changeCount = (i + 1);
    if (i > 100)
    {
      purge();
      this.changeCount = 0;
    }
    for (;;)
    {
      Object localObject1 = super.put(new Referenced(paramObject1, this.queue, null), paramObject2);
      return localObject1;
      if (this.changeCount % 10 == 0) {
        purgeOne();
      }
    }
  }
  
  public void putAll(Map paramMap)
  {
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        put(localEntry.getKey(), localEntry.getValue());
      }
    }
  }
  
  protected void rehash()
  {
    purge();
    super.rehash();
  }
  
  /* Error */
  public Object remove(Object paramObject)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 27	org/apache/commons/logging/impl/WeakHashtable:changeCount	I
    //   6: istore_3
    //   7: aload_0
    //   8: iload_3
    //   9: iconst_1
    //   10: iadd
    //   11: putfield 27	org/apache/commons/logging/impl/WeakHashtable:changeCount	I
    //   14: iload_3
    //   15: bipush 100
    //   17: if_icmple +32 -> 49
    //   20: aload_0
    //   21: invokespecial 72	org/apache/commons/logging/impl/WeakHashtable:purge	()V
    //   24: aload_0
    //   25: iconst_0
    //   26: putfield 27	org/apache/commons/logging/impl/WeakHashtable:changeCount	I
    //   29: aload_0
    //   30: new 63	org/apache/commons/logging/impl/WeakHashtable$Referenced
    //   33: dup
    //   34: aload_1
    //   35: aconst_null
    //   36: invokespecial 66	org/apache/commons/logging/impl/WeakHashtable$Referenced:<init>	(Ljava/lang/Object;Lorg/apache/commons/logging/impl/WeakHashtable$1;)V
    //   39: invokespecial 59	java/util/Hashtable:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   42: astore 4
    //   44: aload_0
    //   45: monitorexit
    //   46: aload 4
    //   48: areturn
    //   49: aload_0
    //   50: getfield 27	org/apache/commons/logging/impl/WeakHashtable:changeCount	I
    //   53: bipush 10
    //   55: irem
    //   56: ifne -27 -> 29
    //   59: aload_0
    //   60: invokespecial 149	org/apache/commons/logging/impl/WeakHashtable:purgeOne	()V
    //   63: goto -34 -> 29
    //   66: astore_2
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	71	0	this	WeakHashtable
    //   0	71	1	paramObject	Object
    //   66	4	2	localObject1	Object
    //   6	12	3	i	int
    //   42	5	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	14	66	finally
    //   20	29	66	finally
    //   29	44	66	finally
    //   49	63	66	finally
  }
  
  public int size()
  {
    purge();
    return super.size();
  }
  
  public String toString()
  {
    purge();
    return super.toString();
  }
  
  public Collection values()
  {
    purge();
    return super.values();
  }
  
  private static final class Entry
    implements Map.Entry
  {
    private final Object key;
    private final Object value;
    
    private Entry(Object paramObject1, Object paramObject2)
    {
      this.key = paramObject1;
      this.value = paramObject2;
    }
    
    Entry(Object paramObject1, Object paramObject2, WeakHashtable.1 param1)
    {
      this(paramObject1, paramObject2);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = false;
      Map.Entry localEntry;
      if (paramObject != null)
      {
        boolean bool2 = paramObject instanceof Map.Entry;
        bool1 = false;
        if (bool2)
        {
          localEntry = (Map.Entry)paramObject;
          if (getKey() != null) {
            break label61;
          }
          if (localEntry.getKey() != null) {
            break label78;
          }
          if (getValue() != null) {
            break label80;
          }
          if (localEntry.getValue() != null) {
            break label78;
          }
        }
      }
      for (;;)
      {
        bool1 = true;
        return bool1;
        label61:
        if (getKey().equals(localEntry.getKey())) {
          break;
        }
        label78:
        label80:
        do
        {
          return false;
        } while (!getValue().equals(localEntry.getValue()));
      }
    }
    
    public Object getKey()
    {
      return this.key;
    }
    
    public Object getValue()
    {
      return this.value;
    }
    
    public int hashCode()
    {
      int i;
      int j;
      if (getKey() == null)
      {
        i = 0;
        Object localObject = getValue();
        j = 0;
        if (localObject != null) {
          break label35;
        }
      }
      for (;;)
      {
        return i ^ j;
        i = getKey().hashCode();
        break;
        label35:
        j = getValue().hashCode();
      }
    }
    
    public Object setValue(Object paramObject)
    {
      throw new UnsupportedOperationException("Entry.setValue is not supported.");
    }
  }
  
  private static final class Referenced
  {
    private final int hashCode;
    private final WeakReference reference;
    
    private Referenced(Object paramObject)
    {
      this.reference = new WeakReference(paramObject);
      this.hashCode = paramObject.hashCode();
    }
    
    private Referenced(Object paramObject, ReferenceQueue paramReferenceQueue)
    {
      this.reference = new WeakHashtable.WeakKey(paramObject, paramReferenceQueue, this, null);
      this.hashCode = paramObject.hashCode();
    }
    
    Referenced(Object paramObject, ReferenceQueue paramReferenceQueue, WeakHashtable.1 param1)
    {
      this(paramObject, paramReferenceQueue);
    }
    
    Referenced(Object paramObject, WeakHashtable.1 param1)
    {
      this(paramObject);
    }
    
    private Object getValue()
    {
      return this.reference.get();
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Referenced;
      boolean bool2 = false;
      Referenced localReferenced;
      Object localObject1;
      Object localObject2;
      if (bool1)
      {
        localReferenced = (Referenced)paramObject;
        localObject1 = getValue();
        localObject2 = localReferenced.getValue();
        if (localObject1 != null) {
          break label72;
        }
        if (localObject2 != null) {
          break label64;
        }
      }
      label64:
      for (int i = 1; (i != 0) && (hashCode() == localReferenced.hashCode()); i = 0)
      {
        bool2 = true;
        return bool2;
      }
      return false;
      label72:
      return localObject1.equals(localObject2);
    }
    
    public int hashCode()
    {
      return this.hashCode;
    }
  }
  
  private static final class WeakKey
    extends WeakReference
  {
    private final WeakHashtable.Referenced referenced;
    
    private WeakKey(Object paramObject, ReferenceQueue paramReferenceQueue, WeakHashtable.Referenced paramReferenced)
    {
      super(paramReferenceQueue);
      this.referenced = paramReferenced;
    }
    
    WeakKey(Object paramObject, ReferenceQueue paramReferenceQueue, WeakHashtable.Referenced paramReferenced, WeakHashtable.1 param1)
    {
      this(paramObject, paramReferenceQueue, paramReferenced);
    }
    
    private WeakHashtable.Referenced getReferenced()
    {
      return this.referenced;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.WeakHashtable
 * JD-Core Version:    0.7.0.1
 */