import groovy.json.JsonSlurper
import groovy.sql.Sql
import ratpack.func.Action
import ratpack.groovy.Groovy
import ratpack.handling.Chain

import static groovy.json.JsonOutput.toJson

//TODO Finish /devices
// - Actually create the devices table
// - Adjust POST handler
//    - Make it possible to create multiple devices at once
// - Add more ways to query (user, location, ID)
class DevicesEndpoint implements Action<Chain> {
    private Sql sql
    private JsonSlurper jsonParser

    DevicesEndpoint(Sql sql, JsonSlurper jsonParser) {
        this.sql = sql
        this.jsonParser = jsonParser
    }

    void execute(Chain chain) throws Exception {
        Groovy.chain(chain) {
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
