public class Main {
    public static void main(String[] args) {
        WaitNotify.TaskData data = new WaitNotify.TaskData(10, 4);
        new Thread(new WaitNotify.Producer(data)).start();
        new Thread(new WaitNotify.Consumer(data)).start();
    }
}
