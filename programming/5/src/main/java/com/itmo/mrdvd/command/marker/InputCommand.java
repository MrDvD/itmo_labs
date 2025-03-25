package com.itmo.mrdvd.command.marker;

import com.itmo.mrdvd.device.input.InputDevice;
import java.util.Optional;

public interface InputCommand<T extends InputDevice> extends Command {
  public InputCommand<T> setInput(T in);

  public Optional<T> getInput();
}
