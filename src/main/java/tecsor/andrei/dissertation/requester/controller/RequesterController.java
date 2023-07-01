package tecsor.andrei.dissertation.requester.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tecsor.andrei.dissertation.requester.dto.ResultDTO;
import tecsor.andrei.dissertation.requester.dto.RiskDTO;
import tecsor.andrei.dissertation.requester.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.requester.model.Client;
import tecsor.andrei.dissertation.requester.model.Risk;
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
        return providerService.process(userStatisticsDTO);
    }

    @PostMapping("/provide")
    @ResponseStatus(value = HttpStatus.OK)
    public void provide(@RequestBody Risk risk) {
        providerService.provide(risk);
    }

    @GetMapping("/{pid}")
    public RiskDTO getResult(@PathVariable("pid") String pid) {
        return providerService.getResult(pid);
    }

}