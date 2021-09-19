import groovy.json.JsonSlurper
import groovy.sql.Sql

import static ratpack.groovy.Groovy.ratpack

//TODO
// - Consider writing separate functions to make handler code less repetitive
// - Add PATCH handlers

ratpack {
    def db = [url: 'jdbc:mysql://localhost/location_data', user: 'LocationtService', password: 'test', driver: 'com.mysql.cj.jdbc.Driver']
    def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
    def jsonParser = new JsonSlurper()

    // This doesn't seem like the most ideal way to do this, but it does work for now. It's an interesting way to do it.
    UsersEndpoint usersEndpoint = new UsersEndpoint(sql, jsonParser)
    LocationsEndpoint locationsEndpoint = new LocationsEndpoint(sql, jsonParser)
    DevicesEndpoint devicesEndpoint = new DevicesEndpoint(sql, jsonParser)

    handlers {
        prefix("users", usersEndpoint)
        prefix("locations", locationsEndpoint)
        prefix("devices", devicesEndpoint)
    }
}