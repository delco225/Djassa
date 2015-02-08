package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class BeiderMorseEncoder
  implements StringEncoder
{
  private PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
  
  public Object encode(Object paramObject)
    throws EncoderException
  {
    if (!(paramObject instanceof String)) {
      throw new EncoderException("BeiderMorseEncoder encode parameter is not of type String");
    }
    return encode((String)paramObject);
  }
  
  public String encode(String paramString)
    throws EncoderException
  {
    if (paramString == null) {
      return null;
    }
    return this.engine.encode(paramString);
  }
  
  public NameType getNameType()
  {
    return this.engine.getNameType();
  }
  
  public RuleType getRuleType()
  {
    return this.engine.getRuleType();
  }
  
  public boolean isConcat()
  {
    return this.engine.isConcat();
  }
  
  public void setConcat(boolean paramBoolean)
  {
    this.engine = new PhoneticEngine(this.engine.getNameType(), this.engine.getRuleType(), paramBoolean);
  }
  
  public void setNameType(NameType paramNameType)
  {
    this.engine = new PhoneticEngine(paramNameType, this.engine.getRuleType(), this.engine.isConcat());
  }
  
  public void setRuleType(RuleType paramRuleType)
  {
    this.engine = new PhoneticEngine(this.engine.getNameType(), paramRuleType, this.engine.isConcat());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.bm.BeiderMorseEncoder
 * JD-Core Version:    0.7.0.1
 */