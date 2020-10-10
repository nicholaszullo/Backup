/**
*	Works but time limit exceeded
*/
public class BuyOrSell {
	public int maxProfit(int[] prices) {
        return recMaxProfit(prices, 0, Integer.MAX_VALUE);
    }
    
    private int recMaxProfit(int[] prices, int index, int buy){
        if (index == prices.length)
            return 0;
        if (prices[index] > buy){
            return Math.max(prices[index]-buy,recMaxProfit(prices,index+1,buy));
        } else {
            return Math.max(recMaxProfit(prices, index+1, prices[index]), recMaxProfit(prices,index+1,buy));
        }
    }
}