package tecsor.andrei.dissertation.requester.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tecsor.andrei.dissertation.requester.dto.ResultDTO;
import tecsor.andrei.dissertation.requester.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.requester.model.Client;
import tecsor.andrei.dissertation.requester.service.ProviderService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/requester/api/v1")
public class RequesterController {
    private final ProviderService providerService;

    public RequesterController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping("")
    public Boolean createRequest(@RequestBody Client client) throws NoSuchAlgorithmException, IOException {
        return providerService.isClientAvailable(client);
    }

    @PostMapping("/process")
    public ResultDTO process(@RequestBody UserStatisticsDTO userStatisticsDTO) {
        ResultDTO resultDTO = providerService.process(userStatisticsDTO);
        return resultDTO;
    }


}