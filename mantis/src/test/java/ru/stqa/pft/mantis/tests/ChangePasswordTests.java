package ru.stqa.pft.mantis.tests;

import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.*;

public class ChangePasswordTests extends TestBase {

    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void testChangePasswordTests() throws IOException, MessagingException {
        UserData userToChangePassword = app.db().users().iterator().next();
        String newPassword = "1111";
        app.admin().login();
        app.admin().manageUsers();
        app.admin().initPasswordReset(userToChangePassword.getId());
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
        String confirmationLink = findConfirmationLink(mailMessages);
        app.admin().changePassword(confirmationLink, newPassword);

        assertTrue(app.newSession().login(userToChangePassword.getUsername(), newPassword));
    }

    private String findConfirmationLink(List<MailMessage> mailMessages) {
        MailMessage mailMessage = mailMessages.get(0);
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}