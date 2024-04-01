import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import groovy.json.JsonSlurperClassic

def call(Map config = [:]) {

    // retrieve the configuration
    Map scmCfg = (Map) config[ConfigConstants.SCM] ?: [:]
    Logger log = new Logger(this)
    log.info("Testing...")
    def jsonSlurper = new JsonSlurperClassic()
    def jsonObject = jsonSlurper.parseText(webhookString)
    return jsonObject
}
