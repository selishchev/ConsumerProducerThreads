import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WaitNotify {
    public static class TaskData {
        private int count;
        private final int time;
        private final int bufferSize;
        private final ConcurrentLinkedDeque<Cupboard> data = new ConcurrentLinkedDeque<>();
        private final CupboardBuilder builder = new CupboardBuilderImpl();
        private final String[] colors = {"brown", "red", "yellow", "green", "blue"};
        public TaskData (int time, int bufferSize) {
            this.time = time;
            this.bufferSize = bufferSize;
        }
        public Cupboard getData() {
            synchronized (data) {
                try {
                    System.out.println("size3: " + data.size());
                    if (data.size() == 0) {
                        int min = 200;
                        int diff = 1001 - min;
                        Random random = new Random();
                        int i = random.nextInt(diff);
                        i += min;
                        count += i;
                        System.out.println(i);
                        data.wait(i);
                    }
                    System.out.println("size4: " + data.size());
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
                return data.poll();
            }
        }

        public void putData() {
            synchronized (data) {
                System.out.println("size1: " + data.size());
                if (data.size() < bufferSize) {
                    int min = 100;
                    int diff = 251 - min;
                    Random random = new Random();
                    int a = random.nextInt(diff);
                    a += min;
                    int b = random.nextInt(diff);
                    b += min;
                    int c = random.nextInt(diff);
                    c += min;
                    Cupboard cupboard = builder
                            .height(a)
                            .length(b)
                            .width(c)
                            .color(colors[random.nextInt(5)])
                            .numOfSections(random.nextInt(10) + 1)
                            .build();
                    data.add(cupboard);
                    System.out.println("Producer put item: " + cupboard.getInfo());
                }
                System.out.println("size2: " + data.size());
            }
        }
    }

    public static class Producer implements Runnable {
        private final TaskData dest;
        int checking = 0;

        public Producer(TaskData dest) {
            this.dest = dest;
        }

        @Override
        public void run() {
            try {
                while (checking < dest.time * 2){
                    dest.putData();
                    Thread.sleep(500);
                    checking++;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            System.out.println("Producer thread finished");
        }
    }

    public static class Consumer implements Runnable {
        final TaskData data;

        public Consumer(TaskData data) {
            this.data = data;
        }

        @Override
        public void run() {
            Cupboard cupboard;
            while (data.count < data.time * 1000) {
                cupboard = data.getData();
                if (cupboard == null) {
                    System.out.println("Consumer timeout");
                } else {
                    System.out.println("Consumer got item: " + cupboard.getInfo());
                }
            }
            System.out.println("Consumer thread finished");
        }
    }
}
