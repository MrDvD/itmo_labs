package lab.classes.exception;

import lab.interfaces.ICapitalisticActive;

public class NegativeBalance extends RuntimeException {
   public NegativeBalance(ICapitalisticActive obj) {
      super(String.format("У %s получился отрицательный баланс.", obj));
   }
}
