package com.talkdesk.tdx.nlp.platform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkdesk.tdx.nlp.platform.entities.rest.TaggedText;
import com.talkdesk.tdx.nlp.platform.entities.rest.TaggedWord;
import com.talkdesk.tdx.nlp.platform.entities.rest.UntaggedText;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TaggingService {

    private final SpacyService spacyService;
    private final ObjectMapper jsonParser;

    private static final Logger logger = LoggerFactory.getLogger(TaggingService.class);

    @Inject
    public TaggingService(ObjectMapper jsonParser, @RestClient SpacyService spacyService) {
        this.jsonParser = jsonParser;
        this.spacyService = spacyService;
    }

    public Uni<List<TaggedWord>> tagText(UntaggedText text) {
        return spacyService.tagText(new UntaggedText(text.getText()))
                .flatMap(response -> {
                    try {
                        return Uni.createFrom().item(jsonParser.readValue(response, TaggedText.class));
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to serialize spaCy response: ", e);
                        return Uni.createFrom().failure(e);
                    }
                }).map(TaggedText::getTaggedWords);
    }
}
