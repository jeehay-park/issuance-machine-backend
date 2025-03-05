
# 📌 프로젝트를 로컬환경에서 실행하는 절차

### 백엔드 서버 실행 (Spring Boot)
- 백엔드 서버를 실행하려면 먼저 MariaDB가 준비되어 있어야 합니다.
- Spring Boot 서버가 실행되면, 자동으로 필요한 테이블이 데이터베이스에 생성됩니다. (`ddl-auto: update` : 구현 중 데이터베이스 테이블이 계속 추가되거나 수정되기 때문에 이렇게 두었습니다.)
- **로컬 백엔드 소스 위치**: `D:\issuance-machine-server-backend`
- **GitHub 백엔드 소스 위치**: `https://github.com/jeehay-park/issuance-machine-backend`
- **MariaDB 접속 정보**:
    - **사용자**: root
    - **비밀번호**: oct102024
    - **포트**: 3306
- **Swagger API 테스트**: 서버가 실행된 후, `http://localhost:17777/swagger-ui/index.html#`에서 API를 테스트할 수 있습니다.