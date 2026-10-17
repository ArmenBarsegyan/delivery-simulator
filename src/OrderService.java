import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private final List<Courier> couriers = List.of(
            new Courier("Иван Петров", "+7-900-111-11-11"),
            new Courier("Мария Сидорова", "+7-900-222-22-22"));
    private final List<DeliveryMethod> deliveryMethods = List.of(
            new StandardDelivery(),
            new ExpressDelivery(),
            new PickupDelivery());
    private int nextId = 1;

    public Order createOrder(Customer customer) {
        Order order = new Order(nextId++, customer);
        orders.add(order);
        return order;
    }

    public Order findOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        throw new IllegalArgumentException("Заказ с номером " + id + " не найден");
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethods;
    }
}