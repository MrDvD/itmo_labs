// package com.itmo.mrdvd.builder;

// import java.util.List;
// import java.util.Optional;
// import java.util.function.Function;

// import com.itmo.mrdvd.device.OutputDevice;
// import com.itmo.mrdvd.device.input.InteractiveInputDevice;

// public abstract class InteractiveBuilder<T> extends Builder<T> {
//    private final InteractiveInputDevice in;
//    private final OutputDevice out;

//    public InteractiveBuilder(T rawObject, InteractiveInputDevice in, OutputDevice out, List<Function<Object, Void>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
//       super(rawObject, setters, objects, validators);
//       this.in = in;
//       this.out = out;
//    }

//    public Builder<T> addSetter(SetterWrapper<Object> setter) {
//       return super.addSetter(setter, null);
//    }

//    // 1. Функция чтения
//    // 2. Функция валидации
//    // 3. Функция установки значения

//    public T build() {
//       for (SetterWrapper<?> f : setters) {
//          String msg = String.format("Введите поле \"%s\"", f.name());
//          if (f.comment().isPresent()) {
//             msg += String.format(" (%s)", f.comment().get());
//          }
//          msg += ": ";
//          Optional<String> result = in.read(msg);
//          if (result.isPresent() && f.validate(result.get())) {
//             f.setter()(result.get());
//          }
//       }
//    }
// }
