# TARS - Time and Activity Recording Software

## Purpose
This piece of software should help memebers of a company, and the company itself, to manage working hours and activities on different projects. Therefore TARS - Time and Activity Recording Software (pretty self-explaining).

## Setup Guide
1. Clone github repo
1. In Eclipse select `File` -> `Import` -> `Existing Projects into Workspace` -> `Browse Folder` -> `Finish`
1. Check Project-Properties (Java Build Path, Targeted Runtimes, ...)
1. Create and fill `src/db.properties` file as shown below
1. Edit `WebContent/WEB-INF/dispatcher-servlet.xml` at bean with `id="mailSender"` and set username and password for [mailtrap.io](https://mailtrap.io/)
1. Publish project to Tomcat and start Tomcat
1. Open Web application ([http://localhost:8080/Tars/](http://localhost:8080/Tars/))
1. Call [http://localhost:8080/Tars/fillUsers](http://localhost:8080/Tars/fillUsers) to fill the database with testing data
1. Log in with credentials (Pattern: [username]/[password]): admin/password, user/password

**`db.properties`** file at the root of **`src`** folder:
```
db.url=jdbc:mysql://localhost/xxx
db.username=xxx
db.password=xxx
```

**`dispatcher-servlet.xml`** file at the root of **`WebContent/WEB-INF`** folder:
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
Fill in your username and password of [mailtrap.io](https://mailtrap.io/)!

## Available Users
The table below shows some available users added by the `/fillUsers` call. The password for all users is `password`.

| Username      | Role           |
| :------------ | :------------- |
| admin         | ADMIN          |
| projectleader | PROJECT-LEADER |
| user          | USER           |
| lindsey       | PROJECT-LEADER |
| charlene      | USER           |
| tim           | PROJECT-LEADER |
| devin         | USER           |
| nancy         | PROJECT-LEADER |

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
* The most difficult part has to do with FetchType.Lazy. We didn't manage to get this working (so used EAGER)
* At first we used DAOs and entitiyManager for database queries. Later on it was much more difficult to add Spring Data JPA to the project. It would have saved us some time, if we had used it from the beginning.
* Time & activity recording (who did what?) was difficult at the end. We should have used this software for this project. How recursive is that?

## Contributors
* [@xK3v](https://github.com/xK3v) - Kazianschütz Kevin
* [@DiOps](https://github.com/DiOps) - Köstinger Nikolaus
* [@PeterBrain](https://github.com/PeterBrain) - Löcker Peter

---
This project was set up for the "Software engineering advanced" lecture at [FH JOANNEUM](https://www.fh-joanneum.at/) Graz.
