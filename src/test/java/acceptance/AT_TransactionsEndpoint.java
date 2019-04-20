package acceptance;

import com.n26.transaction.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AT_TransactionsEndpoint extends AT_Base{

    @Test
    public void return_201_when_for_successful_POST_transactions() throws Exception {
        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(transactionJson("12.3343", now())))
                .andExpect(status().is(CREATED.value()));

        assertEquals(1, transactionStatisticsStore.size());
    }

    @Test
    public void return_204_case_transaction_is_older_than_60_seconds() throws Exception {
        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(transactionJson("12.3343", twoMinutesAgo())))
                .andExpect(status().is(NO_CONTENT.value()));

        assertEquals(0, transactionStatisticsStore.size());
    }

    @Test
    public void return_400_case_json_is_invalid() throws Exception {
        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(transactionJson("", now())))
                .andExpect(status().is(BAD_REQUEST.value()));

        assertEquals(0, transactionStatisticsStore.size());
    }

    @Test
    public void return_400_for_malformed_json() throws Exception {
        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(malformedJson()))
                .andExpect(status().is(BAD_REQUEST.value()));

        assertEquals(0, transactionStatisticsStore.size());
    }

    @Test
    public void return_422_case_timestamp_is_in_the_future() throws Exception {
        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(transactionJson("12.3343", tomorrow())))
                .andExpect(status().is(UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    public void delete_all_transactions() throws Exception {
        transactionStatisticsStore.add(new Transaction(new BigDecimal("100.0000"), dateTimeNow()));
        transactionStatisticsStore.add(new Transaction(new BigDecimal("50.0000"), dateTimeNow()));

        mockMvc.perform(delete(TRANSACTIONS_ENDPOINT))
                .andExpect(status().is(NO_CONTENT.value()));

        assertEquals(0, transactionStatisticsStore.size());
    }

    @Test
    public void return_422_when_cant_parse_date() throws Exception {
        String invalidDateFormat = dateTimeNow().format(DateTimeFormatter.ofPattern("mm/dd/YYYY hh:MM"));

        mockMvc.perform(post(TRANSACTIONS_ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(transactionJson("12.3343", invalidDateFormat)))
                .andExpect(status().is(UNPROCESSABLE_ENTITY.value()));
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
