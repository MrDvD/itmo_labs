package com.itmo.mrdvd.executor.commands.shell;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.executor.Executor;
import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;
import com.itmo.mrdvd.shell.Shell;

/** Sends the query to a server and passes the response to the executor. */
public class ProcessQueryCommand implements ShellCommand, CommandWithParams<Void> {
  protected final ClientProxy proxy;
  protected final Executor exec;
  protected final Shell shell;
  protected List<?> params;

  public ProcessQueryCommand(Shell shell, ClientProxy proxy, Executor exec) {
    this.shell = shell;
    this.proxy = proxy;
    this.exec = exec;
  }

  @Override
  public ProcessQueryCommand setShell(Shell shell) {
    return new ProcessQueryCommand(shell, this.proxy, this.exec);
  }

  @Override
  public Optional<Shell> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public ProcessQueryCommand withParams(List<?> params) {
    this.params = params;
    return this;
  }

  @Override
  public Void execute() throws IllegalStateException, RuntimeException {
    if (this.proxy == null) {
      throw new IllegalStateException("Не предоставлен клиентский прокси для обработки запроса.");
    }
    if (this.params == null || this.params.isEmpty() || !(this.params.get(0) instanceof Query)) {
      throw new IllegalStateException("Не предоставлен запрос для обработки.");
    }
    Query q = (Query) this.params.get(0);
    Optional<TransportProtocol> proto = this.proxy.getProtocol();
    if (proto.isEmpty() || proto.get().getDeserializers().isEmpty()) {
      throw new IllegalStateException("Нет установленных обработчиков ответа от сервера.");
    }
    String rawResponse = this.proxy.send(q);
    Optional<?> uncastedResponse =
        proto.get().getDeserializers().get(0).deserialize(rawResponse, Query.class);
    if (uncastedResponse.isPresent()) {
      try {
        Query response = (Query) uncastedResponse.get();
        this.exec.processQuery(response, List.of(this.shell));
      } catch (ClassCastException e) {
        throw new RuntimeException("Невозможно обработать ответ сервера.");
      }
    }
    return null;
  }

  @Override
  public String name() {
    return "process_query";
  }
}
