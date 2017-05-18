package pl.stepwise

import grails.config.Config
import org.grails.config.PropertySourcesConfig
import pl.stepwise.event.PrimaryMailAccountEvent
import pl.stepwise.event.SecondaryMailAccountEvent
import reactor.spring.context.annotation.Consumer
import reactor.spring.context.annotation.Selector

import javax.annotation.PostConstruct

@Consumer
class EmailNotificationService {

    def mailService

    def grailsApplication

    Config extendedMailConfig

    @PostConstruct
    void postInit() {
        extendedMailConfig = new PropertySourcesConfig(grailsApplication.config['extendedMail'])
    }

    @Selector('pl.stepwise.primary.mail')
    def sendMailUsingPrimaryAccount(PrimaryMailAccountEvent event) {
        log.debug 'Sending an email from primary account'
        mailService.sendMail {
            multipart true
            to 'recipient@stepwise.pl'
            subject 'Email from primary account'
            text "Hello Primary Account"
            html '<h1>Hello Primary Account</h1>'
        }
    }

    @Selector('pl.stepwise.secondary.mail')
    def sendMailUsingSecondaryAccount(SecondaryMailAccountEvent event) {
        log.debug 'Sending an email from secondary account'
        mailService.sendMail(extendedMailConfig) {
            multipart true
            from extendedMailConfig.from
            to 'recipient@stepwise.pl'
            subject 'Email from secondary account'
            text 'Hello Secondary Account'
            html '<h1>Hello Secondary Account</h1>'
        }
    }
}
