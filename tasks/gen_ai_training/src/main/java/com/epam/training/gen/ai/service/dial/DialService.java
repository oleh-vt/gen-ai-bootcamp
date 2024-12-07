package com.epam.training.gen.ai.service.dial;

import com.epam.training.gen.ai.model.Deployment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class DialService {

    private final DialApiClient dialApiClient;

    private final ConcurrentMap<String, Deployment> deploymentModels = new ConcurrentHashMap<>();

    public Collection<Deployment> getDeployments() {
        if (deploymentModels.isEmpty()) {
            fetchDeployments();
        }
        return deploymentModels.values();
    }

    private void fetchDeployments() {
        dialApiClient.getDeployments()
                .forEach(d -> deploymentModels.put(d.id(), d));
    }

    public boolean exists(String model) {
        if (deploymentModels.isEmpty()) {
            fetchDeployments();
        }
        return deploymentModels.containsKey(model);
    }

}
