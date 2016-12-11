package test;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.junit.After;
import org.junit.Before;

import java.util.Random;
import java.util.concurrent.Callable;

import static java.util.UUID.randomUUID;

/**
 * JUnit Tests.
 *
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public abstract class AndroidTestBase {
  protected static final int NUMBER_NEGATIVE_ONE = -1;
  protected static final int NUMBER_ZERO = 0;
  protected static final int NUMBER_ONE = 1;
  protected static final int DENSITY_LDPI = 36;
  protected static final int DENSITY_MDPI = 48;
  protected static final int DENSITY_HDPI = 72;
  protected static final int DENSITY_XHDPI = 96;
  protected static final int DENSITY_XXHDPI = 144;
  protected static final int DENSITY_XXXHDPI = 192;
  protected static final String STRING_EMPTY = "";
  protected static final String STRING_NULL = null;
  protected static final String STRING_UNIQUE = randomUUID().toString();
  protected static final String STRING_UNIQUE2 = randomUUID().toString() + randomUUID().toString();
  protected static final String STRING_UNIQUE3 = randomUUID().toString();
  protected static final Integer INTEGER_RANDOM = new Random().nextInt();
  protected static final Integer INTEGER_RANDOM_POSITIVE = new Random().nextInt(Integer.SIZE - 1);
  protected static final Long LONG_RANDOM = new Random().nextLong();
  protected static final Float FLOAT_RANDOM = new Random().nextFloat();
  protected static final Double DOUBLE_RANDOM = new Random().nextDouble();

  @Before public void setUp() throws Exception {
    RxAndroidPlugins.reset();
    RxAndroidPlugins.onMainThreadScheduler(Schedulers.trampoline());
    RxAndroidPlugins.initMainThreadScheduler(new Callable<Scheduler>() {
      @Override public Scheduler call() throws Exception {
        return Schedulers.trampoline();
      }
    });
    RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
      @Override public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
        return Schedulers.trampoline();
      }
    });
    RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
      @Override public Scheduler apply(Scheduler scheduler) throws Exception {
        return Schedulers.trampoline();
      }
    });
  }

  @After public void tearDown() throws Exception {
    RxAndroidPlugins.reset();
  }
}
