package com.itmo.mrdvd.command;

import com.itmo.mrdvd.device.FileMeta;
import com.itmo.mrdvd.device.OutputDevice;

public class ReadEnvironmentFilepathCommand implements Command {
  private final String envName;
  private final OutputDevice log;
  private final FileMeta file;

  public ReadEnvironmentFilepathCommand(String envName, FileMeta file, OutputDevice log) {
    this.envName = envName;
    this.file = file;
    this.log = log;
  }

  @Override
  public void execute() {
    String filePath = System.getenv(envName);
    if (filePath != null) {
      file.setPath(filePath);
      log.writeln(String.format("[INFO] Переменная окружения \"%s\" успешно прочитана.", envName));
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
