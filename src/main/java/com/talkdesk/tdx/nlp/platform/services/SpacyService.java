package com.talkdesk.tdx.nlp.platform.services;

import com.talkdesk.tdx.nlp.platform.entities.rest.UntaggedText;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@RegisterRestClient(configKey = "spacy.api")
public interface SpacyService {

    @POST
    @Path("/dep")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces("text/string")
    Uni<String> tagText(UntaggedText text);
}
