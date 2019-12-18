package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import ru.stqa.pft.addressbook.model.ContactData;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactInfoTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().mainPage();
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstName("t").withLastName("tr")
                    .withAddress("tre").withMobilePhone("1").withHomePhone("2").withWorkPhone("3")
                    .withEmail("qwert").withEmail2("wef").withEmail3("efq"));
        }
    }

    @Test
    public void testContactInfo() {
        app.goTo().mainPage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
        assertThat(contact.getAllEmails(), equalTo(mergeEmailes(contactInfoFromEditForm)));
        assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));
    }

    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
                .stream().filter((s) -> ! s.equals(""))
                .map(ContactInfoTests::cleanedPhones)
                .collect(Collectors.joining("\n"));
    }

    private String mergeEmailes(ContactData contact) {
        return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s) -> !s.equals(""))
                .collect(Collectors.joining("\n"));
    }

    public static String cleanedPhones(String phone) {
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }
}
