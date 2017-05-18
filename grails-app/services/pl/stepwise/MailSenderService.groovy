package pl.stepwise

import pl.stepwise.event.PrimaryMailAccountEvent
import pl.stepwise.event.SecondaryMailAccountEvent

class MailSenderService {

    def sendFromPrimaryAccount() {
        notify 'pl.stepwise.primary.mail',
                new PrimaryMailAccountEvent()
    }

    def sendFromSecondaryAccount() {
        notify 'pl.stepwise.secondary.mail',
                new SecondaryMailAccountEvent()
    }
}
