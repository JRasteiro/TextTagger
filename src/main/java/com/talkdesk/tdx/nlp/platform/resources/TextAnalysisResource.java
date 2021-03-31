package com.talkdesk.tdx.nlp.platform.resources;

import com.talkdesk.tdx.nlp.platform.entities.rest.RawText;
import com.talkdesk.tdx.nlp.platform.entities.rest.TaggedWord;
import com.talkdesk.tdx.nlp.platform.entities.rest.UntaggedText;
import com.talkdesk.tdx.nlp.platform.services.TaggingService;
import com.talkdesk.tdx.nlp.platform.services.TextLoggingRepositoryService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/text/analyze")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TextAnalysisResource {

    private final TaggingService taggingService;
    private final TextLoggingRepositoryService textLoggingRepositoryService;

    private static final Logger logger = LoggerFactory.getLogger(TextAnalysisResource.class);

    public TextAnalysisResource(TaggingService taggingService, TextLoggingRepositoryService textLoggingRepositoryService) {
        this.taggingService = taggingService;
        this.textLoggingRepositoryService = textLoggingRepositoryService;
    }

    @POST
    public Uni<List<TaggedWord>> analyze(RawText text, @Context HttpServerRequest request) {
        logger.info("Analyzing text '{}'", text.getText());
        return taggingService.tagText(new UntaggedText(text.getText()))
                .and(textLoggingRepositoryService.storeSentence(request.remoteAddress().toString(), text.getText()))
                .map(Tuple2::getItem1);
    }
}
