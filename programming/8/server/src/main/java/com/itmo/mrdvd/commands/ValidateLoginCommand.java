package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.AuthResponse;
import com.itmo.mrdvd.AuthStatus;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.util.List;

public class ValidateLoginCommand implements Command<AuthResponse> {
  private final Validator<AuthID> idValidator;

  public ValidateLoginCommand(Validator<AuthID> idValidator) {
    this.idValidator = idValidator;
  }

  @Override
  public AuthResponse execute(List<Object> params) {
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    if (params.get(0) instanceof AuthID authId) {
      if (this.idValidator.validate(authId)) {
        return AuthResponse.newBuilder().setStatus(AuthStatus.AUTHORIZED).build();
      }
    }
    return AuthResponse.newBuilder().setStatus(AuthStatus.INVALID).build();
  }

  @Override
  public String name() {
    return "validate_login";
  }

  @Override
  public String signature() {
    return name() + " {auth_id}";
  }

  @Override
  public String description() {
    return "валидировать токен для аутентификации";
  }
}
