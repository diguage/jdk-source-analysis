import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalcTest {
  @Test
  public void testAdd() {
    int result = Calc.add(1, 3);
    Assert.assertEquals(4, result);
//    assertEquals(5, result);
  }
}
