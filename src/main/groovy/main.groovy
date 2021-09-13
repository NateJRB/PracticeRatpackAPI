// For now I'm just using ratpack.groovy as my execution point. Keeping this here in case I want it later.

package app

import ratpack.groovy.Groovy
import ratpack.server.RatpackServer

class MainGroovy {

    public static void main(String[] args) {
        RatpackServer.start { spec -> spec
                .handlers(Groovy.chain {
                    get {
                        render "Hello, World!"
                    }
                })
        }
    }
}