package com.itmo.mrdvd.proxy;

/** DTO for carrying UpdateQuery info. */
public class UpdateDTO<T> {
  private Long id;
  private T object;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }
}
