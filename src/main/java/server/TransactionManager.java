package server;

import utils.Order;
import utils.OrderBook;
import java.util.Queue;

public class TransactionManager {
    private OrderBook orderManager = new OrderBook();

    public void markCompleteOrder(Order sellOrder, Order buyOrder){
        orderManager.removeSellOrder(sellOrder);
        orderManager.removeBuyOrder(buyOrder);
        orderManager.putHistoryOrderIfAbsent(sellOrder);
        orderManager.putHistoryOrderIfAbsent(buyOrder);
    }

    public void checkPriceMatch(){
        Queue<Order> buyOrders = orderManager.getBuyOrders();
        Queue<Order> sellOrders = orderManager.getSellOrders();

        for(Order sellOrder: sellOrders){
            for(Order buyOrder: buyOrders){
                if(sellOrder.getPricePerAction() == buyOrder.getPricePerAction()){
                    if(sellOrder.getSharesNumber() == buyOrder.getSharesNumber()){
                        this.markCompleteOrder(sellOrder,buyOrder);
                    }
                    if(sellOrder.getSharesNumber() < buyOrder.getSharesNumber()){
                        orderManager.putHistoryOrderIfAbsent(sellOrder);
                        orderManager.removeSellOrder(sellOrder);
                        buyOrder.setSharesNumber(buyOrder.getSharesNumber() - sellOrder.getSharesNumber());
                    }
                    else{
                        orderManager.putHistoryOrderIfAbsent(buyOrder);
                        orderManager.removeBuyOrder(buyOrder);
                        sellOrder.setSharesNumber(sellOrder.getSharesNumber() - buyOrder.getSharesNumber());
                    }
                }
            }
        }
    }

}
