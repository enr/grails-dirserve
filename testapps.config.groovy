
String projectDirCommon = new File('target').absolutePath

dirserve {

    grailsw = true

    pluginInstall = true

    projectDir = projectDirCommon

    // append timestamp to testapp name?
    timestamp = false

    // will be appended to grails-app/conf/Config.groovy
    customConfig = '''
//grails.plugin.dirserve.basedir = './src/dirserve'
grails.plugin.dirserve.forceIndex = true
'''


    // packages added to Config.groovy in log4j section
    log {
        debug = [  'grails.app.conf.BootStrap',
                   'grails.app.filters',
                   'grails.app.dataSource',
                   'grails.app.tagLib',
                   'grails.app.services',
                   'grails.app.controllers',
                   'grails.app.domain']
    }

}
