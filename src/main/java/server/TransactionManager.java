package server;

import utils.Order;
import utils.OrderBook;

import java.util.Queue;

public class TransactionManager {
    private OrderBook orderManager = new OrderBook();

    public void markCompleteOrder(Order order){
        orderManager.removeSellOrder(order);
        orderManager.removeBuyOrder(order);
        orderManager.putHistoryOrderIfAbsent(order);
    }

    public void checkPriceMatch(){
        Queue<Order> buyOrders = orderManager.getBuyOrders();
        Queue<Order> sellOrders = orderManager.getSellOrders();

//        for(int i = 0;i< sellOrders.size();i++){
//        }

    }

}
