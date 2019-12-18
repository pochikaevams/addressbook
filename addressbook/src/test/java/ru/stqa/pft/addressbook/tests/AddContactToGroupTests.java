package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.testng.Assert.assertEquals;

public class AddContactToGroupTests extends TestBase{

    @BeforeMethod
    public void ensurePreconditionsForContact() {
        if (app.db().contacts().size() == 0) {
            app.goTo().mainPage();
            app.contact().create(new ContactData().withFirstName("Maria").withLastName("Ivanova").
                    withAddress("dsfe").withMobilePhone("qwsdfe").withEmail("qwerty"), true);
        }
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test1").withHeader("test2").withFooter("test3"));
        }
        for (ContactData contact : app.db().contacts()) {
            if (contact.getGroups().size() < app.db().groups().size()) {
                break;
            } else {
                app.goTo().groupPage();
                app.group().create(new GroupData().withName("Test123"));
            }
        }
    }

    @Test
    public void testAddingToGroup(){
        ContactData contact = null;
        for (ContactData newContact : app.db().contacts())
            if (newContact.getGroups().size() < app.db().groups().size()) {
                contact = newContact;
            }
        Groups before = contact.getGroups();
        Groups groups = app.db().groups();
        groups.removeAll(before);
        GroupData selectedGroup = groups.iterator().next();
        app.goTo().mainPage();
        app.group().select(selectedGroup.getId());
        app.contact().selectContactById(contact.getId());
        app.contact().addToGroupSelectedContacts();
        Groups after = app.db().contactById(contact.getId()).iterator().next().getGroups();
        assertEquals(before.withAdded(selectedGroup), after);
        verifyContactListInUIByGroup(selectedGroup);
    }
}