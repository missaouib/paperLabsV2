package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.domain.OrderIdentifierSequences;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class NextSequenceService {

    private MongoOperations mongoOperations;

    public NextSequenceService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public String getNextOrderIdentifierSequence()
    {
        OrderIdentifierSequences orderIdentifierSequences = mongoOperations.findAndModify(
            query(where("_id").is("orderIdentifierSequences")),
            new Update().inc("seq",1),
            options().returnNew(true).upsert(true),
            OrderIdentifierSequences.class);
        return String.valueOf(orderIdentifierSequences == null ? "" : orderIdentifierSequences.getSeq());
    }
}
