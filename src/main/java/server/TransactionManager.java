package server;

import utils.Order;
import utils.OrderBook;
import java.util.Queue;
import java.util.UUID;

public class TransactionManager {
    private OrderBook orderBook = new OrderBook();

    public void addBuyOrder(Order order){
        orderBook.putBuyOrderIfAbsent(order);
    }

    public void addSellOrder(Order order){
        orderBook.putSellOrderIfAbsent(order);
    }

    private void markCompleteTransaction(Order sellOrder, Order buyOrder){
        System.out.println("Orders have same price/action and shares number.");
        orderBook.removeSellOrder(sellOrder);
        orderBook.removeBuyOrder(buyOrder);
        orderBook.putHistoryOrder(sellOrder);
        orderBook.putHistoryOrder(buyOrder);
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " matched!");
    }

    private void markPartialBuyOrder(Order sellOrder, Order buyOrder){
        System.out.println("Buy order actions ( " + buyOrder.getSharesNumber()+", "+buyOrder.getPricePerAction()+" ) are greater than sell order actions ( " + sellOrder.getSharesNumber()+", "+sellOrder.getPricePerAction()+" ). Buy order remains available.");
        orderBook.putHistoryOrder(sellOrder);
        orderBook.removeSellOrder(sellOrder);
        buyOrder.setSharesNumber(buyOrder.getSharesNumber() - sellOrder.getSharesNumber());
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " partially matched!");
    }

    private void markPartialSellOrder(Order sellOrder, Order buyOrder){
        System.out.println("Buy order actions ( " + buyOrder.getSharesNumber()+", "+buyOrder.getPricePerAction()+" ) are less than sell order actions ( " + sellOrder.getSharesNumber()+", "+sellOrder.getPricePerAction()+" ). Sell order remains available.");
        orderBook.putHistoryOrder(buyOrder);
        orderBook.removeBuyOrder(buyOrder);
        sellOrder.setSharesNumber(sellOrder.getSharesNumber() - buyOrder.getSharesNumber());
        System.out.println("Sell order from client " + sellOrder.getClientId() + " and buy order from client " + buyOrder.getClientId() + " partially matched!");
    }

    public synchronized void checkPriceMatch(){
        System.out.println("looking for a match...");
        Queue<Order> buyOrders = orderBook.getBuyOrders();
        Queue<Order> sellOrders = orderBook.getSellOrders();

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
            System.out.println("** transactions history: " + orderBook.getHistoryOrders());
        else
            System.out.println("nothing found this time.");
    }

    public void modifyOrderSharesNumber(UUID orderId, String orderType, int sharesNumber, UUID clientID) {
        orderBook.modifyOrder(orderId, orderType, sharesNumber, 0, clientID, "sharesNumber");
    }


    public void modifyOrderPrice(UUID orderId,String orderType, double price, UUID clientID) {
        orderBook.modifyOrder(orderId, orderType, 0, price, clientID, "price");
    }

    @Override
    public String toString() {
        return "\n## Active orders ##\n" +
                orderBook.toString() +
                '}';
    }

}
