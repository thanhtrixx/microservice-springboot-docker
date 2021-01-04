# microservice-springboot-docker

### I. Problem

1. Bank ABC want to provide a new feature on its website. The feature is to purchase prepaid data for a SIM card by getting a voucher code from a 3rd party. Anyone can purchase the prepaid data on the website without login.
2. The 3rd party provides an API for the client to call via HTTP(s) protocol to get the voucher code. The API always returns a voucher code after 3 to 120 seconds, it depends on the network traffic at that time.
3. The bank wants to build a new service(s) to integrate with that 3rd party. But it expects that the API will return voucher code or a message that says the request is being processed within 30 seconds.
4. If the web application receives the voucher code, it will show on the website for customers to use. In case that the code can't be returned in-time, the customer can receive it via SMS later.
5. The customer can check all the voucher codes that they have purchased by phone number on the website, but it needs to be secured.
6. Assume that the payment has been done before the website call to the services to get the voucher code.

### II. Analize

1. From Problem #1 &#8594; We need have a ThirdParty service.

2. From Problem #1 & Problem #4 &#8594; We need have a PhoneCarrier service.

3. Problem #2 & Problem #3 & Problem #4 &#8594; The main service (I named PurchaseData) must handle for the following 2 cases:

    3.1. Network traffic good. ThirdParty service return less than 30 seconds. PurchaseData can response data voucher to user.
    
    3.2. Network traffic bad. ThirdParty service return greater than 30 seconds. PurchaseData must stop the current job and have another job run after when ThirdParty service return data voucher will to send data voucher to the user by SMS.

4. From Analize #3.2 &#8594; There must be an intermediary service (I named DataVoucher) to continue waiting for the ThirdParty service to return the data voucher.

5. From Analize #3.2 & Analize #4 &#8594; How can PurchaseData know the Third Party has returned the data voucher to the user to send the SMS?

6. From the question in Analize #5. We can solve it by the following 2 ways:

    6.1. DataVoucher notify to PurchaseData after ThirdParty response
    6.2. PurchaseData call DataVoucher to ask the data voucher from ThirdParty

7. From Problem #5 &#8594; must use database

8. From Problem #5 &#8594; must apply authentication

9. From Problem #5 & Analize #8 &#8594; there is a contradictory problem. Can an authenticated user see the data of another user?

