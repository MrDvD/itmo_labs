package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.shell.Shell;

public interface ShellInfo {
  public void setShell(Shell<?, ?, ?> shell);
  public Optional<Shell<?, ?, ?>> getShell();
}
