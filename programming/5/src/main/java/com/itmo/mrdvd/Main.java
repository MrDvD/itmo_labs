package com.itmo.mrdvd;

import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.shell.TicketShell;

public class Main {
  public static void main(String[] args) {
    Console console = new Console();
    TicketShell shell = new TicketShell(console, console);
    shell.open();
  }
}
