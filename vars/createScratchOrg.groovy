#!/usr/bin/env groovy
import com.claimvantage.jsl.Org

def call(Org org) {
    
    echo "Create scratch org ${org.name}"
    
    def HUB_ORG = env.HUB_ORG_DH
    def SFDC_HOST = env.SFDC_HOST_DH
    def CONNECTED_APP_CONSUMER_KEY = env.CONNECTED_APP_CONSUMER_KEY_DH

    shWithStatus "sfdx force:auth:jwt:grant --clientid ${CONNECTED_APP_CONSUMER_KEY} --username ${HUB_ORG} --jwtkeyfile ${jwt_key_file} --setdefaultdevhubusername --instanceurl ${SFDC_HOST}"

    // Username identifies the org in later stages
    def create = shWithResult "sfdx force:org:create --definitionfile config/project-scratch-def.json --json --setdefaultusername"
    org.username = create.username
    org.orgId = create.orgId

    // Password and instance useful for manual debugging after the build (if org kept)
    shWithStatus "sfdx force:user:password:generate --targetusername ${org.username}"
    def display = shWithResult "sfdx force:org:display --json --targetusername ${org.username}"
    org.password = display.password
    org.instanceUrl = display.instanceUrl

    echo "Created scratch org name ${org.name} username ${org.username} password ${org.password} url ${org.instanceUrl} orgId ${org.orgId}"
}
