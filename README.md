## 🏗️ 백엔드 (Spring Boot) 환경 구축

### **① JDK 21 & IntelliJ 설치**

1. JDK 21 설치.
2. **IntelliJ IDEA** 설치 및 프로젝트 열기 (`build.gradle`로 열기).
3. `File` > `Project Structure` > `Project SDK`를 21로 설정.

### **② IntelliJ 필수 설정**

1. **Lombok 활성화**: `Settings` > `Build, Execution, Deployment` > `Compiler` > `Annotation Processors` > **[Enable annotation processing]** 체크.
2. **Gradle 설정**: `Settings` > `Build, Execution, Deployment` > `Build Tools` > `Gradle` > **'Build and run using'**을 **IntelliJ IDEA**로 변경 (빌드 속도 향상).

### ③ Supabase DB 연결 (PostgreSQL 기반)

- **Supabase 프로젝트 생성:** [Supabase Dashboard](https://supabase.com/) 로그인 후 `New Project`를 생성합니다. (Database Password를 반드시 메모하세요.)
- **JDBC 접속 정보 확인:** `Project Settings > Database` 메뉴에서 **Connection string > JDBC** 탭을 클릭하여 정보를 확인합니다.
- **src/main/resources/application.yml 수정** (MySQL 대신 PostgreSQL 설정 사용)

### **📂 백엔드 표준 폴더 구조**

- **`src/main/java/com/jimmy/reborn/`**
    - `controller/`: API 엔드포인트 및 요청 매핑 (예: `UserController.java`)
    - `service/`: 핵심 비즈니스 로직 및 알고리즘 (예: `XpService.java`)
    - `domain/`:
        - `entity/`: DB 테이블 매핑 객체 (예: `User.java`)
        - `repository/`: DB 접근 인터페이스 (JPA)
    - `dto/`: 클라이언트와 주고받는 데이터 객체 (예: `LoginRequest.java`)
    - `global/`: 공통 예외 처리, 보안 설정, 유틸리티 클래스
- **`src/main/resources/`**
    - `application.yml`: DB 연결 정보 및 환경 설정 파일