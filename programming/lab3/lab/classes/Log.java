package lab.classes;

public final class Log {
   static long line = 0;
   public static long getTime() {
      return ++line;
   }
   public static class Console {
      public static void print(Object message) {
         System.out.print(message);
      }
      public static void printf(String message, Object ... args) {
         System.out.printf(message, args);
      }
      public static void println(Object message) {
         System.out.println(message);
      }
   }
}
