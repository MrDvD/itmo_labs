package com.itmo.mrdvd.command.marker;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.input.InputDevice;

public interface CommandHasParams extends Command {
   public InputDevice getParamsInput();
}
