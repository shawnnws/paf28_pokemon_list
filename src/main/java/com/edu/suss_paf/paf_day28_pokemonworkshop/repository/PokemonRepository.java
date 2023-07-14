package com.edu.suss_paf.paf_day28_pokemonworkshop.repository;

import java.util.List;

import org.bson.Document;
import org.bson.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

@Repository
public class PokemonRepository {
    
    @Autowired
    MongoTemplate mongoTemplate;

    /*

     db.pokemon.aggregate([
    {
        $unwind: "$type"
    },
    {
        $group: {
            _id : "$type",
            name: { $push: "$name"}
        }
    }
    ]);

     */

    public List<Document> listPokemonTypes() {
        UnwindOperation unwindTypes = Aggregation.unwind("type");
        GroupOperation groupByTypes = Aggregation.group("type");
        SortOperation sortTypes = Aggregation.sort(Sort.by(Direction.ASC, "_id"));

        Aggregation pipeline = Aggregation.newAggregation(unwindTypes, groupByTypes, sortTypes);
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "pokemon", Document.class);

        return results.getMappedResults();
    }

    /*
    db.pokemon.aggregate([
    {
        $match: { type: "Ghost" }
    },
    {
        $project: { id: 1, name: 1, image: 1, _id: 0 }
    },
    {
        $sort: { count: 1 }
    }
]);
     */

     public List<Document> listPokemonByType(String type) {

        MatchOperation matchType = Aggregation.match(Criteria.where("type").is(type));
        ProjectionOperation projectPokemonType =  Aggregation.project("id", "name", "img").andExclude("_id");
        SortOperation sortById = Aggregation.sort(Sort.by(Direction.ASC, "id"));

        Aggregation pipeline = Aggregation.newAggregation(matchType, projectPokemonType, sortById);
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "pokemon", Document.class);

        return results.getMappedResults();
     }

    // public Document findPokemonByName(String name) {

    //     Criteria criteria = Criteria.where("name").regex(name, "i");

    //     Query query = Query.query(criteria);

    //     List<Document> results = mongoTemplate.find(query, Document.class, "pokemon");

    //     if (results.size() <= 0) {
    //         return;
    //     }

    //     Document root = results.get(0);

    //     Document stats = root.get("stats", Document.class);
    //     for(String i : List.of("hp", "attack", "defense", "spattack", "spdefence", "speed"))
    //     System.out.printf("hp: %s\n", i, stats.getString(i));

    //     Document moves = root.get("moves", Document.class);
    //     List<Document> level = moves.getList("level",Document.class);
    //     for (Document d : level) {
    //         System.out.printf("name: %s\n", d.getString("name"));
    //     }

        
    // }
}
