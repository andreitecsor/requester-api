package tecsor.andrei.dissertation.requester.tcp.client;

import tecsor.andrei.dissertation.requester.dto.ResultDTO;
import tecsor.andrei.dissertation.requester.model.EncryptedUserStatistics;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

public class TcpClient {
    public static ResultDTO process(EncryptedUserStatistics encryptedUserStatistics) throws IOException {
        String hostname = "localhost"; // server hostname
        int port = 8070; // server port number
        try (Socket socket = new Socket(hostname, port)) {
            OutputStream out = socket.getOutputStream();
            //send the encryptedUserStatistics through socket
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(encryptedUserStatistics.getSize());
            out.write(buffer.array());

            out.write(encryptedUserStatistics.getGamblingPercent());
            out.write(encryptedUserStatistics.getOverspendingScore());
            out.write(encryptedUserStatistics.getImpulsiveBuyingScore());
            out.write(encryptedUserStatistics.getMeanDepositSum());
            out.write(encryptedUserStatistics.getMeanReportedIncome());
            out.write(encryptedUserStatistics.getNoMonthsDeposited());

            // receive a response from the server
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] sizeBytes = new byte[8];
            dataInputStream.readFully(sizeBytes);
            long size = ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getLong();
            ResultDTO dto = new ResultDTO();
            dto.setSize((int) size);
            System.out.println("Response from server: " + size);

            byte[] encryptedResult = new byte[(int) size];
            dataInputStream.readFully(encryptedResult);
            dto.setResult(Base64.getEncoder().encodeToString(encryptedResult));
            return dto;

        }

    }
}
