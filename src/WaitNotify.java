import java.util.concurrent.ConcurrentLinkedDeque;

public class WaitNotify {
    // --------------------------------------------------------
    // объект хранения данных, член data является монитором
    // через который общаются поток производителя и поток потребителя
    public static class TaskData {
        private final ConcurrentLinkedDeque data = new ConcurrentLinkedDeque<String>();
        public String getData() {
            synchronized (data) {
                try {
                    if (data.size() == 0) {
                        // ожидаем появление данных полторы секунды
                        data.wait(500);
                    }
                } catch (Exception e) {
                }
                return (String) data.poll();
            }
        }

        public void putData(String s) {
            synchronized (data) {
                data.add(s);
                // уведомляем ожидающий поток о появлении данных для обработки
                data.notify();
            }
            // попробуйте вынести уведомление за блок синхронизации
            // ошибки не будет, но и работать как ожидалось не будет
            // data.notify();
        }
    }
    // --------------------------------------------------------
    // задача производителя - порождает данные, это может занимать
    // вполне заметное время (закачка данных из интернета/разбор текста/тп)
    //
    public static class Producer implements Runnable {
        private final TaskData dest;
        int checking = 0;
        public Producer(TaskData dest) {
            this.dest = dest;
        }
        @Override
        public void run() {
            try {
                while (checking < 20){
                    dest.putData("produced - ");
                    System.out.println(checking);
                    Thread.sleep(500); // засыпаем на секунду
                    checking++;
                }
            } catch (Exception e) {
            }
            System.out.println("Producer thread finished");
        }
    }
    // --------------------------------------------------------
    // задача потребителя - как только появляются доступные данные, что-то делает с ними
    // потребитель автоматически завершает работу, если 3 раза подряд он не получил данные
    public static class Consumer implements Runnable {
        final TaskData data;
        int checking = 0;
        public Consumer(TaskData data) {
            this.data = data;
        }
        @Override
        public void run() {
            String s;
            while (checking < 6) {
                s = data.getData();
                if (s == null) {
                    checking++;
                    System.out.println("Consumer timeout: " + checking);
                } else {
                    System.out.println("Consumer get item: " + s);
                    checking=0;
                }
            }
            System.out.println("Cosumer thread finished");
        }
    }

    public static void main(String[] args) {
        int count = 10;
        TaskData data = new TaskData();
        new Thread(new Producer(data)).start();
        new Thread(new Consumer(data)).start();
    }
}
