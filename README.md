# client-transaction
Running client-transaction application

It's a spring boot application

find and run com.investec.clienttransaction.ClientTransactionApplication.java class by clicking on Ctrl + Shift + F10

There are four REST API endpoints to create, find, update and delete client transaction

1.	Create client transaction

POST http://localhost:8080/api/client/create


JSON request example

{
  "firstName": "Khensani",
  "lastName": "Mbalati",
  "fullname": "Khensani Joyce",
  "idNumber": 8709090530080,
  "mobileNumber": "0793654345",
  "physicalAddress": "Rose, Norfolk Street, Midrand, 2000",
"transactions": [
                    {
                        "amount": 150.0
                    }
                ]
} 

2.	Find client transaction

POST http://localhost:8080/api/client/find

JSON request example

{
  "firstName": "Joey",
  "idNumber": "8709090530080",
  "mobileNumber": "0793954545"
} 

3.	Update client transaction

PUT http://localhost:8080/api/client/update

JSON request example

{
  "firstName": "Khen",
  "lastName": "Mbalati",
  "fullname": "Khensani Joyce",
  "idNumber": 8709090530080,
  "mobileNumber": "0799654345",
  "physicalAddress": "Rose, java Street, Midrand, 2000",
"transactions": [
                    {
                        "amount": 700.0
                    }
                ]
} 

4.	Delete client transaction

DELETE http://localhost:8080/api/client/delete?idNumber=8709090530080
