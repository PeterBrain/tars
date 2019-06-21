# TARS - Time and Activity Recording Software

## Purpose
This piece of software should help memebers of a company, and the company itself, to manage working hours and activities on different projects. Therefore TARS - Time and Activity Recording Software (pretty self-explaining).

## Setup Guide
1. Download project source
2. Create `New dynamic Web project` eclipse project, convert to maven project and import sources
3. create and fill `src/db.properties` file
4. Change jpa-properties in `WebContent/WEB-INF/dispatcher-servlet.xml` to required attributes (`validate`, `update`, or `create-drop`)
5. edit `WebContent/WEB-INF/dispatcher-servlet.xml` at bean with `id="mailSender"` and set username and password for [mailtrap.io](https://mailtrap.io/)
6. Setup your eclipse project (Server, Runtime, ...)
7. Publish project to Tomcat and start Tomcat
8. [Open Web application](http://localhost:8080/Tars/)
9. Call `http://localhost:8080/Tars/fillUsers`
10. Login credentials: admin/password, user/password

Don't forget to create and edit your **`db.properties`** file at the root of **`src`** folder!
```
db.url=jdbc:mysql://localhost/xxx
db.username=xxx
db.password=xxx
```

Don't forget to edit your **`dispatcher-servlet.xml`** file at the root of **`WEB-INF`** folder and fill in your username and password of [mailtrap.io](https://mailtrap.io/)!
```xml
<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    <property name="host" value="smtp.mailtrap.io" />
    <property name="port" value="2525" />
    <property name="username" value="xxxxxxxxxxxxxxx" /> <!-- edit this value -->
    <property name="password" value="xxxxxxxxxxxxxxx" /> <!-- edit this value -->

    <property name="javaMailProperties">
        <props>
            <prop key="mail.smtp.auth">true</prop>
            <prop key="mail.smtp.starttls.enable">true</prop>
        </props>
    </property>
</bean>
```
## Available Users
The table below shows some available users added by the `/fillUsers` call. The password for all users is `password`.

| Username              | Roles                                  |
| :-------------------- | :------------------------------------- |
| admin                 | ADMIN                                  |
| projectleader         | PROJECT-LEADER                         |
| user                  | USER                                   |
| lindsey               | PROJECT-LEADER                         |
| charlene              | USER                                   |
| tim                   | PROJECT-LEADER                         |
| devin                 | USER                                   |
| nancy                 | PROJECT-LEADER                         |

## Workload distribution
The table below shows the members of the team, who worked on specific features. At the end of the project all team members worked on each feature in the backend and frontend.

| Feature / Work        | Team Member(s)                         |
| :-------------------- | :------------------------------------- |
| User Management       | Köstinger Nikolaus, Peter Löcker       |
| Entries               | Kazianschütz Kevin, Peter Löcker       |
| Projects & Categories | Kazianschütz Kevin, Peter Löcker       |
| Export                | Peter Löcker                           |
| Security Messages     | Kazianschütz Kevin, Köstinger Nikolaus |
| Charts                | Köstinger Nikolaus                     |
| Modification History  | Kazianschütz Kevin, Köstinger Nikolaus |

## Lessons learned
* The most difficult part has to do with FetchType.Lazy. We didn't manage to get this working (we used EAGER)
* At first we used DAOs and entitiyManager for database queries. Later on it was much more difficult to add Spring Data JPA to the project. It would have saved us some time, if we had used it from the beginning.
* Time & activity recording (who did what?) was difficult at the end. We should have used this software for this project. How recursive is that?

## Contributors
* [@xK3v](https://github.com/xK3v) - Kazianschütz Kevin
* [@DiOps](https://github.com/DiOps) - Köstinger Nikolaus
* [@PeterBrain](https://github.com/PeterBrain) - Löcker Peter

---
This project was set up for the "Software engineering advanced" lecture at [FH JOANNEUM](https://www.fh-joanneum.at/) Graz.
