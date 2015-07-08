package docs.stream;

import akka.actor.ActorRef;

import java.util.function.Predicate;

/**
 * Acts as if `System.out.println()` yet swallows all messages. Useful for putting printlines in examples yet without poluting the build with them.
 */
public class SilenceSystemOut {

  private SilenceSystemOut() {
  }

  public static System get() {
    return new System(new System.Println() {
      @Override
      public void println(String s) {
        // ignore
      }
    });
  }

  public static System get(ActorRef probe) {
    return new System(new System.Println() {
      @Override
      public void println(String s) {
        probe.tell(s, ActorRef.noSender());
      }
    });
  }

  public static System get(Predicate<String> filter, ActorRef probe) {
    return new System(new System.Println() {
      @Override
      public void println(String s) {
        if (filter.test(s))
          probe.tell(s, ActorRef.noSender());
      }
    });
  }

  public static class System {
    public final Println out;

    public System(Println out) {
      this.out = out;
    }

    public static abstract class Println {
      public abstract void println(String s);

      public void println(Object s) {
        println(s.toString());
      }
    }

  }

}
