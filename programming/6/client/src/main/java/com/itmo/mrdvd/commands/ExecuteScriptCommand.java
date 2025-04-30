package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.device.DataFileDescriptor;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.service.shell.AbstractShell;

/**
 * Propably it would be better to make this as a method of AbstractShell
 * instead of as a separate command?
 */
public class ExecuteScriptCommand implements Command<Void> {
  private final DataFileDescriptor fd;
  // move this field to AbstractShell!
  private final Set<Path> usedPaths;

  public ExecuteScriptCommand(DataFileDescriptor fd) {
    this(fd, new HashSet<>());
  }

  public ExecuteScriptCommand(DataFileDescriptor fd, Set<Path> usedPaths) {
    this.fd = fd;
    this.usedPaths = usedPaths;
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
    if (usedPaths.contains(p.get())) {
      throw new RuntimeException(String.format("Обнаружена петля, экстренное завершение исполнения скрипта \"%s\".", p.get().getFileName().toString()));
    }
    usedPaths.add(p.get());
    shell.setTty(shell.getTty().get().setIn(fd));
    ...
    // MOVE usedPaths to executor (map of objects). Otherwise the command won't work as expected
    usedPaths.remove(p.get());
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
