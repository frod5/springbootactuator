package hello.order.v1;

import hello.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV1 implements OrderService {

    //MeterRegistry
    //마이크로미터 기능을 제공하는 핵심 컴포넌트
    //스프링을 통해서 주입 받아서 사용하고, 이곳을 통해서 카운터, 게이지 등을 등록한다.
    private final MeterRegistry meterRegistry;

    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV1(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();

        Counter.builder("my.order") //Counter.builder(name) 를 통해서 카운터를 생성한다. name 에는 메트릭 이름을 지정한다.
                .tag("class", this.getClass().getName()) //tag 를 사용했는데, 프로메테우스에서 필터할 수 있는 레이블로 사용된다.
                .tag("method", "order")
                .description("order")
                .register(meterRegistry) //register(registry) : 만든 카운터를 MeterRegistry 에 등록한다. 이렇게 등록해야 실제 동작한다
                .increment(); //increment() : 카운터의 값을 하나 증가한다.
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();

        Counter.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "cancel")
                .description("cancel")
                .register(meterRegistry).increment();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
