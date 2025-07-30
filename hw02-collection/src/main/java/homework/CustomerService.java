package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class CustomerService {
    private final NavigableMap <Customer, String> map =new TreeMap<>(
            Comparator.comparingLong(Customer::getScores)

    );


    public Map.Entry<Customer, String> getSmallest() {

        Map.Entry<Customer,String> data = map.firstEntry();
        return data == null ? null : Map.entry(abstractCustomer(data.getKey()), data.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {

        Map.Entry<Customer,String> data = map.higherEntry(customer);
        return data == null ? null : Map.entry(abstractCustomer(data.getKey()), data.getValue());
    }

    public void add(Customer customer, String data) {
        this.map.put(customer,data);
    }

    private Customer abstractCustomer(Customer customer){
        return new Customer(customer.getId(), customer.getName(), customer.getScores());
    }
}
