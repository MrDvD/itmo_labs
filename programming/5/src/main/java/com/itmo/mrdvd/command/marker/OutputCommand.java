package com.itmo.mrdvd.command.marker;

import com.itmo.mrdvd.device.OutputDevice;
import java.util.Optional;

public interface OutputCommand extends Command {
  public OutputDevice setOutput(OutputDevice in);

  public Optional<OutputDevice> getOutput();
}
