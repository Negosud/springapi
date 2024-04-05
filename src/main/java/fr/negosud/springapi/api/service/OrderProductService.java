package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.OrderProduct;
import fr.negosud.springapi.api.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Autowired
    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public Optional<OrderProduct> getOrderProductById(long id) {
        return orderProductRepository.findById(id);
    }

    public void saveOrderProduct(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    public void markAsReady(OrderProduct orderProduct) throws AssertionError {
        assert orderProduct.getPreparedAt() == null : "OrderProduct " + orderProduct.getId() + " is already prepared";
        orderProduct.setPreparedAt(new Date());
        orderProduct.setPreparedBy(ActionUserContextHolder.getActionUser());
        saveOrderProduct(orderProduct);
    }


}
