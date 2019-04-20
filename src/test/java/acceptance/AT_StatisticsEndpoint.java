package acceptance;

import com.n26.transaction.Transaction;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AT_StatisticsEndpoint extends AT_Base {

    @Test
    public void return_statistics_for_transactions() throws Exception {
        transactionStatisticsStore.add(new Transaction(new BigDecimal("100.0000"), dateTimeNow()));
        transactionStatisticsStore.add(new Transaction(new BigDecimal("50.0000"), dateTimeNow()));

        mockMvc.perform(get(STATISTICS_ENDPOINT))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.sum", is("150.00")))
                .andExpect(jsonPath("$.avg", is("75.00")))
                .andExpect(jsonPath("$.max", is("100.00")))
                .andExpect(jsonPath("$.min", is("50.00")))
                .andExpect(jsonPath("$.count", is(2)));
    }

}
