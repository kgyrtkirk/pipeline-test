package foo;
import org.junit.Test;
import static org.junit.Assert.*;

public class Hello91Test {
    private static final int MULTIPLIER;
    static {
        String multiplier = System.getenv("MULTIPLIER");
        MULTIPLIER = multiplier != null ? Integer.parseInt(multiplier) : 1;
    }
    @Test public void one() {
        if (Math.random() < 0.015) {
            fail("oops");
        }
    }
    @Test public void two() {}
    @Test public void three() throws Exception {
        Thread.sleep(910 * MULTIPLIER);
    }
    @Test public void four() throws Exception {
        Thread.sleep(1000 * MULTIPLIER);
    }
}
