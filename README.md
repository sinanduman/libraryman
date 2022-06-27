# Getting Started

### Library Api v0.1

It is a simple implementation of Library. Here I just implemented **Members** section.  
You can test the work either using **Swagger** or **Postman**.
I'll explain using Swagger.

### How It works


* You must get a JWT token to create/delete/query members
* I'll use sample User information.
  * userId = 1
  * username = sinanduman@gmail.com
  * password = "12344321"
  * validity = 1 day
  * scopes
    * lib.bo.c
    * lib.bo.r
    * lib.bo.r.all
    * lib.bo.return
    * lib.bm.r
    * lib.bm.c
    * lib.bm.d
    * lib.m.c
    * lib.m.r
    * lib.m.d
    * lib.m.r.all
* Firstly, you have to hava e JWT token
* To get a JWT token;
  * You can use **LoginController** api
    * When you click LoginController, you click Tryout, you must enter **Basic c2luYW5kdW1hbkBnbWFpbC5jb206MTIzNDQzMjE=**  
    * Also you can get Basic Auth from [Debug Bear](https://www.debugbear.com/basic-auth-header-generator)
    * After execute, you should get a sample JWT token from the response
    * `
      {  
  "username": "sinanduman@gmail.com",    
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJpYW0ubGlicmFyeS1zeXN0ZW0uY29tIiwic3ViIjoiMSIsImlzcyI6ImlhbS5saWJyYXJ5LXN5c3RlbS5jb20iLCJzY29wZXMiOlsibGliLmJtLmMiLCJsaWIuYm0uZCIsImxpYi5ibS5yIiwibGliLmJvLmMiLCJsaWIuYm8uciIsImxpYi5iby5yLmFsbCIsImxpYi5iby5yZXR1cm4iLCJsaWIubS5jIiwibGliLm0uZCIsImxpYi5tLnIiLCJsaWIubS5yLmFsbCJdLCJleHAiOjE2NTYzOTgxMDMsImlhdCI6MTY1NjMxMTcwM30.Il29JAl_irwXTRxKIoRAhU_AoDR72X08pxAbWrDhMwc"  
  }
` 
  * You can use sample User information to create sample JWT token from [jamiekurtz.com](http://jwtbuilder.jamiekurtz.com/) 

* After getting JWT token either from Swagger or other link, you can  call all the Api endpoints from Swagger UI.
* First you have to click Authorize button to enter Token.
  * Enter "Bearer `Token`" to input field and click Authorize. Else you'll get 403 Response when you call Api endpoints.
* After you are authenticated, you can call Member endpoints.
* All Api Endpoints have Role checks. If you don't have required Role, you'll get 403 Response. 

## Sample Api Calls

### Validation
Request fields must conform to validation rules. Otwerwise you'll get 400 - BAD_REQUEST Response  

#### Name, Surname
* Not Empty  
* Less than or equal 100 chars  

#### Date
* Not Empty
* Regex Pattern: `"^[12][0-9]{3}-[0-9]{2}-[0-9]{2}[T ][0-9]{2}:[0-9]{2}:[0-9]{2}.*"`  
* Sample Date: 2022-06-26T00:00:00  

#### Email
* Not Empty
* Regex Pattern: `"[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}"`  

#### Phone Number
* Not Empty
* Regex Pattern: `"^0?[1-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}$"`  
* Sample Phone Number: 0555 280 0229


### Member Create
`POST -> /library/api/v1/members` You need ***lib.m.c*** role  

REQUEST   
> `{
"email": "aliduman@gmail.com",   
"name": "Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"  
}` 

RESPONSE   
> `{
"memberId": 1,
"email": "aliduman@gmail.com",  
"name": "Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"
}`  


### Member Update
`PUT -> /library/api/v1/members/{id}` You need ***lib.m.c*** role  

REQUEST
> `id: 1`  
> `{
"email": "sinanaliduman@gmail.com",  
"name": "Sinan Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"
}`

RESPONSE  
> `{
"memberId": 1,  
"email": "aliduman@gmail.com",  
"name": "Sinan Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"  
}`  


### Member Delete
`DELETE -> /library/api/v1/members/{id}` You need ***lib.m.d*** role  

REQUEST
> `1`  

RESPONSE  
> `204 - No Content`  


### Member Get (Id)

`GET -> /library/api/v1/members/{id}` You need ***lib.m.r, lib.m.r.all*** role  

REQUEST  
> `id: 1`  

RESPONSE  
> `{
"memberId": 1,  
"email": "aliduman@gmail.com",  
"name": "Sinan Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"  
}`


### Member Get (All)

`GET -> /library/api/v1/members` You need ***lib.m.r.all*** role  

REQUEST  
> `name`  
> `surname`  
> `email`  
> `phoneNumber`    

RESPONSE
> `[
{
"memberId": 1,  
"email": "aliduman@gmail.com",  
"name": "Sinan Ali",  
"surname": "Duman",  
"phoneNumber": "0555 280 0229",  
"joinDate": "2022-06-26T00:00:00"  
}
]`


## Database design

#### Tables
##### [User]

<table>
<thead>
    <tr>
        <th>Field</th>
        <th>Type</th>
        <th>Null</th>
        <th>Key</th>
        <th>Default</th>
        <th>Ekstra</th>
    </tr>
</thead>
<tr>
    <td>user_id</td>
    <td>int(11)</td>
    <td>NO</td>
    <td>PRI</td>
    <td>NULL</td>
    <td>auto_increment</td>
</tr>
<tr>
    <td>username</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>password</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>role</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
</table>

##### [Member]

<table>
<thead>
    <tr>
        <th>Field</th>
        <th>Type</th>
        <th>Null</th>
        <th>Key</th>
        <th>Default</th>
        <th>Ekstra</th>
    </tr>
</thead>
<tr>
    <td>member_id</td>
    <td>int(11)</td>
    <td>NO</td>
    <td>PRI</td>
    <td>NULL</td>
    <td>auto_increment</td>
</tr>
<tr>
    <td>name</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>surname</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>email</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>phone_number</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
<tr>
    <td>join_date</td>
    <td>varchar(255)</td>
    <td>YES</td>
    <td></td>
    <td>NULL</td>
    <td></td>
</tr>
</table>

