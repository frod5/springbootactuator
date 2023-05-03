package hello.order.v2;

import hello.order.OrderService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV2 implements OrderService {


    //앞서 만든 OrderServiceV1 의 가장 큰 단점은 메트릭을 관리하는 로직이 핵심 비즈니스 개발 로직에
    //침투했다는 점이다. 이런 부분을 분리하려면 어떻게 해야할까? 바로 스프링 AOP를 사용하면 된다.
    //직접 필요한 AOP를 만들어서 적용해도 되지만, 마이크로미터는 이런 상황에 맞추어 필요한 AOP
    //구성요소를 이미 다 만들어두었다.

    private AtomicInteger stock = new AtomicInteger(100);



    //@Counted 애노테이션을 측정을 원하는 메서드에 적용한다. 주문과 취소 메서드에 적용했다.
    //그리고 메트릭 이름을 지정하면 된다. 여기서는 이전과 같은 my.order 를 적용했다.
    //참고로 이렇게 사용하면 tag 에 method 를 기준으로 분류해서 적용한다.
    //@Counted 를 사용하면 result , exception , method , class 같은 다양한 tag 를 자동으로 적용한다.
    @Counted("my.order")
    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
    }

    @Counted("my.order")
    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
