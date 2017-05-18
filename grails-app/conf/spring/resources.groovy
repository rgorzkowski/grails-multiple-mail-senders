import pl.stepwise.mail.config.MailMessageBuilderFactory

// Place your Spring DSL code here
beans = {

    mailMessageBuilderFactory(MailMessageBuilderFactory) { bean ->
        bean.autowire = 'byName'
    }
}
