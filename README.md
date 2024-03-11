## TechStack

| 카테고리 | 기술 |
| --- | --- |
| Language, Framework | Java, Spring Boot |
| Database | MySQL, Redis, Flyway |
| ORM | Spring Data JPA, QueryDSL |
| 운영 환경 구축 | EC2, Docker |
| Build | Gradle |
| CI/CD | Github Actions |
| Library | Spring Open Feign, OAuth 2.0, spring actuator |

## 실행방법
1. git clone [repository url)
2. 환경변수를 설정해줍니다.(env.properties)
3. db를 실행합니다.
  ```shell
  cd resources
  cd local-develop-environment
  docker-compose up
  ```
5. spring boot를 실행합니다.

