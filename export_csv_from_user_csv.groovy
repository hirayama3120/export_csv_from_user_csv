@Grapes([
    @Grab(group = 'com.h2database', module = 'h2', version = '1.3.157'),
    @GrabConfig(systemClassLoader = true)
])
import groovy.sql.Sql

def database = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

def query = 
"""
    SELECT
        *
    FROM 
        CSVREAD('user.csv') S
    ORDER BY ID
"""

def today = new Date().format("yyyyMMdd")
def input_csv = new File("user.csv")
def export_csv = new File("users_${today}.csv")

if(!input_csv.exists()){
	println "user.csv not found"
    return
}

if(export_csv.exists()){
	println "Today's file is created"
    return
}else{
    export_csv.append("ID,FirstName,LastName,Age,MailAddress\n")

    database.eachRow(query) {
        res -> export_csv.append("${res.ID},${res.FirstName},${res.LastName},${res.Age},${res.MailAddress}\n")
    }
}

