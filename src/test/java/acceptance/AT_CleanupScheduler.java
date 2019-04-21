package acceptance;

import com.n26.transaction.Transaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class AT_CleanupScheduler extends AT_Base {

    @Value("${cleanupInMilliseconds}")
    private int cleanupInMilliseconds;

    @Test
    public void cleans_transaction_statistics_older_than_seconds_to_live() throws InterruptedException {
        transactionStatisticsStore.add(new Transaction(new BigDecimal("100.0000"), dateTimeNow()));
        transactionStatisticsStore.add(new Transaction(new BigDecimal("50.0000"), dateTimeNow()));

        Thread.sleep(cleanupInMilliseconds);

        assertEquals(0, transactionStatisticsStore.size());
    }
}
