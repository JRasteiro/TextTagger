package com.talkdesk.tdx.nlp.platform.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import com.talkdesk.tdx.nlp.platform.entities.rest.POS;
import com.talkdesk.tdx.nlp.platform.entities.rest.TaggedWord;
import com.talkdesk.tdx.nlp.platform.services.TaggingService;
import com.talkdesk.tdx.nlp.platform.services.TextLoggingRepositoryService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class TextAnalysisResourceTest {

    private final String validText = "{\"text\": \"test\"}";

    @InjectMock
    TaggingService taggingService;

    @InjectMock
    TextLoggingRepositoryService textLoggingRepositoryService;

    @Test
    public void givenValidText_whenCallingAnalyzeEndpoint_thenSucceed() {
        Mockito.when(taggingService.tagText(Mockito.any()))
                .thenReturn(Uni.createFrom().item(List.of(new TaggedWord("test", POS.NOUN))));

        Mockito.when(textLoggingRepositoryService.storeSentence(Mockito.any(), Mockito.any()))
                .thenReturn(Uni.createFrom().item(true));

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(validText)
                .post("/text/analyze")
            .then()
                .statusCode(200)
                .body("$.size", is(1),
                        "[0].text", is("test"),
                        "[0].tag", is(POS.NOUN.name()));
    }

    @Test
    public void givenValidText_whenDatabaseServiceFails_thenSucceedLoggingDBError() {
        Mockito.when(taggingService.tagText(Mockito.any()))
                .thenReturn(Uni.createFrom().item(List.of(new TaggedWord("test", POS.NOUN))));

        Mockito.when(textLoggingRepositoryService.storeSentence(Mockito.any(), Mockito.any()))
                .thenReturn(Uni.createFrom().item(false));

        given()
            .when()
                .contentType(ContentType.JSON)
                .body(validText)
                .post("/text/analyze")
            .then()
            .statusCode(200)
            .body("$.size", is(1),
                    "[0].text", is("test"),
                    "[0].tag", is(POS.NOUN.name()));
    }
}
