environments {
    development {

        grails {
            mail {
                'default' {
                    from = "sender@stepwise.pl"
                }
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }
    }

    test {

        grails {
            mail {
                'default' {
                    from = "sender@stepwise.pl"
                }
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }
    }
}