package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.model.Deployment;
import com.epam.training.gen.ai.service.dial.DialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("deployments")
public class DeploymentsController {
    private final DialService dialService;

    public DeploymentsController(DialService dialService) {
        this.dialService = dialService;
    }

    @GetMapping
    List<Deployment> listDeployments() {
        return dialService.getDeployments();
    }
}
