package com.itmo.mrdvd.proxy;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
import org.apache.hc.core5.http.io.SessionInputBuffer;

public class HttpProtocol<T extends HttpMessage & HttpEntityContainer>
    implements TransportProtocol {
  protected AbstractMessageParser<T> parser;

  public HttpProtocol(AbstractMessageParser<T> parser) {
    this.parser = parser;
  }

  @Override
  public InputStream getPayload(InputStream stream) throws RuntimeException {
    SessionInputBuffer buffer; // research this buffer as i don't understand why is it used
    try {
      return this.parser.parse(buffer, stream).getEntity().getContent();
    } catch (IOException | HttpException e) {
      throw new RuntimeException(e);
    }
  }
}
