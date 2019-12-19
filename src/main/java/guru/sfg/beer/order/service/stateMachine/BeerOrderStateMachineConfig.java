package guru.sfg.beer.order.service.stateMachine;

import guru.sfg.beer.order.service.domain.BeerOrderEventNum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventNum> {

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventNum> states) throws Exception {
        states.withStates()
                .initial(BeerOrderStatusEnum.NEW)
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))
                .end(BeerOrderStatusEnum.PICKED_UP)
                .end(BeerOrderStatusEnum.DELIVERED)
                .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION)
                .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEventNum> transitions) throws Exception {
        transitions.withExternal().source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.NEW).event(BeerOrderEventNum.VALIDATE_ORDER)
                .and()
                .withExternal().source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATED).event(BeerOrderEventNum.VALIDATION_PASSED)
                .and()
                .withExternal().source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATION_EXCEPTION).event(BeerOrderEventNum.VALIDATION_FAILED);
    }
}
