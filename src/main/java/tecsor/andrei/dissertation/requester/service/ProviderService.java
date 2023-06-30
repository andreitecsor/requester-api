package tecsor.andrei.dissertation.requester.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import tecsor.andrei.dissertation.requester.dto.ResultDTO;
import tecsor.andrei.dissertation.requester.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.requester.model.Client;
import tecsor.andrei.dissertation.requester.model.EncryptedUserStatistics;
import tecsor.andrei.dissertation.requester.tcp.client.TcpClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static tecsor.andrei.dissertation.requester.model.EncryptedUserStatistics.fromDto;

@Service
public class ProviderService {

    private final ApiCaller apiCaller;

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

    public boolean isClientAvailable(Client client) throws NoSuchAlgorithmException, IOException {
        String hashedId = prepareHash(client.getPid());
        System.out.println(hashedId);
        Call<Boolean> callClientAvailability = apiCaller.isClientAvailable(hashedId);
        Response<Boolean> response = callClientAvailability.execute();
        return Boolean.TRUE.equals(response.body());

    }

    public ResultDTO process(UserStatisticsDTO userStatisticsDTO) {
        EncryptedUserStatistics encryptedUserStatistics = fromDto(userStatisticsDTO);
        try {
            return TcpClient.process(encryptedUserStatistics);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}