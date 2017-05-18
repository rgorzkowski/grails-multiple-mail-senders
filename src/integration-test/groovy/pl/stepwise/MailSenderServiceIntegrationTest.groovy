package pl.stepwise

import com.icegreen.greenmail.util.GreenMailUtil
import grails.plugin.greenmail.GreenMail
import grails.test.mixin.integration.Integration
import org.springframework.mail.MailAuthenticationException
import pl.stepwise.event.PrimaryMailAccountEvent
import reactor.bus.Event
import reactor.bus.selector.ClassSelector
import reactor.fn.Consumer
import spock.lang.Specification

import javax.mail.internet.MimeMessage
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Integration
class MailSenderServiceIntegrationTest extends Specification {

    MailSenderService mailSenderService
    GreenMail greenMail

    def "should send an email from primary account"() {
        given:
            CountDownLatch latch = new CountDownLatch(1)
            def event
            mailSenderService.on("pl.stepwise.primary.mail", {
                event = it
                latch.countDown()
            })
        when:
            mailSenderService.sendFromPrimaryAccount()
            latch.await(5, TimeUnit.SECONDS)
        then:
            assert event instanceof PrimaryMailAccountEvent
            assert greenMail.getReceivedMessages().length == 1
            MimeMessage message = greenMail.getReceivedMessages()[0]
            assert GreenMailUtil.getAddressList(message.from) == 'sender@stepwise.pl' // see mail configuration in application.groovy
            assert message.to == 'recipient@stepwise.pl' // see EmailNotificationService.groovy
            assert message.subject == 'Email from primary account'
    }

    def "should send an email from secondary account"() {
        given:
            CountDownLatch latch = new CountDownLatch(1)
            Exception e = null
            mailSenderService.on(new ClassSelector(Exception), new Consumer<Event<Exception>>() {
                @Override
                void accept(Event<Exception> excEvent) {
                    e = excEvent.getData()
                    latch.countDown()
                }
            })
        when:
            mailSenderService.sendFromSecondaryAccount()
            latch.await(5, TimeUnit.SECONDS)
        then:
            e && e instanceof MailAuthenticationException
    }

    void tearDown() {
        greenMail.deleteAllMessages()
    }
}
