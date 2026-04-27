package com.appointments;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class CreateAppointmentTest {

    @Test
    void shouldCreateAppointment(final Page page) {
        page.navigate("http://localhost:3000/owner?tab=event-types");
        page.getByRole(AriaRole.BUTTON).getByText("Создать тип").click();
        page.getByLabel("Название *").fill("Test Event");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Создать").setExact(true)).click();
        assertThat(page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("Test Event")).first()).isVisible();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Гость")).click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Типы событий"))).isVisible();

        page.getByRole(AriaRole.LISTITEM).getByText("Test Event").first().click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Слоты"))).isVisible();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Забронировать")).first().click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Заголовок")).fill("Test Appointment");
        page.getByRole(AriaRole.DIALOG).getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Забронировать").setExact(true)).click();

        assertThat(page.getByText("Test Appointment")).isVisible();
    }
}
