package jm.desprez

import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class DWConfiguration : Configuration() {
    val rootPath: String = ""
    val swagger: SwaggerBundleConfiguration = SwaggerBundleConfiguration()
}