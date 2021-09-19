import groovy.json.JsonSlurper
import groovy.sql.Sql
import ratpack.func.Action
import ratpack.groovy.Groovy
import ratpack.handling.Chain

import static groovy.json.JsonOutput.toJson

class LocationsEndpoint implements Action<Chain> {
    private Sql sql
    private JsonSlurper jsonParser

    LocationsEndpoint(Sql sql, JsonSlurper jsonParser) {
        this.sql = sql
        this.jsonParser = jsonParser
    }

    void execute(Chain chain) throws Exception {
        Groovy.chain(chain) {
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
                        request.getBody().then { locationData ->
                            def locationJson = jsonParser.parse(locationData.inputStream)
                            render toJson(sql.executeInsert("INSERT INTO locations (name) VALUES (${locationJson.name})"))
                        }
                    }
                }
            }
        }
    }
}