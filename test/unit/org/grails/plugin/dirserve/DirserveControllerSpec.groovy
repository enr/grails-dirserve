package org.grails.plugin.dirserve

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

// import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
// import org.codehaus.groovy.grails.plugins.web.mimes.MimeTypesFactoryBean
// import org.codehaus.groovy.grails.web.mime.DefaultMimeUtility

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(DirserveController)
class DirserveControllerSpec extends Specification {

    /*
    def mimeTypes = [ html:'text/html', xml:'text/xml', text:'text/plain',
                        js:'text/javascript', css:'text/css', all:'* /*',
                        json:'application/json']
                        */
    
    /*
     * see https://github.com/grails/grails-core:
     * grails-plugin-mimetypes/src/test/groovy/org/codehaus/groovy/grails/web/mime/MimeUtilitySpec.groovy
     */
    def setup() {
        // in tests grailsMimeUtility has only the "html" mime registered.
        /*
        def ga = new DefaultGrailsApplication()
        ga.config.grails.mime.types = [ html : ['text/html','application/xhtml+xml'],
                                        xml  : ['text/xml', 'application/xml'],
                                        text : 'text/plain',
                                        js   : 'text/javascript',
                                        rss  : 'application/rss+xml',
                                        atom : 'application/atom+xml',
                                        css  : 'text/css',
                                        csv  : 'text/csv',
                                        all  : '* /*',
                                        json : ['application/json','text/json']
                                      ]
        final factory = new MimeTypesFactoryBean(grailsApplication: ga)
        def mimeTypes = factory.getObject()
        def mimeUtility = new DefaultMimeUtility(mimeTypes)
        controller.grailsApplication = ga
        controller.grailsMimeUtility = mimeUtility
        */
    }

    @Unroll
    def "should set the correct content type (#response_content_type) for asset #asset"() {
        given:
            controller.params.asset = "${asset}"
            controller.params.dirserveBase = "./testapps/dirserve/dirserve-dev"
        and:
            controller.index()
            //println response.getContentAsByteArray()
            //println response.status
            //println response.text
        expect:
            response.contentType == "${response_content_type}"
            response.status == 200
        where:
            asset                                       | response_content_type
            'index.html'                                | 'text/html;charset=utf-8'
            'fonts/glyphicons-halflings-regular.woff'   | 'application/font-woff;charset=utf-8'
            // in tests grailsMimeUtility has only the "html" mime registered.
            //'scripts/e8fd329a.scripts.js'               | 'JS'
            //'styles/eecb0c04.vendor.css'                | 'CSS'
            //'favicon.ico'                               | 'ICO'
    }

    @Unroll
    def "should return 404 if file does not exists [#asset]"() {
        given:
            controller.params.asset = asset
            controller.params.dirserveBase = "./testapps/dirserve/dirserve-dev"
        and:
            controller.index()
        expect:
            response.status == 404
        where:
            asset                   | _
            null                    | _
            ''                      | _
            'thisfiledoesnotexist'  | _
    }

    @Unroll
    def "should return 500 if dirserveBase is invalid [#dirserveBase]"() {
        given:
            controller.params.asset = "test.html"
            controller.params.dirserveBase = dirserveBase
        and:
            controller.index()
        expect:
            response.status == 500
        where:
            dirserveBase            | _
            null                    | _
            ''                      | _
            'thisdirdoesnotexist'   | _
    }
    
    @Unroll
    def "should manage unknown content type [#asset]"() {
        given:
            controller.params.asset = "${asset}"
            controller.params.dirserveBase = "./testapps/dirserve/dirserve-dev"
        and:
            controller.index()
        expect:
            response.contentType == "application/octet-stream;charset=utf-8"
            response.status == 200
        where:
            asset            | _
            'testfile'       | _
            'testfile.xxx'   | _
    }

}
