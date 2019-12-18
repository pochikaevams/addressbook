package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.*;
import org.openqa.selenium.support.ui.Select;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import org.testng.Assert;
import java.util.List;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void submitContactCreation() {
        click(By.name("submit"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("email"), contactData.getEmail());
        type(By.name("email2"), contactData.getEmail2());
        type(By.name("email3"), contactData.getEmail3());

//        if (creation) {
//            new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
//        } else {
//            Assert.assertFalse(isElementPresent(By.name("new_group")));
//        }
    }

    public void fillContactForm(ContactData contactData) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("email"), contactData.getEmail());
        type(By.name("email2"), contactData.getEmail2());
        type(By.name("email3"), contactData.getEmail3());
    }

    private void submitAddContactToGroup(GroupData group) {
        new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(group.getName());
        click(By.name("add"));
    }

    public void submitDeleteContactFromGroup() {
        click(By.name("remove"));
    }

    public void goBack() {
        click(By.cssSelector("select[name=\"group\"] > option[value='']"));
    }

    private void selectContact() {
        click(By.name("selected[]"));
    }

    public void gotoNewContactPage() {
        if (wd.findElement(By.cssSelector("h1")).getText().equals("Edit / add address book entry")) {
            return;
        }
        click(By.linkText("add new"));
    }

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[id='" + id +"']")).click();
    }

    public void initContactModificationById(int id) {
        wd.findElement(By.xpath("//tr[.//*[@id='" + id + "']]//*[@title='Edit']")).click();
    }

    public void deleteSelectedContact() {
        click(By.xpath("//input[@value='Delete']"));
    }

    public void confirmAlert() {
        wd.switchTo().alert().accept();
    }

    public void editContact() {
        click(By.cssSelector("img[title=\"Edit\"]"));
    }

    public void confirmUpdateContact() {
        click(By.name("update"));
    }

    public void create(ContactData contact, boolean group) {
        gotoNewContactPage();
        fillContactForm(contact, group);
        submitContactCreation();
        contactCache = null;
        goToMainPage();
    }

    public void create(ContactData contact) {
        gotoNewContactPage();
        fillContactForm(contact);
        submitContactCreation();
        contactCache = null;
        goToMainPage();
    }

    public void modify(ContactData contact) {
        initContactModificationById(contact.getId());
        fillContactForm(contact, false);
        confirmUpdateContact();
        contactCache = null;
        goToMainPage();
    }

    public void delete(ContactData contact) {
        goToMainPage();
        selectContactById(contact.getId());
        deleteSelectedContact();
        contactCache = null;
        confirmAlert();
        goToMainPage();
    }

    private void initContactPreviewById(int id) {
        wd.findElement(By.cssSelector("[href='view.php?id=" + id +"']")).click();
    }

    private void goToMainPage() {
        if (isElementPresent(By.id("maintable"))) {
            return;
        }
        click(By.linkText("home"));
    }

    public boolean isThereAContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public int count() {
        return wd.findElements(By.cssSelector("[name=entry]")).size();
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.cssSelector("[name=entry]"));
        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
            String firstName = element.findElement(By.cssSelector("td:nth-child(3)")).getText();
            String lastName = element.findElement(By.cssSelector("td:nth-child(2)")).getText();
            String address = element.findElement(By.cssSelector("td:nth-child(4)")).getText();
            String allPhones = element.findElement(By.cssSelector("td:nth-child(6)")).getText();
            String allEmailes = element.findElement(By.cssSelector("td:nth-child(5)")).getText();
            contactCache.add(new ContactData().withId(id).withFirstName(firstName).withLastName(lastName)
                    .withAllPhones(allPhones).withAddress(address).withAllEmails(allEmailes));
        }
        return new Contacts(contactCache);
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
        String homePhone = wd.findElement(By.name("home")).getAttribute("value");
        String mobilePhone = wd.findElement(By.name("mobile")).getAttribute("value");
        String workPhone = wd.findElement(By.name("work")).getAttribute("value");
        String address = wd.findElement(By.name("address")).getText();
        String email = wd.findElement(By.name("email")).getAttribute("value");
        String email2 = wd.findElement(By.name("email2")).getAttribute("value");
        String email3 = wd.findElement(By.name("email3")).getAttribute("value");
        goToMainPage();
        return new ContactData().withId(contact.getId()).withFirstName(firstName).withLastName(lastName)
                .withHomePhone(homePhone).withMobilePhone(mobilePhone).withWorkPhone(workPhone)
                .withAddress(address).withEmail(email).withEmail2(email2).withEmail3(email3);
    }

    public ContactData infoFromViewForm(ContactData contact) {
        initContactPreviewById(contact.getId());
        String allMainInfo = wd.findElement(By.cssSelector("div#content")).getText();
        goToMainPage();
        return new ContactData().withAllMainInfo(allMainInfo);
    }

    public void addToGroup(ContactData contact, GroupData group) {
        selectContactById(contact.getId());
        submitAddContactToGroup(group);
    }

    public void addToGroupSelectedContacts() {
        click(By.xpath("//input[@value='Add to']"));
    }

    public void selectGroup(String name){
        click(By.name("to_group"));
        new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(name);
    }

    public void showContactsInGroup(String name){
        click(By.name("group"));
        new Select(wd.findElement(By.name("group"))).selectByVisibleText(name);
    }
}
