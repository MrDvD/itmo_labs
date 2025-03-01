package com.itmo.mrdvd.command;

import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;

public class ReadEnvironmentFilepathCommand implements Command {
   private final String envName;
   private final OutputDevice log;
   private final FileDescriptor file;
   public ReadEnvironmentFilepathCommand(String envName, FileDescriptor file, OutputDevice log) {
      this.envName = envName;
      this.file = file;
      this.log = log;
   }
   @Override
   public void execute(String[] params) {
      String filePath = System.getenv(envName);
      if (filePath != null) {
         file.setPath(filePath);
         log.writeln("[INFO] Переменная окружения успешно прочитана.");
      } else {
         log.writeln(String.format("[ERROR] Ошибка чтения переменной окружения '%s'.", envName));
      }
   }
   @Override
   public String name() {
      return "read_env";
   }
   @Override
   public String signature() {
      return name();
   }
   @Override
   public String description() {
      return "считать файл с коллекцией по пути из переменной окружения";
   }
}
