package hello.order.v3;

import hello.order.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV3 implements OrderService {

    private final MeterRegistry registry;
    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV3(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void order() {

        //Timer
        //Timer는 좀 특별한 메트릭 측정 도구인데, 시간을 측정하는데 사용된다.
        //카운터와 유사한데, Timer 를 사용하면 실행 시간도 함께 측정할 수 있다.
        //Timer 는 다음과 같은 내용을 한번에 측정해준다.
        //seconds_count : 누적 실행 수 - 카운터
        //seconds_sum : 실행 시간의 합 - sum
        //seconds_max : 최대 실행 시간(가장 오래걸린 실행 시간) - 게이지
        //내부에 타임 윈도우라는 개념이 있어서 1~3분 마다 최대 실행 시간이 다시 계산된다.

        //Timer.builder(name) 를 통해서 타이머를 생성한다. name 에는 메트릭 이름을 지정한다.
        //tag 를 사용했는데, 프로메테우스에서 필터할 수 있는 레이블로 사용된다.
        //주문과 취소는 메트릭 이름은 같고 tag 를 통해서 구분하도록 했다.
        //register(registry) : 만든 타이머를 MeterRegistry 에 등록한다. 이렇게 등록해야 실제 동작한다.
        //타이머를 사용할 때는 timer.record() 를 사용하면 된다. 그 안에 시간을 측정할 내용을 함수로 포함하면 된다.
        Timer timer = Timer.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "order")
                .description("order")
                .register(registry);

        timer.record(() -> {
            log.info("주문");
            stock.decrementAndGet();
            sleep(500);
        });
    }

    @Override
    public void cancel() {
        Timer timer = Timer.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "cancel")
                .description("cancel")
                .register(registry);

        timer.record(() -> {
            log.info("취소");
            stock.incrementAndGet();
            sleep(200);
        });
    }

    private static void sleep(int l) {
        try {
            Thread.sleep(l + new Random().nextInt(200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
