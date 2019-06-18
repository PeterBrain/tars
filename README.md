# TARS - Time and Activity Recording Software

Don't forget to edit your **db.properties** file at the root of **src**!

```
db.url=jdbc:mysql://localhost/xxx
db.username=xxx
db.password=xxx
```

## Inital setup steps
1. call http://[ip-address]/Tars/fillUsers
2. call http://[ip-address]/Tars/fillCategories
3. call http://[ip-address]/Tars/fillProjects
4. call http://[ip-address]/Tars/fillEntries

## ToDo
* Methods:
    * Archive
        * edit entry (if project leader is admin -> entry update doesn't work)
        * add category to entry
    * Export
        * excel
        * email
    * Modification History
        * add history entry

---
This project was set up for the "Software engineering advanced" lecture at [FH JOANNEUM](https://www.fh-joanneum.at/) Graz.
