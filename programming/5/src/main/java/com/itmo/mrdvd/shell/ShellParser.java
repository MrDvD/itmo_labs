package com.itmo.mrdvd.shell;

import java.util.Optional;

public interface ShellParser {
   public Optional<ShellQuery> parse(String line);
}
