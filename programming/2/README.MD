# Как собирать проект вручную?

### Structure

Структура проекта выглядит так:

```
./   # мы здесь
 ---> MANIFEST.MF   # см. далее
 |
 ---> src/   # наш код
 |   |---> lab/
 |         |---> Main.java
 |         |---> CustomPokemons.java
 |         |---> CustomPhysicalMoves.java
 |         |---> CustomSpecialMoves.java
 |         |---> CustomStatusMoves.java
 ---> out/   # внешние зависимости
 |   |---> Pokemon.jar
 ---> lab/   # создано после javac (см. далее)
     |---> Main.class
     |---> *.class
```

> **NB!** В каждом классе первой строчкой должен быть прописан проект: `package lab`, где `lab` — относительный путь до вашего класса (верен для схемы выше). В противном случае `javac` нарушит вашу иерархию классов и закинет все `.class`-файлы в одну директорию. 

### MANIFEST

Минимально рабочий `MANIFEST.MF`, который нужно прописать ручками:

```
Main-Class: lab.Main
Class-Path: out/Pokemon.jar
```

### Commands

Наконец, прописываем команды в следующей последовательности:

```bash
javac -cp "out/Pokemon.jar:lab/" -d . src/lab/*
jar -cfm lab.jar MANIFEST.MF lab/*
```

> **NB!** В команде `javac` ошибочно опускать параметр `-d .`, поскольку кроме указания директории назначения скомпилированных классов он ещё заставляет компилятор соблюдать иерархию проекта, которая была выстроена выше. _(да, это странное и неочевидное поведение, но такова жизнь)_ 

В результате получим `lab.jar` файл, который можно запускать:

```bash
java -jar lab.jar
```
