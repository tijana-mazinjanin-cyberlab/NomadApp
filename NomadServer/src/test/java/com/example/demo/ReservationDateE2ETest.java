package com.example.demo;


import Services.AccommodationService;
import Services.UserService;
import com.example.demo.pages.EditPage;
import com.example.demo.pages.HomePage;
import com.example.demo.pages.LoginPage;
import model.Accommodation;
import model.Guest;
import model.Host;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDateE2ETest extends TestBase {

    HomePage homePage;
    LoginPage loginPage;
    EditPage editPage;

    @Autowired
    UserService userService;

    @Autowired
    AccommodationService accommodationService;

    Host host;
    Guest guest;

    static final String ROLE = "Developer";
    static final String HOST_USERNAME = "test@host.mail";
    static final String GUEST_USERNAME = "guest@user.name";
    static final String EMAIL = "email";
    static final String PASSWORD = "testPassword123";

    public void insertTestUsers(){
        host = new Host();
        host.setUsername(HOST_USERNAME);
        host.setPassword(PASSWORD);
        host.setVerified(true);
        host.setSuspended(false);
        host.setAddress("AAAA");
        host.setFirstName("AAAA");
        host.setLastName("AAAA");
        host.setPhoneNumber("0694251300");
        host.setId(1L);
        userService.create(host);
        userService.confirmRegistration(HOST_USERNAME);
        System.out.println(userService.findOne(1L));
        System.out.println(userService.findOne(1L).isVerified());
        guest = new Guest();
        guest.setUsername(GUEST_USERNAME);
        guest.setPassword(PASSWORD);
        guest.setVerified(true);
        guest.setSuspended(false);
        userService.create(guest);

    }
    public void insertTestAccommodation(){
        Accommodation accommodation = new Accommodation();
        accommodation.setHost((Host)host);
        accommodation.setVerified(true);
        accommodation.setImages(new ArrayList<String>());
        accommodation.getImages().add("A.jpg");
        accommodation.setDeadlineForCancellation(2);
        accommodationService.createAccommodation(accommodation);

    }
    @BeforeAll
    public void setUp(){
        insertTestUsers();
        insertTestAccommodation();

    }
    @BeforeEach
    public void loginHost(){
        loginPage = new LoginPage(driver);
        loginPage.login(HOST_USERNAME, PASSWORD);

        homePage = new HomePage(driver);
        homePage.clickListing();
        homePage.clickEdit();
        editPage = new EditPage(driver);
        editPage.clickNextMonth();

    }

    public void loginGuest(){
        loginPage = new LoginPage(driver);
        loginPage.login(GUEST_USERNAME, PASSWORD);

        homePage = new HomePage(driver);

    }


    @ParameterizedTest
    @MethodSource("provideValidDates")
    @DisplayName("Should set unavailable for valid dates")
    public void shouldSetUnavailableForValidDates(String start, String end) throws InterruptedException {
        editPage.setDatesUnavailable(start, end);
        Assertions.assertTrue(editPage.operationSuccess());
        saveSuccess();
        driver.navigate().refresh();
        homePage.clickEdit();
        editPage.clickNextMonth();
        assertDateRangeUnavailable(start, end);
    }
    @ParameterizedTest
    @MethodSource("provideValidDates")
    @DisplayName("Should set Available for valid dates")
    public void shouldSetAvailableForValidDates(String start, String end) throws InterruptedException {
        editPage.setDatesUnavailable(start, end);
        editPage.setDatesAvailable(start, end);
        Assertions.assertTrue(editPage.operationSuccess());
        saveSuccess();
        homePage.clickEdit();
        editPage.clickNextMonth();
//        assertDateRangeAvailable(start, end);
    }
    @ParameterizedTest
    @MethodSource("provideValidDatesValidPrices")
    @DisplayName("Should set price for valid dates")
    public void shouldSetPriceForValidDates(String start, String end, String price) throws InterruptedException {
        editPage.setDatesPrice(start, end, price);
        Assertions.assertTrue(editPage.operationSuccess());
        saveSuccess();
    }
    @ParameterizedTest
    @MethodSource("provideValidDatesInvalidPrices")
    @DisplayName("Should fail for valid dates invalid prices")
    public void shouldFailSetPriceForValidDatesInvalidPrices(String start, String end, String price) throws InterruptedException {
        editPage.setDatesPrice(start, end, price);
        Assertions.assertTrue(editPage.operationFail());
        saveSuccess();
    }
    @ParameterizedTest
    @MethodSource("provideInvalidDatesInvalidPrices")
    @DisplayName("Should fail for invalid dates invalid prices")
    public void shouldFailSetPriceForInValidDatesInvalidPrices(String start, String end, String price) throws InterruptedException {
        editPage.setDatesPrice(start, end, price);
        Assertions.assertTrue(editPage.operationFail());
        saveSuccess();
    }
    @ParameterizedTest
    @MethodSource("provideDatesInThePast")
    @DisplayName("should Not Select Either Date In The Past")
    public void shouldNotSelectEitherDateInThePast(String start, String end) throws InterruptedException {
        editPage.clickPreviousMonth();
        editPage.clickPreviousMonth();
        editPage.setDatesUnavailable(start, end);
        Assertions.assertTrue(editPage.operationFail());
        saveSuccess();

//        Assertions.assertTrue(editPage.checkNotSelectedDate(end));
//        Assertions.assertTrue(editPage.checkNotSelectedDate(start));
    }
    @ParameterizedTest
    @MethodSource("provideInvalidDates")
    @DisplayName("Should not select range for invalid dates")
    public void shouldNotSelectRangeForInvalidDates(String start, String end) throws InterruptedException {
        editPage.setDatesUnavailable(start, end);
        Assertions.assertTrue(editPage.checkNotSelectedDate(end));
        saveSuccess();
    }
    @ParameterizedTest
    @MethodSource("provideInvalidDeadlines")
    @DisplayName("Should fail for invalid deadline")
    public void shouldFailForInvalidDeadline(String deadline) throws InterruptedException {
        editPage.setDeadline(deadline);
        saveFail();
    }
    @ParameterizedTest
    @MethodSource("provideNonNumberDeadlines")
    @DisplayName("Should pass for deadline that is not a number (deadline remains 0 because of textbox configuration)")
    public void shouldPassForNonNumberDeadline(String deadline) throws InterruptedException {
        editPage.setDeadline(deadline);
        saveSuccess();
    }
    @ParameterizedTest
    @MethodSource("provideValidDeadlines")
    @DisplayName("Should pass for valid deadline")
    public void shouldPassForValidDeadline(String deadline) throws InterruptedException {
        editPage.setDeadline(deadline);
        saveSuccess();
    }
    @RepeatedTest(3)
    @DisplayName("should Fail Making Unavailable For No Date Selected")
    public void shouldFailMakingUnavailableForNoDateSelected() throws InterruptedException {
        editPage.clickMakeUnavailable();
        Assertions.assertTrue(editPage.operationFail());
        saveSuccess();
    }
    @RepeatedTest(3)
    @DisplayName("should Fail Making available For No Date Selected")
    public void shouldFailMakingAvailableForNoDateSelected() throws InterruptedException {
        editPage.clickMakeAvailable();
        Assertions.assertTrue(editPage.operationFail());
        saveSuccess();
    }
    public void saveSuccess(){
        editPage.saveChanges();
        Assertions.assertTrue(editPage.operationSuccess());

    }
    public void saveFail(){
        editPage.clickSave();
        Assertions.assertTrue(editPage.operationFail());
    }
    private void assertDateRangeUnavailable(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date sDate = null;
        Date eDate = null;
        try {
            sDate = formatter.parse(start);
            eDate = formatter.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        Date i = new Date();
        for(; !c.getTime().equals(eDate); c.add(Calendar.DATE, 1)){
           Assertions.assertTrue( editPage.isDateUnavailable(formatter.format(c.getTime())));
        }

    }
    private void assertDateRangeAvailable(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date sDate = null;
        Date eDate = null;
        try {
            sDate = formatter.parse(start);
            eDate = formatter.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        Date i = new Date();
        for(; !c.getTime().equals(eDate); c.add(Calendar.DATE, 1)){
            Assertions.assertTrue( editPage.isDateAvailable(formatter.format(c.getTime())));
        }

    }
    public Stream<Arguments> provideValidDates() {
        return Stream.of(
                //Valid dates
                Arguments.of("February 16, 2024", "February 20, 2024"),
                Arguments.of("February 1, 2024", "February 4, 2024"),
                Arguments.of("February 6, 2024", "February 10, 2024"),
                //Valid dates that overlap (should still pass)
                Arguments.of("February 17, 2024", "February 20, 2024"),
                Arguments.of("February 2, 2024", "February 4, 2024"),
                Arguments.of("February 7, 2024", "February 10, 2024")
        );
    }
    public Stream<Arguments> provideValidDatesInvalidPrices() {
        return Stream.of(
                //Valid dates
                Arguments.of("February 16, 2024", "February 20, 2024", "-5"),
                Arguments.of("February 1, 2024", "February 4, 2024", "0"),
                Arguments.of("February 6, 2024", "February 10, 2024", "-100"),
                //Valid dates that overlap (should still pass)
                Arguments.of("February 17, 2024", "February 20, 2024", "-5"),
                Arguments.of("February 2, 2024", "February 4, 2024", "0"),
                Arguments.of("February 7, 2024", "February 10, 2024", "-100")
        );
    }
    public Stream<Arguments> provideInvalidDatesInvalidPrices() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("February 20, 2024", "February 16, 2024", "-5"),
                Arguments.of("February 4, 2024", "February 1, 2024", "0"),
                Arguments.of("February 10, 2024", "February 6, 2024","-100")
        );
    }
    public Stream<Arguments> provideInvalidDates() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("February 20, 2024", "February 16, 2024"),
                Arguments.of("February 4, 2024", "February 1, 2024"),
                Arguments.of("February 10, 2024", "February 6, 2024")
        );
    }
    public Stream<Arguments> provideDatesInThePast() {
        return Stream.of(
                //Dates in the past
                Arguments.of("December 16, 2023", "December 20, 2023"),
                Arguments.of("December 1, 2023", "December 4, 2023"),
                Arguments.of("December 6, 2023", "December 10, 2023")
        );
    }


    public Stream<Arguments> provideValidDatesValidPrices() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("February 20, 2024", "February 16, 2024", "5"),
                Arguments.of("February 4, 2024", "February 1, 2024", "1"),
                Arguments.of("February 10, 2024", "February 6, 2024","100")
        );
    }

    public Stream<Arguments> provideInvalidDeadlines() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("0"),
                Arguments.of("-5"),
                Arguments.of("-10")
        );
    }
    public Stream<Arguments> provideNonNumberDeadlines() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("Radoslav"),
                Arguments.of("Dragislav"),
                Arguments.of("Jovislav"),
                Arguments.of("")
        );
    }


    public Stream<Arguments> provideValidDeadlines() {
        return Stream.of(
                //Dates where the end is before the start
                Arguments.of("1"),
                Arguments.of("5"),
                Arguments.of("10")
        );
    }
}
