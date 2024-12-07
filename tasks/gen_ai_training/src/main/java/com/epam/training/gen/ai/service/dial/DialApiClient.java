package com.epam.training.gen.ai.service.dial;

import com.epam.training.gen.ai.model.Deployment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class DialApiClient {

    private static final String DEPLOYMENTS_URL = "/openai/deployments";

    private final RestClient restClient;

    public DialApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    List<Deployment> getDeployments() {
        return restClient.get()
                .uri(DEPLOYMENTS_URL)
                .retrieve()
                .body(DeploymentsResponse.class)
                .data();
    }

    record DeploymentsResponse(List<Deployment> data) {
    }
}
