package com.talkdesk.tdx.nlp.platform.services;

import com.talkdesk.tdx.nlp.platform.config.MockDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;

@QuarkusTest
@QuarkusTestResource(MockDatabase.class)
public class TextLoggingRepositoryServiceTest {

    @Inject
    PgPool client;

    @Inject
    TextLoggingRepositoryService textLoggingRepositoryService;

    @Test
    public void givenAnAccess_whenDatabaseOK_thenStoreAndSuccess() {
        client.query("DROP TABLE IF EXISTS wordtagger_usage")
                .flatMap(r -> client.query("CREATE TABLE wordtagger_usage(id SERIAL PRIMARY KEY, client varchar(50), rawtext TEXT)"))
                .await().atMost(Duration.ofSeconds(10));

        Uni<Boolean> opResult = textLoggingRepositoryService.storeSentence("localhost", "test sentence");

        Assert.assertTrue("Expecting success result", opResult.await().indefinitely());
    }

    @Test
    public void givenAnAccess_whenDatabaseError_thenHandleExceptionAndReturnFalse() {
        // Drop the table but don't create a new one, causing the write to fail
        client.query("DROP TABLE IF EXISTS wordtagger_usage").await().atMost(Duration.ofSeconds(10));

        Uni<Boolean> opResult = textLoggingRepositoryService.storeSentence("localhost", "test sentence");

        Assert.assertFalse("Expecting failure result", opResult.await().indefinitely());
    }
}
