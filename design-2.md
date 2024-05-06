<h1><center>Campus Events and Entertainment Center</center></h1>

<center><b>Team</b>: 2</center>

<center><b>Members</b>: 伦天乐，冯泽欣，罗嘉诚，苏军又，徐霄阳</center>

## 1 Project Architecture && UI Design

### 1.1 Architecture

![image-architecture](./assets/sprint1_image1.png)

**Description**:

Accoridng to our expectation of the number of our target user, monolithic architecture is enough. 

We split our back-end into 3 layers: DAO layer which provides service for accessing and managing data in database and transfering data to objects, service layer which provide service based on the objects loaded from database, controller layer which provide RESTful API for front-end. In addition, we implement OSS service with Minio and payment service with Alipay SDK.

Our front-ends includes two system: user-end system and manage system which are all developed with Vue 3.0 framework.

### 1.2 UI Design

#### 1.2.1 User End

**Description**: The 3 images shows the main 3 pages of user-end system which are homepage, exploring events and view event detail for purchasing.

![image-user-end-1](./assets/sprint1_user_end_1.png)

![image-user-end-2](./assets/sprint1_user_end_2.png)

![image-user-end-3](./assets/sprint1_user_end_3.png)

#### 1.2.2 Administrator End

**Description**: These pages show the main function of managing system.

![image-admin_end_1](./assets/sprint1_admin_end_1.png)

![image-admin_end_2](./assets/sprint1_admin_end_2.png)

![image-admin_end_3](./assets/sprint1_admin_end_3.png)

![image-admin_end_4](./assets/sprint1_admin_end_4.png)

![image-admin_end_5](./assets/sprint1_admin_end_5.png)

![image-admin_end_6](./assets/sprint1_admin_end_6.png)

![image-admin_end_7](./assets/sprint1_admin_end_7.png)

![image-admin_end_8](./assets/sprint1_admin_end_8.png)

![image-admin_end_9](./assets/sprint1_admin_end_9.png)
