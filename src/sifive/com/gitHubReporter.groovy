class GitHubReporter implements Serializable {

    static final String GITHUB_OAUTH_TOKEN_CREDENTIALS_ID = 'bab094ff-ed07-4f37-ab33-67803368e526'

    private final def pipelineContext
    private final def sendGitHubStatus
    private final String context = "CI Launcher"

    GitHubReporter(def pipelineContext, def sendGitHubStatus) {
        this.pipelineContext = pipelineContext
        this.sendGitHubStatus = sendGitHubStatus
    }

    private String getJobURL() {
        pipelineContext.BUILD_URL
    }

    private void sendStatus(String statusesURL, String status, String description, String context, String targetURL) {
        def requestBody = JsonOutput.toJson([
                state      : status,
                description: description,
                target_url : targetURL,
                context    : context
        ])
        if (sendGitHubStatus){
            sendRequest(statusesURL, requestBody)
        } else {
            pipelineContext.echo "NOT Sending Status to GitHub: ${requestBody}"
        }
    }

    void sendStatusNoLink(String statusesURL, String status, String description, String context) {
        def requestBody = JsonOutput.toJson([
                state      : status,
                description: description,
                context    : context
        ])
        if (sendGitHubStatus){
            sendRequest(statusesURL, requestBody)
        } else {
            pipelineContext.echo "NOT Sending Status to GitHub: ${requestBody}"
        }
    }

    private void sendRequest(String statusesURL, String requestBody) {
        pipelineContext.withCredentials([[
                $class       : 'StringBinding',
                credentialsId: GITHUB_OAUTH_TOKEN_CREDENTIALS_ID,
                variable     : 'GITHUB_OAUTH_TOKEN',
        ]]) {
            pipelineContext.sh """
            set +x
            curl --fail -H "Authorization: token \$GITHUB_OAUTH_TOKEN" -H 'Content-Type: application/json' -X POST '${statusesURL}' -d '${requestBody}'
            """
        }
    }

    void sendErrorStatus(String statusesURL) {
        sendStatus(statusesURL, "error", "CI Launching", context, getJobURL())
    }

    void sendFailureStatus(String statusesURL) {
        sendStatus(statusesURL, "failure", "CI Launching", context, getJobURL())
    }

    void sendPendingStatus(String statusesURL) {
        sendStatus(statusesURL, "pending", "CI Launching", context, getJobURL())
    }

    void sendSuccessStatus(String statusesURL) {
        sendStatus(statusesURL, "success", "CI Launcher Finished", context, getJobURL())
    }

}
