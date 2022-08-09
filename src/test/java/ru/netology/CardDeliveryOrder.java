package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrder {

    @BeforeEach
    public void openForm() {
        open("http://localhost:9999/");
    }
    public String calendar(int amount){
        return LocalDate.now().plusDays(amount).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    @Test
    public void successfulSending() {
        Configuration.holdBrowserOpen = true;
        $x("//input[@placeholder='Город']").val("Воронеж");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").val(calendar(3));
        $("[data-test-id='name'] input").val("Трухачев Сергей");
        $("[data-test-id='phone'] input").val("+79012345678");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").should(exactText("Встреча успешно забронирована на " + calendar(3)));

    }

    @Test
    void dateCheck() {
        $x("//input[@placeholder=\"Город\"]").val("Москва");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(calendar(1));
        $("[data-test-id='name'] input").val("Трухачев Сергей");
        $("[data-test-id='phone'] input").val("+79111734314");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub").should(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void cityFieldCheck() {
        $x("//input[@placeholder=\"Город\"]").val("Moskva");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(calendar(3));
        $("[data-test-id='name'] input").val("Трухачев Сергей");
        $("[data-test-id='phone'] input").val("+79111734314");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='city'] .input__sub").should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void fieldValidationFirstAndLastName() {
        $x("//input[@placeholder=\"Город\"]").val("Воронеж");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(calendar(3));
        $("[data-test-id='name'] input").val("Sergey Trukhachev");
        $("[data-test-id='phone'] input").val("+79111734314");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='name'] .input__sub").should(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void phoneFieldCheck() {
        $x("//input[@placeholder=\"Город\"]").val("Воронеж");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(calendar(3));
        $("[data-test-id='name'] input").val("Трухачев Сергей");
        $("[data-test-id='phone'] input").val("+79111");
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='phone'] .input__sub").should(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void checkboxValidation() {
        $x("//input[@placeholder=\"Город\"]").val("Воронеж");
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder=\"Дата встречи\"]").val(calendar(3));
        $("[data-test-id='name'] input").val("Трухачев Сергей");
        $("[data-test-id='phone'] input").val("+79111734314");
        $x("//*[contains(text(),'Забронировать')]").click();
        $("[data-test-id='agreement'].input_invalid").should(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }



}
