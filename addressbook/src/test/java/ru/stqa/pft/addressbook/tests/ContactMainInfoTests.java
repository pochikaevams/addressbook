package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import ru.stqa.pft.addressbook.model.ContactData;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactMainInfoTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().mainPage();
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstName("Marisdf").withLastName("sdfsdf")
                    .withAddress("sdfsdf").withMobilePhone("1").withHomePhone("2").withWorkPhone("3")
                    .withEmail("qwerty").withEmail2("sdfsd").withEmail3("dfgd"));
        }
    }

    @Test
    public void testContactMainInfo() {
        app.goTo().mainPage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromViewForm = app.contact().infoFromViewForm(contact);
        ContactData contactInfoFromEditForm = convertContactToMatchViewFormat(app.contact().infoFromEditForm(contact));

        assertThat(contactInfoFromViewForm.getAllMainInfo(), equalTo(cleanedEditInfo(mergeMainInfo(contactInfoFromEditForm))));
    }

    private String mergeMainInfo(ContactData contact) {
        return Arrays.asList(contact.getFirstName(), contact.getLastName(), contact.getAddress()
                , contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone()
                , contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s) -> ! s.equals(""))
                .collect(Collectors.joining("\n"));
    }

    public static String cleanedEditInfo(String info) {
        return info.replaceFirst("\n", " ");
    }

    public static ContactData convertContactToMatchViewFormat(ContactData contact) {
        String homePhone = contact.getHomePhone();
        String mobilePhone = contact.getMobilePhone();
        String workPhone = contact.getWorkPhone();
        String address = contact.getAddress();

        if (!address.equals("")) {
            address += "\n";
        }
        if (!homePhone.equals("")) {
            homePhone = "H: " + homePhone;
        }
        if (!mobilePhone.equals("")) {
            mobilePhone = "M: " + mobilePhone;
        }
        if (!workPhone.equals("")) {
            workPhone = "W: " + workPhone + "\n";
        }
        return contact.withAddress(address).withHomePhone(homePhone).withMobilePhone(mobilePhone).withWorkPhone(workPhone);
    }
}
