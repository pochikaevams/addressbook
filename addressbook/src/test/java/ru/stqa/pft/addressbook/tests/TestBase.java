package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import org.openqa.selenium.remote.BrowserType;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBase {

    protected static final ApplicationManager app =
            new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

    @BeforeSuite
    public void setUp() throws Exception {
        app.init();
    }

    @AfterSuite
    public void tearDown() {
        app.stop();
    }

    public void verifyContactListInUIByGroup(GroupData group) {
        if(Boolean.getBoolean("verifyUI")){
            Contacts dbContacts = app.db().contacts();
            dbContacts.removeIf(contactData -> !contactData.getGroups().contains(group));
            app.goTo().mainPage();
            app.contact().showContactsInGroup(group.getName());
            Contacts uiContacts = app.contact().all();
            assertThat(uiContacts, equalTo(dbContacts.stream()
                    .map((g)-> new ContactData().withId(g.getId()).withLastName(g.getLastName())
                            .withAddress(g.getAddress()).withFirstName(g.getFirstName())
                            .withAllPhones(g.getAllPhones()).withAllEmails(g.getAllEmails()))
                    .collect(Collectors.toSet())));
        }
    }
}
