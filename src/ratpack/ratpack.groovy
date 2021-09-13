import groovy.json.JsonSlurper
import groovy.sql.Sql

import static groovy.json.JsonOutput.toJson
import static ratpack.groovy.Groovy.ratpack

ratpack {
    def db = [url:'jdbc:mysql://localhost/location_data', user:'LocationtService', password:'test', driver:'com.mysql.cj.jdbc.Driver']
    def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

    def jsonParser = new JsonSlurper()

    handlers {
        prefix("users") {
            path {
                byMethod {
                    get("list") { //TO-DO: This doesn't work under the path closure
                        sql.rows("SELECT * FROM users").collect { result ->
                            render toJson(result)
                        }
                    }
                    get {
                        String username = request.queryParams.username

                        sql.rows("SELECT * FROM users WHERE username = ${username}").collect { result ->
                            render toJson(result)
                        }
                    }
                    put {
                        request.getBody().then {userData ->
                            def userJson = jsonParser.parse(userData.inputStream)
                            sql.executeInsert("INSERT INTO users (fname, lname) VALUES (${userJson.fname}, ${userJson.lname})").collect {
                                render toJson(it)
                            }
                        }
                    }
                }
            }
        }
        prefix("locations") {
            get("list") {
                sql.rows("SELECT * FROM locations").collect { result ->
                    render toJson(result)
                }
            }
        }
        prefix("devices") {
            get {}
        }
    }
}