# Test status in Github.Actions: [![statusbadge](../../actions/workflows/classroom.yml/badge.svg?branch=main&event=workflow_dispatch)](../../actions/workflows/classroom.yml)

### Run
C/CPP example, main.c/main.cpp file is required to build
```
./c_test.exe 8.8 1 0xdc9f + 0xd736
```

Python example, main.py file is required
```
python main.py 8.8 1 0xdc9f + 0xd736
```

Java example, Main.java file is required
```
javac Main.java
java Main 8.8 1 0xdc9f + 0xd736
```

### Versions

**C/C++**:
```
Ubuntu clang version 18.1.3 (1ubuntu1)
Target: x86_64-pc-linux-gnu
```

```
clang version 18.1.8
Target: x86_64-pc-windows-msvc
```

> [!IMPORTANT]
> Чтобы GitHub Workflow отработал верно, файл с [функцией `main`](https://en.cppreference.com/w/c/language/main_function) должен располагаться в корне репозитория.
> Если работа выполняется на `C++`, то файл, содержащий функцию `main` должен называться `main.cpp`. Иначе – `main.c`.


**Python**:
```
Python 3.12.5
```

**Java**:
```
openjdk 21.0.4 2024-07-16 LTS
OpenJDK Runtime Environment Temurin-21.0.4+7 (build 21.0.4+7-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.4+7 (build 21.0.4+7-LTS, mixed mode, sharing)
```
