package server;

import utils.Order;
import utils.OrderBook;
import java.util.Queue;

public class TransactionManager {
    private OrderBook orderManager = new OrderBook();

    public void addBuyOrder(Order order){
        orderManager.putBuyOrderIfAbsent(order);
    }

    public void addSellOrder(Order order){
        orderManager.putSellOrderIfAbsent(order);
    }

    private void markCompleteTransaction(Order sellOrder, Order buyOrder){
        System.out.println("Orders have same price/action and shares number.");
        orderManager.removeSellOrder(sellOrder);
        orderManager.removeBuyOrder(buyOrder);
        orderManager.putHistoryOrder(sellOrder);
        orderManager.putHistoryOrder(buyOrder);
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " matched!");
    }

    private void markPartialBuyOrder(Order sellOrder, Order buyOrder){
        System.out.println("Sell order actions are less than buy order actions. Buy order remains available.");
        orderManager.putHistoryOrder(sellOrder);
        orderManager.removeSellOrder(sellOrder);
        buyOrder.setSharesNumber(buyOrder.getSharesNumber() - sellOrder.getSharesNumber());
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " partially matched!");
    }

    private void markPartialSellOrder(Order sellOrder, Order buyOrder){
        System.out.println("Buy order actions are less than sell order actions. Sell order remains available.");
        orderManager.putHistoryOrder(buyOrder);
        orderManager.removeBuyOrder(buyOrder);
        sellOrder.setSharesNumber(sellOrder.getSharesNumber() - buyOrder.getSharesNumber());
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " partially matched!");
    }

    public synchronized void checkPriceMatch(){
        System.out.println("looking for a match...");
        Queue<Order> buyOrders = orderManager.getBuyOrders();
        Queue<Order> sellOrders = orderManager.getSellOrders();

        boolean match = false;
        for(Order sellOrder: sellOrders){
            for(Order buyOrder: buyOrders){
                if(sellOrder.getPricePerAction() == buyOrder.getPricePerAction()){
                    if(sellOrder.getSharesNumber() == buyOrder.getSharesNumber()) {
                        match = true;
                        this.markCompleteTransaction(sellOrder, buyOrder);
                    }
                    else if(sellOrder.getSharesNumber() < buyOrder.getSharesNumber()) {
                        this.markPartialBuyOrder(sellOrder, buyOrder);
                        match = true;
                    }
                    else {
                        this.markPartialSellOrder(sellOrder, buyOrder);
                        match = true;
                    }
                }
            }
        }
        if(match)
            System.out.println("** transactions history: " + orderManager.getHistoryOrders());
        else
            System.out.println("nothing found this time.");
    }

}
