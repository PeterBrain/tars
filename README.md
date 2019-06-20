# TARS - Time and Activity Recording Software

Don't forget to create/edit your **db.properties** file at the root of **src** folder!

```
db.url=jdbc:mysql://localhost/xxx
db.username=xxx
db.password=xxx
```

Don't forget to edit your **dispatcher-servlet.xml** file at the root of **WEB-INF** folder and fill in your username and password of [mailtrap.io](https://mailtrap.io/)!

```
<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    <property name="host" value="smtp.mailtrap.io" />
    <property name="port" value="2525" />
    <property name="username" value="xxxxxxxxxxxxxxx" />
    <property name="password" value="xxxxxxxxxxxxxxx" />

    <property name="javaMailProperties">
        <props>
            <prop key="mail.smtp.auth">true</prop>
            <prop key="mail.smtp.starttls.enable">true</prop>
        </props>
    </property>
</bean>
```

## Inital setup steps
1. create and fill db.properties file
2. edit dispatcher-servlet.xml at bean with id="mailSender"
3. call http://[ip-address]/Tars/fillUsers

## ToDo
* Methods:
    * Archive (M)
        * edit Entry (stack overflow as admin)
    * Export (M) (works, but needs to be optimized)
        * excel (only selected ids)
        * email (only selected ids)
    * Modification History (M)
        * add history entry
    * Dashboard
        * Overtime and holiday (M)
    * Settings (HD)
        * admin should change some settings

## Contributors
* [@xK3v](https://github.com/xK3v) - Kevin Kazianschütz
* [@DiOps](https://github.com/DiOps) - Nikolaus Köstinger
* [@PeterBrain](https://github.com/PeterBrain) - Peter Löcker

---
This project was set up for the "Software engineering advanced" lecture at [FH JOANNEUM](https://www.fh-joanneum.at/) Graz.
