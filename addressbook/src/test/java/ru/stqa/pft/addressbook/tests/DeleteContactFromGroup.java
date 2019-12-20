package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class DeleteContactFromGroup extends TestBase {

    @BeforeMethod
    public void ensurePreconditionsForContact() {
        if (app.db().contacts().size() == 0) {
            app.goTo().mainPage();
            app.contact().create(new ContactData().withFirstName("Maria").withLastName("Ivanova").
                    withAddress("dsfe").withMobilePhone("qwsdfe").withEmail("qwerty").withGroup("test1"), true);
        }
        if(app.db().groups().size() == 0){
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test1").withHeader("test2").withFooter("test3"));
        }
    }

    @Test
    public void testDeletingFromGroup(){
        Groups before = null;
        GroupData selectedGroup = null;
        ContactData selectedContact = null;
        int groupId = 0;
        for (ContactData contact : app.db().contacts()) {
            if (contact.getGroups().size() == 0) {
                app.goTo().mainPage();
                app.contact().selectContactById(contact.getId());
                app.contact().addToGroupSelectedContacts();
                selectedContact = contact;
                selectedGroup = app.db().contactById(selectedContact.getId()).iterator().next().getGroups().iterator().next();
                groupId = selectedGroup.getId();
                before = contact.getGroups();
            } else {
                selectedContact = contact;
                selectedGroup = contact.getGroups().iterator().next();
                groupId = selectedGroup.getId();
                before = contact.getGroups();
            }
        }
        app.goTo().mainPage();
        Groups groups = app.db().groups();
        GroupData pickedGroup = groups.iterator().next();
        app.group().filterGroup(pickedGroup);
//        app.group().select(groupId);
        app.contact().selectContactById(selectedContact.getId());
        app.contact().submitDeleteContactFromGroup();
        Groups after = app.db().contactById(selectedContact.getId()).iterator().next().getGroups();
        assertEquals(before.without(selectedGroup), after);
        app.contact().goBack();
        verifyContactListInUIByGroup(selectedGroup);
    }
}