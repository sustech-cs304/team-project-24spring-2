
# Campus-Event-System RESTful API

Campus-Event-System RESTful 文档

## Endpoints

- [Campus-Event-System RESTful API](#campus-event-system-restful-api)
  - [Endpoints](#endpoints)
  - [Event](#event)
    - [1. explore-events](#1-explore-events)
    - [2. explore-events-size](#2-explore-events-size)
    - [3. list-events](#3-list-events)
    - [4. delete-event](#4-delete-event)
    - [5. audit-event](#5-audit-event)
    - [6. create-event](#6-create-event)
    - [7. update-event](#7-update-event)
    - [8. get-event](#8-get-event)
    - [9. list-events-size](#9-list-events-size)
    - [10. get-event-user-tickets-status](#10-get-event-user-tickets-status)
    - [11. publish-event](#11-publish-event)
  - [User](#User)
    - [1. login-password](#1-login-password)
    - [2. register](#2-register)
    - [3. profile](#3-profile)
    - [4. get-upload-images](#4-get-upload-images)
    - [5. fetch-register-code](#5-fetch-register-code)
    - [6. get-tickets](#6-get-tickets)
    - [7. checkout-ticket](#7-checkout-ticket)
    - [8. get-tickets](#8-get-tickets)
  - [Comment](#Comment)
    - [1. make-comment](#1-make-comment)
    - [2. delete-comment](#2-delete-comment)
    - [3. get-comment](#3-get-comment)
    - [4. get-comments](#4-get-comments)
    - [5. get-comment-attachments](#5-get-comment-attachments)
  - [File](#File)
    - [1. upload](#1-upload)
  - [Global](#Global)
    - [1. get-setting](#1-get-setting)
    - [2. set-setting](#2-set-setting)
    - [3. get-settings](#3-get-settings)
  - [Order](#Order)
    - [1. make-order](#1-make-order)
    - [2. get-order](#2-get-order)
    - [3. cancel-order](#3-cancel-order)
    - [4. get-orders](#4-get-orders)

--------



## Event

该目录下包含了所有与活动相关的API



### 1. explore-events


获取某页的活动

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/explore-events
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| page |  | 页数 |
| size |  | (Optional) 每页的数量, 默认为10 |
| title |  | (Optional) 搜索名称 |
| category |  | (Optional) 筛选种类 |



### 2. explore-events-size


获取浏览的所有活动的数量（仅包含PENDING和IN_PROGRESS状态的）


***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/explore-events-size
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| title |  | (Optional) 搜索名称 |
| category |  | (Optional) 筛选种类 |



### 3. list-events


（用于后台）列出活动（所有的活动），需要权限>=1，部门管理（权限=1）只能看到自己的活动，高等级管理看到所有活动


***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/list-events
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| page |  | 页数，-1则显示所有 |
| size |  | (Optional) 每页数量，默认为10 |
| title |  | (Optional) 搜索名称 |
| category |  | (Optional) 筛选种类 |
| publisher |  | (Optional) 筛选发布者（通过名称） |
| statuses |  | (Optional) 筛选状态，如EDITING, AUDITING, EDITING, FINISHED, IN_PROGRESS，可多个，用单个逗号隔开 |
| publisherId |  | (Optional) 筛选发布者（通过ID） |



### 4. delete-event


删除事件，需要权限>=1，部门管理（权限=1）只能删自己的活动


***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/delete-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动Id |



### 5. audit-event


审核活动，必须权限>=2


***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/event/audit-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动Id |
| pass |  | 是否通过 |



***Body:***

```js        
{
    "reason": "reason"
}
```



### 6. create-event

创建活动，必须权限>=1

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/event/create-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Body:***

```js        
{
    "title": "event",
    "start_time": 0,
    "end_time": 0,
    "document_url": "",
    "image_url": "/image/image",
    "latitude": 35,
    "longitude": 120,
    "location_name": "Faculty Office",
    "category": "course",
    "tickets": [
        {
            "description": "VIP Ticket",
            "price": 888,
            "total_amount": 50,
            "sold_amount": 10
        },
        {
            "description": "Normal Ticket",
            "price": 120,
            "total_amount": 100
        }
    ]
}
```



### 7. update-event

更新活动，必须权限>=1

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/event/update-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动Id |



***Body:***

```js        
{
    "title": "event",
    "start_time": 1714420650298,
    "end_time": 1714430650298,
    "document_url": "/documents/test_event.md",
    "image_url": "/image/test_image.md",
    "latitude": 35,
    "longitude": 120,
    "location_name": "Faculty Office",
    "category": "course",
    "tickets": [
        {
            "description": "VIP Ticket",
            "price": 888,
            "total_amount": 50,
            "sold_amount": 10
        },
        {
            "description": "Normal Ticket",
            "price": 120,
            "total_amount": 100
        }
    ]
}
```



### 8. get-event

获得某个活动

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/get-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | (Optional) 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动Id |



### 9. list-events-size

列出活动数量

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/list-events-size
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| title |  | (Optional) 筛选名称 |
| category |  | (Optional) 筛选分类 |
| publisher |  | (Optional) 筛选发布者（通过名称） |
| statuses |  | (Optional) 筛选状态（可多个，逗号","分隔） |
| publisherId |  | (Optional) 筛选发布者（通过Id) |



### 10. get-event-user-tickets-status

获取活动当前票务信息

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/get-event-user-tickets-status
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动id |



### 11. publish-event

发布活动到审核

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/event/publish-event
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventId |  | 活动id |



## User



### 1. login-password

登录

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/user/login
```



***Body:***

```js        
{
    "user_input": "admin",
    "password": "admin"
}
```



### 2. register

注册

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/user/register
```



***Body:***

```js        
{
    "nickname": "test",
    "password": "test",
    "email": "test@test.com",
    "phone": "13333333333",
    "realName": "test"
}
```



### 3. profile

获取个人简介

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/profile
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



### 4. get-upload-images

获取用户图床

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/get-upload-images
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



### 5. fetch-register-code

获取邮箱注册验证码

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/fetch-register-code
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| email |  | 邮箱 |



### 6. get-tickets

获取用户的票根

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/get-tickets
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



### 7. checkout-ticket

检票入场，必须权限>=1

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/checkout-ticket
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| ticketId |  | 票根Id |



### 8. get-tickets

获取用户所有票根

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/user/get-tickets
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



## Comment



### 1. make-comment

评论

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/comment/make-comment
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Body:***

```js        
{
    "event_id": "",
    "content": ""
}
```



### 2. delete-comment

删除评论

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/comment/delete-comment
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| commentId |  | 评论Id |



### 3. get-comment

获取评论

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/comment/get-comment
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| commentId |  | 评论Id |



### 4. get-comments

获取活动所有评论

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/comment/get-comments
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| eventID |  | 活动Id |
| page |  | 页数 |
| size |  | 每页大小 |



### 5. get-comment-attachments



***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/comment/get-comment-attachments
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| commentId |  | 评论Id |



## File



### 1. upload

上传

***Endpoint:***

```bash
Method: POST
Type: FILE
URL: localhost:8080/api/file/upload
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| usage |  | 填写comment（上传评论附件），event（上传活动封面和文档），user（上传图床），avatar（上传头像） |
| commentId |  | (Optional) 如果是上传评论附件填写 |
| eventId |  | (Optional) 如果是上传事件的图片、文档填写 |



## Global



### 1. get-setting

获取全局设置

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/global/get-setting
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| key |  |  |



### 2. set-setting

设置全局设置，必须权限>=2

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/global/set-setting
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| key |  |  |
| value |  |  |



### 3. get-settings

获得所有全局设置

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/global/get-settings
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



## Order



### 1. make-order

创建订单

***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/order/make-order
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Body:***

```js        
{
    "ticketId": ""
}
```



### 2. get-order

获取订单

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/order/get-order
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| orderId |  | 订单id |



### 3. cancel-order

取消订单

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/order/cancel-order
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| orderId |  | orderId |



### 4. get-orders

获取所有订单

***Endpoint:***

```bash
Method: POST
Type: 
URL: localhost:8080/api/order/get-orders
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Authorization |  | 验证Token |



---
[Back to top](#campus-event-system-restful-api)

>Generated by [docgen](https://github.com/thedevsaddam/docgen)
