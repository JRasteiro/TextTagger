package com.talkdesk.tdx.nlp.platform.parsers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.talkdesk.tdx.nlp.platform.entities.rest.POS;

import java.io.IOException;

public class PosParser extends JsonDeserializer<POS> {

    @Override
    public POS deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        ObjectCodec codec = parser.getCodec();
        JsonNode tag = codec.readTree(parser);

        if (tag.isTextual()) {
            return POS.fromTag(tag.asText());
        }

        return null;
    }
}
