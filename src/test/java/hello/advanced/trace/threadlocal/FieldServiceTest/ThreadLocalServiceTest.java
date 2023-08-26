package hello.advanced.trace.threadlocal.FieldServiceTest;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService threadLocalService = new ThreadLocalService();


    @Test
    @DisplayName("동시성 문제가 없는 이름 저장 테스트")
    public void noConcurrencySaveNameTest() {
        log.info("main start");
        Runnable userA = () -> {
            threadLocalService.logic("userA");
        };
        Runnable userB = () -> {
            threadLocalService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(2000);    // 동시성 문제 발생 X
        threadB.start();
        sleep(3000);    // 메인 쓰레드 종료 대기

        log.info("main exit");
    }

    @Test
    @DisplayName("동시성 문제가 발생하는 이름 저장 테스트")
    public void concurrencySaveNameTest() {
        log.info("main start");
        Runnable userA = () -> {
            threadLocalService.logic("userA");
        };
        Runnable userB = () -> {
            threadLocalService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(100);    // 동시성 문제 발생
        threadB.start();
        sleep(3000);    // 메인 쓰레드 종료 대기

        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
