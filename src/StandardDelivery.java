public class StandardDelivery implements DeliveryMethod {
    @Override
    public String getName() {
        return "Стандартная доставка курьером";
    }

    @Override
    public double calculateCost(double orderSum) {
        return 300;
    }

    @Override
    public int estimateDays() {
        return 3;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}