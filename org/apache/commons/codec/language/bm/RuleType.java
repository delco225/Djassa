package org.apache.commons.codec.language.bm;

public enum RuleType
{
  private final String name;
  
  static
  {
    RuleType[] arrayOfRuleType = new RuleType[3];
    arrayOfRuleType[0] = APPROX;
    arrayOfRuleType[1] = EXACT;
    arrayOfRuleType[2] = RULES;
    $VALUES = arrayOfRuleType;
  }
  
  private RuleType(String paramString)
  {
    this.name = paramString;
  }
  
  public String getName()
  {
    return this.name;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.bm.RuleType
 * JD-Core Version:    0.7.0.1
 */