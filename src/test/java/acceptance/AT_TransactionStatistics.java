package acceptance;

import com.n26.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AT_TransactionStatistics {

    private static final ZonedDateTime ZONED_DATE_TIME_NOW = ZonedDateTime.now(ZoneId.of("UTC"));

    private static final String NOW =
        ZONED_DATE_TIME_NOW.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    private static final String TWO_MINUTES_AGO =
        ZONED_DATE_TIME_NOW.minusMinutes(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    private static final String TOMORROW =
        ZONED_DATE_TIME_NOW.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    @Autowired
    private GenericWebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void return_201_when_for_successful_POST_transactions() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson("12.3343",NOW)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void return_204_case_transaction_is_older_than_60_seconds() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson("12.3343", TWO_MINUTES_AGO)))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void return_400_case_json_is_invalid() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson("", NOW)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void return_400_for_malformed_json() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson()))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void return_422_case_timestamp_is_in_the_future() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson("12.3343", TOMORROW)))
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    private String malformedJson() {
        return "{json}";
    }

    private String transactionJson(String amount, String timeStamp) {
        //language=JSON
        return "{\n" +
                "  \"amount\": \""+ amount +"\",\n" +
                "  \"timestamp\": \"" + timeStamp + "\"\n" +
                "}";
    }
}
