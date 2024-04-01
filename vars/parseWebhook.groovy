import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import groovy.json.JsonSlurperClassic

def call(String webhookString) {

    if (webhookString.isEmpty()) {
        throw new IllegalArgumentException('Webhook string cannot be empty')
    }
    Logger log = new Logger(this)
    log.info("Testing...")
    def jsonSlurper = new JsonSlurperClassic()
    def jsonObject = jsonSlurper.parseText(webhookString)
    return jsonObject
}
