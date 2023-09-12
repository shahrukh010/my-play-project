package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.ElasticsearchClient;
import models.Product;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import play.mvc.Result;

import java.io.IOException;

import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;

public class ElasticsearchService {


    RestHighLevelClient restClient = ElasticsearchClient.getClient();

    public SearchResponse filterByBalanceRange(String indexName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // Create a bool query with a filter clause
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // Create a range filter for the balance field
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("balance")
                .gte(1)
                .lte(1500);

        // Add the range filter to the bool query as a filter
        boolQuery.filter(rangeQuery);

        sourceBuilder.query(boolQuery);
        searchRequest.source(sourceBuilder);

        return restClient.search(searchRequest, RequestOptions.DEFAULT);
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


    public void indexProduct(Product product) {
        try {
            IndexRequest request = new IndexRequest("products")
                    .id(String.valueOf(product.getId()))
                    .source(
                            "productName", product.getProductName(),
                            "category", product.getCategory(),
                            "brand", product.getBrand(),
                            "price", product.getPrice(),
                            "inStock", product.getInStock()
                    );

            restClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}




