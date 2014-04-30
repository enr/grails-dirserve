package org.grails.plugin.dirserve

import org.codehaus.groovy.grails.web.mime.MimeType
import org.codehaus.groovy.grails.web.mime.MimeUtility
import org.apache.commons.io.FilenameUtils

class DirserveController {

    def grailsApplication

    def grailsMimeUtility

    private static final ADDITIONAL_MIME_MAP = [
        'png':'image/png',
        'woff':'application/font-woff'
    ]

    def index() {
        boolean forceIndex = grailsApplication.config.grails.plugin.dirserve.forceIndex ?: false
        log.debug "Configuration grails.plugin.dirserve.forceIndex=${forceIndex}"
        String asset = params.asset ?: (forceIndex ? 'index.html' : '')

        if (!asset) {
            log.warn "Asset param missing. Sending 404"
            response.sendError(404)
            return
        }
        def extension = FilenameUtils.getExtension(asset)
        log.debug "Resolved asset: ${asset} [${extension}]"

        // conf mode
        //def dir = grailsApplication.config.grails.plugin.dirserve.basedir
        // params mode
        def dir = params.dirserveBase
        log.debug "Resolved configuration dir: ${dir}"
        // if !dir throw new error
        if (!dir) {
            log.warn "Configuration error. No dirserveBase set"
            response.sendError(500)
            return
        }
        File dirserveBase = new File(dir)
        if (!dirserveBase.exists()) {
            log.warn "Configuration error. dirserveBase ${dir} not found"
            response.sendError(500)
            return
        }

        dir = (dir.endsWith('/') ?  dir[0..-2] : dir)
        File file = new File("${dir}/${asset}")
        /*
        file = (file.endsWith('/') ?  file[0..-2] : file)
        if (file == dir && grailsApplication.config.grails.plugin.dirserve.forceIndex) {
            file = "${file}/index.html"
        }
        */
        // check canonical path starts with basedir
        log.debug "Resolved file: ${file}"
        if (!file.exists()) {
            log.warn "Requested file not found: ${file.getCanonicalPath()}"
            response.sendError(404)
            return
        }

        if (isNativelyKnown(extension)) {
            render(file: file)
            return
        }

        String contentType = getContentTypeForExtension(extension)
        if (contentType) {
            render(file: file, contentType: contentType)
            return
        }

        // unknown content type strategies:
        // - fallback 'application/octet-stream'
        // - ignore
        // - error throw new RuntimeException("Unknown file type ${asset}")
        render(file: file, contentType: 'application/octet-stream')
    }

    private boolean isNativelyKnown(String extension) {
        /*
        println "grailsMimeUtility=${grailsMimeUtility}"
        println "extension=${extension}"
        for (MimeType m: grailsMimeUtility.getKnownMimeTypes()) {
            println "   [MIME] ${m.name} ${m.extension}"
        }
        */
        MimeType mimeType = grailsMimeUtility.getMimeTypeForExtension(extension);
        return (mimeType != null)
    }

    private String getContentTypeForExtension(String extension) {
        return ADDITIONAL_MIME_MAP.get(extension)
    }
}
