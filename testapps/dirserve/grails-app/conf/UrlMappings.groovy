
import grails.util.Environment

class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/spa/$asset**" {
            controller = 'dirserve'
            action = 'index'
            dirserveBase = ((Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) ? './dirserve-dev' : './dirserve-prod')
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
