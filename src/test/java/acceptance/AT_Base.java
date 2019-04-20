package acceptance;

import com.n26.Application;
import com.n26.infrastructure.TransactionStatisticsStore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public abstract class AT_Base {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    static final String TRANSACTIONS_ENDPOINT = "/transactions";
    static final String STATISTICS_ENDPOINT = "/statistics";

    @Autowired
    private GenericWebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Autowired
    TransactionStatisticsStore transactionStatisticsStore= new TransactionStatisticsStore();

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        transactionStatisticsStore.clear();
    }

    String now() {
        return ZonedDateTime.now(UTC).format(FORMATTER);
    }

    String twoMinutesAgo() {
        return ZonedDateTime
                .now(UTC)
                .minusMinutes(2)
                .format(FORMATTER);
    }

    String tomorrow() {
        return ZonedDateTime
                .now(UTC)
                .plusDays(1)
                .format(FORMATTER);
    }

    ZonedDateTime dateTimeNow() {
        return ZonedDateTime.now(UTC);
    }
}
