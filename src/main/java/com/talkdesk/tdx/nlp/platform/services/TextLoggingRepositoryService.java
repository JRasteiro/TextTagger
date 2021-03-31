package com.talkdesk.tdx.nlp.platform.services;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class TextLoggingRepositoryService {

    private final PgPool dbClient;

    private static final Logger logger = LoggerFactory.getLogger(TextLoggingRepositoryService.class);

    @Inject
    public TextLoggingRepositoryService(PgPool dbClient) {
        this.dbClient = dbClient;
    }

    @Transactional
    public Uni<Boolean> storeSentence(String client, String text) {
        return dbClient.preparedQuery("INSERT INTO wordtagger_usage (client, rawtext) VALUES ($1, $2)",
                    Tuple.of(client, text))
                .onItem().apply(pgRowSet -> pgRowSet.rowCount() == 1)
                .onFailure().invoke(ex -> logger.error("Failed to store entry on database: ", ex))
                .onFailure().recoverWithItem(false);
    }
}
