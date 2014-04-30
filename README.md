Grails Dirserve plugin
======================

Serve static contents from a filesystem directory.


Usage
-----

For every directory you want to serve, add configuration to UrlMappings file:

    "/spa/$asset**" {
        controller = 'dirserve'
        action = 'index'
        dirserveBase = (( Environment.current == Environment.DEVELOPMENT || 
                          Environment.current == Environment.TEST) ? 
                          '/path/dev' : '/path/prod')
    }


Sample app
----------

This repository contains a sample app.

To see it in action:

    ./grailsw compile
    ./grailsw create-testapps -plain-output
    cd target/testapps-dirserve
    ./grailsw run-app

Go to:

    http://localhost:8080/testapp-dirserve/spa/index.html

to see the served directory.


License
-------

Apache 2.0 - see LICENSE file.

   Copyright 2014 grails-dirserve contributors

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
