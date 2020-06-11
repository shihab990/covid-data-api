package com.shihab.coviddata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.shihab.coviddata.entities.CovidData;
import com.shihab.coviddata.repo.CovidDataRepository;

import java.text.ParseException;
import java.util.Optional;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CovidDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CovidDataRepository mockRepository;

    @Before
    public void init() throws ParseException {
    	CovidData data = new CovidData(1L, "Jack", "infected", "2011-10-02 18:48:05");
        when(mockRepository.findById(1L)).thenReturn(Optional.of(data));
    }

    @WithMockUser("USER")
    @Test
    public void find_login_ok() throws Exception {

    	mockMvc.perform(get("/api/covid/id/1"))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.id", is(1)))
		        .andExpect(jsonPath("$.name", is("Jack")))
		        .andExpect(jsonPath("$.state", is("infected")))
		        .andExpect(jsonPath("$.time", is("2011-10-02 18:48:05")));
    }

    @Test
    public void find_nologin_401() throws Exception {
        mockMvc.perform(get("/api/covid/id/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
