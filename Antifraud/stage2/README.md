## Description

Enterprise applications like an anti-fraud system are always used by different types of users having various access
levels. It is not necessary that every user can access every part of the system. It becomes more important in the
financial systems.

In this stage, a couple of rest endpoints are added to the system that enables user management. For the sake of
simplicity, only two types of the user are considered. In real systems, however, there is a wider variety of roles and
different access levels.

An anti-fraud system consists of two types of users:

* Normal users who are able to ask whether a transaction is allowed
* Admin users that can in addition to the query for a transaction, can change the main settings of the antifraud system
  e.g. add a stolen card number, add a prohibited IP address, etc. Furthermore, admin users can add/update/remove other
  users.

## Objectives

Add the following rest-endpoint

* An endpoint enables admin user(s) to add a user `POST /antifraud/user` enabling add new user
* An endpoint enables admin user(s) to see a list of all users `GET /antifraud/user` with the encrypted password
* An endpoint enables the admin user(s) to delete a user `DELETE /antifraud/user/{userId}`

## Examples

```
[
  {  
    "id":1,
    "name":"John Doe", 
    "username":"johen_doe", 
    "role":"user", 
    "password":"b51bfcfa2e352e4de32560f74b0cff3f"
  }, 
  { 
    "id":2, 
    "name":"Richard Roe", 
    "username":"richard2020", 
    "role":"admin", 
    "password":"e3274be5c857fb42ab72d786e281b4b8"
  }
]```
