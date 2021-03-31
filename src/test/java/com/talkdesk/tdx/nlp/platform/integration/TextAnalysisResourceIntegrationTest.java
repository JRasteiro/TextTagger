package com.talkdesk.tdx.nlp.platform.integration;

import com.talkdesk.tdx.nlp.platform.config.MockDatabase;
import com.talkdesk.tdx.nlp.platform.entities.rest.POS;
import com.talkdesk.tdx.nlp.platform.services.SpacyService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@QuarkusTestResource(MockDatabase.class)
public class TextAnalysisResourceIntegrationTest {

    private static final String validText = "{\"text\": \"How are you today?\"}";
    private static final String mockedResponse = "{\"arcs\":[{\"dir\":\"left\",\"end\":1,\"label\":\"advmod\",\"start\":0,\"text\":\"How\"},{\"dir\":\"right\",\"end\":2,\"label\":\"nsubj\",\"start\":1,\"text\":\"you\"},{\"dir\":\"right\",\"end\":3,\"label\":\"npadvmod\",\"start\":1,\"text\":\"today\"},{\"dir\":\"right\",\"end\":4,\"label\":\"punct\",\"start\":1,\"text\":\"?\"}],\"words\":[{\"tag\":\"WRB\",\"text\":\"How\"},{\"tag\":\"VBP\",\"text\":\"are\"},{\"tag\":\"PRP\",\"text\":\"you\"},{\"tag\":\"NN\",\"text\":\"today\"},{\"tag\":\".\",\"text\":\"?\"}]}";

    @Inject
    PgPool client;

    @InjectMock
    @RestClient
    SpacyService spacyService; // Mock External Dependency

    @Test
    public void givenValidText_whenCallingAnalyzeEndpoint_thenSucceed() {
        client.query("DROP TABLE IF EXISTS wordtagger_usage")
                .flatMap(r -> client.query("CREATE TABLE wordtagger_usage(id SERIAL PRIMARY KEY, client varchar(50), rawtext TEXT)"))
                .await().atMost(Duration.ofSeconds(10));

        Mockito.when(spacyService.tagText(Mockito.any())).thenReturn(Uni.createFrom().item(mockedResponse));

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(validText)
                .post("/text/analyze")
            .then()
                .statusCode(200)
                .body("$.size", is(5),
                        "[0].text", is("How"),
                        "[0].tag", is(POS.ADV.name()),
                        "[1].text", is("are"),
                        "[1].tag", is(POS.VERB.name()),
                        "[2].text", is("you"),
                        "[2].tag", is(POS.PRON.name()),
                        "[3].text", is("today"),
                        "[3].tag", is(POS.NOUN.name()),
                        "[4].text", is("?"),
                        "[4].tag", is(POS.PUNCT.name())
                );
    }

    @Test
    public void givenValidText_whenCallingAnalyzeEndpointWithDbDown_thenHandleErrorButSucceed() {
        // Drop the table but don't create a new one, causing the write to fail
        client.query("DROP TABLE IF EXISTS wordtagger_usage").await().atMost(Duration.ofSeconds(10));

        Mockito.when(spacyService.tagText(Mockito.any())).thenReturn(Uni.createFrom().item(mockedResponse));

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(validText)
                .post("/text/analyze")
            .then()
                .statusCode(200)
                .body("$.size", is(5),
                    "[0].text", is("How"),
                    "[0].tag", is(POS.ADV.name()),
                    "[1].text", is("are"),
                    "[1].tag", is(POS.VERB.name()),
                    "[2].text", is("you"),
                    "[2].tag", is(POS.PRON.name()),
                    "[3].text", is("today"),
                    "[3].tag", is(POS.NOUN.name()),
                    "[4].text", is("?"),
                    "[4].tag", is(POS.PUNCT.name())
                );
    }
}
