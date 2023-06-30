package tecsor.andrei.dissertation.requester.model;

import lombok.Data;
import tecsor.andrei.dissertation.requester.dto.UserStatisticsDTO;

import java.util.Base64;

@Data
public class EncryptedUserStatistics {
    private int size;
    private byte[] gamblingPercent;
    private byte[] overspendingScore;
    private byte[] impulsiveBuyingScore;
    private byte[] meanDepositSum;
    private byte[] meanReportedIncome;
    private byte[] noMonthsDeposited;

    //constructor with size parameter to initialize the arrays
    public EncryptedUserStatistics(int size) {
        this.size = size;
        gamblingPercent = new byte[size];
        overspendingScore = new byte[size];
        impulsiveBuyingScore = new byte[size];
        meanDepositSum = new byte[size];
        meanReportedIncome = new byte[size];
        noMonthsDeposited = new byte[size];
    }

    public static EncryptedUserStatistics fromDto(UserStatisticsDTO dto) {
        EncryptedUserStatistics encrypted = new EncryptedUserStatistics(dto.getSize());

        encrypted.setGamblingPercent(Base64.getDecoder().decode(dto.getGamblingPercent()));
        encrypted.setOverspendingScore(Base64.getDecoder().decode(dto.getOverspendingScore()));
        encrypted.setImpulsiveBuyingScore(Base64.getDecoder().decode(dto.getImpulsiveBuyingScore()));
        encrypted.setMeanDepositSum(Base64.getDecoder().decode(dto.getMeanDepositSum()));
        encrypted.setMeanReportedIncome(Base64.getDecoder().decode(dto.getMeanReportedIncome()));
        encrypted.setNoMonthsDeposited(Base64.getDecoder().decode(dto.getNoMonthsDeposited()));

        return encrypted;
    }
}
