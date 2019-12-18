package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class AdminHelper extends HelperBase {

    public AdminHelper(ApplicationManager app) {
        super(app);
    }

    public void login() {
        wd.get(app.getProperty("web.baseUrl") + "login_page.php");
        type(By.name("username"), app.getProperty("web.adminLogin"));
        type(By.name("password"), app.getProperty("web.adminPassword"));
        click(By.cssSelector("input[value='Login']"));
    }

    public void manageUsers() {
        click(By.cssSelector("a[href*='manage_user_page']"));
    }

    public void initPasswordReset(int idToDelete) {
        click(By.cssSelector("[href$='user_id=" + idToDelete + "']"));
        click(By.cssSelector("input[value='Reset Password']"));
    }

    public void changePassword(String confirmationLink, String newPassword) {
        wd.get(confirmationLink);
        type(By.name("password"), newPassword);
        type(By.name("password_confirm"), newPassword);
        click(By.cssSelector("input[value='Update User']"));
    }
}