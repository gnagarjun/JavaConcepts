/*Here's a simple Java example demonstrating the use of wait() and notify() for inter-thread communication:

In this example:

Message class represents a message that can be produced by the producer and consumed by the consumer.
The setMessage() method is used by the producer to set a message, and getMessage() method is used by the consumer to retrieve the message.
The producer waits until the consumer consumes the message, and vice versa, using wait() and notify() methods for synchronization.
The ProducerConsumerExample class creates two threads: one for the producer and one for the consumer. */


public class Message {
    private String message;
    private boolean messageReady = false;

    // Method to set the message
    public synchronized void setMessage(String message) {
        while (messageReady) {
            // Wait until the previous message is consumed
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.message = message;
        messageReady = true;
        notify(); // Notify the consumer that the message is ready
    }

    // Method to get the message
    public synchronized String getMessage() {
        while (!messageReady) {
            // Wait until a message is available
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        messageReady = false;
        notify(); // Notify the producer that the message has been consumed
        return message;
    }
}

public class ProducerConsumerExample {
    public static void main(String[] args) {
        final Message message = new Message();

        // Producer thread
        Thread producerThread = new Thread(() -> {
            String[] messages = {"Hello", "World", "Bye"};
            for (String msg : messages) {
                message.setMessage(msg);
                System.out.println("Producer sent: " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Consumer thread
        Thread consumerThread = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                String receivedMessage = message.getMessage();
                System.out.println("Consumer received: " + receivedMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
