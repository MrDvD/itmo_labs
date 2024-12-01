package lab.classes.exception;

public class AlreadyInitialized extends Exception {
   public AlreadyInitialized(String nameObject) {
      super(String.format("%s уже инициализирован.\n", nameObject));
   } 
}
