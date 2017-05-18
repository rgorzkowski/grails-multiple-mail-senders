package pl.stepwise.mail.config

import grails.config.Config
import grails.plugins.mail.MailMessageBuilder
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

import javax.mail.Session

class MailMessageBuilderFactory extends grails.plugins.mail.MailMessageBuilderFactory {

    @Override
    MailMessageBuilder createBuilder(Config config) {
        MailSender mailSender = this.mailSender
        if (config.props) {
            mailSender = getMailSender(config)
        }
        new MailMessageBuilder(mailSender, config, mailMessageContentRenderer)
    }

    MailSender getMailSender(config) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl()
        if (config.host) {
            mailSender.host = config.host
        } else if (!config.jndiName) {
            def envHost = System.getenv()['SMTP_HOST']
            if (envHost) {
                mailSender.host = envHost
            } else {
                mailSender.host = "localhost"
            }
        }

        if (config.encoding) {
            mailSender.defaultEncoding = config.encoding
        } else if (!config.jndiName) {
            mailSender.defaultEncoding = "utf-8"
        }

        if (config.port) {
            mailSender.port = config.port
        }

        if (config.username != null) {
            mailSender.username = config.username
        }

        if (config.password != null) {
            mailSender.password = config.password
        }

        if (config.protocol != null) {
            mailSender.protocol = config.protocol
        }

        if (config.props) {
            Session session = Session.getInstance(config.props.toProperties())
            session.setDebug(config.debug ?: false)
            mailSender.session = session
        }

        mailSender
    }
}
