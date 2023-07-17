package tecsor.andrei.dissertation.requester.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import tecsor.andrei.dissertation.requester.dto.ClientRequestDTO;
import tecsor.andrei.dissertation.requester.dto.ResultDTO;
import tecsor.andrei.dissertation.requester.dto.RiskDTO;
import tecsor.andrei.dissertation.requester.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.requester.model.EncryptedUserStatistics;
import tecsor.andrei.dissertation.requester.model.Risk;
import tecsor.andrei.dissertation.requester.tcp.client.TcpClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static tecsor.andrei.dissertation.requester.model.EncryptedUserStatistics.fromDto;

@Service
public class ProviderService {
    public static final String ANSI_YELLOW = "\u001B[93m";

    private final ApiCaller apiCaller;

    private final Map<String, Risk> clientRiskMap = new HashMap<>();

    public ProviderService(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    @NotNull
    private static String prepareHash(String pid) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(pid.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean isClientAvailable(ClientRequestDTO clientRequest) throws NoSuchAlgorithmException, IOException {
        String hashedId = prepareHash(clientRequest.getPid());
        System.out.println(ANSI_YELLOW + "The hashed value for client id " + clientRequest.getPid() + " is " + hashedId);
        if (clientRequest.getFid().equals("p1")) {
            System.out.println("Checking if client is available for provider 1");
            Call<Boolean> callClientAvailability = apiCaller.isClientAvailable(hashedId);
            Response<Boolean> response = callClientAvailability.execute();
            System.out.println("Client availability for provider 1 is " + response.body());
            System.out.println();
            return Boolean.TRUE.equals(response.body());
        }
        System.out.println("The provider is not present in the schema");
        System.out.println();
        return false;
    }

    public ResultDTO process(UserStatisticsDTO userStatisticsDTO) {
        System.out.println("\nTransforming the user statistics dto to the encrypted version");
        EncryptedUserStatistics encryptedUserStatistics = fromDto(userStatisticsDTO);
        try {
            return TcpClient.process(encryptedUserStatistics);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void provide(Risk risk) {
        System.out.println("\nUser with pid " + risk.getPid() + " has a risk score of " + risk.getScore() + " from provider " + risk.getFid());
        clientRiskMap.put(risk.getPid(), risk);
        System.out.println("DONE!");
    }

    public RiskDTO getResult(String fid, String pid) throws NoSuchAlgorithmException {
        String hashedPid = prepareHash(pid);
        if (clientRiskMap.isEmpty() || clientRiskMap.get(hashedPid) == null) {
            return new RiskDTO(-1, "processing");
        }
        return new RiskDTO(clientRiskMap.get(hashedPid).getScore(), "done");
    }
}