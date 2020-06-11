package com.shihab.coviddata;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shihab.coviddata.entities.CovidData;
import com.shihab.coviddata.repo.CovidDataRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CovidDataControllerRestTemplateTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CovidDataRepository mockRepository;

    @Before
    public void init() throws ParseException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	CovidData covidData = new CovidData(1L, "Jack", "infected", new Timestamp(dateFormat.parse("2011-10-02 18:48:05").getTime()).toString());
        when(mockRepository.findById(1L)).thenReturn(Optional.of(covidData));
    }
    

    @Test
    public void find_by_id_OK() throws Exception {

    	String expected = "{\"id\":1,\"name\":\"Jack\",\"state\":\"infected\",\"time\":\"2011-10-02 18:48:05.0\"}";

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/covid/id/1", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void find_nologin_401() throws Exception {

        String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/id/1\"}";

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/covid/id/1", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }
    
    @Test
    public void find_all_data_OK() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(1L, "Jack", "infected", "2011-10-02 18:48:05.0"),
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"),
                new CovidData(3L, "Cristin", "deceased", "2013-10-02 18:48:05.0"));
    	
    	when(mockRepository.findAll()).thenReturn(covidData);

        String expected = om.writeValueAsString(covidData);
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/covid", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockRepository, times(1)).findAll();
    }
    
    @Test
    public void find_all_data_wrong_user_password_401() throws Exception {
    	
    	List<CovidData> covidData = Arrays.asList(
                new CovidData(1L, "Jack", "infected", "2011-10-02 18:48:05.0"),
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"),
                new CovidData(3L, "Cristin", "deceased", "2013-10-02 18:48:05.0"));
    	
    	when(mockRepository.findAll()).thenReturn(covidData);

    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid\"}";
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wrong_user", "wrong_password")
                .getForEntity("/api/covid", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void find_by_state_OK() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(1L, "Jack", "infected", "2011-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByState("infected")).thenReturn(covidData);

        String expected = om.writeValueAsString(covidData);
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/covid/state/infected", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockRepository, times(1)).searchByState("infected");
    }
    
    @Test
    public void find_by_state_wrong_user_password_401() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(1L, "Jack", "infected", "2011-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByState("infected")).thenReturn(covidData);
    	
    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/state/infected\"}";
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wrong_user", "wrong_password")
                .getForEntity("/api/covid/state/infected", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void find_by_timestamp_OK() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"),
                new CovidData(3L, "Cristin", "deceased", "2013-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByStateDate(null, "2011-12-02 18:45:05.0", "2013-10-02 18:50:05.0")).thenReturn(covidData);

        String expected = om.writeValueAsString(covidData);
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/covid/search?startDate=2011-12-02 18:45:05.0&endDate=2013-10-02 18:50:05.0", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockRepository, times(1)).searchByStateDate(null, "2011-12-02 18:45:05.0", "2013-10-02 18:50:05.0");
    }
    
    @Test
    public void find_by_timestamp_wrong_user_password_401() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"),
                new CovidData(3L, "Cristin", "deceased", "2013-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByStateDate(null, "2011-12-02 18:45:05.0", "2013-10-02 18:50:05.0")).thenReturn(covidData);
    	
    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/search\"}";
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wrong_user", "wrong_password")
                .getForEntity("/api/covid/search?startDate=2011-12-02 18:45:05.0&endDate=2013-10-02 18:50:05.0", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void find_by_state_timestamp_OK() throws Exception {

    	List<CovidData> covidData = Arrays.asList(
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByStateDate("recovered", "2011-10-02 18:45:05.0", "2013-10-02 18:50:05.0")).thenReturn(covidData);

        String expected = om.writeValueAsString(covidData);
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/covid/search?state=recovered&startDate=2011-10-02 18:45:05.0&endDate=2013-10-02 18:50:05.0", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(mockRepository, times(1)).searchByStateDate("recovered", "2011-10-02 18:45:05.0", "2013-10-02 18:50:05.0");
    }
    
    @Test
    public void find_by_state_timestamp_wrong_user_password_401() throws Exception {
    	
    	List<CovidData> covidData = Arrays.asList(
                new CovidData(2L, "Michael", "recovered", "2012-10-02 18:48:05.0"));
    	
    	when(mockRepository.searchByStateDate("recovered", "2011-10-02 18:45:05.0", "2013-10-02 18:50:05.0")).thenReturn(covidData);

    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/search\"}";
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wrong_user", "wrong_password")
                .getForEntity("/api/covid/search?state=recovered&startDate=2011-10-02 18:45:05.0&endDate=2013-10-02 18:50:05.0", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void save_data_OK() throws Exception {

        CovidData covidData = new CovidData(4L, "John", "infected", "2020-06-10 18:48:05.0");
        when(mockRepository.save(any(CovidData.class))).thenReturn(covidData);

        String expected = om.writeValueAsString(covidData);

        ResponseEntity<String> response = restTemplate
        		.withBasicAuth("admin", "password")
        		.postForEntity("/api/covid", covidData, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).save(any(CovidData.class));

    }
    
    @Test
    public void save_data_wrong_user_password_401() throws Exception {
    	
    	CovidData covidData = new CovidData(4L, "John", "infected", "2020-06-10 18:48:05.0");
        when(mockRepository.save(any(CovidData.class))).thenReturn(covidData);
        
    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid\"}";
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wrong_user", "wrong_password")
                .getForEntity("/api/covid", String.class);

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void update_data_OK() throws Exception {

    	CovidData covidData = new CovidData(1L, "Jack", "recovered", "2011-10-02 18:48:05.0");
        when(mockRepository.save(any(CovidData.class))).thenReturn(covidData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(covidData), headers);

        ResponseEntity<String> response = restTemplate
        		.withBasicAuth("admin", "password")
        		.exchange("/api/covid/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(om.writeValueAsString(covidData), response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(CovidData.class));

    }
    
    @Test
    public void update_data_wrong_user_password_401() throws Exception {
    	
    	CovidData covidData = new CovidData(1L, "Jack", "recovered", "2011-10-02 18:48:05.0");
        when(mockRepository.save(any(CovidData.class))).thenReturn(covidData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(covidData), headers);
        
        String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/1\"}";

        ResponseEntity<String> response = restTemplate
        		.withBasicAuth("wrong_user", "wrong_password")
        		.exchange("/api/covid/1", HttpMethod.PUT, entity, String.class);
        

        printJSON(response);

        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    
    @Test
    public void delete_data_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate
        		.withBasicAuth("admin", "password")
        		.exchange("/api/covid/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void delete_data_wrong_user_password_401() throws Exception {
    	
    	doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate
        		.withBasicAuth("wrong_user", "wrong_password")
        		.exchange("/api/covid/1", HttpMethod.DELETE, entity, String.class);
        
        
    	String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/api/covid/1\"}";

        printJSON(response);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
