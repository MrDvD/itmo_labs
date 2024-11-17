package lab.interfaces;

import java.util.List;

import lab.enums.Environment;

public interface IEnvironment {
   void setEnvironment(Environment ... obj);
   List<Environment> getEnvironment();
}
