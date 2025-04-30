package com.itmo.mrdvd.commands.todo;

import com.itmo.mrdvd.device.FileMeta;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.shell.ProxyShell;
import java.util.Optional;

public class ReadEnvironmentFilepathCommand implements Command {
  private final String envName;
  private final ProxyShell<?, ?> shell;
  private final FileMeta file;

  public ReadEnvironmentFilepathCommand(String envName, FileMeta file) {
    this(envName, file, null);
  }

  public ReadEnvironmentFilepathCommand(String envName, FileMeta file, ProxyShell<?, ?> shell) {
    this.envName = envName;
    this.file = file;
    this.shell = shell;
  }

  @Override
  public ReadEnvironmentFilepathCommand setShell(ProxyShell<?, ?> shell) {
    return new ReadEnvironmentFilepathCommand(envName, file, shell);
  }

  @Override
  public Optional<ProxyShell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    String filePath = System.getenv(envName);
    if (filePath != null) {
      file.setPath(filePath);
      getShell()
          .get()
          .getOut()
          .writeln(String.format("[INFO] Переменная окружения \"%s\" успешно прочитана.", envName));
    } else {
      getShell()
          .get()
          .getOut()
          .writeln(String.format("[ERROR] Ошибка чтения переменной окружения '%s'.", envName));
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
