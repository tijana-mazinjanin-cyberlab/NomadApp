package com.example.demo;

import DTO.AccommodationDTO;
import Services.AccommodationService;
import Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Accommodation;
import model.Host;
import model.enums.AccommodationType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")
@AutoConfigureTestDatabase()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDateIntegrationTest {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    private AccommodationService accommodationService;
    private static String token = "";
    private Host host;
    public void login() throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject user = new JSONObject();
        user.put("username", "host@gmail.com");
        user.put("password",  "hostPassword");

        HttpEntity<String> request =
                new HttpEntity<String>(user.toString(), headers);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/auth/login",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        token = responseEntity2.getBody().split("\"")[3];
        System.out.println(responseEntity2.getStatusCode());
    }
    public void addAccommodation(){
        Accommodation accommodation = new Accommodation();

        accommodation.setHost(host);

        Accommodation accommodation2 = new Accommodation();
        accommodation2.setHost(host);

        accommodationService.createAccommodation(accommodation);
        accommodationService.createAccommodation(accommodation2);
    }
    public void register() throws JSONException {

        host = new Host();
        host.setUsername("host@gmail.com");
        host.setPassword("hostPassword");
        host.setAddress("Test Street");
        host.setFirstName("Name");
        host.setLastName("Surname");
        host.setPhoneNumber("0694251300");
        host.setNotificationPreferences(new HashMap<>());
        userService.create(host);
        userService.confirmRegistration("host@gmail.com");
    }
    @BeforeAll
    public void setupUser() throws JSONException {
        register();
        login();
        addAccommodation();
    }
    private static JSONObject getDateRange(String start, String end) throws JSONException {

        JSONObject dateRange = new JSONObject();
        dateRange.put("startDate", start);
        dateRange.put("finishDate",  end);
        return dateRange;
    }
    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
    private static String getDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd yyyy");
        return sdf2.format(sdf.parse(date));
    }
    @Test
    @DisplayName("Should return empty list of dates")
    public void shouldReturnEmptyDates() {
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, dates.size());
    }

    @Test
    @DisplayName("Should add reservation date")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldAddReservationDate() throws JSONException, ParseException {

        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-01", "2025-01-03").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/1",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(3, dates.size());
    }
    @ParameterizedTest
    @MethodSource("provideNonExistentAccommodation")
    @DisplayName("Should fail for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailForNonExistentAccommodation(String accommodationId) throws JSONException, ParseException {

        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-01", "2025-01-03").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/" + accommodationId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }

    @ParameterizedTest
    @DisplayName("Should fail for invalid date range")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    @MethodSource("provideIllegalDates")
    public void shouldFailForInvalidDateRange(String startDate, String endDate) throws JSONException, ParseException {


        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange(startDate, endDate).toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/2",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }

    @ParameterizedTest
    @MethodSource("provideNonExistentAccommodation")
    @DisplayName("Should fail for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailMakingUnavailableForNonExistentAccommodation(String accommodationId) throws JSONException, ParseException {


        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-03", "2025-01-01").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/" + accommodationId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }
    @ParameterizedTest
    @MethodSource("provideNonExistentAccommodation")
    @DisplayName("Should fail setting deadline for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailSettingDeadlineForNonExistentAccommodation(String accommodationId) throws JsonProcessingException {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(Long.parseLong(accommodationId));
        accommodation.setDeadlineForCancellation(5);


        HttpEntity<String> request =
                new HttpEntity<String>(accommodationToString(accommodation), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/" + accommodationId,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<AccommodationDTO> responseEntity = restTemplate.exchange("/api/accommodations/" + accommodationId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AccommodationDTO>() {
                });

        AccommodationDTO returnedAccommodation = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertNull(returnedAccommodation);
    }
    @ParameterizedTest
    @MethodSource("provideInvalidDeadlines")
    @DisplayName("Should fail setting deadline for invalidDeadline")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailForInvalidDeadline(int deadline) throws JSONException, ParseException, JsonProcessingException {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setDeadlineForCancellation(deadline);


        HttpEntity<String> request =
                new HttpEntity<String>(accommodationToString(accommodation), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/" + "1",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<AccommodationDTO> responseEntity = restTemplate.exchange("/api/accommodations/" + "1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AccommodationDTO>() {
                });

        AccommodationDTO returnedAccommodation = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertNotEquals(returnedAccommodation.getDeadlineForCancellation(), deadline);
    }
    @ParameterizedTest
    @MethodSource("provideValidDeadlinesAndAccommodations")
    @DisplayName("Should set deadline for valid deadline and accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldSucceedForValidDeadlineAndExistentAccommodation(String accommodationId, int deadline) throws JSONException, ParseException, JsonProcessingException {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(Long.parseLong(accommodationId));
        accommodation.setDeadlineForCancellation(deadline);


        HttpEntity<String> request =
                new HttpEntity<String>(accommodationToString(accommodation), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/" + accommodationId,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<AccommodationDTO> responseEntity = restTemplate.exchange("/api/accommodations/" + accommodationId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AccommodationDTO>() {
                });

        AccommodationDTO returnedAccommodation = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(returnedAccommodation.getDeadlineForCancellation(), deadline);
    }
    @ParameterizedTest
    @DisplayName("Should add price")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    @MethodSource("provideExistentAccommodationAndValidPrice")
    public void shouldAddPrice(String accommodationId, String price) throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", price);
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/"+accommodationId+ "/" +getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double returnPrice = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(returnPrice, Double.valueOf(price));
    }
    @ParameterizedTest
    @MethodSource("provideExistentAccommodationAndValidPrice")
    @DisplayName("Should overwrite existing price")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldOverwriteExistingPrice(String accommodationId, String price) throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", "10000");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        resDate.put("price", price);
        request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity3 = restTemplate.exchange("/api/accommodations/price/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/" + accommodationId + "/" +getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double returnPrice = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity3.getStatusCode());
        assertEquals(returnPrice, Double.valueOf(price));
    }
    @ParameterizedTest
    @MethodSource("provideIllegalDates")
    @DisplayName("Should fail for illegal date range")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailForIllegalDateRange(String startDate, String endDate, String checkDate) throws JSONException, ParseException {

        JSONObject resDate = getDateRange(startDate, endDate);
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/2",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/2/"+getDate(checkDate),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertNotEquals(price, 50.0);
    }

    @ParameterizedTest
    @DisplayName("Should fail setting price for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    @MethodSource("provideNonExistentAccommodation")
    public void shouldFailPriceForNonExistentAccommodation(String accommodationId) throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/" + accommodationId,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/"  + accommodationId + "/" +getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertNull(price);
    }

    public Stream<Arguments> provideIllegalDates() {
        return Stream.of(
                Arguments.of("2025-01-03", "2025-01-01", "2025-01-02"),
                Arguments.of("2019-01-03", "2019-01-01", "2019-01-02")
        );
    }

    public Stream<Arguments> provideNonExistentAccommodation() {
        return Stream.of(
                Arguments.of("5"),
                Arguments.of("6"),
                Arguments.of("7"),
                Arguments.of("8")
        );
    }
    public Stream<Arguments> provideExistentAccommodation() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("2")
        );
    }
    public Stream<Arguments> provideExistentAccommodationAndValidPrice() {
        return Stream.of(
                Arguments.of("1", "20"),
                Arguments.of("2", "20"),
                Arguments.of("1", "50"),
                Arguments.of("2", "10")
        );
    }
    private String accommodationToString(Accommodation accommodation) throws JsonProcessingException {
        accommodation.setAccommodationType(AccommodationType.ROOM);
        accommodation.setAddress("AAAAA");
        accommodation.setName("AAAAA");
        accommodation.setDescription("AAAAA");
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(5);
        accommodation.setDefaultPrice(10);



        accommodation.setHost(host);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(accommodation);
    }

    public Stream<Arguments> provideInvalidDeadlines() {
        return Stream.of(
                Arguments.of(-5),
                Arguments.of(-100),
                Arguments.of(-2)
        );
    }

    public Stream<Arguments> provideValidDeadlinesAndAccommodations() {
        return Stream.of(
                Arguments.of("1", 5),
                Arguments.of("2", 0),
                Arguments.of("1", 0),
                Arguments.of("2", 11)

        );
    }
}