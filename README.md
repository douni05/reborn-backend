# ♻️ Re:Born Backend

Re:Born 서비스의 Spring Boot 백엔드 서버입니다.

---

## 🛠 기술 스택

| 구분 | 기술 |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5 |
| **ORM** | Spring Data JPA (Hibernate 6) |
| **Database** | Supabase PostgreSQL |
| **Auth** | JWT (jjwt 0.12.3) |
| **Push** | Firebase Admin SDK 9.2.0 (FCM) |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Build** | Gradle |

---

## 📁 폴더 구조

```
src/main/java/com/jimmy/reborn_backend/
├── controller/
│   ├── MemberController.java       # 회원 API
│   ├── ExpertPartnerController.java # 전문가 API
│   ├── ReformRequestController.java # 리폼 요청 API
│   ├── AnalysisController.java     # AI 분석 API
│   ├── ActionController.java       # XP 행동 API
│   └── TipController.java          # 오늘의 팁 API
├── service/
│   ├── MemberService.java
│   ├── ExpertPartnerService.java
│   ├── ReformRequestService.java   # 요청 수락/거절/완료 + FCM 알림
│   ├── AnalysisService.java
│   ├── XpService.java              # XP & 레벨 계산
│   ├── FcmService.java             # Firebase 푸시 알림 전송
│   └── DailyTipService.java
├── domain/
│   ├── entity/                     # JPA 엔티티 (Member, ExpertPartner 등)
│   ├── repository/                 # Spring Data JPA 인터페이스
│   └── enums/
│       └── RequestStatus.java      # PENDING / ACCEPTED / REJECTED / COMPLETED / CANCELLED
├── dto/                            # 요청/응답 DTO
└── global/
    ├── config/
    │   ├── FcmConfig.java          # Firebase 초기화
    │   ├── SecurityConfig.java
    │   └── CorsConfig.java
    ├── jwt/
    │   └── JwtUtil.java
    └── GlobalExceptionHandler.java
```

---

## 🔌 주요 API 엔드포인트

| Method | Path | 설명 |
| :--- | :--- | :--- |
| POST | `/api/v1/members/join` | 회원가입 / 로그인 (소셜) |
| GET | `/api/v1/members/me` | 내 프로필 조회 |
| PATCH | `/api/v1/members/nickname` | 닉네임 변경 |
| PATCH | `/api/v1/members/title` | 칭호 변경 |
| PATCH | `/api/v1/members/fcm-token` | FCM 토큰 저장 |
| GET | `/api/v1/experts` | 전문가 목록 조회 |
| POST | `/api/v1/experts/register` | 전문가 등록 |
| POST | `/api/v1/reform-requests` | 리폼 요청 생성 |
| GET | `/api/v1/reform-requests/my` | 내 요청 목록 |
| POST | `/api/v1/reform-requests/{id}/accept` | 요청 수락 (전문가) |
| POST | `/api/v1/reform-requests/{id}/reject` | 요청 거절 (전문가) |
| POST | `/api/v1/reform-requests/{id}/complete` | 요청 완료 (전문가) |
| POST | `/api/v1/actions/reform` | 리폼 등록 (XP +100) |
| POST | `/api/v1/actions/disposal` | 폐기물 처리 (XP +50) |
| GET | `/api/v1/tips/today` | 오늘의 실천 팁 |

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🚀 로컬 개발 환경 설정

### 1. 필수 설치

- JDK 21
- IntelliJ IDEA
  - `Settings > Build > Annotation Processors > Enable annotation processing` 체크
  - `Settings > Build > Gradle > Build and run using: IntelliJ IDEA` 설정

### 2. application-local.yml 생성

`src/main/resources/application-local.yml` 파일을 직접 생성하여 아래 내용 입력:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://[Supabase Pooler Host]:5432/postgres?prepareThreshold=0
    username: postgres.[project-ref]
    password: [DB 비밀번호]

jwt:
  secret: [JWT 시크릿 키]

ai:
  server:
    url: http://localhost:8000
```

> Supabase 접속 정보: [Supabase Dashboard](https://supabase.com) → Connect → Connection string

### 3. Firebase 설정

- Firebase Console → 프로젝트 설정 → 서비스 계정 → **새 비공개 키 생성**
- 다운로드한 JSON 파일을 `src/main/resources/` 에 배치
- 파일명: `reborn-2c50e-firebase-adminsdk-fbsvc-bf05103003.json`
- ⚠️ 해당 파일은 `.gitignore`에 등록되어 있어 팀원 간 직접 공유 필요

### 4. 실행

IntelliJ에서 `RebornBackendApplication` 실행 또는:

```bash
./gradlew bootRun
```

---

## 🔔 FCM 알림 흐름

| 트리거 | 수신자 | 알림 내용 |
| :--- | :--- | :--- |
| 전문가가 요청 **수락** | 요청자 (유저) | "리폼 요청 수락 🎉" |
| 전문가가 요청 **거절** | 요청자 (유저) | "리폼 요청 거절" |
| 전문가가 **완료** 처리 | 요청자 (유저) | "리폼 완료 ✅ +150 XP" |
