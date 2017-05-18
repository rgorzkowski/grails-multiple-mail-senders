package pl.stepwise

import java.util.concurrent.TimeUnit

class MailController {

    MailSenderService mailSenderService

    def index() {

    }

    def sendFromPrimaryAccount() {
        mailSenderService.sendFromPrimaryAccount()
        TimeUnit.MILLISECONDS.sleep(300)
        forward(controller : 'greenmail', action: 'index')
    }

    def sendFromSecondaryAccount() {
        mailSenderService.sendFromSecondaryAccount()
    }
}
