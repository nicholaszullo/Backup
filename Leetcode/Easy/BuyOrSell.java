public class BuyOrSell {
    public int maxProfit(int[] prices) {
        int buy = Integer.MAX_VALUE;
        int profit = 0;
        for (int i = 0; i < prices.length; i++){
            buy = prices[i] < buy ? prices[i] : buy;
            profit = prices[i] - buy > profit ? prices[i] - buy : profit;
            
        }
        return profit;
        
    }
}