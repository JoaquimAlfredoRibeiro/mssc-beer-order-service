package guru.sfg.beer.order.service.stateMachine.actions;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventNum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventNum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventNum> context) {

        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER, ValidateOrderRequest.builder()
                            .beerOrder(beerOrderMapper.beerOrderToDto(beerOrder))
                            .build());

        log.debug("Send validation request for orderID: " + beerOrderId);
    }
}
