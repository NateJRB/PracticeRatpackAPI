import groovy.json.JsonSlurper
import groovy.sql.Sql

import static groovy.json.JsonOutput.toJson
import static ratpack.groovy.Groovy.ratpack

//TODO
// - Move each handler to it's own file (ie 'locationsEndpoint')?
// - Consider writing separate functions to make handler code less repetitive
// - Add PATCH handlers

ratpack {
    def db = [url:'jdbc:mysql://localhost/location_data', user:'LocationtService', password:'test', driver:'com.mysql.cj.jdbc.Driver']
    def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

    def jsonParser = new JsonSlurper()

    handlers {
        // /users
        prefix("users") {
            get("list") {
                render toJson(sql.rows("SELECT * FROM users"))
            }

            path {
                byMethod {
                    get {
                        String username = request.queryParams.username

                        render toJson(sql.rows("SELECT * FROM users WHERE username = ${username}"))
                    }

                    post {
                        request.getBody().then {userData ->
                            def userJson = jsonParser.parse(userData.inputStream)
                            render toJson(sql.executeInsert("INSERT INTO users (fname, lname) VALUES (${userJson.fname}, ${userJson.lname})"))
                        }
                    }
                }
            }
        }

        // /locations
        prefix("locations") {
            get("list") {
                render toJson(sql.rows("SELECT * FROM locations"))
            }

            path {
                byMethod {
                    get {
                        String username = request.queryParams.username

                        render toJson(sql.rows("SELECT * FROM locations WHERE username = ${username}"))
                    }
                    post {
                        request.getBody().then {locationData ->
                            def locationJson = jsonParser.parse(locationData.inputStream)
                            render toJson(sql.executeInsert("INSERT INTO locations (name) VALUES (${locationJson.name})"))
                        }
                    }
                }
            }
        }

        // /devices
        //TODO Finish /devices
        // - Actually create the devices table
        // - Adjust POST handler
        //    - Make it possible to create multiple devices at once
        // - Add more ways to query (user, location, ID)
        prefix("devices") {
            get("list") {
                render toJson(sql.rows("SELECT * FROM devices"))
            }

            path {
                byMethod {
                    get {
                        String username = request.queryParams.username

                        render toJson(sql.rows("SELECT * FROM devices WHERE username = ${username}"))
                    }
//                    post {
//                        request.getBody().then {deviceData ->
//                            def deviceJson = jsonParser.parse(deviceData.inputStream)
//                            render toJson(sql.executeInsert("INSERT INTO devices (fname, lname) VALUES (${userJson.fname}, ${userJson.lname})"))
//                        }
//                    }
                }
            }
        }
    }
}