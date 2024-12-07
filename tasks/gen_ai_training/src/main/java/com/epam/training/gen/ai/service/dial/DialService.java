package com.epam.training.gen.ai.service.dial;

import com.epam.training.gen.ai.model.Deployment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialService {

    private final DialApiClient dialApiClient;

    public DialService(DialApiClient dialApiClient) {
        this.dialApiClient = dialApiClient;
    }

    public List<Deployment> getDeployments() {
        return dialApiClient.getDeployments();
    }

}
