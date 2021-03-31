package com.talkdesk.tdx.nlp.platform.services;

import com.talkdesk.tdx.nlp.platform.entities.rest.TaggedWord;
import com.talkdesk.tdx.nlp.platform.entities.rest.UntaggedText;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import java.util.*;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class TaggingServiceTest {

    @InjectMock
    @RestClient
    SpacyService spacyService;

    @Inject
    TaggingService taggingService;

    private static final String mockedResponse = "{\"arcs\":[{\"dir\":\"left\",\"end\":1,\"label\":\"advmod\",\"start\":0,\"text\":\"How\"},{\"dir\":\"right\",\"end\":2,\"label\":\"nsubj\",\"start\":1,\"text\":\"you\"},{\"dir\":\"right\",\"end\":3,\"label\":\"npadvmod\",\"start\":1,\"text\":\"today\"},{\"dir\":\"right\",\"end\":4,\"label\":\"punct\",\"start\":1,\"text\":\"?\"}],\"words\":[{\"tag\":\"WRB\",\"text\":\"How\"},{\"tag\":\"VBP\",\"text\":\"are\"},{\"tag\":\"PRP\",\"text\":\"you\"},{\"tag\":\"NN\",\"text\":\"today\"},{\"tag\":\".\",\"text\":\"?\"}]}";

    @Test
    public void givenValidText_whenCallingSpacy_thenExpectValidResponse() {
        Mockito.when(spacyService.tagText(Mockito.any())).thenReturn(Uni.createFrom().item(mockedResponse));

        List<TaggedWord> taggedText = taggingService.tagText(new UntaggedText("How are you today?"))
                .await().indefinitely();

        Assert.assertFalse("At least one tagged word must be present", taggedText.isEmpty());
        Assert.assertNotNull("A tagged word must contain text", taggedText.get(0).getText());
        Assert.assertNotNull("A tagged word must contain a valid Tag", taggedText.get(0).getTag());

        // Validate actual content
        Assert.assertEquals("How", taggedText.get(0).getText());
        Assert.assertEquals("ADV", taggedText.get(0).getTag().name());
        Assert.assertEquals("are", taggedText.get(1).getText());
        Assert.assertEquals("VERB", taggedText.get(1).getTag().name());
        Assert.assertEquals("you", taggedText.get(2).getText());
        Assert.assertEquals("PRON", taggedText.get(2).getTag().name());
        Assert.assertEquals("today", taggedText.get(3).getText());
        Assert.assertEquals("NOUN", taggedText.get(3).getTag().name());
        Assert.assertEquals("?", taggedText.get(4).getText());
        Assert.assertEquals("PUNCT", taggedText.get(4).getTag().name());
    }

    @Test
    public void givenValidText_whenSpacyIsDown_thenExpectError() {
        final String expectedMessage = "Connection Refused";
        Mockito.when(spacyService.tagText(Mockito.any())).thenThrow(new RuntimeException(expectedMessage));

        try {
            taggingService.tagText(new UntaggedText("How are you today?")).await().indefinitely();
        } catch (Exception ex) {
            Assert.assertTrue("Expected Runtime Exception", ex instanceof RuntimeException);
            Assert.assertEquals(expectedMessage, ex.getMessage());
        }
    }
}
