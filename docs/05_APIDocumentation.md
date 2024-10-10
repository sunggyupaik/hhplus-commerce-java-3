# ecommerce API Documentation

## 1. 상품 조회

- 주어진 식별자에 해당하는 상품을 반환합니다.

### Request

- URL: `/api/v1/items/{id}`
- Method: GET
- URL Params:
    - `id`: Long (상품 식별자)

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "itemId": 3,
    "itemName": "겨울 코트",
    "itemPrice": 10000,
    "itemOptionResponse": {
      "itemOptionId": 10,
      "itemOptionSize": "95",
      "itemOptionColor": "빨강",
      "itemOptionPrice": 0,
      "quantity": 10
    }
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "item not found",
  "errorCode": "404"
}
```

## 2. 잔액 조회

- 주어진 식별자에 해당하는 고객의 포인트 잔액을 반환합니다.

### Request

- URL: `/api/v1/customers/points`
- Method: GET
- Headers:
    - `customerId`: Long (고객 식별자)

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "customerId": 3,
    "point": 2500
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "customer not found",
  "errorCode": "404"
}
```

## 3. 포인트 충전

- 주어진 식별자에 해당하는 고객의 포인트 잔액을 반환합니다.

### Request

- URL: `/api/v1/customers/points/charge`
- Method: POST
- Headers:
    - `customerId`: Long (고객 식별자)
    - `Content-Type` : application/json
- Body:

```
{
  "amount": "1000"
}
```

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "customerId": 3,
    "point": 1000
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "point less than zero",
  "errorCode": "400"
}
```


- Body:

```
{
  "amount": 1000
}
```
### Response

```
{
  "result": "SUCCESS",
  "data": {
    "itemId": 3,
    "itemName": "겨울 코트",
    "itemPrice": 10000,
    "itemOptionResponse": {
      "itemOptionId": 10,
      "itemOptionSize": "95",
      "itemOptionColor": "빨강",
      "itemOptionPrice": 0,
      "quantity": 10
    }
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "customer not found",
  "errorCode": "404"
}
```

## 4. 주문 생성

- 주어진 식별자와 주문 정보로 해당하는 주문을 생성합니다.

### Request

- URL: `/api/v1/orders`
- Method: POST
- Headers:
    - `customerId`: Long (고객 식별자)
    - `Content-Type` : application/json
- Body:

```
{
  "customerId": 1,
  "receiverCity": "서울",
  "receiverStreet": "새나무로",
  "receiverZipcode": "123-1",
  "orderItemRequestList": [
    {
      "orderCount": 10,
      "itemId": 20,
      "itemName": "겨울 코트",
      "itemPrice": 10000,
      "deliveryStatus": "INIT",
      "orderItemOptionRequest": {
        "itemOptionSize": "95",
        "itemOptionColor": "빨강",
        "itemOptionPrice": 0
      }
    }
  ]
}
```

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "orderId": 10
  },
  "message": null,
  "errorCode": null
}
```

### Error
```
{
"result": "FAIL",
"data": null
"message": "stock quantity insufficient",
"errorCode": "400"
}
```

## 4. 주문 결제

- 주어진 식별자와 주문 정보로 해당하는 주문을 생성합니다.

### Request

- URL: `/api/v1/orders/payment`
- Method: POST
- Headers:
    - `customerId`: Long (고객 식별자)
    - `Content-Type` : application/json
- Body:

```
{
  "orderId": 1,
  "paymentMethod": "Toss",
  "amount": 1000
}
```

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "paymentId": 1,
    "orderId": 1,
    "paymentMethod": "Toss",
    "amount": 1000
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "already paid",
  "errorCode": "400"
}
```
```
{
"result": "FAIL",
"data": null
"message": "point insufficient",
"errorCode": "400"
}
```

## 6. 상위 상품 조회

- 3일간 가장 많이 팔린 상품 5개를 반환합니다.

### Request

- URL: `/api/v1/items/best`
- Method: GET

### Response

```
{
  "result": "SUCCESS",
  "data": [
    {
      "itemId": 11,
      "itemName": "가을 코트",
      "itemPrice": 40000,
      "itemOptionResponse": {
        "itemOptionId": 1,
        "itemOptionSize": "95",
        "itemOptionColor": "파랑",
        "itemOptionPrice": 0,
        "quantity": 35
      }
    },
    {
      "itemId": 22,
      "itemName": "가을 코트",
      "itemPrice": 30000,
      "itemOptionResponse": {
        "itemOptionId": 2,
        "itemOptionSize": "100",
        "itemOptionColor": "빨강",
        "itemOptionPrice": 0,
        "quantity": 25
      }
    },
    {
      "itemId": 33,
      "itemName": "여성 코트",
      "itemPrice": 25000,
      "itemOptionResponse": {
        "itemOptionId": 3,
        "itemOptionSize": "105",
        "itemOptionColor": "노랑",
        "itemOptionPrice": 0,
        "quantity": 20
      }
    },
    {
      "itemId": 44,
      "itemName": "남성 코트",
      "itemPrice": 10000,
      "itemOptionResponse": {
        "itemOptionId": 4,
        "itemOptionSize": "95",
        "itemOptionColor": "파랑",
        "itemOptionPrice": 0,
        "quantity": 15
      }
    },
    {
      "itemId": 55,
      "itemName": "겨울 코트",
      "itemPrice": 20000,
      "itemOptionResponse": {
        "itemOptionId": 5,
        "itemOptionSize": "100",
        "itemOptionColor": "검정",
        "itemOptionPrice": 0,
        "quantity": 10
      }
    }
  ],
  "message": null,
  "errorCode": null
}
```

## 7. 장바구니 조회

- 주어진 식별자에 해당하는 고객의 포인트 잔액을 반환합니다.

### Request

- URL: `/api/v1/carts`
- Method: GET
- Headers:
    - `customerId`: Long (고객 식별자)

### Response

```
{
  "result": "SUCCESS",
  "data": [
    {
      "customerId": 3,
      "itemId": 21,
      "itemName": "겨울 코트",
      "itemPrice": 10000,
      "cartItemOptionResponse": {
        "itemOptionId": 1,
        "itemOptionSize": "95",
        "itemOptionColor": "빨강",
        "itemOptionPrice": 0,
        "quantity": 1
      }
    },
    {
      "customerId": 3,
      "itemId": 22,
      "itemName": "가을 난방",
      "itemPrice": 20000,
      "cartItemOptionResponse": {
        "itemOptionId": 10,
        "itemOptionSize": "105",
        "itemOptionColor": "파랑",
        "itemOptionPrice": 0,
        "quantity": 12
      }
    }
  ],
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "customer not found",
  "errorCode": "404"
}
```

```
{
  "result": "FAIL",
  "data": null
  "message": "item not found",
  "errorCode": "404"
}
```

## 8. 장바구니 추가

- 주어진 고객 식별자, 상품정보로 장바구니에 상품을 추가합니다.

### Request

- URL: `/api/v1/customers/points/charge`
- Method: POST
- Headers:
  - `customerId`: Long (고객 식별자)
  - `Content-Type` : application/json
- Body:

```
{
  "itemId": 1,
  "itemOptionId": 2,
  "quantity": 5
}
```

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "customerId": 3,
    "itemId": 1,
    "itemOptionId": null,
    "quantity": 5
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "customer not found",
  "errorCode": "404"
}
```

```
{
  "result": "FAIL",
  "data": null
  "message": "item not found",
  "errorCode": "404"
}
```

## 9. 장바구니 삭제

- 주어진 고객, 상품, 상품옵션 식별자로 장바구니 상품을 삭제합니다.

### Request

- URL: `/api/v1/carts`
- Method: POST
- Headers:
  - `customerId`: Long (고객 식별자)
  - `Content-Type` : application/json
- Body:

```
{
  "itemId": 1,
  "itemOptionId": 2,
  "quantity": 5
}
```

### Response

```
{
  "result": "SUCCESS",
  "data": {
    "customerId": 3,
    "itemId": 1,
    "itemOptionId": 2,
    "quantity": 10
  },
  "message": null,
  "errorCode": null
}
```

### Error

```
{
  "result": "FAIL",
  "data": null
  "message": "customer not found",
  "errorCode": "404"
}
```

```
{
  "result": "FAIL",
  "data": null
  "message": "item not found",
  "errorCode": "404"
}
```
