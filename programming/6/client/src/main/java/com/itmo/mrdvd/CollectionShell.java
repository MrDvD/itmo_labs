package com.itmo.mrdvd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public class CollectionShell extends AbstractShell {
  private boolean isOpen;

  public CollectionShell(TTY tty) {
    this(tty, new HashMap<>());
  }

  public CollectionShell(TTY tty, Map<String, Object> args) {
    super(tty, args);
  }

  @Override
  public void start() {
    setArg("exit", this);
    this.isOpen = true;
    while (this.isOpen) {
      if (getTty().getIn() instanceof InteractiveInputDevice back) {
        back.write("> ");
      }
      while (!getTty().getIn().hasNext()) {
        getTty().getIn().closeIn();
        stop();
        return;
      }
      // Optional<String> cmd;
      // try {
      //   cmd = processLine();
      // } catch (IOException e) {
      //   stop();
      //   continue;
      // }
      if (cmd.isPresent()) {
        getTty().getOut()
            .writeln(
                String.format(
                    "[ERROR] Команда '%s' не найдена: введите 'help' для просмотра списка доступных команд.",
                    cmd.get()));
      }
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
