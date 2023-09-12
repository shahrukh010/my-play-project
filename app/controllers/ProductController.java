package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.ElasticsearchClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import play.mvc.Controller;

import play.mvc.Result;
import services.ElasticsearchService;

import javax.inject.Inject;
import java.io.IOException;


public class ProductController extends Controller {

    RestHighLevelClient restClient = ElasticsearchClient.getClient();

    private final ElasticsearchService elasticsearchService;

    @Inject
    public ProductController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    public Result listAllDocuments(String indexName) {
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchAllQuery()); // Match all documents
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

            // Process the search response...
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resultJson = objectMapper.valueToTree(searchResponse.getHits().getHits());

            return ok(resultJson);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return internalServerError("An error occurred while retrieving documents.");
        }
    }



    public Result searchByGender(String gender) throws IOException {
        SearchRequest searchRequest = new SearchRequest("accounts"); // Replace with your actual index name
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("gender", gender));
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        // Process the search response...
        return ok(searchResponse.toString());
        }

    public Result retrieveFirstnames(String indexName) {
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // Specify the fields to retrieve
            sourceBuilder.fetchSource("firstname", null);

            // Add your query here (e.g., match_all to retrieve all documents)
            sourceBuilder.query(QueryBuilders.matchAllQuery());

            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            return ok(searchResponse.toString());
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return internalServerError("An error occurred while retrieving firstnames.");
        }
    }


    public Result filterByBalanceRange(String indexName) {
        try {
            SearchResponse searchResponse = elasticsearchService.filterByBalanceRange(indexName);
            return ok(searchResponse.toString());
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return internalServerError("An error occurred while filtering by balance range.");
        }
    }


}
