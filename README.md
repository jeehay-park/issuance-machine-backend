
# 📌 백엔드 서버 실행 (Spring Boot) - 필수 사항

### **필수 소프트웨어**
- **JDK (Java Development Kit)**:
  - Spring Boot 프로젝트는 Java로 작성되어 있으므로 JDK가 설치되어 있어야 합니다.

- **필수 JDK 버전**: JDK 17 이상 (현재 Java 17.0.11 LTS 버전 사용)

- 확인 방법:
  ```bash
  java -version
  ```

  출력 예시:
  ```
  java version "17.0.11" 2024-04-16 LTS
  Java(TM) SE Runtime Environment (build 17.0.11+7-LTS-207)
  Java HotSpot(TM) 64-Bit Server VM (build 17.0.11+7-LTS-207, mixed mode, sharing)
  ```
  [JDK 다운로드 링크](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

  <br><br>

  환경 변수 설정
  ```
  Variable name: JAVA_HOME
  Variable value: D:\Java\jdk-17
  ```

<br><br>


- **`gradlew`**  (Gradle Wrapper):
  `gradlew`는 Gradle Wrapper로, 해당 프로젝트에서 자동으로 적합한 Gradle 버전을 다운로드하고 실행해줍니다.
  따라서 사용자는 별도로 Gradle을 설치하지 않고도 프로젝트를 빌드할 수 있습니다.

  - `gradlew` 및 `gradlew.bat`: 운영 체제에 맞는 Gradle 실행 파일입니다.
  
  - `build.gradle`: 프로젝트의 빌드 설정이 담긴 파일입니다.
  
  - `settings.gradle`: 프로젝트의 설정을 포함한 파일입니다.

  - 환경 변수 설정 예시
  ```
  Variable name: GRADLE_USER_HOME
  Variable value: D:\gradle_cache
  ```
  - Gradle version 정보
  ```bash
    ------------------------------------------------------------
    Gradle 8.10.1
    ------------------------------------------------------------
    
    Build time:    2024-09-09 07:42:56 UTC
    Revision:      8716158d3ec8c59e38f87a67f1f311f297b79576
    
    Kotlin:        1.9.24
    Groovy:        3.0.22
    Ant:           Apache Ant(TM) version 1.10.14 compiled on August 16 2023
    Launcher JVM:  17.0.11 (Oracle Corporation 17.0.11+7-LTS-207)
    Daemon JVM:    D:\Java\jdk-17 (no JDK specified, using current Java home)
    OS:            Windows 11 10.0 amd64
  ```

<br><br>

- **MariaDB** (데이터베이스):
  - **MariaDB 설치**가 필요합니다. [MariaDB 다운로드 링크](https://mariadb.org/download/)
  - **포트**: 3306
  - **사용자**: root
  - **비밀번호**: `oct102024`

<br><br>

- **Git** (소스 코드 클론 및 버전 관리):
  - 프로젝트 소스를 GitHub에서 클론하려면 Git이 설치되어 있어야 합니다.
  - [Git 다운로드 링크](https://git-scm.com/downloads)

<br><br>

### **프로젝트 클론**
- 먼저 Git을 사용하여 프로젝트 소스를 클론합니다:
  ```bash
  git clone https://github.com/jeehay-park/issuance-machine-backend.git
  ```  
<br><br>

### **프로젝트 빌드 및 실행**

- **백엔드 빌드**:
  - 백엔드를 빌드하려면 다음 명령어를 실행하세요:
    ```bash
    ./gradlew clean build
    ```

- **백엔드 실행**:
  - 백엔드를 실행하려면 다음 명령어를 실행하세요:
    ```bash
    ./gradlew bootRun
    ```

