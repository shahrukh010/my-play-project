package main;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticsearchClient {

    private static RestHighLevelClient restClient;

    public static RestHighLevelClient getClient() {
        if (restClient == null) {
            restClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("localhost", 9200, "http")
                    )
            );
        }
        return restClient;
    }
}
