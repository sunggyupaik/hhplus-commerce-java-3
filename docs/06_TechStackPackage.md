# Tech Stack & Package

## Tech Stack

- Java 17
- Spring Boot, Spring Data JPA
- MySQL
- JUnit

## Package

```
── common/
    ── exception/
    ── response/
        ── CommonResponse.java
        ── CommonControllerAdvice.java
── config/
── interfaces/
    ── item/
        ── ItemApiController.java
        ── ItemApiSpec.java // Swagger MockAPI 작성
── application/
    ── order/
        ── OrderFacade.java // 결제, 재고, 포인트 등 서로 다른 도메인 작업 지시
── domain/
    ── item/
        ── dto/
        ── itemOption
            ── ItemOption.java
        ── inventory
            ── Inventory.java
        ── Item.java
        ── ItemService.java // 주요 도메인 흐름 관리
        ── ItemReader.java (I/F)
        ── ItemStore.java (I/F)
── infra/
    ── item/
        ── ItemReaderImpl.java (IMPL)
        ── ItemStoreImpl.java (IMPL)
        ── ItemRepository.java (JPA)
```

`Item` 기준으로 작성했으며 다른 도메인도 비슷합니다. `interfaces`, `application`, `domain`, `infra` 4부분으로 나눴습니다. 
`ItemApiController`는 `ItemService` 혹은 `ItemFacade`를 의존합니다. `Repository`를 추상화하여 비지니스 로직은 오직 비지니스에만 신경쓰도록 합니다. 
서비스가 커짐에 따라 `ItemService`에 로직이 과도하게 많아질 것을 대비해, `usecase`를 활용해 작은 단위로 조립하는 방법도 고민해보겠습니다.
