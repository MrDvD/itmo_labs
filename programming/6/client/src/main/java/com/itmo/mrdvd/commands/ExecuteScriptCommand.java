package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.device.DataFileDescriptor;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class ExecuteScriptCommand implements Command<Void> {
  private final DataFileDescriptor fd;

  public ExecuteScriptCommand(DataFileDescriptor fd) {
    this.fd = fd;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalArgumentException {
    if (params.size() < 2) {
      throw new IllegalArgumentException("Недостаточное количество аргументов команды.");
    }
    AbstractShell shell = null;
    String path = null;
    try {
      shell = (AbstractShell) params.get(0);
      path = (String) params.get(1);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось обработать переданные аргументы.");
    }
    if (shell.getTty().isEmpty()) {
      throw new IllegalArgumentException("Предоставлен интерпретатор без TTY.");
    }
    DataFileDescriptor file = fd.duplicate();
    try {
      file.setPath(path);
    } catch (InvalidPathException | NoSuchElementException e) {
      throw new IllegalArgumentException("Не удалось распознать файл со скриптом по пути.");
    }
    file.openIn();
    Optional<Path> p = file.getPath();
    if (p.isEmpty()) {
      throw new IllegalArgumentException("Не удалось обратиться к файлу по адресу.");
    }
    try {
      TTY tty = shell.getTty().get().setIn(fd);
      tty.setName(p.get().toAbsolutePath().toString());
      shell.setTty(tty);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(String.format("Обнаружена петля, завершение выполнения: %s.", p.get().getFileName()));
    }
    return null;
  }

  @Override
  public String name() {
    return "execute_script";
  }

  @Override
  public String signature() {
    return name() + " file_name";
  }

  @Override
  public String description() {
    return "считать и исполнить скрипт из указанного файла";
  }
}
