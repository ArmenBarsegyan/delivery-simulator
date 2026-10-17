public class ExpressDelivery implements DeliveryMethod {
    @Override
    public String getName() {
        return "Экспресс-доставка";
    }

    @Override
    public double calculateCost(double orderSum) {
        return 600 + orderSum * 0.05;
    }

    @Override
    public int estimateDays() {
        return 1;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}