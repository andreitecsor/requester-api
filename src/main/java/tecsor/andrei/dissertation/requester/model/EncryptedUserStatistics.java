package tecsor.andrei.dissertation.requester.model;

import lombok.Data;

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
}
