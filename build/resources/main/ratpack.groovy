import groovy.json.JsonSlurper
import groovy.sql.Sql

import static groovy.json.JsonOutput.toJson
import static ratpack.groovy.Groovy.ratpack
//TODO
// - Move each handler to it's own file (ie 'locationsEndpoint')?
// - Consider writing separate functions to make handler code less repetitive
// - Add PATCH handlers



ratpack {
    def db = [url: 'jdbc:mysql://localhost/location_data', user: 'LocationtService', password: 'test', driver: 'com.mysql.cj.jdbc.Driver']
    def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
    def jsonParser = new JsonSlurper()


    UsersEndpoint usersEndpoint = new UsersEndpoint(sql, jsonParser)
    LocationsEndpoint locationsEndpoint = new LocationsEndpoint(sql, jsonParser)

    handlers {
        // /users
        prefix("users", usersEndpoint)

        // /locations
        prefix("locations", locationsEndpoint)

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