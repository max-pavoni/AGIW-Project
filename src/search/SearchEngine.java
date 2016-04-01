package search;

import java.util.Map;

import org.elasticsearch.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;


public class SearchEngine {


	public static void main(String[]args){
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("client.transport.sniff", true)
				.put("cluster.name", "my-cluster").build();
		Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9200));
		client.close();
	}
}
