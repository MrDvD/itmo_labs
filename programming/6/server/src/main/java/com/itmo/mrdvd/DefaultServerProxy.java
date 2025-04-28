package com.itmo.mrdvd;

import com.itmo.mrdvd.proxy.ServerProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.commands.response.Response;
import com.itmo.mrdvd.executor.queries.Query;

/**
 * Deprecated.
 */
public class DefaultServerProxy implements ServerProxy {
  @Override
  public Response processQuery(Query q) throws IllegalArgumentException {
    Optional<Command<?>> cmd = getCommand(q.getCmd());
    // return error query if command not found
    if (cmd.isEmpty()) {
      throw new IllegalArgumentException("Не удалось распознать запрос.");
    }
    if (cmd.get() instanceof CommandWithParams<?> cmdWithParams) {
      List<?> resultParams = List.of();
      if (prefixParams != null) {
        resultParams = prefixParams;
      }
      resultParams = Stream.concat(resultParams.stream(), q.getArgs().stream()).toList();
      Object result = cmdWithParams.withParams(resultParams).execute();
      if (!(result instanceof Void)) {
        // return query based on result
      } else {
        // return success query
        // (error query will be sent higher in proxy level)
      }
      // how to return result if command fetches data for client?
    } else {
      cmd.get().execute();
    }
  }
}
