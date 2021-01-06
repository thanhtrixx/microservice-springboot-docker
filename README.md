# microservice-springboot-docker

## I. Problem

1. Bank ABC want to provide a new feature on its website. The feature is to purchase prepaid data for a SIM card by getting a voucher code from a 3rd party. Anyone can purchase the prepaid data on the website without login.
2. The 3rd party provides an API for the client to call via HTTP(s) protocol to get the voucher code. The API always returns a voucher code after 3 to 120 seconds, it depends on the network traffic at that time.
3. The bank wants to build a new service(s) to integrate with that 3rd party. But it expects that the API will return voucher code or a message that says the request is being processed within 30 seconds.
4. If the web application receives the voucher code, it will show on the website for customers to use. In case that the code can't be returned in-time, the customer can receive it via SMS later.
5. The customer can check all the voucher codes that they have purchased by phone number on the website, but it needs to be secured.
6. Assume that the payment has been done before the website call to the services to get the voucher code.

## II. Analize

1. From Problem #1 &#8594; We need have a ThirdParty service.
2. From Problem #1 & Problem #4 &#8594; We need have a PhoneCarrier service.
3. Problem #2 & Problem #3 & Problem #4 &#8594; The main service (I named PurchaseData) must handle for the following 2 cases:

    3.1. Network traffic good. ThirdParty service return less than 30 seconds. PurchaseData can response data voucher to user.
    3.2. Network traffic bad. ThirdParty service return greater than 30 seconds. PurchaseData must stop the current job and have another job run after when ThirdParty service return data voucher will to send data voucher to the user by SMS.

4. From Analize #3.2 &#8594; There must be an intermediary service (I named DataVoucher) to continue waiting for the ThirdParty service to return the data voucher.
5. From Analize #3.2 & Analize #4 &#8594; How can PurchaseData know the Third Party has returned the data voucher to the user to send the SMS?
6. From the question in Analize #5. We can solve it by the following 2 ways:

    6.1. DataVoucher notify to PurchaseData after ThirdParty response.
    6.2. PurchaseData call DataVoucher to ask the data voucher from ThirdParty.

7. From Problem #5 &#8594; must use database.
8. From Problem #5 &#8594; must apply authentication.
9. From Problem #5 & Analize #8 &#8594; there is a contradictory problem. Can an authenticated user see the data of another user? 

### II.1. PurchaseData: active or passive?

From Analize #6, we have 2 options to choose from. Each option has its own strengths and weaknesses.

##### PurchaseData active strengths:
+ All logic is concentrated in PurchaseData
+ Not dependent on Data Voucher notify

##### PurchaseData passive strengths:
+ PurchaseData simplified

**&#8594; PurchaseData active better than.**

### II.2. Security problem

From Analize #9. I find the requirement above is unreasonable, so I made a little change to the more reasonable request:

1. Authenticated requests to purchase data voucher from the user.
2. Authenticated requests to get list of vouchers that have been purchased by user.

## III. Technical stack

Based on the above requirements and my personal experience. I will choose a Technical stack as follows:

+ Java 8
+ Spring Boot 2
+ MySql 5.6 + Spring JPA + Hibernate
+ JWT + Spring Security + Jjwt
+ Spring Cloud Sleuth
+ Redis 6.0 + Spring Data + Jedis
+ Gradle 6.7
+ Docker + Docker-compose
+ Junit5 + Mockito 3.6

## IV. Architecture Documents

### IV.1. Architecture layers diagram

![Architecture layers diagram](https://trile.dev/img/post/cc-1-architecture.svg)

### IV.2. Functions diagram

![Functions diagram](https://trile.dev/img/post/cc-1-functions.svg)
 
### IV.3. Deploy diagram

![Deploy diagram](https://trile.dev/img/post/cc-1-deploy.svg)

### IV.4. Entity diagram

![Entity diagram](https://trile.dev/img/post/cc-1-entity.svg)

### IV.5. Sequel diagrams

###### Login

![Login](https://trile.dev/img/post/cc-1-sequence-user-login.svg)

###### Purchase data voucher

![Purchase data voucher](https://trile.dev/img/post/cc-1-sequence-purchase-data-voucher.svg)

###### Get purchased data vouchers

![Get purchased data vouchers](https://trile.dev/img/post/cc-1-sequence-get-purchased-data-vouchers.svg)

###### Apply data voucher

![Apply data voucher](https://trile.dev/img/post/cc-1-sequence-apply-data-voucher.svg)


## V. APIs

[Here](docs/api-spec.apib)

## VI. Test

Perform the following steps to run the test:

+ Start docker-compose:

```
./docker-compose-up.sh -f
```

[![Asciicast](https://asciinema.org/a/382753.svg)](https://asciinema.org/a/382753)

Use option **-f** to build jar files & docker images

+ Login to get JWT token. I use [HTTPie](https://httpie.io/) because it's readable than *curl*:

```
http POST 'http://localhost:8080/user/login?name=trile&password=123123'
```

![Login success](https://trile.dev/img/post/cc-1-login.png)

+ Login with invalid username or password:

```
http POST 'http://localhost:8080/user/login?name=trile&password=incorrect-pass'
```

![Login incorrect pass](https://trile.dev/img/post/cc-1-login-incorrect-pass.png)

+ Purchase data:

```
http POST 'http://localhost:8080/purchase-data/' \
    Authorization:'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjoxNjA5ODIwNDM2fQ.kL0ABL5te5DEsBbEmRqgsK__nan4FGj0Q2gLhNh8FJEo0acqjMHY3-X22fRjJCZQglwnTeRzHPMqXdpul4-vjg'
```

![Purchase data](https://trile.dev/img/post/cc-1-purchase-data-sms.png)

At backend

![Send SMS](https://trile.dev/img/post/cc-1-send-sms.png)

+ Purchase data with token invalid:

```
http POST 'http://localhost:8080/purchase-data/' \
    Authorization:'Bearer token_invalid'
```

![Purchase data with token invalid](https://trile.dev/img/post/cc-1-purchase-data-voucher-token-invalid.png)

+ List purchased data vouchers:

```
http GET 'http://localhost:8080/purchase-data/' \
    Authorization:'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjoxNjA5OTMxMjYzfQ.mp3pnFYvfcKHj9Pr3sQocGlSXfXowNvREZzyreQxhTYTMIfcNsNdHK4dgxhoFKNwjeGhiYcsN9zeYiQwQceYLg'
```

![Purchase data with token invalid](https://trile.dev/img/post/cc-1-purchased-data-vouchers.png)

+ List purchased data vouchers with token invalid:

```
http GET 'http://localhost:8080/purchase-data/' \
    Authorization:'Bearer token_invalid'
```

![List purchased data vouchers with token invalid](https://trile.dev/img/post/cc-1-purchased-data-vouchers-token-invalid.png)

+ Test Tracing with B3 Propagation

```
http POST 'http://localhost:8080/purchase-data/' \
    Authorization:'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjoxNjA5OTMxMjYzfQ.mp3pnFYvfcKHj9Pr3sQocGlSXfXowNvREZzyreQxhTYTMIfcNsNdHK4dgxhoFKNwjeGhiYcsN9zeYiQwQceYLg' x-b3-traceid:'74c34ca310a4392e' \
    b3:'80f198ee56343ba864fe8b2a57d3eff7-e457b5a2e4d86bd1-1-05e3ac9a4f6e3b90'
```

![B3 Propagation](https://trile.dev/img/post/cc-1-b3-propagation.png)

##### UnitTest

UnitTest classes:

###### PurchaseData

+ [PurchaseDataServiceTest](purchase-data/src/test/java/tri/le/purchasedata/service/PurchaseDataServiceTest.java)
+ [JwtServiceTest](purchase-data/src/test/java/tri/le/purchasedata/service/JwtServiceTest.java)

###### DataVoucher

+ [DataVoucherServiceTest](data-voucher/src/test/java/tri/le/datavoucher/service/DataVoucherServiceTest.java)
