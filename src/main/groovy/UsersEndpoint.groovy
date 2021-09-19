import groovy.json.JsonSlurper
import groovy.sql.Sql
import ratpack.func.Action
import ratpack.groovy.Groovy
import ratpack.handling.Chain

import static groovy.json.JsonOutput.toJson

class UsersEndpoint implements Action<Chain> {
    private Sql sql
    private JsonSlurper jsonParser

    UsersEndpoint(Sql sql, JsonSlurper jsonParser) {
        this.sql = sql
        this.jsonParser = jsonParser
    }

    void execute(Chain chain) throws Exception {
        Groovy.chain(chain) {
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
                        request.getBody().then { userData ->
                            def userJson = jsonParser.parse(userData.inputStream)
                            render toJson(sql.executeInsert("INSERT INTO users (fname, lname) VALUES (${userJson.fname}, ${userJson.lname})"))
                        }
                    }
                }
            }
        }
    }
}