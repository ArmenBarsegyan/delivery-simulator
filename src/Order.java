import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private DeliveryMethod deliveryMethod;
    private Courier courier;
    private OrderStatus status = OrderStatus.CREATED;

    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Добавлять позиции можно только в новый заказ");
        }
        items.add(item);
    }

    public void setDeliveryMethod(DeliveryMethod method) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Менять способ доставки можно только в новом заказе");
        }
        this.deliveryMethod = method;
        if (!method.requiresCourier()) {
            this.courier = null;
        }
    }

    public void assignCourier(Courier newCourier) {
        if (deliveryMethod == null) {
            throw new IllegalStateException("Сначала выберите способ доставки");
        }
        if (!deliveryMethod.requiresCourier()) {
            throw new IllegalStateException("Для способа \"" + deliveryMethod.getName() + "\" курьер не нужен");
        }
        if (status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Заказ уже доставлен, курьера назначить нельзя");
        }
        this.courier = newCourier;
    }

    public void changeStatus(OrderStatus next) {
        if (!status.canMoveTo(next)) {
            throw new IllegalStateException("Нельзя перевести заказ из \"" + status + "\" в \"" + next + "\"");
        }
        if (next == OrderStatus.CONFIRMED && (items.isEmpty() || deliveryMethod == null)) {
            throw new IllegalStateException("Для подтверждения нужны позиции и способ доставки");
        }
        if (next == OrderStatus.IN_DELIVERY && deliveryMethod.requiresCourier() && courier == null) {
            throw new IllegalStateException("Сначала назначьте курьера");
        }
        this.status = next;
    }

    public double getItemsCost() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getTotal();
        }
        return sum;
    }

    public double getDeliveryCost() {
        if (deliveryMethod == null) {
            return 0;
        }
        return deliveryMethod.calculateCost(getItemsCost());
    }

    public double getTotalCost() {
        return getItemsCost() + getDeliveryCost();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Заказ №").append(id).append(" [").append(status).append("]\n");
        sb.append("  Клиент: ").append(customer).append("\n");
        sb.append("  Позиции:\n");
        for (OrderItem item : items) {
            sb.append("    ").append(item).append("\n");
        }
        if (deliveryMethod == null) {
            sb.append("  Доставка: не выбрана\n");
        } else {
            sb.append("  Доставка: ").append(deliveryMethod.getName())
                    .append(", ").append(getDeliveryCost()).append(" руб., срок ")
                    .append(deliveryMethod.estimateDays()).append(" дн.\n");
        }
        if (courier == null) {
            sb.append("  Курьер: не назначен\n");
        } else {
            sb.append("  Курьер: ").append(courier).append("\n");
        }
        sb.append("  Итого: ").append(getTotalCost()).append(" руб.");
        return sb.toString();
    }
}