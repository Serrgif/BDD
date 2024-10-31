package ru.netology.test.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.test.data.DataHelper;
import ru.netology.test.page.DashboardPage;
import ru.netology.test.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.test.data.DataHelper.*;

class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;

    @BeforeEach
    void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
    }

    @Test
    void ValidTest() {
        int firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        int amount = generateValidAmount(firstCardBalance);
        int expectedBalanceFirstCard = firstCardBalance - amount;
        int expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        int BalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        int BalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, BalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, BalanceSecondCard);
    }
}
